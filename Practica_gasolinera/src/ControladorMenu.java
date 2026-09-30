import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ControladorMenu {

    public void darAltaCliente(GestorUsuario gUsuario, GestorFicherosEnCSV gCliente) {
        Cliente c=gUsuario.datosCliente(generadorIDCliente(gCliente));
        gCliente.guadarEnFichero(c);
    }

    public void listaClientes(GestorFicherosEnCSV gCliente) {
        List<Object> listaLeer = gCliente.leerFichero();
        if (listaLeer.isEmpty()) {
            System.out.println("No hay clientes registrados.\n");
            return;
        }

        for (Object obj : listaLeer) {
            if (obj instanceof Cliente c) {
                System.out.println(c.toString());
            }
        }
    }



    public List<Cliente> buscarClientes(Cliente c) {

        return new ArrayList<>();
    }

    public Cliente buscarClientes(int id) {


        return null;
    }



    public void registrarPago(GestorUsuario gUsuario, GestorFicherosEnCSV gPagos) {
        Pagos p=gUsuario.datosPagos(generadorIDPago(gPagos));
        gPagos.guadarEnFichero(p);
    }

    public void listaPagos(GestorFicherosEnCSV gPagos){
        List <Object> listaLeer=gPagos.leerFichero();
        if (listaLeer.isEmpty()) {
            System.out.println("No hay pagos registrados.\n");
            return;
        }

        for (Object obj : listaLeer) {
            if (obj instanceof Pagos p) {
                System.out.println(p.toString());
            }
        }
    }

    //asume que el fichero solo lo modifica esta aplicación y que el id más alto está en la última línea
    public int generadorIDCliente(GestorFicherosEnCSV gestor) {
        List<Object> lista = gestor.leerFichero();

        if (lista == null || lista.isEmpty()) {
            return 1;
        }

        Object ultimoObjeto = lista.get(lista.size() - 1);
        Cliente ultimoCliente = (Cliente) ultimoObjeto;

        return ultimoCliente.getId() + 1;
    }

    public int generadorIDPago(GestorFicherosEnCSV gestor) {
        List<Object> lista = gestor.leerFichero();

        if (lista == null || lista.isEmpty()) {
            return 1;
        }

        Object ultimoObjeto = lista.get(lista.size() - 1);
        Pagos ultimoPago = (Pagos) ultimoObjeto;

        return ultimoPago.getId() + 1;
    }
}
