package vista;
import controlador.OrdenControlador;
import modelo.Orden;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel para mostrar y gestionar órdenes del mesero.
 */
public class OrdenesPanel extends JPanel {

    private final JTable tabla;
    private final DefaultTableModel modeloTabla;
    private final OrdenControlador controlador;
    private final String mesero;

    public OrdenesPanel(String mesero) {
        this.mesero = mesero;
        this.controlador = new OrdenControlador();

        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Órdenes del Mesero: " + mesero, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{
                "ID", "Mesa", "Productos", "Total", "Estado"
        }, 0);

        tabla = new JTable(modeloTabla);
        cargarOrdenes();

        JPanel panelBotones = new JPanel();
     //   JButton btnNueva = new JButton("Nueva Orden");
        JButton btnModificar = new JButton("Modificar Orden");
        JButton btnCerrar = new JButton("Cerrar Cuenta");

      //  panelBotones.add(btnNueva);
        panelBotones.add(btnModificar);
        panelBotones.add(btnCerrar);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Acciones
       //tnNueva.addActionListener(e -> {
           //ontrolador.crearOrdenInteractiva(mesero);
        //  cargarOrdenes();
    //  });

        btnModificar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                int idOrden = (int) modeloTabla.getValueAt(fila, 0);
                controlador.modificarOrdenInteractiva(idOrden);
                cargarOrdenes();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una orden para modificar.");
            }
        });

        btnCerrar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                int idOrden = (int) modeloTabla.getValueAt(fila, 0);
                controlador.cerrarCuenta(idOrden);
                cargarOrdenes();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una orden para cerrar.");
            }
        });
    }

    private void cargarOrdenes() {
        modeloTabla.setRowCount(0);
        List<Orden> ordenes = controlador.obtenerOrdenesPorMesero(mesero);
        for (Orden o : ordenes) {
            modeloTabla.addRow(new Object[]{
                    o.getId(), o.getMesa(), o.getProductosTexto(), o.getTotal(), o.getEstado()
            });
        }
    }
}

