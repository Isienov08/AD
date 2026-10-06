import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.nio.file.StandardOpenOption.APPEND;

public class GestorFicherosEnJSON  implements GestorFichero{
    Path ruta;
    private Function<Cliente, String> serializadorCliente;
    private Function<Pagos, String> serializadorPagos;
    private Function<String, Cliente> deserializadorCliente;
    private Function<String, Pagos> deserializadorPagos;

    public GestorFicherosEnJSON(Path ruta) {
        this.ruta=ruta;
        crearFichero();
        this.serializadorCliente=cliente ->
            "{"+
                    "id:"+cliente.getId()+","+
                    "nombre:"+cliente.getNombre()+","+
                    "telefono:"+cliente.getTelefono()+","+
                    "matricula:"+cliente.getMatricula()+
            "}";

        this.serializadorPagos = pagos ->
            "{"+
                    "id:"+pagos.getId() + "," +
                    "idCliente:"+pagos.getIdCliente() + "," +
                    "fecha:"+pagos.getFecha() + "," +
                    "importe:"+pagos.getImporte() + "," +
                    "litros:"+pagos.getLitros() + "," +
                    "combustible:"+pagos.getCombustible()+
            "}";

        this.deserializadorCliente=linea -> {
            String[] campos = linea.split(",", -1);

            String idLinea=campos[0];
            String[] id=idLinea.split(":", -1);

            String nombreLinea=campos[1];
            String[] nombre=nombreLinea.split(":", -1);

            String telefonoLinea=campos[2];
            String[] telefono=telefonoLinea.split(":", -1);

            String matriculaLinea=campos[3];
            String[] matricula=matriculaLinea.split(":", -1);

            return new Cliente(
                    Integer.parseInt(id[1]),
                    nombre[1],
                    telefono[1],
                    matricula[1]);
        };
        this.deserializadorPagos = linea -> {
            String[] campos = linea.split(",", -1);

            String idLinea=campos[0];
            String[] id=idLinea.split(":", -1);

            String idClienteLinea=campos[1];
            String[] idCliente=idClienteLinea.split(":", -1);

            String fechaLinea=campos[2];
            String[] fecha=fechaLinea.split(":", -1);

            String importeLinea=campos[3];
            String[] importe=importeLinea.split(":", -1);

            String litrosLinea=campos[4];
            String[] litros=litrosLinea.split(":", -1);

            String combustibleLinea=campos[1];
            String[] combustible=combustibleLinea.split(":", -1);

            DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Pagos pago;
            try {
                pago = new Pagos(
                        Integer.parseInt(id[1]),
                        Integer.parseInt(idCliente[1]),
                        LocalDate.parse(fecha[1], formateador), // Linea cambiada a LocalDate
                        Double.parseDouble(importe[1]),
                        Double.parseDouble(litros[1]),
                        combustible[1]
                );
            } catch (DateTimeParseException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Error al deserializar la línea [" + linea + "]", e);
            }

            return pago;
        };

    }

    private void crearFichero() {//al crear el fichero que se creen las {} [] iniciales y al terminar
        try {
            // Se utiliza la clase Files para comprobar la existencia
            if (!Files.exists(this.ruta)) {
                // Se utiliza la clase Files para la creación física
                Files.createFile(this.ruta);
            }
        } catch (IOException e) {
            System.out.println("Ocurrió un error al crear el fichero: " + e.getMessage());
        }
    }

    @Override
    public void guadarEnFichero(Object o) { //tener en cuenta las llaves
        try (BufferedWriter bw=Files.newBufferedWriter(this.ruta, APPEND)){
            String objetoEnCSV = "";
            if (o instanceof Cliente){
                objetoEnCSV=this.serializadorCliente.apply((Cliente) o);
            }
            if (o instanceof Pagos){
                objetoEnCSV=this.serializadorPagos.apply((Pagos) o);
            }
            bw.write(objetoEnCSV);
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Ocurrió un error al guardar el fichero: " + e.getMessage());
        }
    }

    @Override
    public List<Object> leerFichero() {//tener en cuenta las llaves
        List<Object> objetos = new ArrayList<>();
        try {
            for (String linea : Files.readAllLines(this.ruta)) {
                String[] campos = linea.split(",", -1);
                if (campos.length == 4) {
                    objetos.add(this.deserializadorCliente.apply(linea));
                } else if (campos.length == 6) {
                    objetos.add(this.deserializadorPagos.apply(linea));
                } else {
                    throw new IllegalArgumentException("Registro no interpretable: " + linea);
                }
            }
        }catch (IOException e) {
            System.out.println("Ocurrió un error al leer el fichero: " + e.getMessage());
        }

        return objetos;
    }
}
