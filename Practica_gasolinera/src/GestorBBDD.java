import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestorBBDD {
    private static final String SELECT_CLIENTES =
            "SELECT id, nombre, telefono, matricula FROM clientes ORDER BY id";
    private static final String SELECT_PAGOS =
            "SELECT id, id_cliente, fecha, importe, litros, combustible FROM pagos ORDER BY id";
    private static final String INSERT_CLIENTE =
            "INSERT INTO clientes (id, nombre, telefono, matricula) VALUES (?, ?, ?, ?)";
    private static final String INSERT_PAGO =
            "INSERT INTO pagos (id, id_cliente, fecha, importe, litros, combustible) VALUES (?, ?, ?, ?, ?, ?)";

    //Gestionaremos los clientes y pagos en esta clase pero manteniendo List<Object> para no tener que cambiar las clases contorladorMenu y gestorUsuario


}
