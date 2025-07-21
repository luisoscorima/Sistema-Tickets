package utp.edu.pe.isi.dwi.sistematickets.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class AsignacionDTO {
    private Integer idAsignacion;
    private Integer idSolicitud;
    private Integer idColaborador;
    private Timestamp fechaAsignacion;
    private Boolean esCoordinador;
    // Para mostrar en tablas
    private String nombreColaborador;
}
