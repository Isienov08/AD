import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        //arranca la app
        ControladorMenu controlador=new ControladorMenu();
        GestorUsuario gUsuario=new GestorUsuario();

        //verificamos la migracion
        if (Files.notExists(Path.of("cliente.json")) && Files.notExists(Path.of("pagos.json"))){
            if (!controlador.migracion(gUsuario)) {
                return;
            }
        }

//        GestorFicherosEnCSV gCliente=new GestorFicherosEnCSV(Path.of("cliente.csv"));
//        GestorFicherosEnCSV gPagos=new GestorFicherosEnCSV(Path.of("pagos.csv"));

        //Cargamos los datos
        GestorFicherosEnJSON gClienteJSON=new GestorFicherosEnJSON(Path.of("cliente.json"));
        GestorFicherosEnJSON gPagosJSON=new GestorFicherosEnJSON(Path.of("pagos.json"));
//        List <Object> clientes=gClienteJSON.leerFichero();
//        List <Object> pagos=gPagosJSON.leerFichero();


        int opcion;

        do {
            // Muestra el menú y lee la opción elegida (0 a 5)
            opcion = gUsuario.menuOpciones();

            switch (opcion) {
                case 1 -> controlador.darAltaCliente(gUsuario, gClienteJSON);
                case 2 -> controlador.listaClientes(gClienteJSON);
                case 3 -> controlador.buscarClientes(gUsuario, gClienteJSON);
                case 4 -> controlador.registrarPago(gUsuario, gPagosJSON, gClienteJSON); // Paso de gCliente para validar la FK
                case 5 -> controlador.listaPagos(gPagosJSON);
                case 0 -> {
                    System.out.println("Hasta pronto.");
                }
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }
}