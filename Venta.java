package modelo;
import java.sql.Timestamp;

/**
 * Representa una venta registrada (después del pago).
 */
public class Venta {
    private int id;
    private int ordenId;
    private Timestamp fecha;
    private double total;
    private String metodoPago;

    public Venta() {}

    public Venta(int id, int ordenId, Timestamp fecha, double total, String metodoPago) {
        this.id = id;
        this.ordenId = ordenId;
        this.fecha = fecha;
        this.total = total;
        this.metodoPago = metodoPago;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrdenId() { return ordenId; }
    public void setOrdenId(int ordenId) { this.ordenId = ordenId; }

    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}

