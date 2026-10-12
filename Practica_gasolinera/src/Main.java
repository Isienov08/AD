import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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

        List <Object> clientes=gClienteJSON.leerFichero();
        List <Object> pagos=gPagosJSON.leerFichero();

        List <Object> clientesNuevos=new ArrayList<>();
        List <Object> pagosNuevos=new ArrayList<>();


        int opcion;

        do {
            // Muestra el menú y lee la opción elegida (0 a 5)
            opcion = gUsuario.menuOpciones();

            switch (opcion) {
                case 1 -> controlador.darAltaCliente(gUsuario, clientes, clientesNuevos);
                case 2 -> controlador.listaClientes(clientes);
                case 3 -> controlador.buscarClientes(gUsuario, clientes);
                case 4 -> controlador.registrarPago(gUsuario, clientes, pagos, pagosNuevos); // Paso de gCliente para validar la FK
                case 5 -> controlador.listaPagos(pagos);
                case 0 -> {
                    System.out.println("Hasta pronto.");
                    //guardamos los datos
                    for (Object o:clientesNuevos){
                        gClienteJSON.guardarEnFichero(o);
                    }
                    for (Object o:pagosNuevos){
                        gPagosJSON.guardarEnFichero(o);
                    }
                }
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }
}