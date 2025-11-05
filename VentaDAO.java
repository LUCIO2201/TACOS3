package dao;
import modelo.Venta;
import util.ConexionDB;
import java.sql.*;

/**
 * DAO para registrar ventas realizadas.
 */
public class VentaDAO {

    public boolean registrarVenta(Venta v) {
        String sql = "INSERT INTO ventas(orden_id, fecha, total, metodo_pago) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, v.getOrdenId());
                ps.setTimestamp(2, v.getFecha());
                ps.setDouble(3, v.getTotal());
                ps.setString(4, v.getMetodoPago());

                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            System.out.println("Error al registrar venta: " + e.getMessage());
            return false;
        }
    }
}

