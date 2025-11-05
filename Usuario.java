package modelo;

/**
 * Clase que representa un usuario del sistema.
 * Se utiliza para el login y control de roles.
 */
public class Usuario {
    private int id;
    private String nombre;
    private String usuario;
    private String contraseña;
    private String rol;

    // Constructor vacío
    public Usuario() {}

    // Constructor con todos los campos
    public Usuario(int id, String nombre, String usuario, String contraseña, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
