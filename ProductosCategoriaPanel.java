package vista;
import controlador.ProductoControlador;
import modelo.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel para mostrar productos filtrados por categoría (entrada, plato, bebida, postre).
 */
public class ProductosCategoriaPanel extends JPanel {

    private final String categoria;
    private final JTable tabla;
    private final DefaultTableModel modeloTabla;
    private final ProductoControlador controlador;

    public ProductosCategoriaPanel(String categoria) {
        this.categoria = categoria;
        this.controlador = new ProductoControlador();

        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Productos - Categoría: " + categoria, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable
            }
        };

        tabla = new JTable(modeloTabla);
        cargarProductos();

        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = controlador.obtenerPorCategoria(categoria);
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                    p.getId(), p.getNombre(), p.getPrecio(), p.getStock()
            });
        }
    }
}
