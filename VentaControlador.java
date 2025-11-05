package controlador;

import dao.DetalleOrdenDAO;
import dao.VentaDAO;
import modelo.DetalleOrden;
import modelo.Venta;
import java.util.List;

/**
 * Controlador para ventas. Se encarga del registro final Y de descontar stock.
 */
public class VentaControlador {
    private final VentaDAO dao;
    private final DetalleOrdenDAO detalleDAO;
    private final ProductoControlador productoControlador;

    public VentaControlador() {
        dao = new VentaDAO();
        detalleDAO = new DetalleOrdenDAO();
        productoControlador = new ProductoControlador();
    }

    /**
     * 1. Registra la Venta en la tabla 'ventas'.
     * 2. Si tiene éxito, obtiene los detalles de la orden.
     * 3. Descuenta el stock de cada producto vendido.
     */
    public boolean registrarVenta(Venta venta) {

        // 1. Registrar la venta
        boolean ventaExitosa = dao.registrarVenta(venta);

        if (ventaExitosa) {
            System.out.println("Venta registrada (ID Orden: " + venta.getOrdenId() + "). Descontando stock...");

            try {
                // 2. Obtener los detalles (items) de la orden vendida
                List<DetalleOrden> detalles = detalleDAO.obtenerDetallesPorOrden(venta.getOrdenId());

                // 3. Recorrer cada item y descontar el stock
                for (DetalleOrden d : detalles) {
                    boolean stockDescontado = productoControlador.descontarStock(d.getProductoId(), d.getCantidad());

                    if (!stockDescontado) {
                        // Si esto falla (ej. no había stock), solo se registra en la consola.
                        // En un sistema más complejo, esto debería revertir la venta (rollback).
                        System.err.println("¡Alerta de Inventario! Venta registrada PERO no se pudo descontar stock para el Producto ID: " + d.getProductoId());
                    }
                }

            } catch (Exception e) {
                // La venta se registró, pero falló el descuento de stock.
                e.printStackTrace();
                System.err.println("Error crítico al intentar descontar stock después de la venta.");
            }
        }
        return ventaExitosa;
    }
}
