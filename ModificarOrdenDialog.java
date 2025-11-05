
package vista;

import controlador.OrdenControlador;
import controlador.ProductoControlador;
import modelo.DetalleOrden;
import modelo.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * JDialog para agregar y quitar productos (con cantidad) a una orden existente.
 */
public class ModificarOrdenDialog extends JDialog {

    private final int idOrden;
    private final OrdenControlador ordenControlador;
    private final ProductoControlador productoControlador;

    private final JTable tablaDetalles;
    private final DefaultTableModel modeloTabla;
    private final JComboBox<Producto> cbProductos;
    private final JSpinner spinnerCantidad;

    public ModificarOrdenDialog(Frame owner, int idOrden) {
        super(owner, "Modificar Orden (ID: " + idOrden + ")", true); // 'true' = modal
        this.idOrden = idOrden;
        this.ordenControlador = new OrdenControlador();
        this.productoControlador = new ProductoControlador();

        setSize(600, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // --- 1. Panel de Items Actuales (Arriba) ---
        JPanel panelActual = new JPanel(new BorderLayout());
        panelActual.setBorder(BorderFactory.createTitledBorder("Productos en la Orden"));

        modeloTabla = new DefaultTableModel(new String[]{"ID Detalle", "Producto", "Cantidad", "Subtotal", "Estado Cocina"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaDetalles = new JTable(modeloTabla);
        // Ocultar la columna ID Detalle (pero la necesitamos para borrar)
        tablaDetalles.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaDetalles.getColumnModel().getColumn(0).setMinWidth(0);

        panelActual.add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);

        JButton btnEliminar = new JButton("Eliminar Producto Seleccionado");
        panelActual.add(btnEliminar, BorderLayout.SOUTH);

        add(panelActual, BorderLayout.CENTER);

        // --- 2. Panel para Agregar Items (Abajo) ---
        JPanel panelAgregar = new JPanel(new FlowLayout());
        panelAgregar.setBorder(BorderFactory.createTitledBorder("Agregar Nuevo Producto"));

        cbProductos = new JComboBox<>();
        cargarComboBoxProductos(); // Llena el ComboBox con productos

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1)); // (Valor inicial, min, max, paso)

        JButton btnAgregar = new JButton("Agregar");

        panelAgregar.add(new JLabel("Producto:"));
        panelAgregar.add(cbProductos);
        panelAgregar.add(new JLabel("Cantidad:"));
        panelAgregar.add(spinnerCantidad);
        panelAgregar.add(btnAgregar);

        add(panelAgregar, BorderLayout.SOUTH);

        // --- 3. Acciones de Botones ---
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());

        // Carga inicial de la tabla
        cargarDetallesOrden();
    }

    /**
     * Llena la tabla con los productos que ya están en la orden.
     */
    private void cargarDetallesOrden() {
        modeloTabla.setRowCount(0); // Limpia la tabla
        List<DetalleOrden> detalles = ordenControlador.obtenerDetalles(idOrden);
        for (DetalleOrden d : detalles) {
            modeloTabla.addRow(new Object[]{
                    d.getId(),
                    d.getNombreProducto(),
                    d.getCantidad(),
                    String.format("%.2f", d.getSubtotal()),
                    d.getEstadoCocina()
            });
        }
    }

    /**
     * Llena el ComboBox con todos los productos disponibles.
     */
    private void cargarComboBoxProductos() {
        List<Producto> productos = productoControlador.obtenerTodos();
        // Para que el JComboBox muestre el nombre del producto
        cbProductos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Producto) {
                    setText(((Producto) value).getNombre());
                }
                return this;
            }
        });

        for (Producto p : productos) {
            cbProductos.addItem(p);
        }
    }

    /**
     * Llama al controlador para agregar el item seleccionado a la orden.
     */
    private void agregarProducto() {
        Producto p = (Producto) cbProductos.getSelectedItem();
        int cantidad = (int) spinnerCantidad.getValue();

        // --- VERIFICACIÓN DE STOCK ---
        assert p != null;
        if (cantidad > p.getStock()) {
            JOptionPane.showMessageDialog(this,
                    "No hay suficiente stock. Solo quedan " + p.getStock() + " de " + p.getNombre(),
                    "Stock Insuficiente",
                    JOptionPane.ERROR_MESSAGE);
            return; // Detiene la operación
        }

        if (ordenControlador.agregarItemAOrden(idOrden, p, cantidad)) {
            JOptionPane.showMessageDialog(this, "Producto agregado.");
            cargarDetallesOrden(); // Recarga la tabla
            spinnerCantidad.setValue(1); // Resetea la cantidad
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Llama al controlador para eliminar el item seleccionado de la orden.
     */
    private void eliminarProducto() {
        int fila = tablaDetalles.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtenemos el ID del detalle (de la columna 0 oculta)
        int idDetalle = (int) modeloTabla.getValueAt(fila, 0);
        String nombreProd = (String) modeloTabla.getValueAt(fila, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que desea eliminar '" + nombreProd + "' de la orden?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (ordenControlador.eliminarItemDeOrden(idDetalle)) {
                JOptionPane.showMessageDialog(this, "Producto eliminado.");
                cargarDetallesOrden(); // Recarga la tabla
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}