import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ControladorMenu {

    public void darAltaCliente(GestorUsuario gUsuario, GestorFichero gCliente) {
        Cliente c=gUsuario.datosCliente(generadorIDCliente(gCliente), gCliente);
        gCliente.guadarEnFichero(c);
        System.out.println("Se ha dado de alta el cliente");
    }

    public List<Cliente> obtenerClientes(GestorFichero gCliente) {
        List<Object> listaLeer = gCliente.leerFichero();
        List<Cliente> clientes = new ArrayList<>();

        for (Object obj : listaLeer) {
            if (obj instanceof Cliente c) {
                clientes.add(c);
            }
        }

        return clientes;
    }

    public void listaClientes(GestorFichero gCliente) {
        List<Cliente> clientes = obtenerClientes(gCliente);

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.\n");
            return;
        }

        // Imprimir la cabecera
        System.out.println(String.format(Cliente.FORMATO, "ID", "NOMBRE", "TELÉFONO", "MATRÍCULA"));

        for (Cliente c : clientes) {
            System.out.println(c.toString());
        }
    }

    public void buscarClientes(GestorUsuario gUsuario, GestorFichero gCliente) {
        // 1. Invocamos leerFichero() que devuelve List<Object>
        List<Object> listaObjetos = gCliente.leerFichero();

        String busqueda = gUsuario.datoBusquedaCliente().toLowerCase();

        boolean encontrado = false;

        if (listaObjetos != null) {
            for (Object obj : listaObjetos) {
                // Verificamos que el objeto sea de tipo Cliente
                if (obj instanceof Cliente) {
                    // Hacemos el cast explícito a Cliente
                    Cliente c = (Cliente) obj;

                    // Comprobamos las coincidencias en Nombre, Teléfono o Matrícula
                    if ((c.getNombre().toLowerCase().contains(busqueda)) ||
                            c.getTelefono().toLowerCase().contains(busqueda) ||
                            c.getMatricula().toLowerCase().contains(busqueda)) {

                        // Imprimimos la cabecera solo con la primera coincidencia
                        if (!encontrado) {
                            System.out.println("ID\tNOMBRE\tTELÉFONO\tMATRÍCULA");
                            encontrado = true;
                        }

                        // Mostramos el cliente formateado
                        System.out.println(c.getId() + "\t" + c.getNombre() + "\t" + c.getTelefono() + "\t" + c.getMatricula());
                    }
                }
            }
        }

        // Si no se encontró nada o la lista estaba vacía
        if (!encontrado) {
            System.out.println("No se han encontrado clientes.");
        }
    }

    public void registrarPago(GestorUsuario gUsuario, GestorFichero gPagos, GestorFichero gCliente) {
        Pagos p=gUsuario.datosPagos(generadorIDPago(gPagos), gCliente);
        gPagos.guadarEnFichero(p);
        System.out.println("Se ha dado de alta el pago");
    }

    public void listaPagos(GestorFichero gPagos){
        List <Object> listaLeer=gPagos.leerFichero();
        if (listaLeer.isEmpty()) {
            System.out.println("No hay pagos registrados.\n");
            return;
        }

        System.out.printf(Pagos.FORMATO + "%n", "ID", "CLIENTE", "FECHA", "IMPORTE", "LITROS", "COMBUSTIBLE");

        for (Object obj : listaLeer) {
            if (obj instanceof Pagos p) {
                System.out.println(p.toString());
            }
        }
    }

    //asume que el fichero solo lo modifica esta aplicación y que el id más alto está en la última línea
    public int generadorIDCliente(GestorFichero gestor) {
        List<Object> lista = gestor.leerFichero();

        if (lista == null || lista.isEmpty()) {
            return 1;
        }

        Object ultimoObjeto = lista.get(lista.size() - 1);
        Cliente ultimoCliente = (Cliente) ultimoObjeto;

        return ultimoCliente.getId() + 1;
    }

    public int generadorIDPago(GestorFichero gestor) {
        List<Object> lista = gestor.leerFichero();

        if (lista == null || lista.isEmpty()) {
            return 1;
        }

        Object ultimoObjeto = lista.get(lista.size() - 1);
        Pagos ultimoPago = (Pagos) ultimoObjeto;

        return ultimoPago.getId() + 1;
    }


}
