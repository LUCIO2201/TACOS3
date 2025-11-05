package modelo;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa una orden en la taquería.
 */
public class Orden {
    private int id;
    private int mesa;
    private String mesero;
    private String estado; // Abierta, Cerrada, etc.
    private double total;
    private List<Producto> productos;  // Productos en la orden

    public Orden() {}

    public Orden(int id, int mesa, String mesero, String estado, double total, List<Producto> productos) {
        this.id = id;
        this.mesa = mesa;
        this.mesero = mesero;
        this.estado = estado;
        this.total = total;
        this.productos = productos;
    }

    public int getId() {
        return id;
    }

    public int getMesa() {
        return mesa;
    }

    public String getMesero() {
        return mesero;
    }

    public String getEstado() {
        return estado;
    }

    public double getTotal() {
        return total;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public void setMesero(String mesero) {
        this.mesero = mesero;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    /**
     * Retorna un texto con la lista de productos (nombres separados por coma)
     */
    public String getProductosTexto() {
        if (productos == null || productos.isEmpty()) return "";
        return productos.stream()
                .map(Producto::getNombre)
                .collect(Collectors.joining(", "));
    }
}
