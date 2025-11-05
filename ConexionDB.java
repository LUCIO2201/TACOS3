package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para gestionar la conexión a la base de datos MySQL.
 * Requiere tener el conector JDBC en el classpath.
 */
public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/taqueriadb";
    private static final String USER = "root";
    private static final String PASS = "220105";

    /**
     * Retorna una conexión válida a la base de datos.
     * @return Objeto Connection o null si hay error.
     */
    public static Connection getConexion() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Error al conectar a la BD: " + e.getMessage());
            return null;
        }
    }
}
