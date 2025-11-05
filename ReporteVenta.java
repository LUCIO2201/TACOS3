package modelo;
import java.sql.Date;

/**
 * Modelo DTO para representar una fila del reporte de ventas.
 * Puede ser una venta individual o un agregado (total por día).
 */
public class ReporteVenta {

    // Puede ser ID de venta o nulo si es un agregado
    private int id;

    // Puede ser la fecha de la venta o el día del agregado
    private Date fecha;

    // Total de la venta o total del día
    private double total;

    public ReporteVenta() {}

    public ReporteVenta(Date fecha, double total) {
        this.fecha = fecha;
        this.total = total;
    }

    public ReporteVenta(int id, Date fecha, double total) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
}