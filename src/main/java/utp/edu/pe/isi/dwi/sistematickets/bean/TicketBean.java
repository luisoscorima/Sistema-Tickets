package utp.edu.pe.isi.dwi.sistematickets.bean;

import utp.edu.pe.isi.dwi.sistematickets.dao.TicketDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.TicketDTO;
import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoSolicitudEnum;
import utp.edu.pe.isi.dwi.sistematickets.enums.PrioridadEnum;
import lombok.Getter;
import lombok.Setter;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("ticketBean")
@SessionScoped
@Getter @Setter
public class TicketBean implements Serializable {

    @Inject
    private TicketDAO ticketDAO;
    @Inject
    private LoginBean loginBean;

    private TicketDTO nuevoTicket = new TicketDTO();
    private TicketDTO ticketSeleccionado = new TicketDTO();

    public List<TicketDTO> getTicketsCliente() {
        if (loginBean.getClienteLogueado() != null) {
            int idCliente = loginBean.getClienteLogueado().getIdCliente();
            return ticketDAO.listarTicketsPorCliente(idCliente);
        }
        return Collections.emptyList();
    }

    public List<TicketDTO> getTicketsColaborador() {
        if (loginBean.getColaboradorLogueado() != null) {
            int idColaborador = loginBean.getColaboradorLogueado().getIdColaborador();
            return ticketDAO.listarTicketsPorColaborador(idColaborador);
        }
        return Collections.emptyList();
    }

    public List<TicketDTO> getTicketsTodos() {
        if (loginBean.esAdmin()) {
            return ticketDAO.listarTodas();
        }
        if (loginBean.getClienteLogueado() != null) {
            return getTicketsCliente();
        }
        if (loginBean.getColaboradorLogueado() != null) {
            return getTicketsColaborador();
        }
        return Collections.emptyList();
    }

    public void registrarTicketCliente() {
        nuevoTicket.setIdCliente(loginBean.getClienteLogueado().getIdCliente());
        nuevoTicket.setEstado(EstadoSolicitudEnum.A); // Abierto por defecto
        ticketDAO.registrarTicket(nuevoTicket);
        nuevoTicket = new TicketDTO();
    }

    public void actualizarTicketCliente() {
        ticketDAO.actualizarTicket(ticketSeleccionado);
    }

    public void actualizarTicketColaborador() {
        ticketDAO.actualizarTicketPorColaborador(ticketSeleccionado);
    }

    public void cerrarTicketCliente(int idSolicitud) {
        ticketDAO.cerrarTicket(idSolicitud);
    }

    public PrioridadEnum[] getPrioridades() {
        return PrioridadEnum.values();
    }

    public EstadoSolicitudEnum[] getEstados() {
        return EstadoSolicitudEnum.values();
    }
}
