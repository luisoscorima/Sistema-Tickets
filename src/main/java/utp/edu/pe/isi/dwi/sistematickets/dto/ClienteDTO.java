package utp.edu.pe.isi.dwi.sistematickets.dto;
import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoEnum;
import utp.edu.pe.isi.dwi.sistematickets.enums.TipoClienteEnum;
import lombok.Data;

@Data
public class ClienteDTO {
    private Integer idCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String emailCliente;
    private String passwordCliente;
    private TipoClienteEnum tipoCliente;
    private EstadoEnum estadoCliente;
    private Integer idEmpresa;
}
