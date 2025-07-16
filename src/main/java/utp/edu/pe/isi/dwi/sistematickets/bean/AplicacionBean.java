package utp.edu.pe.isi.dwi.sistematickets.bean;

import utp.edu.pe.isi.dwi.sistematickets.dao.AplicacionDAO;
import utp.edu.pe.isi.dwi.sistematickets.dao.EmpresaDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.AplicacionDTO;
import utp.edu.pe.isi.dwi.sistematickets.dto.EmpresaDTO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("aplicacionBean")
@SessionScoped
public class AplicacionBean implements Serializable {

    @Inject private AplicacionDAO aplicacionDAO;
    @Inject private EmpresaDAO   empresaDAO;
    @Inject private LoginBean    loginBean;

    // **Propiedades que necesita JSF**
    private AplicacionDTO nuevaAplicacion = new AplicacionDTO();
    private AplicacionDTO aplicacionSeleccionada = new AplicacionDTO();

    // --- getters & setters ---
    public AplicacionDTO getNuevaAplicacion() {
        return nuevaAplicacion;
    }
    public void setNuevaAplicacion(AplicacionDTO nuevaAplicacion) {
        this.nuevaAplicacion = nuevaAplicacion;
    }

    public AplicacionDTO getAplicacionSeleccionada() {
        return aplicacionSeleccionada;
    }
    public void setAplicacionSeleccionada(AplicacionDTO aplicacionSeleccionada) {
        this.aplicacionSeleccionada = aplicacionSeleccionada;
    }
    // -------------------------

    /** Dropdown de empresas */
    public List<EmpresaDTO> getEmpresas() {
        return empresaDAO.listarEmpresas();
    }

    /** Lista de aplicaciones (administrador vs. cliente) */
    public List<AplicacionDTO> getAplicaciones() {
        if (loginBean.esCliente()) {
            Integer idEmp = loginBean.getClienteLogueado().getIdEmpresa();
            return idEmp == null
                ? Collections.emptyList()
                : aplicacionDAO.listarPorEmpresa(idEmp);
        }
        return aplicacionDAO.listarAplicaciones();
    }

    /** Mostrar nombre de la empresa en la tabla */
    public String obtenerNombreEmpresaPorId(Integer idEmpresa) {
        return getEmpresas().stream()
                .filter(e -> e.getIdEmpresa().equals(idEmpresa))
                .map(EmpresaDTO::getRazonSocial)
                .findFirst()
                .orElse("Sin Empresa");
    }

    /** CRUD */
    public void registrarAplicacion() {
        aplicacionDAO.registrarAplicacion(nuevaAplicacion);
        nuevaAplicacion = new AplicacionDTO();
    }
    public void actualizarAplicacion() {
        aplicacionDAO.actualizarAplicacion(aplicacionSeleccionada);
    }
    public void eliminarAplicacion(int id) {
        aplicacionDAO.eliminarAplicacion(id);
    }
    public void seleccionarParaEditar(AplicacionDTO a) {
        this.aplicacionSeleccionada = a;
    }
}
