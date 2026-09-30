import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {

        //id cliente automatico
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
                case 1:
                    controlador.darAltaCliente(gUsuario, gCliente);
                    break;
                case 2:
                    controlador.listaClientes(gCliente);
                    break;
                case 3:
                    //controlador.buscarClientes();
                    break;
                case 4:
                    controlador.registrarPago(gUsuario, gPagos);
                    break;
                case 5:
                    controlador.listaPagos(gPagos);
                    break;
                case 0:
                    System.out.println("Hasta pronto.");
                    break;
            }

        } while (opcion != 0); // Repite el bucle mientras la opción no sea salir (0)
    }
}