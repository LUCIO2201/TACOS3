package controlador;
import dao.MesaDAO;
import dao.OrdenDAO;
import modelo.Mesa;
import modelo.Orden;

import java.util.List;

/**
 * Controlador para mesas. Controla estados y lista las mesas.
 */
public class MesaControlador{
    private final MesaDAO dao;
    private final OrdenDAO ordenDAO;

    public MesaControlador() {
        dao = new MesaDAO();
        ordenDAO = new OrdenDAO();
    }

  /*
    public List<Mesa> listarMesas() {
      return dao.listarMesas();
    }
   */

    public void cambiarEstadoMesa(int numeroMesa, boolean ocupada) {
        dao.actualizarEstadoMesa(numeroMesa, ocupada);
    }

    public List<Mesa> obtenerMesas() {
        return dao.obtenerMesas();
    }

    public boolean asignarOrdenAMesa(int numeroMesa, String mesero) {
        // 1. Marcar la mesa como ocupada
        boolean estadoActualizado = dao.actualizarEstadoMesa(numeroMesa, true);

        if (estadoActualizado) {
            // 2. Crear la orden asociada a esa mesa
            Orden orden = new Orden();
            orden.setMesa(numeroMesa);
            orden.setMesero(mesero);
            orden.setEstado("Abierta");
            orden.setTotal(0.0);
            return ordenDAO.crearOrden(orden);
        }
        return false;
    }
}

