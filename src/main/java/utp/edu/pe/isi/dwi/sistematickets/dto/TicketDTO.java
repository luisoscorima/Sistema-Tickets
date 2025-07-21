package utp.edu.pe.isi.dwi.sistematickets.dto;

import lombok.Data;
import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoSolicitudEnum;
import utp.edu.pe.isi.dwi.sistematickets.enums.PrioridadEnum;
import java.sql.Timestamp;
import java.util.List;

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
    // Extras para la UI
    private String nombreCliente;
    private String apellidoCliente;
    private String nombreTipoSolicitud;
    private String nombreAplicacion;
    // Asignaciones
    private Integer idCoordinador;
    private List<Integer> colaboradoresAsignados;
    private String nombreCoordinador;
    private List<String> nombresColaboradores;
}
