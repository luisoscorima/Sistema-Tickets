package utp.edu.pe.isi.dwi.sistematickets.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class ActividadDTO {
    private Integer idActividad;
    private Integer idAsignacion;
    private String descripcion;
    private Timestamp inicio;
    private Timestamp fin;
    private Boolean esFinal;
// extras
    private Integer idColaborador;
    private String nombreColaborador;

}
