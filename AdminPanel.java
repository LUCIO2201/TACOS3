package vista;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel principal del administrador.
 */
public class AdminPanel extends JFrame {
    private Usuario usuario;
    private final JPanel panelContenido;

    public AdminPanel(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Panel Administrador - Taquería");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Encabezado
        JLabel lblTitulo = new JLabel("Panel del Administrador", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Barra lateral
        JPanel panelLateral = new JPanel(new GridLayout(6, 1, 5, 5));
        panelLateral.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnUsuarios = new JButton("Ver Usuarios");
        JButton btnReportes = new JButton("Reportes de Ventas");
        JButton btnInventario = new JButton("Inventario");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        panelLateral.add(btnUsuarios);
        panelLateral.add(btnReportes);
        panelLateral.add(btnInventario);
        panelLateral.add(Box.createVerticalGlue());
        panelLateral.add(btnCerrarSesion);

        add(panelLateral, BorderLayout.WEST);

        // Panel de contenido dinámico
        panelContenido = new JPanel(new BorderLayout());
        add(panelContenido, BorderLayout.CENTER);

        // Eventos
        btnUsuarios.addActionListener(e -> mostrarPanelUsuarios());
        btnReportes.addActionListener(e -> mostrarPanelReportes());
        btnInventario.addActionListener(e -> mostrarPanelInventario());
        btnCerrarSesion.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private void mostrarPanelUsuarios() {
        panelContenido.removeAll();
        panelContenido.add(new UsuariosPanel(), BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarPanelReportes() {
        panelContenido.removeAll();
        panelContenido.add(new ReportesPanel(), BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarPanelInventario() {
        panelContenido.removeAll();
        panelContenido.add(new InventarioPanel(), BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
}

