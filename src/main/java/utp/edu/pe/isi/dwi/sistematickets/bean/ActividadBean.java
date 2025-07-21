package utp.edu.pe.isi.dwi.sistematickets.bean;

import jakarta.annotation.PostConstruct;
import utp.edu.pe.isi.dwi.sistematickets.dao.ActividadDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.ActividadDTO;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import utp.edu.pe.isi.dwi.sistematickets.dao.AsignacionDAO;
import utp.edu.pe.isi.dwi.sistematickets.dao.TicketDAO;
import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoSolicitudEnum;

@Named("actividadBean")
@SessionScoped
public class ActividadBean implements Serializable {

    public void verificarAcceso() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        LoginBean loginBean = (LoginBean) ctx.getExternalContext().getSessionMap().get("loginBean");
        if (loginBean == null || !loginBean.esColaborador()) {
            try {
                ctx.getExternalContext().redirect("login.xhtml");
                ctx.responseComplete(); // <--- ¡Esto es importante!
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Inject
    private ActividadDAO actividadDAO;
    @Inject
    private LoginBean loginBean;
    @Inject
    private TicketDAO ticketDAO;
    @Inject
    private AsignacionDAO asignacionDAO;

    private ActividadDTO nuevaActividad = new ActividadDTO();
    private Integer idSolicitudSeleccionada;

    public List<ActividadDTO> getActividadesPorSolicitud() {
        if (idSolicitudSeleccionada != null) {
            return actividadDAO.listarActividadesPorSolicitud(idSolicitudSeleccionada);
        }
        return null;
    }

    public ActividadDTO getNuevaActividad() {
        return nuevaActividad;
    }

    public void setNuevaActividad(ActividadDTO a) {
        this.nuevaActividad = a;
    }

    public Integer getIdSolicitudSeleccionada() {
        return idSolicitudSeleccionada;
    }

    public void setIdSolicitudSeleccionada(Integer id) {
        this.idSolicitudSeleccionada = id;
    }

    public void registrarActividad() {
        Integer idAsignacion = asignacionDAO.obtenerIdAsignacionPorSolicitudYColaborador(
                idSolicitudSeleccionada,
                loginBean.getColaboradorLogueado().getIdColaborador()
        );
        nuevaActividad.setIdAsignacion(idAsignacion);
        nuevaActividad.setIdColaborador(loginBean.getColaboradorLogueado().getIdColaborador());
        actividadDAO.registrarActividad(nuevaActividad);

        if (Boolean.TRUE.equals(nuevaActividad.getEsFinal()) && idSolicitudSeleccionada != null) {
            ticketDAO.actualizarEstadoTicket(idSolicitudSeleccionada, EstadoSolicitudEnum.S);
        }

        // refrescamos el DTO para el siguiente registro
        nuevaActividad = new ActividadDTO();
    }

    public boolean ticketEstaSolucionado() {
        List<ActividadDTO> acts = getActividadesPorSolicitud();
        if (acts != null) {
            for (ActividadDTO a : acts) {
                if (Boolean.TRUE.equals(a.getEsFinal())) {
                    return true;
                }
            }
        }
        return false;
    }

}
