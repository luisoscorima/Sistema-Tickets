package utp.edu.pe.isi.dwi.sistematickets.bean;

import utp.edu.pe.isi.dwi.sistematickets.dao.LoginDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.ClienteDTO;
import utp.edu.pe.isi.dwi.sistematickets.dto.ColaboradorDTO;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;

@Named("loginBean")
@SessionScoped
@Getter
@Setter
public class LoginBean implements Serializable {

    @Inject
    private LoginDAO loginDAO;

    private String email;
    private String password;
    private String tipoUsuario = "cliente"; // o "colaborador"

    private ClienteDTO clienteLogueado;
    private ColaboradorDTO colaboradorLogueado;

    public String login() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if ("cliente".equals(tipoUsuario)) {
            clienteLogueado = loginDAO.loginCliente(email, password);
            if (clienteLogueado != null) {
                return "dashboardCliente?faces-redirect=true";
            }
        } else {
            colaboradorLogueado = loginDAO.loginColaborador(email, password);
            if (colaboradorLogueado != null) {
                return "dashboardColaborador?faces-redirect=true";
            }
        }
        // Mensaje de error (lo puedes mostrar en la página)
        facesContext.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Correo o contraseña incorrectos", null));
        return null;
    }

    public String logout() {
        clienteLogueado = null;
        colaboradorLogueado = null;
        email = null;
        password = null;
        tipoUsuario = "cliente";
        return "login?faces-redirect=true";
    }

    public String nombreUsuario() {
        if (colaboradorLogueado != null) {
            return colaboradorLogueado.getNombreColab() + " " + colaboradorLogueado.getApellidoColab();
        }
        if (clienteLogueado != null) {
            return clienteLogueado.getNombreCliente() + " " + clienteLogueado.getApellidoCliente();
        }
        return "Usuario";
    }

}
