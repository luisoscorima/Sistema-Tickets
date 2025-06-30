package utp.edu.pe.isi.dwi.sistematickets.enums;

public enum TipoClienteEnum {
    N("Natural"),
    J("Jurídica");

    private final String label;

    TipoClienteEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
