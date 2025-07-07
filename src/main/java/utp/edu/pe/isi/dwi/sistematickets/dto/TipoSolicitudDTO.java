package utp.edu.pe.isi.dwi.sistematickets.dto;

import lombok.Data;

@Data
public class TipoSolicitudDTO {

    private Integer idTipoSolicitud;
    private String tipoSolicitud;
    private String descripcion;
    
    public TipoSolicitudDTO() {
    } // constructor vacío

    public TipoSolicitudDTO(Integer idTipoSolicitud, String tipoSolicitud, String descripcion) {
        this.idTipoSolicitud = idTipoSolicitud;
        this.tipoSolicitud = tipoSolicitud;
        this.descripcion = descripcion;
    }

    // getters y setters...
}
