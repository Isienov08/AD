import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GestorFicherosEnJSON  implements GestorFichero{
    private final Path ruta;
    private Function<Cliente, String> serializadorCliente;
    private Function<Pagos, String> serializadorPagos;
    private Function<String, Cliente> deserializadorCliente;
    private Function<String, Pagos> deserializadorPagos;

    public GestorFicherosEnJSON(Path ruta) {
        this.ruta=ruta;
        crearFichero();
        this.serializadorCliente = cliente ->
                "{" +
                        "\"id\": " + cliente.getId() + "," +
                        "\"nombre\": \"" + cliente.getNombre() + "\"," +
                        "\"telefono\": \"" + cliente.getTelefono() + "\"," +
                        "\"matricula\": \"" + cliente.getMatricula() + "\"" +
                        "}";

        this.serializadorPagos = pagos ->
                "{" +
                        "\"id\": " + pagos.getId() + "," +
                        "\"clienteId\": " + pagos.getIdCliente() + "," +
                        "\"fecha\": \"" + pagos.getFecha() + "\"," +
                        "\"importe\": " + pagos.getImporte() + "," +
                        "\"litros\": " + pagos.getLitros() + "," +
                        "\"combustible\": \"" + pagos.getCombustible() + "\"" +
                        "}";

        this.deserializadorCliente = linea -> {
            String[] campos = linea.split(",", -1);

            try {
                return new Cliente(
                        Integer.parseInt(extraerValor(campos[0])),
                        extraerValor(campos[1]),
                        extraerValor(campos[2]),
                        extraerValor(campos[3])
                );
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Error al deserializar el cliente", e);
            }
        };

        this.deserializadorPagos = linea -> {
            String[] campos = linea.split(",", -1);
            DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            try {
                return new Pagos(
                        Integer.parseInt(extraerValor(campos[0])),
                        Integer.parseInt(extraerValor(campos[1])),
                        LocalDate.parse(extraerValor(campos[2]), formateador),
                        Double.parseDouble(extraerValor(campos[3])),
                        Double.parseDouble(extraerValor(campos[4])),
                        extraerValor(campos[5])
                );
            } catch (DateTimeParseException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Error al deserializar la línea un pago", e);
            }
        };

    }

    private String extraerValor(String claveValor) {
        String limpia = claveValor.replace("{", "").replace("}", "").trim();
        String[] partes = limpia.split(":", 2);
        if (partes.length < 2) {
            return "";
        }
        String valor = partes[1].trim();
        // Si el valor viene entre comillas (texto), se las quitamos
        if (valor.length() >= 2 && valor.startsWith("\"") && valor.endsWith("\"")) {
            valor = valor.substring(1, valor.length() - 1);
        }
        return valor;
    }

    private void crearFichero() {
        try {
            if (!Files.exists(this.ruta)) {
                Files.writeString(this.ruta, "[]"); // almacenamiento vacío
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo crear el almacenamiento " + this.ruta, e);
        }
    }

    @Override
    public void guardarEnFichero(Object o) {
        //Leo el fichero para tener la lista de clientes|pagos
        //Si falla (IOException o registro mal formado), lanza excepción y no se escribe nada
        List<Object> objetos = leerFichero();

        //Añado
        objetos.add(o);

        //Reescribo el fichero completo desde 0
        String salto = System.lineSeparator();

        String cuerpo = objetos.stream()
                .map(obj -> "  " + serializar(obj))                           // map: cada objeto -> su línea
                .reduce((a, b) -> a.concat("," + salto).concat(b))  // reduce: junta todas las líneas en un texto
                .orElse("");                                                   // por si la lista estuviera vacía

        String contenido = "[" + salto + cuerpo + salto + "]" + salto;

        try {
            Files.writeString(this.ruta, contenido);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar en " + this.ruta, e);
        }
    }

    private String serializar(Object o) {
        return switch (o) {
            case Cliente c -> this.serializadorCliente.apply(c);
            case Pagos p -> this.serializadorPagos.apply(p);
            default -> throw new IllegalArgumentException(
                    "Tipo no soportado: " + o.getClass().getSimpleName());
        };
    }

    @Override
    public List<Object> leerFichero() {
        List<Object> objetos = new ArrayList<>();
        try {
            for (String linea : Files.readAllLines(this.ruta)) {
                String registro = linea.trim();

                //En las líneas que no son registros pasa a la siguiente
                if (registro.isEmpty() || registro.equals("[") || registro.equals("]") || registro.equals("[]")) {
                    continue;
                }

                //Quita la coma que separa un registro del siguiente
                if (registro.endsWith(",")) {
                    registro = registro.substring(0, registro.length() - 1);
                }

                //Distingue cliente o pago por número de campos
                String[] campos = registro.split(",", -1);
                if (campos.length == 4) {
                    objetos.add(this.deserializadorCliente.apply(registro));
                } else if (campos.length == 6) {
                    objetos.add(this.deserializadorPagos.apply(registro));
                } else {
                    throw new IllegalArgumentException("Registro no interpretable: " + linea);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer " + this.ruta, e);
        }
        return objetos;
    }
}
