package utp.edu.pe.isi.dwi.sistematickets.dto;

import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoEnum;
import lombok.Data;

@Data
public class EmpresaDTO {
    private Integer idEmpresa;
    private String ruc;
    private String razonSocial;
    private String direccion;
    private String telefono;
    private EstadoEnum estadoEmpresa;
}

