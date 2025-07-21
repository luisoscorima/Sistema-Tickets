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
        // Creamos DTOs con valores por defecto
        nuevoCliente = new ClienteDTO();
        clienteSeleccionado = new ClienteDTO();

        // Asignamos un valor inicial a tipoCliente para que nunca sea null
        TipoClienteEnum defaultTipo = TipoClienteEnum.values()[0];
        nuevoCliente.setTipoCliente(defaultTipo);
        clienteSeleccionado.setTipoCliente(defaultTipo);

        // Si el usuario es Cliente, fijamos idEmpresa en nuevoCliente
        if (loginBean.esCliente()) {
            Integer miEmpresa = loginBean.getClienteLogueado().getIdEmpresa();
            nuevoCliente.setIdEmpresa(miEmpresa);
            // para el form de edición también podríamos fijarlo
            clienteSeleccionado.setIdEmpresa(miEmpresa);
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
        // Al seleccionar para editar, asegúrate de que no quede ningún campo null
        this.clienteSeleccionado = clienteSeleccionado;
        if (this.clienteSeleccionado.getTipoCliente() == null) {
            this.clienteSeleccionado.setTipoCliente(TipoClienteEnum.values()[0]);
        }
        if (!loginBean.esCliente() && this.clienteSeleccionado.getIdEmpresa() == null) {
            // como admin, deja “Sin Empresa” si es null
            this.clienteSeleccionado.setIdEmpresa(null);
        }
    }

    public void registrarCliente() {
        // Estado por defecto
        if (nuevoCliente.getEstadoCliente() == null) {
            nuevoCliente.setEstadoCliente(EstadoEnum.A);
        }
        clienteDAO.registrarCliente(nuevoCliente);
        // Limpiamos y volvemos a cargar defaults
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
