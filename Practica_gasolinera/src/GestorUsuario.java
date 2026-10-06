import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Scanner;

public class GestorUsuario {

    private final Scanner sc = new Scanner(System.in);

    public int menuOpciones () {
        System.out.println("=== GESTIÓN DE GASOLINERA ===");
        System.out.println("1. Dar de alta un cliente");
        System.out.println("2. Listar clientes");
        System.out.println("3. Buscar clientes");
        System.out.println("4. Procesar un pago de repostaje");
        System.out.println("5. Consultar pagos");
        System.out.println("0. Salir");
        System.out.print("Opción: ");

        int opcion = -1;
        boolean correcto = false;

        while (!correcto) {
            String linea = sc.nextLine().trim();
            try {
                opcion = Integer.parseInt(linea);
                if (opcion >= 0 && opcion <= 5) {
                    correcto = true;
                } else {
                    System.out.println("Opción no válida. Debe ser un número entre 0 y 5.");
                    System.out.print("Opción: ");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número entero.");
                System.out.print("Opción: ");
            }
        }

        return opcion;
    }

    public Cliente datosCliente(int idGenerado, GestorFichero gCliente) {
        while (true) {
            try {
                System.out.print("Introduce el nombre del cliente: ");
                String nombre = sc.nextLine();

                System.out.print("Introduce el teléfono del cliente: ");
                String telefono = sc.nextLine();

                System.out.print("Introduce la matrícula del cliente: ");
                String matricula = sc.nextLine();

                // Validación de matrícula duplicada
                if (existeMatricula(matricula, gCliente)) {
                    throw new IllegalArgumentException("La matrícula " + matricula + " ya está registrada a nombre de otro cliente");
                }

                // La validación ocurre dentro del constructor/setters de Cliente
                return new Cliente(idGenerado, nombre, telefono, matricula);

            } catch (IllegalArgumentException e) {
                // Muestra el mensaje de error definido en el setter ("El nombre no puede estar vacío", etc.)
                System.out.println("Error: " + e.getMessage() + ". Inténtalo de nuevo.\n");
            }
        }
    }

    public boolean existeMatricula(String matriculaBuscada, GestorFichero gCliente) {
        List<Object> listaLeer = gCliente.leerFichero();
        if (listaLeer != null) {
            for (Object obj : listaLeer) {
                if (obj instanceof Cliente c) {
                    // Comparamos ignorando mayúsculas/minúsculas y espacios
                    if (c.getMatricula().equalsIgnoreCase(matriculaBuscada.trim())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Pagos datosPagos(int idGenerado, GestorFichero gCliente) {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .withResolverStyle(ResolverStyle.STRICT);

        while (true) {
            try {
                System.out.print("ID del cliente: ");
                int idCliente = Integer.parseInt(sc.nextLine().trim());

                // Validación de la existencia del cliente
                if (!existeCliente(idCliente, gCliente)) {
                    throw new IllegalArgumentException("No existe ningún cliente con el ID " + idCliente);
                }

                System.out.print("Fecha (dd/MM/yyyy; vacío para hoy): ");
                String textoFecha = sc.nextLine().trim();

                // Conversión de String a LocalDate
                LocalDate fecha;
                if (textoFecha.isEmpty()) {
                    fecha = LocalDate.now(); // Asigna la fecha actual si está vacío
                } else {
                    fecha = LocalDate.parse(textoFecha, formateador); // Convierte y valida el texto
                }

                System.out.print("Importe (€): ");
                double importe = Double.parseDouble(sc.nextLine().trim().replace(",", "."));

                System.out.print("Litros: ");
                double litros = Double.parseDouble(sc.nextLine().trim().replace(",", "."));

                System.out.print("Combustible: ");
                String combustible = sc.nextLine();

                // Pasa la instancia de LocalDate directamente al constructor
                return new Pagos(idGenerado, idCliente, fecha, importe, litros, combustible);

            } catch (NumberFormatException e) {
                System.out.println("Error: El ID del cliente, el importe y los litros deben ser números válidos. Inténtalo de nuevo.\n");
            } catch (DateTimeParseException e) {
                System.out.println("Error: La fecha no es válida. Formato requerido: dd/MM/yyyy. Inténtalo de nuevo.\n");
            } catch (IllegalArgumentException e) {
                // Muestra las validaciones propias de los setters de Pagos (importe/litros <= 0, etc.)
                System.out.println("Error: " + e.getMessage() + ". Inténtalo de nuevo.\n");
            }
        }
    }

    public boolean existeCliente(int idBuscado, GestorFichero gCliente) {
        List<Object> listaLeer = gCliente.leerFichero();
        if (listaLeer != null) {
            for (Object obj : listaLeer) {
                if (obj instanceof Cliente c && c.getId() == idBuscado) {
                    return true;
                }
            }
        }
        return false;
    }

    public String datoBusquedaCliente() {

        String texto = "";
        boolean correcto = false;

        while (!correcto) {
            System.out.print("Introduce el texto de búsqueda: ");
            texto = sc.nextLine().trim();
            if (!texto.isEmpty()) {
                correcto = true;
            } else {
                System.out.println("El texto de búsqueda no puede estar vacío.");
            }
        }

        return texto;
    }
}
