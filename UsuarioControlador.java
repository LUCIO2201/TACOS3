package controlador;
import dao.UsuarioDAO;
import modelo.Usuario;
import java.util.List;

/**
 * Controlador para usuarios, incluye login y CRUD.
 */
public class UsuarioControlador {
    private final UsuarioDAO dao;

    public UsuarioControlador() {
        dao = new UsuarioDAO();
    }

    public Usuario login(String usuario, String contraseña) {
        return dao.autenticar(usuario, contraseña);
    }

    public List<Usuario> listarUsuarios() {
        return dao.listar();
    }

    public boolean agregarUsuario(Usuario u) {
        return dao.agregar(u);
    }

    public boolean eliminarUsuario(int id) {
        return dao.eliminar(id);
    }

    public boolean actualizarUsuario(Usuario u) {
        return dao.editar(u);
    }
}

