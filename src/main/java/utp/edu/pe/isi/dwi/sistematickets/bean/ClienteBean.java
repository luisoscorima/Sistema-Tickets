package utp.edu.pe.isi.dwi.sistematickets.bean;

import utp.edu.pe.isi.dwi.sistematickets.dao.ClienteDAO;
import utp.edu.pe.isi.dwi.sistematickets.dao.EmpresaDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.ClienteDTO;
import utp.edu.pe.isi.dwi.sistematickets.dto.EmpresaDTO;
import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoEnum;
import utp.edu.pe.isi.dwi.sistematickets.enums.TipoClienteEnum;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("clienteBean")
@SessionScoped
public class ClienteBean implements Serializable {

    @Inject private LoginBean loginBean;
    @Inject private ClienteDAO clienteDAO;
    @Inject private EmpresaDAO empresaDAO;

    private ClienteDTO nuevoCliente;
    private ClienteDTO clienteSeleccionado;

    @PostConstruct
    public void init() {
        // Inicializamos DTOs
        nuevoCliente = new ClienteDTO();
        clienteSeleccionado = new ClienteDTO();
        // Si el usuario es Cliente, fijamos su empresa y no permitimos cambiarla
        if (loginBean.esCliente()) {
            Integer miEmpresa = loginBean.getClienteLogueado().getIdEmpresa();
            nuevoCliente.setIdEmpresa(miEmpresa);
        }
    }

    public void verificarAcceso() {
        if (loginBean == null || !loginBean.esCliente()) {
            try {
                FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("login.xhtml");
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
        // Estado por defecto
        if (nuevoCliente.getEstadoCliente() == null) {
            nuevoCliente.setEstadoCliente(EstadoEnum.A);
        }
        clienteDAO.registrarCliente(nuevoCliente);
        // Reset y volver a fijar empresa
        init();
    }

    public void actualizarCliente() {
        clienteDAO.actualizarCliente(clienteSeleccionado);
    }

    public void cambiarEstadoCliente(ClienteDTO c) {
        EstadoEnum nuevoEstado = c.getEstadoCliente() == EstadoEnum.A
                                ? EstadoEnum.I
                                : EstadoEnum.A;
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

    /** Clientes activos de la misma empresa (solo para rol Cliente) */
    public List<ClienteDTO> getClientesPorEmpresa() {
        if (loginBean.esCliente()) {
            Integer idEmp = loginBean.getClienteLogueado().getIdEmpresa();
            if (idEmp != null) {
                return clienteDAO.listarPorEmpresa(idEmp);
            }
        }
        return Collections.emptyList();
    }
}
