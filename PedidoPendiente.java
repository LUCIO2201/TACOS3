
package modelo;

/**
 * Modelo DTO para la vista de cocina. Combina la mesa, el producto y el estado.
 */
public class PedidoPendiente {
    private int ordenId;
    private int mesa;
    private int productoId;
    private String nombreProducto;
    private String estadoCocina;

    public PedidoPendiente() {}

    // Getters y Setters
    public int getOrdenId() { return ordenId; }
    public void setOrdenId(int ordenId) { this.ordenId = ordenId; }

    public int getMesa() { return mesa; }
    public void setMesa(int mesa) { this.mesa = mesa; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getEstadoCocina() { return estadoCocina; }
    public void setEstadoCocina(String estadoCocina) { this.estadoCocina = estadoCocina; }
}