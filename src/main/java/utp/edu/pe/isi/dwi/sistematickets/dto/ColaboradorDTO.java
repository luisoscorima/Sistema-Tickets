package utp.edu.pe.isi.dwi.sistematickets.dto;
import lombok.Data;

@Data
public class ColaboradorDTO {
    private Integer idColaborador;
    private String nombreColab;
    private String apellidoColab;
    private String emailColab;
    private String passwordColab;
    private Integer idRol;
    private String nombreRol;
    private Boolean estadoColab;
}