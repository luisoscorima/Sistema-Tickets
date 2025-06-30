package utp.edu.pe.isi.dwi.sistematickets.bean;

import utp.edu.pe.isi.dwi.sistematickets.dao.ColaboradorDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.ColaboradorDTO;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.List;
import utp.edu.pe.isi.dwi.sistematickets.dao.RolDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.RolDTO;

@Named("colaboradorBean")
@SessionScoped
public class ColaboradorBean implements Serializable {

    @Inject
    private ColaboradorDAO colaboradorDAO;
    @Inject
    private RolDAO rolDAO;

    private ColaboradorDTO nuevoColaborador = new ColaboradorDTO();
    private ColaboradorDTO colaboradorSeleccionado = new ColaboradorDTO();

    public List<ColaboradorDTO> getColaboradores() {
        return colaboradorDAO.listarColaboradores();
    }

    public List<RolDTO> getRoles() {
        return rolDAO.listarRoles();
    }

    public ColaboradorDTO getNuevoColaborador() {
        return nuevoColaborador;
    }

    public void setNuevoColaborador(ColaboradorDTO c) {
        this.nuevoColaborador = c;
    }

    public ColaboradorDTO getColaboradorSeleccionado() {
        return colaboradorSeleccionado;
    }

    public void setColaboradorSeleccionado(ColaboradorDTO c) {
        this.colaboradorSeleccionado = c;
    }

    public void registrarColaborador() {
        colaboradorDAO.registrarColaborador(nuevoColaborador);
        nuevoColaborador = new ColaboradorDTO(); // Limpiar formulario
    }

    public void actualizarColaborador() {
        colaboradorDAO.actualizarColaborador(colaboradorSeleccionado);
        colaboradorSeleccionado = new ColaboradorDTO(); // <-- esto limpia el seleccionado
    }

    public void cambiarEstadoColaborador(ColaboradorDTO c) {
        boolean nuevoEstado = !c.getEstadoColab();
        colaboradorDAO.cambiarEstadoColaborador(c.getIdColaborador(), nuevoEstado);
    }

    public String obtenerNombreRolPorId(int idRol) {
        return getRoles().stream()
                .filter(r -> r.getIdRol() == idRol)
                .map(RolDTO::getNombreRol)
                .findFirst()
                .orElse("Sin Rol");
    }
    
    public void seleccionarParaEditar(ColaboradorDTO c) {
        this.colaboradorSeleccionado = c;
    }
}
