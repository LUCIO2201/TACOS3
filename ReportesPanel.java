package vista;

import controlador.ReporteControlador;
import modelo.ReporteVenta;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Panel para ver reportes de ventas y generar PDFs.
 */
public class ReportesPanel extends JPanel {
    private JTable tabla;
    private final DefaultTableModel modeloTabla;
    private final ReporteControlador controlador;

    public ReportesPanel() {
        controlador = new ReporteControlador();
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Reportes de Ventas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"Fecha", "Total del Día"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnSemanal = new JButton("Ver Semanal");
        JButton btnMensual = new JButton("Ver Mensual");
        JButton btnGenerarPDF = new JButton("Generar PDF");
        panelBotones.add(btnSemanal);
        panelBotones.add(btnMensual);
        panelBotones.add(btnGenerarPDF);

        add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnSemanal.addActionListener(e -> cargarReporte("semanal"));
        btnMensual.addActionListener(e -> cargarReporte("mensual"));
        btnGenerarPDF.addActionListener(e -> generarPDF());

        // Carga inicial (semanal por defecto)
        cargarReporte("semanal");
    }

    private void cargarReporte(String tipo) {
        modeloTabla.setRowCount(0);
        List<ReporteVenta> ventas = tipo.equals("semanal")
                ? controlador.obtenerVentasSemanales()
                : controlador.obtenerVentasMensuales();

        for (ReporteVenta r : ventas) {
            modeloTabla.addRow(new Object[]{
                    r.getFecha(),
                    String.format("%.2f", r.getTotal())
            });
        }
    }


    private void generarPDF() {
        String[] opciones = {"Reporte Semanal", "Reporte Mensual"};
        String tipo = (String) JOptionPane.showInputDialog(this,
                "¿Qué reporte desea generar en PDF?",
                "Generar PDF",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (tipo == null) {
            return; // El usuario canceló
        }

        // 2. Obtener los datos según la selección
        List<ReporteVenta> ventas;
        if (tipo.equals("Reporte Semanal")) {
            ventas = controlador.obtenerVentasSemanales();
        } else {
            ventas = controlador.obtenerVentasMensuales();
        }

        if (ventas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay datos de ventas para este reporte.", "Sin Datos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        File archivo = controlador.generarReportePDF(ventas, tipo);
        if (archivo != null && archivo.exists()) {
            JOptionPane.showMessageDialog(this, "PDF generado exitosamente:\n" + archivo.getAbsolutePath());
            try {
                // Abre el archivo PDF con el programa predeterminado
                Desktop.getDesktop().open(archivo);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo abrir el PDF automáticamente.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se pudo generar el PDF.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
