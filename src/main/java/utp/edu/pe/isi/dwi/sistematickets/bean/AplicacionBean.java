package utp.edu.pe.isi.dwi.sistematickets.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import utp.edu.pe.isi.dwi.sistematickets.dao.AplicacionDAO;
import utp.edu.pe.isi.dwi.sistematickets.dao.EmpresaDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.AplicacionDTO;
import utp.edu.pe.isi.dwi.sistematickets.dto.EmpresaDTO;

@Named("aplicacionBean")
@SessionScoped
public class AplicacionBean implements Serializable {

    @Inject
    private AplicacionDAO aplicacionDAO;
    @Inject
    private EmpresaDAO empresaDAO;

    private AplicacionDTO nuevaAplicacion = new AplicacionDTO();
    private AplicacionDTO aplicacionSeleccionada = new AplicacionDTO();

    public List<AplicacionDTO> getAplicaciones() {
        return aplicacionDAO.listarAplicaciones();
    }

    public List<EmpresaDTO> getEmpresas() {
        return empresaDAO.listarEmpresas();
    }

    public AplicacionDTO getNuevaAplicacion() {
        return nuevaAplicacion;
    }

    public void setNuevaAplicacion(AplicacionDTO a) {
        this.nuevaAplicacion = a;
    }

    public AplicacionDTO getAplicacionSeleccionada() {
        return aplicacionSeleccionada;
    }

    public void setAplicacionSeleccionada(AplicacionDTO a) {
        this.aplicacionSeleccionada = a;
    }

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

    public String obtenerNombreEmpresaPorId(Integer idEmpresa) {
        return getEmpresas().stream()
                .filter(e -> e.getIdEmpresa().equals(idEmpresa))
                .map(EmpresaDTO::getRazonSocial)
                .findFirst().orElse("Sin Empresa");
    }
}
