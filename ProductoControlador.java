package controlador;
import dao.ProductoDAO;
import modelo.Producto;
import java.util.List;

/**
 * Controlador para productos. Maneja la lógica entre la vista y el DAO.
 */
public class ProductoControlador {
    private final ProductoDAO dao;

    public ProductoControlador() {
        dao = new ProductoDAO();
    }

    public List<Producto> obtenerTodos() {
        return dao.listarTodos();
    }

    public boolean actualizarProducto(Producto p) {
        return dao.actualizarProducto(p);
    }

    public List<Producto> obtenerPorCategoria(String categoria) {
        return dao.listarPorCategoria(categoria);
    }

    public boolean actualizarStock(int productoId, int nuevoStock) {
        return dao.actualizarStock(productoId, nuevoStock);
    }

    public boolean agregarProducto(Producto p) {
        return dao.insertarProducto(p);
    }

    public boolean eliminarProducto(int id) {
        return dao.eliminarProducto(id);
    }

    /**
     * Llama al DAO para descontar el stock de un producto.
     */
    public boolean descontarStock(int productoId, int cantidad) {
        return dao.descontarStock(productoId, cantidad);
    }
}