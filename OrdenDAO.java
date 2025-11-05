package dao;
import modelo.Orden;
import modelo.Producto;
import util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para acceso a datos de órdenes.
 */
public class OrdenDAO {

    private final Connection conn = ConexionDB.getConexion();

    /**
     * Crea una orden sin productos inicialmente.
     */
    public boolean crearOrden(Orden orden) {
        String sql = "INSERT INTO ordenes (mesa, mesero, estado, total) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, orden.getMesa());
            ps.setString(2, orden.getMesero());
            ps.setString(3, orden.getEstado());
            ps.setDouble(4, 0.0); // total inicial 0
            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        orden.setId(idGenerado);
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene las órdenes abiertas de un mesero.
     */
    public List<Orden> obtenerOrdenesPorMesero(String mesero) {
        List<Orden> ordenes = new ArrayList<>();
        String sql = "SELECT * FROM ordenes WHERE mesero = ? AND estado = 'Abierta' ORDER BY id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mesero);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Orden orden = new Orden();
                    orden.setId(rs.getInt("id"));
                    orden.setMesa(rs.getInt("mesa"));
                    orden.setMesero(rs.getString("mesero"));
                    orden.setEstado(rs.getString("estado"));
                    orden.setTotal(rs.getDouble("total"));
                    orden.setProductos(obtenerProductosDeOrden(orden.getId()));
                    ordenes.add(orden);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordenes;
    }

    /**
     * Obtiene productos asignados a una orden.
     * (ACTUALIZADO para leer de 'detalle_orden' en lugar de 'orden_productos')
     */
    private List<Producto> obtenerProductosDeOrden(int idOrden) {
        List<Producto> productos = new ArrayList<>();

        // --- INICIO DE LA CORRECCIÓN ---
        // SQL CORREGIDO: Lee de 'detalle_orden' y hace JOIN con 'productos'
        String sql = "SELECT p.id, p.nombre, p.categoria, p.precio, p.stock, d.cantidad " +
                "FROM productos p " +
                "JOIN detalle_orden d ON p.id = d.producto_id " + // <-- TABLA CORREGIDA
                "WHERE d.orden_id = ?";
        // --- FIN DE LA CORRECCIÓN ---

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOrden);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // (Usamos el constructor de Producto que corregimos anteriormente)
                    Producto p = new Producto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("categoria"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    );

                    // NOTA: El modelo 'Orden.java'
                    // solo almacena List<Producto>, por lo que getProductosTexto()
                    // no mostrará la cantidad (ej. "3x Tacos").
                    // Sin embargo, esta corrección soluciona el error que te impide iniciar sesión.

                    // Añadimos el producto (una vez por cada cantidad)
                    int cantidad = rs.getInt("cantidad");
                    for(int i=0; i < cantidad; i++) {
                        productos.add(p);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * Actualiza el total y estado de una orden.
     */
    public boolean actualizarOrden(Orden orden) {
        String sql = "UPDATE ordenes SET total = ?, estado = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, orden.getTotal());
            ps.setString(2, orden.getEstado());
            ps.setInt(3, orden.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene TODAS las órdenes abiertas, sin importar el mesero.
     * (Usado por el CajeroPanel)
     */
    public List<Orden> obtenerTodasLasOrdenesAbiertas() {
        List<Orden> ordenes = new ArrayList<>();
        String sql = "SELECT * FROM ordenes WHERE estado = 'Abierta' ORDER BY id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Orden orden = new Orden();
                    orden.setId(rs.getInt("id"));
                    orden.setMesa(rs.getInt("mesa"));
                    orden.setMesero(rs.getString("mesero"));
                    orden.setEstado(rs.getString("estado"));
                    orden.setTotal(rs.getDouble("total"));
                    orden.setProductos(obtenerProductosDeOrden(orden.getId()));
                    ordenes.add(orden);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordenes;
    }

    /**
     *
     * Necesario para que el controlador actualice el total.
     */
    public Orden obtenerOrdenPorId(int idOrden) {
        String sql = "SELECT * FROM ordenes WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOrden);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Orden orden = new Orden();
                    orden.setId(rs.getInt("id"));
                    orden.setMesa(rs.getInt("mesa"));
                    orden.setMesero(rs.getString("mesero"));
                    orden.setEstado(rs.getString("estado"));
                    orden.setTotal(rs.getDouble("total"));
                    orden.setProductos(obtenerProductosDeOrden(orden.getId()));
                    return orden;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * NUEVO: Recalcula el total de la orden basado en la suma de sus detalles.
     */
    public boolean recalcularTotalOrden(int ordenId) {
        String sqlSubtotal = "SELECT SUM(subtotal) AS nuevoTotal FROM detalle_orden WHERE orden_id = ?";
        String sqlUpdate = "UPDATE ordenes SET total = ? WHERE id = ?";

        double nuevoTotal = 0.0;

        try (Connection conn = ConexionDB.getConexion()) {
            // 1. Calcular el nuevo total
            assert conn != null;
            try (PreparedStatement psSubtotal = conn.prepareStatement(sqlSubtotal)) {
                psSubtotal.setInt(1, ordenId);
                try (ResultSet rs = psSubtotal.executeQuery()) {
                    if (rs.next()) {
                        nuevoTotal = rs.getDouble("nuevoTotal");
                    }
                }
            }

            // 2. Actualizar la tabla 'ordenes'
            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                psUpdate.setDouble(1, nuevoTotal);
                psUpdate.setInt(2, ordenId);
                return psUpdate.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
