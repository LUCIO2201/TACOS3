package vista;

import controlador.OrdenControlador;
import controlador.VentaControlador;
import modelo.Orden;
import modelo.Usuario;
import modelo.Venta;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class CajeroPanel extends JFrame {

    private final Usuario cajero;
    private final OrdenControlador ordenControlador;
    private final VentaControlador ventaControlador;
    private final JTable tablaOrdenes;
    private final DefaultTableModel modeloTabla;

    public CajeroPanel(Usuario usuario) {
        this.cajero = usuario;
        this.ordenControlador = new OrdenControlador();
        this.ventaControlador = new VentaControlador();

        setTitle("Panel de Caja - " + cajero.getUsuario());
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Título
        JLabel lblTitulo = new JLabel("Órdenes Abiertas para Cobrar", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(lblTitulo, BorderLayout.NORTH);

        // Tabla de órdenes
        String[] columnas = {"ID Orden", "Mesa", "Mesero", "Total", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaOrdenes = new JTable(modeloTabla);
        add(new JScrollPane(tablaOrdenes), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnRegistrarPago = new JButton("Registrar Pago");
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        panelBotones.add(btnRegistrarPago);
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnCerrarSesion);
        add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnRefrescar.addActionListener(e -> cargarOrdenesAbiertas());
        btnCerrarSesion.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        btnRegistrarPago.addActionListener(e -> registrarPago());

        // Carga inicial
        cargarOrdenesAbiertas();
    }

    private void cargarOrdenesAbiertas() {
        modeloTabla.setRowCount(0);
        List<Orden> ordenes = ordenControlador.obtenerTodasLasOrdenesAbiertas();
        for (Orden o : ordenes) {
            modeloTabla.addRow(new Object[]{
                    o.getId(),
                    o.getMesa(),
                    o.getMesero(),
                    o.getTotal(),
                    o.getEstado()
            });
        }
    }

    private void registrarPago() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una orden de la lista.");
            return;
        }

        int idOrden = (int) modeloTabla.getValueAt(fila, 0);
        double total = (double) modeloTabla.getValueAt(fila, 3);

        String[] metodos = {"Efectivo", "Tarjeta", "QR"};
        String metodoPago = (String) JOptionPane.showInputDialog(this,
                "Total a pagar: $" + total + "\nSeleccione método de pago:",
                "Registrar Pago",
                JOptionPane.QUESTION_MESSAGE, null, metodos, metodos[0]);

        if (metodoPago == null) {
            return; // El usuario canceló
        }

        // 1. Crear el objeto Venta
        Venta venta = new Venta();
        venta.setOrdenId(idOrden);
        venta.setTotal(total);
        venta.setMetodoPago(metodoPago);
        venta.setFecha(new Timestamp(System.currentTimeMillis())); // Fecha y hora actual

        // 2. Registrar la venta
        boolean ventaExitosa = ventaControlador.registrarVenta(venta);

        if (ventaExitosa) {
            // 3. Cerrar la orden (esto también libera la mesa)
            boolean ordenCerrada = ordenControlador.cerrarCuenta(idOrden);

            if (ordenCerrada) {
                JOptionPane.showMessageDialog(this, "¡Pago registrado y orden cerrada exitosamente!");
                cargarOrdenesAbiertas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al cerrar la orden (ID: " + idOrden + ").");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se pudo registrar la venta en la base de datos.");
        }
    }
}