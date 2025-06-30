package utp.edu.pe.isi.dwi.sistematickets.enums;

public enum TipoSolicitudEnum {
    E("Error"),
    C("Capacitación"),
    R("Requerimiento");

    private final String label;

    TipoSolicitudEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
