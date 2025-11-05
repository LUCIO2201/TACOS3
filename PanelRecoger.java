
package vista;

import controlador.CocineroControlador;
import modelo.PedidoPendiente;

import javax.swing.*;
        import javax.swing.table.DefaultTableModel;
import java.awt.*;
        import java.util.List;

public class PanelRecoger extends JPanel {

    private final String mesero;
    private final CocineroControlador controlador;
    private JTable tablaListos;
    private DefaultTableModel modeloTabla;

    public PanelRecoger(String mesero) {
        this.mesero = mesero;
        this.controlador = new CocineroControlador();
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Pedidos Listos para Recoger", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID Orden", "Mesa", "ID Producto", "Producto", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaListos = new JTable(modeloTabla);

        // Ocultamos la columna del ID del Producto (columna 2)
        tablaListos.getColumnModel().getColumn(2).setMinWidth(0);
        tablaListos.getColumnModel().getColumn(2).setMaxWidth(0);

        add(new JScrollPane(tablaListos), BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnMarcarEntregado = new JButton("Marcar como Entregado");
        JButton btnRefrescar = new JButton("Refrescar");
        panelBotones.add(btnMarcarEntregado);
        panelBotones.add(btnRefrescar);
        add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnRefrescar.addActionListener(e -> cargarPedidosListos());

        btnMarcarEntregado.addActionListener(e -> marcarEntregado());

        cargarPedidosListos();
    }

    private void cargarPedidosListos() {
        modeloTabla.setRowCount(0);
        List<PedidoPendiente> listos = controlador.obtenerPedidosListos(mesero);
        for (PedidoPendiente p : listos) {
            modeloTabla.addRow(new Object[]{
                    p.getOrdenId(),
                    p.getMesa(),
                    p.getProductoId(),
                    p.getNombreProducto(),
                    p.getEstadoCocina()
            });
        }
    }

    private void marcarEntregado() {
        int fila = tablaListos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto listo.");
            return;
        }

        int ordenId = (int) modeloTabla.getValueAt(fila, 0);
        int productoId = (int) modeloTabla.getValueAt(fila, 2); // Columna oculta

        if (controlador.marcarComoEntregado(ordenId, productoId)) {
            JOptionPane.showMessageDialog(this, "Producto marcado como ENTREGADO.");
            cargarPedidosListos(); // Refrescar la lista
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado.");
        }
    }
}
