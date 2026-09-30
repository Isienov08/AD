import java.time.LocalDate;

public class Pagos {
    private int id;
    private int idCliente;
    private LocalDate fecha;
    private double importe;
    private double litros;
    private String combustible;

    public Pagos(int id, int idCliente, LocalDate fecha, double importe, double litros, String combustible) {
        this.id = id;
        setIdCliente(idCliente);
        setFecha(fecha);
        setImporte(importe);
        setLitros(litros);
        setCombustible(combustible);
    }

    // Getters y Setters con validaciones centralizadas
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("El ID del cliente debe ser un entero positivo.");
        }
        this.idCliente = idCliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        this.fecha = fecha;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        // Validar positivo y máximo 2 decimales
        if (importe <= 0) {
            throw new IllegalArgumentException("El importe debe ser mayor que cero.");
        }
        if (Math.round(importe * 100.0) != importe * 100.0) {
            throw new IllegalArgumentException("El importe debe tener como máximo dos decimales.");
        }
        this.importe = importe;
    }

    public double getLitros() {
        return litros;
    }

    public void setLitros(double litros) {
        // Validar positivo y máximo 2 decimales
        if (litros <= 0) {
            throw new IllegalArgumentException("Los litros deben ser mayores que cero.");
        }
        if (Math.round(litros * 100.0) != litros * 100.0) {
            throw new IllegalArgumentException("Los litros deben tener como máximo dos decimales.");
        }
        this.litros = litros;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        if (combustible == null || combustible.trim().isEmpty()) {
            throw new IllegalArgumentException("El combustible no puede estar vacío.");
        }
        this.combustible = combustible.trim();
    }

    @Override
    public String toString() {
        return "Pagos{" +
                "id=" + id +
                ", idCliente=" + idCliente +
                ", fecha=" + fecha +
                ", importe=" + importe +
                ", litros=" + litros +
                ", combustible='" + combustible + '\'' +
                '}';
    }
}