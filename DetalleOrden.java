package modelo;

/**
 * Detalle de un producto en una orden específica.
 * (Actualizado para incluir campos de UI)
 */
public class DetalleOrden {
    private int id;
    private int ordenId;
    private int productoId;
    private int cantidad;
    private double subtotal;
    private String nombreProducto; // Para mostrar en la tabla
    private String estadoCocina;   // Para la lógica de la cocina

    public DetalleOrden() {}

    // Getters y Setters (incluyendo los nuevos)

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrdenId() { return ordenId; }
    public void setOrdenId(int ordenId) { this.ordenId = ordenId; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getEstadoCocina() { return estadoCocina; }
    public void setEstadoCocina(String estadoCocina) { this.estadoCocina = estadoCocina; }
}