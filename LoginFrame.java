package vista;
import controlador.UsuarioControlador;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.JSplitPane;
import java.awt.Image;

/**
 * Ventana de inicio de sesión.
 */
public class LoginFrame extends JFrame {
    private final JTextField txtUsuario;
    private final JPasswordField txtContrasena;
    private final JButton btnIngresar;
    private final UsuarioControlador usuarioControlador;

    public LoginFrame() {
        setTitle("Iniciar Sesión -- Taquería");
        setSize(400, 500); // Tamaño inicial
        // (Opcional) Establece un tamaño mínimo para que no se colapse
        setMinimumSize(new Dimension(350, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); // Layout principal

        usuarioControlador = new UsuarioControlador();

        // --- 1. Panel del Logo (Solución sin clase nueva) ---

        // Carga la imagen primero para que la clase anónima pueda usarla
        final Image logoImage;
        URL imgURL = getClass().getResource("/recursos/logo.png");
        if (imgURL != null) {
            logoImage = new ImageIcon(imgURL).getImage();
        } else {
            System.err.println("No se pudo cargar la imagen: /recursos/logo.png");
            logoImage = null;
        }

        JPanel panelLogo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // Dibuja el fondo
                if (logoImage != null) {
                    // --- Lógica de escalado de imagen ---
                    int panelWidth = getWidth();
                    int panelHeight = getHeight();
                    int imgWidth = logoImage.getWidth(null);
                    int imgHeight = logoImage.getHeight(null);
                    double imgAspect = (double) imgWidth / imgHeight;
                    double panelAspect = (double) panelWidth / panelHeight;
                    int x, y, w, h;

                    // Lógica para centrar y escalar (letterboxing)
                    if (panelAspect > imgAspect) {
                        h = panelHeight;
                        w = (int) (h * imgAspect);
                        x = (panelWidth - w) / 2;
                        y = 0;
                    } else {
                        w = panelWidth;
                        h = (int) (w / imgAspect);
                        x = 0;
                        y = (panelHeight - h) / 2;
                    }
                    // Dibuja la imagen escalada
                    g.drawImage(logoImage, x, y, w, h, this);
                }
            }
        };

        JPanel panelLogin = new JPanel(new GridLayout(5, 1, 10, 10));
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Font fuenteCampos = new Font("Arial", Font.PLAIN, 16);
        Font fuenteEtiquetas = new Font("Arial", Font.BOLD, 14);

        JLabel lblUser = new JLabel("Usuario:");
        txtUsuario = new JTextField();
        JLabel lblPass = new JLabel("Contraseña:");
        txtContrasena = new JPasswordField();
        btnIngresar = new JButton("Ingresar");

        lblUser.setFont(fuenteEtiquetas);
        txtUsuario.setFont(fuenteCampos);
        lblPass.setFont(fuenteEtiquetas);
        txtContrasena.setFont(fuenteCampos);
        btnIngresar.setFont(fuenteEtiquetas);

        panelLogin.add(lblUser);
        panelLogin.add(txtUsuario);
        panelLogin.add(lblPass);
        panelLogin.add(txtContrasena);
        panelLogin.add(btnIngresar);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelLogo, panelLogin);
        splitPane.setResizeWeight(0.90);

        // Evita que el usuario pueda arrastrar el divisor
        splitPane.setEnabled(false);
        splitPane.setBorder(null); // Quita el borde del divisor

        // Añade el JSplitPane al centro del JFrame
        add(splitPane, BorderLayout.CENTER);

        // --- 4. Evento del Botón (Sin cambios) ---
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText().trim();
                String pass = new String(txtContrasena.getPassword());

                Usuario u = usuarioControlador.login(usuario, pass);
                if (u != null) {
                    abrirPanelPorRol(u);
                    dispose(); // Cierra el login
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Abre el panel correspondiente según el rol del usuario autenticado.
     * (Asumiendo que MeseroPanel es un JFrame)
     */
    private void abrirPanelPorRol(Usuario usuario) {
        switch (usuario.getRol().toLowerCase()) {
            case "admin":
                new AdminPanel(usuario).setVisible(true);
                break;

            case "mesero":
                new MeseroPanel(usuario).setVisible(true);
                break;

            case "cocinero":
                 new CocineroPanel(usuario).setVisible(true);
                break;
            case "cajero":
                new CajeroPanel(usuario).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Rol no reconocido: " + usuario.getRol());
        }
    }
}