import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MigraCSVtoJson {
    public record Resultado(int clientes, int pagos) {}


    //Para migrar lo que se va a hacer es leer en csv y guardar en json
    public Resultado migrar (Path clienteCsv, Path pagoCsv, Path clienteJson, Path pagoJson){

        //compruebo si estan bien las rutas
        if (Files.notExists(clienteCsv)) {
            throw new IllegalArgumentException("No existe el CSV de clientes: " + clienteCsv);
        }
        if (Files.notExists(pagoCsv)) {
            throw new IllegalArgumentException("No existe el CSV de pagos: " + pagoCsv);
        }
        if (Files.exists(clienteJson)) {
            throw new IllegalStateException("El JSON de clientes ya existe: " + clienteJson);
        }
        if (Files.exists(pagoJson)) {
            throw new IllegalStateException("El JSON de pagos ya existe: " + pagoJson);
        }

        System.out.println("Validación completada.");

        try {
            //Creo los gestores de CSV y leo los ficheros
            GestorFichero gClienteCsv=new GestorFicherosEnCSV(clienteCsv);
            List<Object> clientes=gClienteCsv.leerFichero();

            GestorFichero gPagoCsv=new GestorFicherosEnCSV(pagoCsv);
            List<Object> pagos=gPagoCsv.leerFichero();

            //Creo los gestores de JSON y guardo en los ficheros
            GestorFichero gClienteJson=new GestorFicherosEnJSON(clienteJson);
            for (Object o : clientes){
                gClienteJson.guardarEnFichero(o);
            }

            GestorFichero gPagoJson=new GestorFicherosEnJSON(pagoJson);
            for (Object o : pagos){
                gPagoJson.guardarEnFichero(o);
            }

            return new Resultado(clientes.size(), pagos.size());
        } catch (RuntimeException e) {
            limpiar(clienteJson, pagoJson);
            throw e;
        }

    }

    // Borra los JSON que la migración haya podido crear antes de fallar.
    private void limpiar(Path clienteJson, Path pagoJson) {
        try {
            Files.deleteIfExists(clienteJson);
            Files.deleteIfExists(pagoJson);
        } catch (IOException e) {
            System.out.println("No se pudieron borrar los JSON: " + e.getMessage());
        }
    }

}
