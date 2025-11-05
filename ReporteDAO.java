
package dao;
import modelo.ReporteVenta;
import util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para generar reportes desde la tabla 'ventas'.
 */
public class ReporteDAO {

    /**
     * Obtiene las ventas totales agrupadas por día de los últimos 7 días.
     */
    public List<ReporteVenta> obtenerVentasSemanales() {
        List<ReporteVenta> reporte = new ArrayList<>();
        // Consulta SQL que agrupa las ventas por día y suma sus totales
        String sql = "SELECT DATE(fecha) as dia, SUM(total) as total_dia " +
                "FROM ventas " +
                "WHERE fecha >= CURDATE() - INTERVAL 7 DAY " +
                "GROUP BY DATE(fecha) " +
                "ORDER BY dia DESC";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reporte.add(new ReporteVenta(
                        rs.getDate("dia"),
                        rs.getDouble("total_dia")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener reporte semanal: " + e.getMessage());
        }
        return reporte;
    }

    /**
     * Obtiene las ventas totales agrupadas por día de los últimos 30 días.
     */
    public List<ReporteVenta> obtenerVentasMensuales() {
        List<ReporteVenta> reporte = new ArrayList<>();
        String sql = "SELECT DATE(fecha) as dia, SUM(total) as total_dia " +
                "FROM ventas " +
                "WHERE fecha >= CURDATE() - INTERVAL 30 DAY " +
                "GROUP BY DATE(fecha) " +
                "ORDER BY dia DESC";

        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    reporte.add(new ReporteVenta(
                            rs.getDate("dia"),
                            rs.getDouble("total_dia")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener reporte mensual: " + e.getMessage());
        }
        return reporte;
    }
}