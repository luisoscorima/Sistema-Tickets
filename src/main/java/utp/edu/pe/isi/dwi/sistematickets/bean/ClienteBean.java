package utp.edu.pe.isi.dwi.sistematickets.bean;

import utp.edu.pe.isi.dwi.sistematickets.dao.ClienteDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.ClienteDTO;
import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoEnum;
import utp.edu.pe.isi.dwi.sistematickets.enums.TipoClienteEnum;
import utp.edu.pe.isi.dwi.sistematickets.dao.EmpresaDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.EmpresaDTO;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Named("clienteBean")
@SessionScoped
public class ClienteBean implements Serializable {

    @Inject
    private ClienteDAO clienteDAO;
    @Inject
    private EmpresaDAO empresaDAO;

    private ClienteDTO nuevoCliente = new ClienteDTO();
    private ClienteDTO clienteSeleccionado = new ClienteDTO();

    public List<ClienteDTO> getClientes() {
        return clienteDAO.listarClientes();
    }

    public List<EmpresaDTO> getEmpresas() {
        return empresaDAO.listarEmpresas();
    }

    public ClienteDTO getNuevoCliente() {
        return nuevoCliente;
    }

    public void setNuevoCliente(ClienteDTO nuevoCliente) {
        this.nuevoCliente = nuevoCliente;
    }

    public ClienteDTO getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(ClienteDTO clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }

    public void registrarCliente() {
        if (nuevoCliente.getEstadoCliente() == null) {
            nuevoCliente.setEstadoCliente(EstadoEnum.A);
        }
        clienteDAO.registrarCliente(nuevoCliente);
        nuevoCliente = new ClienteDTO();
    }

    public void actualizarCliente() {
        clienteDAO.actualizarCliente(clienteSeleccionado);
    }

    public void cambiarEstadoCliente(ClienteDTO c) {
        EstadoEnum nuevoEstado = c.getEstadoCliente() == EstadoEnum.A ? EstadoEnum.I : EstadoEnum.A;
        clienteDAO.cambiarEstadoCliente(c.getIdCliente(), nuevoEstado);
    }

    public void seleccionarParaEditar(ClienteDTO c) {
        this.clienteSeleccionado = c;
    }

    public TipoClienteEnum[] getTiposCliente() {
        return TipoClienteEnum.values();
    }

    public EstadoEnum[] getEstados() {
        return EstadoEnum.values();
    }

    public String obtenerNombreEmpresaPorId(Integer idEmpresa) {
        if (idEmpresa == null) {
            return "Sin Empresa";
        }
        return getEmpresas().stream()
                .filter(e -> e.getIdEmpresa().equals(idEmpresa))
                .map(EmpresaDTO::getRazonSocial)
                .findFirst()
                .orElse("Sin Empresa");
    }
}
