import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        //excepciones
        //control  de errores
        ControladorMenu controlador=new ControladorMenu();
        GestorUsuario gUsuario=new GestorUsuario();
        GestorFicherosEnCSV gCliente=new GestorFicherosEnCSV(Path.of("cliente.csv"));
        GestorFicherosEnCSV gPagos=new GestorFicherosEnCSV(Path.of("pagos.csv"));

        int opcion;

        do {
            // Muestra el menú y lee la opción elegida (0 a 5)
            opcion = gUsuario.menuOpciones();

            switch (opcion) {
                case 1 -> controlador.darAltaCliente(gUsuario, gCliente);
                case 2 -> controlador.listaClientes(gCliente);
                case 3 -> controlador.buscarClientes(gUsuario, gCliente);
                case 4 -> controlador.registrarPago(gUsuario, gPagos, gCliente); // Paso de gCliente para validar la FK
                case 5 -> controlador.listaPagos(gPagos);
                case 0 -> System.out.println("Hasta pronto.");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }
}