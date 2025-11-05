package controlador;

import dao.ReporteDAO;
import modelo.ReporteVenta;
import util.GeneradorPDF;
import java.io.File;
import java.util.List;

public class ReporteControlador {

    private final ReporteDAO dao;
    private final GeneradorPDF generador; // <-- 2. Añadir el generador

    public ReporteControlador() {
        this.dao = new ReporteDAO();
        this.generador = new GeneradorPDF(); // <-- 3. Inicializarlo
    }

    public List<ReporteVenta> obtenerVentasSemanales() {
        return dao.obtenerVentasSemanales();
    }

    public List<ReporteVenta> obtenerVentasMensuales() {
        return dao.obtenerVentasMensuales();
    }

    /**
     * Llama al GeneradorPDF para crear el archivo.
     * @param ventas La lista de datos a imprimir.
     * @param titulo El título del reporte.
     * @return El archivo File generado.
     */
    public File generarReportePDF(List<ReporteVenta> ventas, String titulo) {
        // 4. Reemplazar el stub por la llamada real
        if (ventas == null || ventas.isEmpty()) {
            System.err.println("No hay datos para generar el PDF.");
            return null;
        }
        return generador.generar(ventas, titulo);
    }
}