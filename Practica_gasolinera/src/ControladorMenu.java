import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ControladorMenu {

    public boolean migracion(GestorUsuario gestorUsuario) {
        String respuesta = gestorUsuario.preguntarMigrar();

        if (respuesta.equals("s")){
            var rutas = gestorUsuario.datosMigrar();
            try {
                var resultado = new MigraCSVtoJson().migrar(
                        rutas.clienteCsv(), rutas.pagoCsv(),
                        rutas.clienteJson(), rutas.pagoJson());
                System.out.println("Clientes migrados: " + resultado.clientes());
                System.out.println("Pagos migrados: " + resultado.pagos());
                System.out.println("Migración completada.");
                return true;
            } catch (RuntimeException e) {
                System.out.println("Error en la migración: " + e.getMessage());
                return false;
            }
        }

        return true;
    }

    public void darAltaCliente(GestorUsuario gUsuario, GestorFichero gCliente) {
        Cliente c=gUsuario.datosCliente(generadorIDCliente(gCliente), gCliente);
        gCliente.guardarEnFichero(c);
//        clientes.add(c);
        System.out.println("Se ha dado de alta el cliente\n");
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

        for (Object c : clientes) {
            System.out.println(c.toString());
        }
    }

    public void buscarClientes(GestorUsuario gUsuario, GestorFichero gCliente) {
        //Invocamos leerFichero() que devuelve List<Object>
        List<Object> listaObjetos = gCliente.leerFichero();

        String busqueda = gUsuario.datoBusquedaCliente().toLowerCase();

        boolean encontrado = false;

        if (listaObjetos != null) {
            for (Object obj : listaObjetos) {
                // Verificamos que el objeto sea de tipo Cliente
                if (obj instanceof Cliente) {
                    // Hacemos el cast a Cliente
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
                        System.out.println(c.getId() + "\t" + c.getNombre() + "\t" + c.getTelefono() + "\t" + c.getMatricula()+ "\n");
                    }
                }
            }
        }

        // Si no se encontró nada o la lista estaba vacía
        if (!encontrado) {
            System.out.println("No se han encontrado clientes. \n");
        }
    }

    public void registrarPago(GestorUsuario gUsuario, GestorFichero gPagos, GestorFichero gCliente) {
        Pagos p=gUsuario.datosPagos(generadorIDPago(gPagos), gCliente);
        gPagos.guardarEnFichero(p);
//        pagos.add(p);
        System.out.println("Se ha dado de alta el pago \n");
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
