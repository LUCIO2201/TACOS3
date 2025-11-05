package vista;

import controlador.MesaControlador;
import modelo.Mesa;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel para mostrar el estado de las mesas y asignar órdenes.
 */
public class MesasPanel extends JPanel {

    private final JPanel mesasGrid;
    private final MesaControlador mesaControlador;
    private final String mesero;

    public MesasPanel(String mesero) {
        this.mesero = mesero;
        this.mesaControlador = new MesaControlador();

        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Mesas - Estado y Asignación", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        mesasGrid = new JPanel(new GridLayout(3, 3, 10, 10)); // Ejemplo 3x3 mesas
        mesasGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mesasGrid, BorderLayout.CENTER);

        cargarMesas();
    }

    private void cargarMesas() {
        mesasGrid.removeAll();
        List<Mesa> mesas = mesaControlador.obtenerMesas();

        for (Mesa mesa : mesas) {
            JButton btnMesa = new JButton("Mesa " + mesa.getNumero());
            btnMesa.setOpaque(true);
            btnMesa.setBorderPainted(false);
            btnMesa.setForeground(Color.WHITE);

            if (mesa.isOcupada()) {
                btnMesa.setBackground(Color.RED);
            } else {
                btnMesa.setBackground(Color.GREEN.darker());
            }

            btnMesa.addActionListener(e -> {
                if (mesa.isOcupada()) {
                    // Abrir orden asociada
                    JOptionPane.showMessageDialog(this,
                            "Mesa " + mesa.getNumero() + " está ocupada.\n" +
                                    "Abrir orden para modificar o cerrar cuenta.");
                    // Aquí iría lógica para abrir orden/modificar
                } else {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Mesa " + mesa.getNumero() + " está libre. ¿Asignar orden?",
                            "Asignar Orden",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Asignar orden a mesa
                        boolean exito = mesaControlador.asignarOrdenAMesa(mesa.getNumero(), mesero);
                        if (exito) {
                            JOptionPane.showMessageDialog(this, "Orden asignada a la mesa.");
                            cargarMesas(); // refrescar estado
                        } else {
                            JOptionPane.showMessageDialog(this, "Error al asignar orden.");
                        }
                    }
                }
            });

            mesasGrid.add(btnMesa);
        }

        revalidate();
        repaint();
    }
}
