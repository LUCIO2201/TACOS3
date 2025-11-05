package vista;

import controlador.CocineroControlador;
import modelo.PedidoPendiente;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CocineroPanel extends JFrame {

    private final JTable tablaPendientes;
    private final DefaultTableModel modeloTabla;
    private final Usuario cocinero;
    private final CocineroControlador controlador; // Nuevo controlador

    public CocineroPanel(Usuario usuario) {
        this.cocinero = usuario;
        this.controlador = new CocineroControlador(); // Inicialización

        setTitle("Panel de Cocina - " + cocinero.getUsuario());
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Título
        JLabel lblTitulo = new JLabel("Pedidos Pendientes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(lblTitulo, BorderLayout.NORTH);

        // Tabla de pedidos (actualmente vacía)
        // Se cambiaron las columnas para reflejar el modelo PedidoPendiente
        String[] columnas = {"ID Orden", "Mesa", "ID Producto", "Producto", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPendientes = new JTable(modeloTabla);
        // Ocultamos la columna del ID del Producto, pero la necesitamos para la lógica
        tablaPendientes.getColumnModel().getColumn(2).setMinWidth(0);
        tablaPendientes.getColumnModel().getColumn(2).setMaxWidth(0);
        tablaPendientes.getColumnModel().getColumn(2).setPreferredWidth(0);

        add(new JScrollPane(tablaPendientes), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnMarcarListo = new JButton("Marcar como Listo");
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        panelBotones.add(btnMarcarListo);
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnCerrarSesion);
        add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnRefrescar.addActionListener(e -> cargarPendientes());

        btnMarcarListo.addActionListener(e -> marcarListoSeleccionado());

        btnCerrarSesion.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        // Carga inicial
        cargarPendientes();
    }

    private void cargarPendientes() {
        modeloTabla.setRowCount(0); // Limpia la tabla
        List<PedidoPendiente> pendientes = controlador.obtenerPedidosPendientes();

        for (PedidoPendiente p : pendientes) {
            modeloTabla.addRow(new Object[]{
                    p.getOrdenId(),
                    p.getMesa(),
                    p.getProductoId(),
                    p.getNombreProducto(),
                    p.getEstadoCocina()
            });
        }
    }

    private void marcarListoSeleccionado() {
        int fila = tablaPendientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para marcar como listo.");
            return;
        }

        // Recuperar IDs de la fila seleccionada (ID Producto está en la columna 2)
        int ordenId = (int) modeloTabla.getValueAt(fila, 0);
        int productoId = (int) modeloTabla.getValueAt(fila, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Marcar el producto: '" + modeloTabla.getValueAt(fila, 3) + "' de la Mesa " + modeloTabla.getValueAt(fila, 1) + " como LISTO?",
                "Confirmar Listo", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controlador.marcarComoListo(ordenId, productoId)) {
                JOptionPane.showMessageDialog(this, "Producto marcado como LISTO.");
                cargarPendientes(); // Refrescar la tabla
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el estado del pedido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}