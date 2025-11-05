package vista;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class MeseroPanel extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public MeseroPanel(Usuario usuario) {

        setTitle("Panel del Mesero");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Panel del Mesero - Usuario: " + usuario.getUsuario(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        // Barra lateral
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnRecogerPedidos = new JButton("Recoger Pedidos Listos");
        btnRecogerPedidos.setBackground(new Color(152, 251, 152)); // Verde pálido

        JButton btnEntradas = new JButton("Ver Entradas");
        JButton btnPlatos = new JButton("Ver Platos");
        JButton btnBebidas = new JButton("Ver Bebidas");
        JButton btnPostres = new JButton("Ver Postres");
        JButton btnOrdenes = new JButton("Ver Órdenes");
        JButton btnMesas = new JButton("Ver Mesas");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        // --- INICIO DE CAMBIOS ---
        // 2. Añadimos el botón al menú
        menuPanel.add(btnRecogerPedidos);
        // --- FIN DE CAMBIOS ---

        menuPanel.add(btnEntradas);
        menuPanel.add(btnPlatos);
        menuPanel.add(btnBebidas);
        menuPanel.add(btnPostres);
        menuPanel.add(btnOrdenes);
        menuPanel.add(btnMesas);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnCerrarSesion);

        add(menuPanel, BorderLayout.WEST);

        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.add(new PanelRecoger(usuario.getUsuario()), "Recoger");
        contentPanel.add(new ProductosCategoriaPanel("entrada"), "Entradas");
        contentPanel.add(new ProductosCategoriaPanel("plato"), "Platos");
        contentPanel.add(new ProductosCategoriaPanel("bebida"), "Bebidas");
        contentPanel.add(new ProductosCategoriaPanel("postre"), "Postres");
        contentPanel.add(new OrdenesPanel(usuario.getUsuario()), "Ordenes");
        contentPanel.add(new MesasPanel(usuario.getUsuario()), "Mesas");

        add(contentPanel, BorderLayout.CENTER);
        btnRecogerPedidos.addActionListener(e -> cardLayout.show(contentPanel, "Recoger"));

        btnEntradas.addActionListener(e -> cardLayout.show(contentPanel, "Entradas"));
        btnPlatos.addActionListener(e -> cardLayout.show(contentPanel, "Platos"));
        btnBebidas.addActionListener(e -> cardLayout.show(contentPanel, "Bebidas"));
        btnPostres.addActionListener(e -> cardLayout.show(contentPanel, "Postres"));
        btnOrdenes.addActionListener(e -> cardLayout.show(contentPanel, "Ordenes"));
        btnMesas.addActionListener(e -> cardLayout.show(contentPanel, "Mesas"));

        btnCerrarSesion.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });
    }
}