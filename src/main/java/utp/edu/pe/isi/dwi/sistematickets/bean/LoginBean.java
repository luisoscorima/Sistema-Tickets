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

    private ClienteDTO clienteLogueado;
    private ColaboradorDTO colaboradorLogueado;

    /* ---------- LOGIN ---------- */
    public String login() {
        FacesContext ctx = FacesContext.getCurrentInstance();

        // 1) Cliente
        clienteLogueado = loginDAO.loginCliente(email, password);
        if (clienteLogueado != null) {
            return "dashboardCliente?faces-redirect=true";
        }

        // 2) Colaborador (o Admin)
        colaboradorLogueado = loginDAO.loginColaborador(email, password);
        if (colaboradorLogueado != null) {
            return esAdmin()
                    ? "dashboardAdmin?faces-redirect=true"
                    : "dashboardColaborador?faces-redirect=true";
        }

        // 3) Fallo
        ctx.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                "Correo o contraseña incorrectos", null));
        return null;
    }

    /* ---------- LOGOUT ---------- */
    public String logout() {
        clienteLogueado = null;
        colaboradorLogueado = null;
        email = password = null;
        return "login?faces-redirect=true";
    }

    /* ---------- HELPERS ---------- */
    public boolean esAdmin() {
        return colaboradorLogueado != null
                && "Administrador".equalsIgnoreCase(colaboradorLogueado.getNombreRol());
    }

    public boolean esColaborador() {
        return colaboradorLogueado != null;
    }

    public boolean esCliente() {
        return clienteLogueado != null;
    }

    public String rutaInicio() {
        if (esAdmin()) {
            return "dashboardAdmin.xhtml";
        } else if (esColaborador()) {
            return "dashboardColaborador.xhtml";
        } else if (esCliente()) {
            return "dashboardCliente.xhtml";
        }
        return "login.xhtml";
    }

    public String nombreUsuario() {
        if (esColaborador()) {
            return colaboradorLogueado.getNombreColab() + ' ' + colaboradorLogueado.getApellidoColab();
        }
        if (esCliente()) {
            return clienteLogueado.getNombreCliente() + ' ' + clienteLogueado.getApellidoCliente();
        }
        return "Usuario";
    }

    public String rolUsuario() {
        if (esAdmin()) {
            return "Admin";
        }
        if (esColaborador()) {
            return "Colaborador";
        }
        if (esCliente()) {
            return "Cliente";
        }
        return "";
    }

    public String iconoRol() {
        if (esAdmin()) {
            return "pi pi-shield";      // 🔰
        }
        if (esColaborador()) {
            return "pi pi-briefcase";   // 💼
        }
        if (esCliente()) {
            return "pi pi-user";        // 👤
        }
        return "pi pi-question";
    }
}
