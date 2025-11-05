package dao;
import modelo.Mesa;
import util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para acceso a datos de mesas.
 */
public class MesaDAO {

    private final Connection conn = ConexionDB.getConexion();

    public List<Mesa> obtenerMesas() {
        List<Mesa> mesas = new ArrayList<>();
        String sql = "SELECT id, numero, ocupada FROM mesas ORDER BY numero";

        try {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Mesa mesa = new Mesa(
                            rs.getInt("id"),
                            rs.getInt("numero"),
                            rs.getBoolean("ocupada")
                    );
                    mesas.add(mesa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mesas;
    }

    /**
     * Cambia el estado de la mesa (ocupada o libre).
     */
    public boolean actualizarEstadoMesa(int numeroMesa, boolean ocupada) {
        String sql = "UPDATE mesas SET ocupada = ? WHERE numero = ?";
        try {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBoolean(1, ocupada);
                ps.setInt(2, numeroMesa);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
