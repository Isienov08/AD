package bbdd;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class ConexionBBDD {
    private static final Path ARCHIVO = Path.of("gasolinera.sqlite");

    private static final String CREAR_CLIENTES = """
            CREATE TABLE IF NOT EXISTS clientes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(100) NOT NULL,
                telefono VARCHAR(20) NOT NULL,
                matricula VARCHAR(15) NOT NULL UNIQUE
            )
            """;

    private static final String CREAR_PAGOS = """
            CREATE TABLE IF NOT EXISTS pagos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_cliente INTEGER NOT NULL,
                fecha DATE NOT NULL,
                importe DECIMAL(10, 2) NOT NULL CHECK (importe > 0),
                litros DECIMAL(10, 2) NOT NULL CHECK (litros > 0),
                combustible VARCHAR(50) NOT NULL,
                FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE
            )
            """;

    public static Connection getConexion() throws SQLException {
        if (!Files.isRegularFile(ARCHIVO)) {
            throw new SQLException("No existe la base SQLite: " + ARCHIVO.toAbsolutePath());
        }
        return abrir();
    }

    private static Connection abrir() throws SQLException {
        Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + ARCHIVO.toAbsolutePath());
        try (Statement st = conexion.createStatement()) {
            // SQLite exige activar las claves foráneas en cada conexión nueva.
            st.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            conexion.close();
            throw e;
        }
        return conexion;
    }

    public static void inicializarTablas() throws SQLException {
        try (Connection conexion = abrir();
             Statement st = conexion.createStatement()) {
            st.execute(CREAR_CLIENTES);
            st.execute(CREAR_PAGOS);
        }
    }

    public static void main(String[] args) {
        if (!Files.isRegularFile(ARCHIVO)) {
            System.err.println("No existe la base SQLite: " + ARCHIVO.toAbsolutePath());
            System.err.println("Prepárala con: python tools/preparar_sqlite.py");
            return;
        }

        String url = "jdbc:sqlite:" + ARCHIVO.toAbsolutePath();
        try (Connection conexion = DriverManager.getConnection(url)) {
            // SQLite exige activar las claves foráneas en cada conexión nueva.
            try (Statement configuracion = conexion.createStatement()) {
                configuracion.execute("PRAGMA foreign_keys = ON");
            }
            System.out.println("Conexión SQLite abierta: " + ARCHIVO.toAbsolutePath());
            System.out.println("Conexión válida: " + conexion.isValid(2));
            System.out.println("Auto-commit: " + conexion.getAutoCommit());
        } catch (SQLException e) {
            System.err.println("Error al conectar con SQLite: " + e.getMessage());
            return;
        }
        System.out.println("La conexión se ha cerrado al salir de try-with-resources.");
    }
}