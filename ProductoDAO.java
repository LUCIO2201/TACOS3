package dao;
import modelo.Producto;
import util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD sobre productos.
 */
public class ProductoDAO {

    /**
     * Listar todos los productos
     */
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getInt("id"));
                    p.setNombre(rs.getString("nombre"));
                    p.setCategoria(rs.getString("categoria"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setStock(rs.getInt("stock"));
                    productos.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * Obtiene un producto por su ID.
     */
    public Producto obtenerPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Usamos el constructor corregido
                    return new Producto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("categoria"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean actualizarProducto(Producto p) {
        String sql = "UPDATE productos SET nombre = ?, categoria = ?, precio = ?, stock = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getCategoria());
            stmt.setDouble(3, p.getPrecio());
            stmt.setInt(4, p.getStock());
            stmt.setInt(5, p.getId());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lista todos los productos de una categoría específica.
     */
    public List<Producto> listarPorCategoria(String categoria) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE categoria = ?";

        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, categoria);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    // CORRECCIÓN: Usar el constructor correcto
                    Producto p = new Producto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            categoria, // Se pasa la categoría que ya se conoce
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    );
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarStock(int productoId, int nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, nuevoStock);
                ps.setInt(2, productoId);
                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }

    public boolean insertarProducto(Producto p) {
        String sql = "INSERT INTO productos(nombre, categoria, precio, stock) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, p.getNombre());
                ps.setString(2, p.getCategoria());
                ps.setDouble(3, p.getPrecio());
                ps.setInt(4, p.getStock());
                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);
                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    // ... (dentro de la clase ProductoDAO)

    /**
     * Descuenta una cantidad específica del stock de un producto.
     * La consulta se asegura de no descontar si no hay suficiente stock.
     * @param productoId El ID del producto a descontar.
     * @param cantidad La cantidad a restar.
     * @return true si la actualización fue exitosa, false si no (ej. no había stock).
     */
    public boolean descontarStock(int productoId, int cantidad) {
        // Esta consulta SQL resta la cantidad del stock actual
        // El "AND stock >= ?" evita que el inventario quede negativo
        String sql = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";

        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, cantidad);
                ps.setInt(2, productoId);
                ps.setInt(3, cantidad); // Para la comprobación 'stock >='

                int filasAfectadas = ps.executeUpdate();

                // Si las filas afectadas son 0, significa que no había stock suficiente
                return filasAfectadas > 0;

            }
        } catch (SQLException e) {
            System.out.println("Error al descontar stock: " + e.getMessage());
            return false;
        }
    }
}

