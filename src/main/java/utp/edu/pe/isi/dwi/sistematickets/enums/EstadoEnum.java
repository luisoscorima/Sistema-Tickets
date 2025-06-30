package utp.edu.pe.isi.dwi.sistematickets.enums;

public enum EstadoEnum {
    A("Activo"), 
    I("Inactivo");
    
    private final String label;
    
    EstadoEnum(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
}
