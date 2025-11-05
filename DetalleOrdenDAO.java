package dao;
import modelo.DetalleOrden;
import modelo.Producto; // Necesario para obtener el precio
import util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar los items (detalles) de una orden.
 */
public class DetalleOrdenDAO {

    /**
     * Inserta un nuevo item (producto) en una orden.
     * @param detalle El objeto DetalleOrden con ordenId, productoId y cantidad.
     * @return true si se agregó correctamente.
     */
    public boolean agregarItem(DetalleOrden detalle) {
        String sql = "INSERT INTO detalle_orden(orden_id, producto_id, cantidad, subtotal, estado_cocina) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            // 1. Obtener el precio del producto para calcular el subtotal
            ProductoDAO productoDAO = new ProductoDAO(); // Usamos ProductoDAO para esto
            Producto p = productoDAO.obtenerPorId(detalle.getProductoId()); // Necesitarás crear este método

            if (p == null) {
                System.out.println("Error: Producto no encontrado para calcular subtotal.");
                return false;
            }

            double subtotal = p.getPrecio() * detalle.getCantidad();
            detalle.setSubtotal(subtotal);

            // 2. Insertar el detalle
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, detalle.getOrdenId());
                ps.setInt(2, detalle.getProductoId());
                ps.setInt(3, detalle.getCantidad());
                ps.setDouble(4, detalle.getSubtotal());
                ps.setString(5, "Pendiente"); // Estado inicial para la cocina

                boolean insertado = ps.executeUpdate() > 0;
                if (insertado) {
                    // 3. Actualizar el total de la orden principal
                    return new OrdenDAO().recalcularTotalOrden(detalle.getOrdenId());
                }
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar detalle de orden: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los items (detalles) de una orden específica.
     * (Actualizado para incluir nombre de producto y estado de cocina)
     */
    public List<DetalleOrden> obtenerDetallesPorOrden(int ordenId) {
        List<DetalleOrden> detalles = new ArrayList<>();
        // --- SQL ACTUALIZADO con JOIN ---
        String sql = "SELECT d.*, p.nombre " +
                "FROM detalle_orden d " +
                "JOIN productos p ON d.producto_id = p.id " +
                "WHERE d.orden_id = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ordenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleOrden d = new DetalleOrden();
                    d.setId(rs.getInt("id"));
                    d.setOrdenId(rs.getInt("orden_id"));
                    d.setProductoId(rs.getInt("producto_id"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setSubtotal(rs.getDouble("subtotal"));
                    d.setEstadoCocina(rs.getString("estado_cocina")); // Añadido
                    d.setNombreProducto(rs.getString("nombre")); // Añadido
                    detalles.add(d);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener detalles: " + e.getMessage());
        }
        return detalles;
    }

// ... (resto de la clase)

    /**
     * Elimina un item (detalle) de una orden.
     * @param idDetalle El ID de la fila en la tabla 'detalle_orden'.
     * @return true si se eliminó.
     */
    public boolean eliminarItem(int idDetalle) {
        int ordenId = -1;
        // Primero, obtenemos el orden_id para poder recalcular el total después
        String sqlSelect = "SELECT orden_id FROM detalle_orden WHERE id = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement psSelect = conn.prepareStatement(sqlSelect)) {
            psSelect.setInt(1, idDetalle);
            try (ResultSet rs = psSelect.executeQuery()) {
                if (rs.next()) {
                    ordenId = rs.getInt("orden_id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar ordenId para eliminar: " + e.getMessage());
            return false;
        }

        if (ordenId == -1) return false; // No se encontró el item

        // Ahora, eliminamos el item
        String sqlDelete = "DELETE FROM detalle_orden WHERE id = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement psDelete = conn.prepareStatement(sqlDelete)) {
            psDelete.setInt(1, idDetalle);
            boolean eliminado = psDelete.executeUpdate() > 0;
            if (eliminado) {
                // Actualizamos el total de la orden principal
                return new OrdenDAO().recalcularTotalOrden(ordenId);
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar item: " + e.getMessage());
        }
        return false;
    }
}