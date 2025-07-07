package utp.edu.pe.isi.dwi.sistematickets.bean;

import utp.edu.pe.isi.dwi.sistematickets.dao.EmpresaDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.EmpresaDTO;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoEnum;

@Named("empresaBean")
@SessionScoped
public class EmpresaBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    public void verificarAcceso() {
        System.out.println("LoginBean: " + loginBean);
        System.out.println("Es admin: " + (loginBean != null && loginBean.esAdmin()));

        if (loginBean == null || !(loginBean.esAdmin())) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Inject
    private EmpresaDAO empresaDAO;

    private EmpresaDTO nuevaEmpresa = new EmpresaDTO();
    private EmpresaDTO empresaSeleccionada = new EmpresaDTO();

    public List<EmpresaDTO> getEmpresas() {
        return empresaDAO.listarEmpresas();
    }

    public EmpresaDTO getNuevaEmpresa() {
        return nuevaEmpresa;
    }

    public void setNuevaEmpresa(EmpresaDTO nuevaEmpresa) {
        this.nuevaEmpresa = nuevaEmpresa;
    }

    public EmpresaDTO getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(EmpresaDTO empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public void registrarEmpresa() {
        empresaDAO.registrarEmpresa(nuevaEmpresa);
        nuevaEmpresa = new EmpresaDTO();
    }

    public void actualizarEmpresa() {
        empresaDAO.actualizarEmpresa(empresaSeleccionada);
        empresaSeleccionada = new EmpresaDTO();
    }

    public void cambiarEstadoEmpresa(EmpresaDTO e) {
        boolean nuevoEstado = e.getEstadoEmpresa() == EstadoEnum.A ? false : true;
        empresaDAO.cambiarEstadoEmpresa(e.getIdEmpresa(), nuevoEstado ? EstadoEnum.A : EstadoEnum.I);
    }

    public void seleccionarParaEditar(EmpresaDTO e) {
        this.empresaSeleccionada = e;
    }

    public EstadoEnum[] getEstados() {
        return EstadoEnum.values();
    }
}
