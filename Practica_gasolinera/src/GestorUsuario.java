import java.util.Scanner;

public class GestorUsuario {
    public int menuOpciones () {
        System.out.println("=== GESTIÓN DE GASOLINERA ===");
        System.out.println("1. Dar de alta un cliente");
        System.out.println("2. Listar clientes");
        System.out.println("3. Buscar clientes");
        System.out.println("4. Procesar un pago de repostaje");
        System.out.println("5. Consultar pagos");
        System.out.println("0. Salir");
        System.out.print("Opción: ");

        Scanner sc=new Scanner(System.in);
        int opcion=sc.nextInt();


        return opcion;
    }

    //pedriDato()
}
