package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;
import util.ConexionDB;

/**
 * DAO para gestionar las operaciones CRUD de usuarios.
 */
public class UsuarioDAO {

    /**
     * Autentica un usuario por nombre y contraseña.
     * @param user Nombre de usuario.
     * @param pass Contraseña.
     * @return Usuario válido si existe, null si no.
     */
    public Usuario autenticar(String user, String pass) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contraseña = ?";
        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, user);
                ps.setString(2, pass);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Usuario(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("usuario"),
                                rs.getString("contraseña"),
                                rs.getString("rol")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al autenticar usuario: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retorna la lista completa de usuarios.
     */
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Usuario u = new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("usuario"),
                            rs.getString("contraseña"),
                            rs.getString("rol")
                    );
                    lista.add(u);
                }

            }
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     */
    public boolean agregar(Usuario u) {
        String sql = "INSERT INTO usuarios(nombre, usuario, contraseña, rol) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, u.getNombre());
                ps.setString(2, u.getUsuario());
                ps.setString(3, u.getContraseña()); // Se recomienda cifrar
                ps.setString(4, u.getRol());

                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            System.out.println("Error al agregar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Edita un usuario existente.
     */
    public boolean editar(Usuario u) {
        String sql = "UPDATE usuarios SET nombre = ?, usuario = ?, contraseña = ?, rol = ? WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, u.getNombre());
                ps.setString(2, u.getUsuario());
                ps.setString(3, u.getContraseña());
                ps.setString(4, u.getRol());
                ps.setInt(5, u.getId());

                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            System.out.println("Error al editar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un usuario por ID.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);
                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}

