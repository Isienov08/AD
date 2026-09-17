import java.util.Date;

public class PagosRepostaje {
    private int id;
    private int idCliente;
    private Date fecha;
    private double importe;
    private double litros;
    private String combustible;

    public PagosRepostaje(int id, int idCliente, Date fecha, double importe, double litros, String combustible) {
        this.id = id;
        this.idCliente = idCliente;
        this.fecha = fecha; //formato de la fecha??
        setImporte(importe);//decimales con , y . ??
        setLitros(litros);
        setCombustible(combustible);
    }


    //Getters y setters
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
        this.idCliente = idCliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        if (importe<=0){
            throw new IllegalArgumentException("El importe no puede estar vacío");
        }else {
            this.importe = importe;
        }
    }

    public double getLitros() {
        return litros;
    }

    public void setLitros(double litros) {
        if (litros<=0){
            throw new IllegalArgumentException("Los litros no puede estar vacío");
        }else {
            this.litros = litros;
        }
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        String combLimpio=combustible.trim();

        if (combLimpio.isEmpty()){
            throw new IllegalArgumentException("El combustible no puede estar vacío");
        }else {
            this.combustible=combLimpio;
        }
    }

//    @Override
//    public String toString() {
//        return "PagosRepostaje{" +
//                "id=" + id +
//                ", idCliente=" + idCliente +
//                ", fecha=" + fecha +
//                ", importe=" + importe +
//                ", litros=" + litros +
//                ", combustible='" + combustible + '\'' +
//                '}';
//    }
}
