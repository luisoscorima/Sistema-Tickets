package utp.edu.pe.isi.dwi.sistematickets.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import utp.edu.pe.isi.dwi.sistematickets.dao.AsignacionDAO;
import utp.edu.pe.isi.dwi.sistematickets.dao.ColaboradorDAO;
import utp.edu.pe.isi.dwi.sistematickets.dao.TicketDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.AsignacionDTO;
import utp.edu.pe.isi.dwi.sistematickets.dto.ColaboradorDTO;
import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoSolicitudEnum;
import utp.edu.pe.isi.dwi.sistematickets.bean.LoginBean;
import java.io.Serializable;
import java.util.List;

@Named("asignacionBean")
@SessionScoped
@Getter
@Setter
public class AsignacionBean implements Serializable {

    @Inject private AsignacionDAO asignacionDAO;
    @Inject private ColaboradorDAO colaboradorDAO;
    @Inject private LoginBean loginBean;
    @Inject private TicketDAO ticketDAO;

    /**
     * ID del ticket seleccionado para asignar
     */
    private Integer idSolicitudSeleccionada;

    /**
     * DTO usado para la nueva asignación
     */
    private AsignacionDTO nuevaAsignacion;

    /**
     * Cache de las asignaciones actuales del ticket
     */
    private List<AsignacionDTO> asignaciones;

    @PostConstruct
    public void init() {
        nuevaAsignacion = new AsignacionDTO();
    }

    /**
     * Devuelve sólo los colaboradores activos para el dropdown
     */
    public List<ColaboradorDTO> getColaboradores() {
        return colaboradorDAO.listarColaboradoresActivos();
    }

    /**
     * Cuando el usuario abre el modal de Asignar, cargamos la lista actual y
     * preparamos el DTO vacío.
     */
    public void setIdSolicitudSeleccionada(Integer id) {
        this.idSolicitudSeleccionada = id;
        this.asignaciones = asignacionDAO.listarAsignacionesPorSolicitud(id);
        this.nuevaAsignacion = new AsignacionDTO();
        this.nuevaAsignacion.setIdSolicitud(id);
    }

    /**
     * Getter para la lista de asignaciones mostrada en la vista
     */
    public List<AsignacionDTO> getAsignaciones() {
        return asignaciones;
    }

    /**
     * Comprueba si el colaborador logueado es coordinador del ticket dado (para
     * habilitar/deshabilitar el botón).
     */
    public boolean esCoordinadorDelTicket(Integer idSolicitud) {
        Integer miId = loginBean.getColaboradorLogueado().getIdColaborador();
        return asignacionDAO.listarAsignacionesPorSolicitud(idSolicitud)
                .stream()
                .anyMatch(a
                        -> a.getIdColaborador().equals(miId)
                && Boolean.TRUE.equals(a.getEsCoordinador())
                );
    }

    /**
     * Permite asignar si eres Admin o coordinador del ticket (método usado en
     * rendered="#{asignacionBean.puedeAsignar(...) }")
     */
    public boolean puedeAsignar(Integer idSolicitud) {
        return loginBean.esAdmin() || esCoordinadorDelTicket(idSolicitud);
    }

    /**
     * Inserta o actualiza la asignación: - Si no existía, la inserta. - Si ya
     * existía (mismo ticket+colaborador), actualiza sólo el flag de
     * coordinador.
     */
    public void registrarAsignacion() {
        boolean pedirCoordinador = Boolean.TRUE.equals(nuevaAsignacion.getEsCoordinador());
        // Solo admin puede asignar coordinadores
        if (!loginBean.esAdmin()) {
            nuevaAsignacion.setEsCoordinador(false);
        }

        asignacionDAO.upsertAsignacion(
                idSolicitudSeleccionada,
                nuevaAsignacion.getIdColaborador(),
                Boolean.TRUE.equals(nuevaAsignacion.getEsCoordinador())
        );

        // Si un admin acaba de poner un coordinador, marcamos el ticket como ASIGNADO
        if (loginBean.esAdmin() && pedirCoordinador) {
            ticketDAO.actualizarEstadoTicket(
                    idSolicitudSeleccionada,
                    EstadoSolicitudEnum.B
            );
        }

        this.asignaciones = asignacionDAO.listarAsignacionesPorSolicitud(idSolicitudSeleccionada);
        this.nuevaAsignacion = new AsignacionDTO();
        this.nuevaAsignacion.setIdSolicitud(idSolicitudSeleccionada);
    }
}
