package utp.edu.pe.isi.dwi.sistematickets.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class AsignacionDTO {
    private Integer idAsignacion;
    private Integer idSolicitud;
    private Integer idColaborador;
    private Timestamp fechaAsignacion;
    private Boolean esCoordinador;
    // Extras si los necesitas:
    private String nombreColaborador; // Para mostrar en tablas
}
