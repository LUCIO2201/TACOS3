package util;
import modelo.ReporteVenta;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Clase utilitaria para crear reportes de ventas en PDF usando Apache PDFBox.
 */
public class GeneradorPDF {

    public File generar(List<ReporteVenta> ventas, String tituloReporte) {
        // Define un nombre de archivo único
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreArchivo = "Reporte_" + tituloReporte.replace(" ", "") + "_" + timeStamp + ".pdf";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Iniciar el stream para "dibujar" en la página
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                // Configuración de fuentes
                PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font fontPlain = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin;
                float yPosition = yStart;
                float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
                float rowHeight = 20f;

                // --- TÍTULO ---
                contentStream.beginText();
                contentStream.setFont(fontBold, 18);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(tituloReporte);
                contentStream.endText();
                yPosition -= 30; // Espacio después del título

                // --- CABECERAS DE LA TABLA ---
                contentStream.setFont(fontBold, 12);
                float xPosition = margin;

                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition, yPosition);
                contentStream.showText("Fecha");
                contentStream.endText();
                xPosition += tableWidth / 2; // Mover a la siguiente columna

                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition, yPosition);
                contentStream.showText("Total del Día");
                contentStream.endText();
                yPosition -= rowHeight;

                // --- FILAS DE DATOS ---
                contentStream.setFont(fontPlain, 12);
                double granTotal = 0.0;

                for (ReporteVenta venta : ventas) {
                    xPosition = margin;

                    contentStream.beginText();
                    contentStream.newLineAtOffset(xPosition, yPosition);
                    contentStream.showText(venta.getFecha().toString());
                    contentStream.endText();
                    xPosition += tableWidth / 2;

                    String totalDia = String.format("$ %.2f", venta.getTotal());
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xPosition, yPosition);
                    contentStream.showText(totalDia);
                    contentStream.endText();
                    yPosition -= rowHeight;

                    granTotal += venta.getTotal();
                }

                // --- GRAN TOTAL ---
                yPosition -= rowHeight; // Espacio extra
                contentStream.setFont(fontBold, 14);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Gran Total:");
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(margin + (tableWidth / 2), yPosition);
                contentStream.showText(String.format("$ %.2f", granTotal));
                contentStream.endText();
            }

            File file = new File(nombreArchivo);
            document.save(file);
            return file;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}