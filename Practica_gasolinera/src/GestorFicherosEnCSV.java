import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.nio.file.StandardOpenOption.APPEND;//??

public class GestorFicherosEnCSV {
    Path ruta;
    private Function<Cliente, String> serializadorCliente;
    private Function<Pagos, String> serializadorPagos;
    private Function<String, Cliente> deserializadorCliente;
    private Function<String, Pagos> deserializadorPagos;



    public GestorFicherosEnCSV(Path ruta) {
        this.ruta=ruta;
        crearFichero();
        this.serializadorCliente=cliente ->
                cliente.getId() + "," +
                        cliente.getNombre() + "," +
                        cliente.getTelefono() + "," +
                        cliente.getMatricula();
        this.serializadorPagos = pagos ->
                pagos.getId() + "," +
                        pagos.getIdCliente() + "," +
                        pagos.getFecha() + "," +
                        pagos.getImporte() + "," +
                        pagos.getLitros() + "," +
                        pagos.getCombustible();
        this.deserializadorCliente=linea -> {
            String[] campos = linea.split(",", -1);
            return new Cliente(
                    Integer.parseInt(campos[0]),
                    campos[1],
                    campos[2],
                    campos[3]);
        };
        this.deserializadorPagos = linea -> {
            String[] campos = linea.split(",", -1);

            return new Pagos(
                    Integer.parseInt(campos[0]),
                    Integer.parseInt(campos[1]),
                    java.sql.Date.valueOf(campos[2]),            // fecha (yyyy-MM-dd)
                    Double.parseDouble(campos[3]),
                    Double.parseDouble(campos[4]),
                    campos[5]
            );
        };

    }

    private void crearFichero() {
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


    public void guadarEnFichero(Object o) {
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


    public List<Object> leerFichero() {
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

