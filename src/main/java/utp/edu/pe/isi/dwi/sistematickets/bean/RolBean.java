package utp.edu.pe.isi.dwi.sistematickets.bean;

import utp.edu.pe.isi.dwi.sistematickets.dao.RolDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.RolDTO;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Named("rolBean")
@SessionScoped
public class RolBean implements Serializable {
    @Inject
    private RolDAO rolDAO;

    private RolDTO nuevoRol = new RolDTO();
    private RolDTO rolSeleccionado = new RolDTO();

    public List<RolDTO> getRoles() {
        return rolDAO.listarRoles();
    }

    public RolDTO getNuevoRol() { return nuevoRol; }
    public void setNuevoRol(RolDTO r) { this.nuevoRol = r; }
    public RolDTO getRolSeleccionado() { return rolSeleccionado; }
    public void setRolSeleccionado(RolDTO r) { this.rolSeleccionado = r; }

    public void registrarRol() {
        rolDAO.registrarRol(nuevoRol);
        nuevoRol = new RolDTO();
    }

    public void actualizarRol() {
        rolDAO.actualizarRol(rolSeleccionado);
    }

    public void eliminarRol(int id) {
        rolDAO.eliminarRol(id);
    }
    public void seleccionarParaEditar(RolDTO r) {
        this.rolSeleccionado = r;
    }
}
