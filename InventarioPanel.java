package vista;

import controlador.ProductoControlador;
import modelo.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de gestión de inventario.
 */
public class InventarioPanel extends JPanel {
    private final JTable tabla;
    private final DefaultTableModel modeloTabla;
    private final ProductoControlador controlador;

    public InventarioPanel() {
        controlador = new ProductoControlador();
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Inventario de Productos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Categoría", "Precio", "Stock"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        cargarProductos();

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnAgregar.addActionListener(e -> mostrarFormulario(null));
        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                Producto p = obtenerProductoDesdeFila(fila);
                mostrarFormulario(p);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para editar.");
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
                int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (controlador.eliminarProducto(id)) {
                        cargarProductos();
                        JOptionPane.showMessageDialog(this, "Producto eliminado.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.");
            }
        });
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        //List<Producto> productos = controlador.obtenerPorCategoria(categoria);
        List<Producto> productos = controlador.obtenerTodos();
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                    p.getId(), p.getNombre(), p.getCategoria(), p.getPrecio(), p.getStock()
            });
        }
    }

    private Producto obtenerProductoDesdeFila(int fila) {
        int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        String nombre = modeloTabla.getValueAt(fila, 1).toString();
        String categoria = modeloTabla.getValueAt(fila, 2).toString();
        double precio = Double.parseDouble(modeloTabla.getValueAt(fila, 3).toString());
        int stock = Integer.parseInt(modeloTabla.getValueAt(fila, 4).toString());

        return new Producto(id, nombre, categoria, precio, stock);
    }

    private void mostrarFormulario(Producto producto) {
        JTextField txtNombre = new JTextField();
        JTextField txtCategoria = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtStock = new JTextField();

        if (producto != null) {
            txtNombre.setText(producto.getNombre());
            txtCategoria.setText(producto.getCategoria());
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
            txtStock.setText(String.valueOf(producto.getStock()));
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Categoría:"));
        panel.add(txtCategoria);
        panel.add(new JLabel("Precio:"));
        panel.add(txtPrecio);
        panel.add(new JLabel("Stock:"));
        panel.add(txtStock);

        int resultado = JOptionPane.showConfirmDialog(this, panel,
                producto == null ? "Agregar Producto" : "Editar Producto",
                JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            Producto nuevo = new Producto();
            nuevo.setNombre(txtNombre.getText());
            nuevo.setCategoria(txtCategoria.getText());
            nuevo.setPrecio(Double.parseDouble(txtPrecio.getText()));
            nuevo.setStock(Integer.parseInt(txtStock.getText()));

            boolean exito;
            if (producto == null) {
                exito = controlador.agregarProducto(nuevo);
            } else {
                nuevo.setId(producto.getId());
                //exito = controlador.actualizarStock();
                exito = controlador.actualizarProducto(nuevo);
            }

            if (exito) {
                cargarProductos();
                JOptionPane.showMessageDialog(this, "Operación exitosa.");
            } else {
                JOptionPane.showMessageDialog(this, "Error en la operación.");
            }
        }
    }
}

