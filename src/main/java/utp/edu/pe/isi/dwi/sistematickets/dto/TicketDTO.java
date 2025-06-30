package utp.edu.pe.isi.dwi.sistematickets.dto;
import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoSolicitudEnum;
import utp.edu.pe.isi.dwi.sistematickets.enums.PrioridadEnum;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class TicketDTO {
    private Integer idSolicitud;
    private Integer idCliente;
    private Integer idTipoSolicitud;
    private Integer idAplicacion;
    private String asunto;
    private String motivo;
    private Timestamp fechaCreacion;
    private EstadoSolicitudEnum estado;
    private PrioridadEnum prioridad;
    private Timestamp fechaCierre;

    // Extras para mostrar en tabla o modales
    private String nombreCliente;
    private String nombreTipoSolicitud;
    private String nombreAplicacion;
    // Si quieres agregar nombre del colaborador asignado, agrégalo aquí
    private String nombreColaboradorAsignado;
}
