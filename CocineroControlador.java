package controlador;
import dao.CocineroDAO;
import modelo.PedidoPendiente;

import java.util.List;

/**
 * Controlador para la lógica del Panel de Cocina.
 */
public class CocineroControlador {
    private final CocineroDAO dao;

    public CocineroControlador() {
        dao = new CocineroDAO();
    }

    public List<PedidoPendiente> obtenerPedidosPendientes() {
        return dao.obtenerPendientes();
    }


    /**
     * Marca un ítem de la orden como "Listo".
     */
    public boolean marcarComoListo(int ordenId, int productoId) {
        return dao.actualizarEstadoItem(ordenId, productoId, "Listo");
    }

    /**
     * Marca un ítem de la orden como "Entregado".
     */
    public boolean marcarComoEntregado(int ordenId, int productoId) {
        return dao.marcarComoEntregado(ordenId, productoId);
    }

        /**
         * Obtiene los pedidos listos para un mesero específico.
         */
        public List<PedidoPendiente> obtenerPedidosListos(String nombreMesero) {
            return dao.obtenerListosPorMesero(nombreMesero);
        }
    }
