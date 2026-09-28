import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
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

    public Cliente datosCliente(int idGenerado) {

        String nombre = "";
        String telefono = "";
        String matricula = "";

        boolean correcto;

        correcto = false;
        while (!correcto) {
            System.out.print("Introduce el nombre del cliente: ");
            nombre = sc.nextLine().trim();
            if (!nombre.isEmpty()) {
                correcto = true;
            } else {
                System.out.println("El nombre no puede estar vacío.");
            }
        }

        correcto = false;
        while (!correcto) {
            System.out.print("Introduce el teléfono del cliente: ");
            telefono = sc.nextLine().trim();
            if (!telefono.isEmpty()) {
                correcto = true;
            } else {
                System.out.println("El teléfono no puede estar vacío.");
            }
        }

        correcto = false;
        while (!correcto) {
            System.out.print("Introduce la matrícula del cliente: ");
            matricula = sc.nextLine().trim().toUpperCase();
            if (!matricula.isEmpty()) {
                correcto = true;
            } else {
                System.out.println("La matrícula no puede estar vacía.");
            }
        }

        return new Cliente(idGenerado, nombre, telefono, matricula);
    }

    public Pagos datosPagos(int idGenerado) {

        int idCliente = 0;
        LocalDate fecha = null;
        double importe = 0;
        double litros = 0;
        String combustible = "";

        boolean correcto;

        correcto = false;
        while (!correcto) {
            System.out.print("Introduce el ID del cliente: ");
            String linea = sc.nextLine().trim();
            try {
                idCliente = Integer.parseInt(linea);
                if (idCliente > 0) {
                    correcto = true;
                } else {
                    System.out.println("El ID del cliente debe ser un entero positivo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número entero.");
            }
        }

        correcto = false;
//        while (!correcto) {
//            System.out.print("Introduce la fecha (dd/MM/aaaa) o vacío para la actual: ");
//            String textoFecha = sc.nextLine().trim();
//            try {
//                if (textoFecha.isEmpty()) {
//                    fecha = LocalDate.now();
//                    correcto = true;
//                } else {
//                    DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/uuuu")
//                            .withResolverStyle(ResolverStyle.STRICT);
//                    fecha = LocalDate.parse(textoFecha, f);
//                    correcto = true;
//                }
//            } catch (Exception e) {
//                System.out.println("La fecha no es válida. Formato requerido: dd/MM/aaaa.");
//            }
//        }

        correcto = false;
        while (!correcto) {
            System.out.print("Introduce el importe en euros: ");
            String textoImporte = sc.nextLine().trim().replace(",", ".");
            try {
                importe = Double.parseDouble(textoImporte);
                if (importe > 0 && textoImporte.matches("\\d+(\\.\\d{1,2})?")) {
                    correcto = true;
                } else {
                    System.out.println("El importe debe ser mayor que cero y tener como máximo dos decimales.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número válido.");
            }
        }

        correcto = false;
        while (!correcto) {
            System.out.print("Introduce los litros: ");
            String textoLitros = sc.nextLine().trim().replace(",", ".");
            try {
                litros = Double.parseDouble(textoLitros);
                if (litros > 0 && textoLitros.matches("\\d+(\\.\\d{1,2})?")) {
                    correcto = true;
                } else {
                    System.out.println("Los litros deben ser mayores que cero y tener como máximo dos decimales.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número válido.");
            }
        }

        correcto = false;
        while (!correcto) {
            System.out.print("Introduce el combustible: ");
            combustible = sc.nextLine().trim();
            if (!combustible.isEmpty()) {
                correcto = true;
            } else {
                System.out.println("El combustible no puede estar vacío.");
            }
        }

        return new Pagos(idGenerado, idCliente, fecha, importe, litros, combustible);
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
