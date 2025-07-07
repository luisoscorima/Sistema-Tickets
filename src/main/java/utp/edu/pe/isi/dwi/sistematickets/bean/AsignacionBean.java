package utp.edu.pe.isi.dwi.sistematickets.bean;

import jakarta.annotation.PostConstruct;
import utp.edu.pe.isi.dwi.sistematickets.dao.AsignacionDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.AsignacionDTO;
import utp.edu.pe.isi.dwi.sistematickets.dao.ColaboradorDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.ColaboradorDTO;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named("asignacionBean")
@SessionScoped
public class AsignacionBean implements Serializable {

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
    private AsignacionDAO asignacionDAO;
    @Inject
    private ColaboradorDAO colaboradorDAO;
    @Inject
    private LoginBean loginBean;

    private AsignacionDTO nuevaAsignacion = new AsignacionDTO();
    private Integer idSolicitudSeleccionada;

    public List<AsignacionDTO> getAsignacionesPorSolicitud() {
        if (idSolicitudSeleccionada != null) {
            return asignacionDAO.listarAsignacionesPorSolicitud(idSolicitudSeleccionada);
        }
        return null;
    }

    public List<ColaboradorDTO> getColaboradores() {
        return colaboradorDAO.listarColaboradores();
    }

    public AsignacionDTO getNuevaAsignacion() {
        return nuevaAsignacion;
    }

    public void setNuevaAsignacion(AsignacionDTO a) {
        this.nuevaAsignacion = a;
    }

    public Integer getIdSolicitudSeleccionada() {
        return idSolicitudSeleccionada;
    }

    public void setIdSolicitudSeleccionada(Integer id) {
        this.idSolicitudSeleccionada = id;
    }

    public void registrarAsignacion() {
        if (nuevaAsignacion.getIdSolicitud() == null && idSolicitudSeleccionada != null) {
            nuevaAsignacion.setIdSolicitud(idSolicitudSeleccionada);
        }
        asignacionDAO.asignarColaborador(nuevaAsignacion);
        nuevaAsignacion = new AsignacionDTO();
    }

    // Este método retorna TRUE si el colaborador logueado es coordinador para ese ticket
    public boolean esCoordinadorDelTicket(int idSolicitud) {
        Integer idColab = loginBean.getColaboradorLogueado().getIdColaborador();
        List<AsignacionDTO> asignaciones = asignacionDAO.listarAsignacionesPorSolicitud(idSolicitud);
        for (AsignacionDTO asg : asignaciones) {
            if (idColab.equals(asg.getIdColaborador()) && Boolean.TRUE.equals(asg.getEsCoordinador())) {
                return true;
            }
        }
        return false;
    }

}
