package dao;

import modelo.PedidoPendiente;
import util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para las operaciones de la cocina.
 */
public class CocineroDAO {

    /**
     * Obtiene todos los productos de órdenes que están en estado 'Pendiente'.
     */
    public List<PedidoPendiente> obtenerPendientes() {
        List<PedidoPendiente> lista = new ArrayList<>();
        String sql = "SELECT op.orden_id, o.mesa, op.producto_id, p.nombre, op.estado_cocina " +
                "FROM orden_productos op " +
                "JOIN ordenes o ON op.orden_id = o.id " +
                "JOIN productos p ON op.producto_id = p.id " +
                "WHERE op.estado_cocina = 'Pendiente' " +
                "ORDER BY o.id, p.id";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PedidoPendiente p = new PedidoPendiente();
                p.setOrdenId(rs.getInt("orden_id"));
                p.setMesa(rs.getInt("mesa"));
                p.setProductoId(rs.getInt("producto_id"));
                p.setNombreProducto(rs.getString("nombre"));
                p.setEstadoCocina(rs.getString("estado_cocina"));
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener pedidos pendientes: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Actualiza el estado de un producto específico en una orden específica.
     */
    public boolean actualizarEstadoItem(int ordenId, int productoId, String estado) {
        String sql = "UPDATE orden_productos SET estado_cocina = ? WHERE orden_id = ? AND producto_id = ? LIMIT 1";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, ordenId);
            ps.setInt(3, productoId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar estado del ítem: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los productos de órdenes que están en estado 'Listo'
     * y pertenecen a un mesero específico.
     */
    public List<PedidoPendiente> obtenerListosPorMesero(String nombreMesero) {
        List<PedidoPendiente> lista = new ArrayList<>();
        String sql = "SELECT op.orden_id, o.mesa, op.producto_id, p.nombre, op.estado_cocina " +
                "FROM orden_productos op " +
                "JOIN ordenes o ON op.orden_id = o.id " +
                "JOIN productos p ON op.producto_id = p.id " +
                "WHERE op.estado_cocina = 'Listo' AND o.mesero = ? " + // Filtrado por mesero
                "ORDER BY o.id, p.id";

        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, nombreMesero); // Asigna el nombre del mesero al query

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        PedidoPendiente p = new PedidoPendiente();
                        p.setOrdenId(rs.getInt("orden_id"));
                        p.setMesa(rs.getInt("mesa"));
                        p.setProductoId(rs.getInt("producto_id"));
                        p.setNombreProducto(rs.getString("nombre"));
                        p.setEstadoCocina(rs.getString("estado_cocina"));
                        lista.add(p);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener pedidos listos por mesero: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Actualiza el estado de un producto a 'Entregado'.
     * (Este método es una extensión de actualizarEstadoItem, pero es bueno tenerlo separado por lógica)
     */
    public boolean marcarComoEntregado(int ordenId, int productoId) {
        return actualizarEstadoItem(ordenId, productoId, "Entregado");
    }
}