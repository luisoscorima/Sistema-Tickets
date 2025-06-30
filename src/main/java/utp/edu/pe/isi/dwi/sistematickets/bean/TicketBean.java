package utp.edu.pe.isi.dwi.sistematickets.bean;
import utp.edu.pe.isi.dwi.sistematickets.dao.TicketDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.TicketDTO;
import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoSolicitudEnum;
import utp.edu.pe.isi.dwi.sistematickets.enums.PrioridadEnum;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Named("ticketBean")
@SessionScoped
public class TicketBean implements Serializable {
    @Inject
    private TicketDAO ticketDAO;
    @Inject
    private LoginBean loginBean;

    private TicketDTO nuevoTicket = new TicketDTO();
    private TicketDTO ticketSeleccionado = new TicketDTO();

    public List<TicketDTO> getTicketsCliente(){
        if (loginBean.getClienteLogueado() != null){
            int idCliente = loginBean.getClienteLogueado().getIdCliente();
            return ticketDAO.listarTicketsPorCliente(idCliente);
        }
        return null;
    }
    public List<TicketDTO> getTicketsColaborador(){
        if (loginBean.getColaboradorLogueado() != null){
            int idColaborador = loginBean.getColaboradorLogueado().getIdColaborador();
            return ticketDAO.listarTicketsPorColaborador(idColaborador);
        }
        return null;
    }
    // Para el registro de tickets
    public TicketDTO getNuevoTicket() { return nuevoTicket; }
    public void setNuevoTicket(TicketDTO t) { this.nuevoTicket = t; }
    
    public TicketDTO getTicketSeleccionado() { return ticketSeleccionado; }
    public void setTicketSeleccionado(TicketDTO t) { this.ticketSeleccionado = t; }
    
    public void actualizarTicketColaborador() {
        ticketDAO.actualizarTicketPorColaborador(ticketSeleccionado);
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
    public void cerrarTicketCliente(int idSolicitud) {
        ticketDAO.cerrarTicket(idSolicitud);
    }

    // Si necesitas enums para los combos:
    public PrioridadEnum[] getPrioridades() {
        return PrioridadEnum.values();
    }
    public EstadoSolicitudEnum[] getEstados() {
        return EstadoSolicitudEnum.values();
    }
}
