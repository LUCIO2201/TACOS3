package vista;
import controlador.UsuarioControlador;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel CRUD de usuarios.
 */
public class UsuariosPanel extends JPanel {
    private final UsuarioControlador controlador;
    private final JTable tabla;
    private final DefaultTableModel modeloTabla;

    public UsuariosPanel() {
        controlador = new UsuarioControlador();
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestión de Usuarios", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // Tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Usuario", "Rol"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        cargarUsuarios();

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnAgregar.addActionListener(e -> mostrarFormulario(null));
        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                Usuario u = obtenerUsuarioDesdeFila(fila);
                mostrarFormulario(u);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario para editar.");
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
                int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (controlador.eliminarUsuario(id)) {
                        cargarUsuarios();
                        JOptionPane.showMessageDialog(this, "Usuario eliminado.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.");
            }
        });
    }

    private void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        List<Usuario> usuarios = controlador.listarUsuarios();
        for (Usuario u : usuarios) {
            modeloTabla.addRow(new Object[]{u.getId(), u.getNombre(), u.getUsuario(), u.getRol()});
        }
    }

    private Usuario obtenerUsuarioDesdeFila(int fila) {
        int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        String nombre = modeloTabla.getValueAt(fila, 1).toString();
        String usuario = modeloTabla.getValueAt(fila, 2).toString();
        String rol = modeloTabla.getValueAt(fila, 3).toString();
        Usuario u = new Usuario();
        u.setId(id);
        u.setNombre(nombre);
        u.setUsuario(usuario);
        u.setRol(rol);
        return u;
    }

    private void mostrarFormulario(Usuario u) {
        JTextField txtNombre = new JTextField();
        JTextField txtUsuario = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        String[] roles = {"admin", "mesero", "cocinero", "cajero"};
        JComboBox<String> cbRol = new JComboBox<>(roles);

        if (u != null) {
            txtNombre.setText(u.getNombre());
            txtUsuario.setText(u.getUsuario());
            cbRol.setSelectedItem(u.getRol());
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Usuario:"));
        panel.add(txtUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPass);
        panel.add(new JLabel("Rol:"));
        panel.add(cbRol);

        int resultado = JOptionPane.showConfirmDialog(this, panel, u == null ? "Agregar Usuario" : "Editar Usuario",
                JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            Usuario nuevo = new Usuario();
            nuevo.setNombre(txtNombre.getText());
            nuevo.setUsuario(txtUsuario.getText());
            nuevo.setContraseña(new String(txtPass.getPassword()));
            nuevo.setRol(cbRol.getSelectedItem().toString());

            boolean exito;
            if (u == null) {
                exito = controlador.agregarUsuario(nuevo);
            } else {
                nuevo.setId(u.getId());
                exito = controlador.actualizarUsuario(nuevo);
            }

            if (exito) {
                cargarUsuarios();
                JOptionPane.showMessageDialog(this, "Operación exitosa.");
            } else {
                JOptionPane.showMessageDialog(this, "Error en la operación.");
            }
        }
    }
}

