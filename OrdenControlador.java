package controlador;
import dao.DetalleOrdenDAO;
import dao.OrdenDAO;
import modelo.DetalleOrden;
import modelo.Orden;
import modelo.Producto;
import javax.swing.JOptionPane;
import java.util.List;
import vista.ModificarOrdenDialog;
import java.awt.Frame;
/**
 * Controlador para la lógica de órdenes (Refactorizado para Cantidad).
 */
public class OrdenControlador {

    private final OrdenDAO ordenDAO;
    private final DetalleOrdenDAO detalleDAO; // DAO para los items

    public OrdenControlador() {
        this.ordenDAO = new OrdenDAO();
        this.detalleDAO = new DetalleOrdenDAO(); // Inicializar el nuevo DAO
    }

    public List<Orden> obtenerOrdenesPorMesero(String mesero) {
        return ordenDAO.obtenerOrdenesPorMesero(mesero);
    }

    public boolean crearOrden(int mesa, String mesero) {
        // ... (Este método no cambia)
        Orden orden = new Orden();
        orden.setMesa(mesa);
        orden.setMesero(mesero);
        orden.setEstado("Abierta");
        orden.setTotal(0.0);
        return ordenDAO.crearOrden(orden);
    }

    /**
     * Agrega un item (producto y cantidad) a la orden.
     */
    public boolean agregarItemAOrden(int idOrden, Producto producto, int cantidad) {
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a cero.");
            return false;
        }

        DetalleOrden detalle = new DetalleOrden();
        detalle.setOrdenId(idOrden);
        detalle.setProductoId(producto.getId());
        detalle.setCantidad(cantidad);

        return detalleDAO.agregarItem(detalle);
    }

    /**
     * Elimina un item (detalle) de la orden.
     */
    public boolean eliminarItemDeOrden(int idDetalle) {
        return detalleDAO.eliminarItem(idDetalle);
    }

    /**
     * Obtiene la lista de detalles (items) de una orden.
     */
    public List<DetalleOrden> obtenerDetalles(int ordenId) {
        return detalleDAO.obtenerDetallesPorOrden(ordenId);
    }


    public boolean cerrarOrden(Orden orden) {
        orden.setEstado("Cerrada");
        return ordenDAO.actualizarOrden(orden);
    }

    public boolean cerrarCuenta(int idOrden) {
        Orden orden = ordenDAO.obtenerOrdenPorId(idOrden);
        if (orden != null) {
            MesaControlador mesaControlador = new MesaControlador();
            mesaControlador.cambiarEstadoMesa(orden.getMesa(), false);

            return cerrarOrden(orden);
        }
        return false;
    }

    public List<Orden> obtenerTodasLasOrdenesAbiertas() {
        return ordenDAO.obtenerTodasLasOrdenesAbiertas();
    }

    public void modificarOrdenInteractiva(int idOrden) {

        ModificarOrdenDialog dialog = new ModificarOrdenDialog(null, idOrden);
        dialog.setVisible(true);
    }
}