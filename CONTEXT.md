# Contexto del repositorio `AD` — Acceso a Datos (DAM2)

Este repositorio reúne las prácticas de la asignatura **Acceso a Datos** del ciclo
DAM2. Contiene dos líneas de trabajo independientes:

1. **`Practica_gasolinera/`**: aplicación de consola para gestionar clientes y
   repostajes de una gasolinera. Ha evolucionado de ficheros CSV a JSON y,
   actualmente, a persistencia en SQLite (trabajo en curso).
2. **`RA2-JDBC/`**: proyecto Gradle con ejemplos y material de JDBC (bloques 02
   y 03) usando el Jardín Botánico como dominio de ejemplo.

Además están el enunciado en PDF de la Práctica 1 y `DiagramaClaseV2.drawio`
(diagrama de clases de la gasolinera).

> Nota: este documento sustituye al antiguo `README.md` de la raíz, que
> describía solo las prácticas 1 y 2 y había quedado desactualizado (indicaba
> JDK 25 y compilación con `javac -d out src/*.java`, cuando el código ya está
> organizado en paquetes). `RA2-JDBC/README.md` se mantiene tal cual.

---

## Estructura general

```
AD/
├── CONTEXT.md                         ← este documento
├── DiagramaClaseV2.drawio
├── cb2658fd-...pdf                    ← enunciado Práctica 1
├── Practica_gasolinera/
│   ├── src/
│   │   ├── MainConFicheros.java       ← punto de entrada (JSON)
│   │   ├── app/                       ← entidades, controlador y entrada de usuario
│   │   ├── practicaConFicheros/       ← CSV, JSON y migración
│   │   └── bbdd/                      ← conexión y gestión SQLite (en curso)
│   ├── sql/01-esquma.sql
│   ├── cliente.csv / pagos.csv
│   ├── cliente.json / pagos.json
│   ├── gasolinera.sqlite
│   └── Practica_gasolinera.iml
└── RA2-JDBC/
    ├── build.gradle.kts / settings.gradle.kts / gradlew
    ├── sql/sqlite/ y sql/mysql/
    ├── src/main/java/.../bloque02/ y bloque03/
    ├── src/test/java/.../bloque02/
    ├── tools/*.py
    └── README.md
```

---

## `Practica_gasolinera`

### Dominio y funcionalidad

Aplicación de consola con un menú (opciones `0`–`5`) para:

1. Dar de alta un cliente.
2. Listar clientes.
3. Buscar clientes (por nombre, teléfono o matrícula).
4. Registrar un pago de repostaje (validando que el cliente exista).
5. Consultar pagos.
0. Salir (guarda los datos nuevos).

Un `Cliente` tiene `id, nombre, telefono, matricula` (matrícula única y en
mayúsculas). Un `Pagos` tiene `id, idCliente, fecha (LocalDate), importe,
litros, combustible`; importe y litros deben ser mayores que cero y con un
máximo de dos decimales. Las validaciones están en los *setters* de las
entidades y en `GestorUsuario`.

### Evolución / estado

| Fase | Estado | Persistencia |
| --- | --- | --- |
| Práctica 1 | Terminada | `GestorFicherosEnCSV` → `cliente.csv`, `pagos.csv` |
| Práctica 2 | Terminada | `GestorFicherosEnJSON` + `MigraCSVtoJson` → `cliente.json`, `pagos.json` |
| Práctica 3 (BBDD) | **En curso** | `ConexionBBDD` listo; `GestorBBDD` aún sin implementar → `gasolinera.sqlite` |

`MainConFicheros` es el arranque actual: si no existen los ficheros JSON,
pregunta al usuario si desea migrar desde CSV; después carga todas las entidades
en `List<Object>` y solo escribe al salir los objetos creados en la sesión.

### Arquitectura

| Clase / paquete | Responsabilidad |
| --- | --- |
| `MainConFicheros` | Arranca la app, lanza la migración si hace falta, carga los datos, muestra el menú y guarda al salir. |
| `app.ControladorMenu` | Ejecuta las opciones del menú y genera los IDs automáticos (último ID + 1). Mantiene la firma con `List<Object>`. |
| `app.GestorUsuario` | Lectura y validación por consola, búsqueda, validación de matrícula duplicada y de cliente existente, y diálogo de migración (`record Rutas`). |
| `app.Cliente`, `app.Pagos` | Entidades con validación en *setters* y `toString` con formato de tabla (`FORMATO`). |
| `practicaConFicheros.GestorFichero` | Interfaz: `guardarEnFichero(Object)` y `leerFichero(): List<Object>`. |
| `practicaConFicheros.GestorFicherosEnCSV` | Acceso a CSV con `Files`; serializadores/deserializadores guardados como `Function`. Detecta el tipo por número de campos (4 = cliente, 6 = pago). |
| `practicaConFicheros.GestorFicherosEnJSON` | Igual con JSON: escribe un array de objetos, uno por línea; lee detectando el tipo por número de campos. Usa `clienteId` en los pagos. |
| `practicaConFicheros.MigraCSVtoJson` | Migración de datos entre ambos formatos. |
| `bbdd.ConexionBBDD` | Abre `jdbc:sqlite:gasolinera.sqlite`, activa `PRAGMA foreign_keys = ON` en cada conexión y crea las tablas (`inicializarTablas`). Tiene un `main` de prueba de conexión. |
| `bbdd.GestorBBDD` | **En construcción**. Ya declara `SELECT_CLIENTES`, `SELECT_PAGOS`, `INSERT_CLIENTE` e `INSERT_PAGO`; la idea es gestionar clientes y pagos devolviendo `List<Object>` para no tener que cambiar `ControladorMenu` ni `GestorUsuario`. |

### Formatos de datos

**CSV** (sin cabecera, separador `,`, UTF-8, rutas relativas a
`Practica_gasolinera/`):

- `cliente.csv`: `id, nombre, telefono, matricula`
- `pagos.csv`: `id, idCliente, fecha (yyyy-MM-dd), importe, litros, combustible`

**JSON** (un array, un objeto por línea):

```json
{"id": 1, "nombre": "Idaira", "telefono": "124578963", "matricula": "1478MNC"}
{"id": 1, "clienteId": 2, "fecha": "2026-10-11", "importe": 15.99, "litros": 45.0, "combustible": "Diesel"}
```

Los ficheros se crean automáticamente en el primer arranque (`[]` en JSON).
El formato de línea está concentrado en el gestor concreto, de modo que cambiar
de almacenamiento implica cambiar esa clase (o añadir otra que implemente
`GestorFichero`) y el punto de entrada donde se instancian los gestores.

**SQLite** (`sql/01-esquma.sql`, espejo de las definiciones en `ConexionBBDD`):

- `clientes(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre, telefono, matricula UNIQUE)`
- `pagos(id INTEGER PRIMARY KEY AUTOINCREMENT, id_cliente, fecha, importe, litros,
  combustible, FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE)`
- `PRAGMA foreign_keys = ON` debe activarse en cada conexión nueva.

### Compilación y ejecución

Desde `Practica_gasolinera/` (el código usa paquetes y `record`/text blocks,
por lo que hace falta un JDK moderno; se recomienda 21 o superior):

```
javac -d out src/MainConFicheros.java src/app/*.java src/practicaConFicheros/*.java src/bbdd/*.java
java -cp out MainConFicheros
```

Para ejecutar las clases de `bbdd` hace falta además el driver de SQLite en el
*classpath* (el mismo `org.xerial:sqlite-jdbc` que usa `RA2-JDBC`).

`out/`, `.idea/` y los artefactos locales están ignorados por Git.

---

## `RA2-JDBC`

Proyecto Gradle independiente, abierto como proyecto en IntelliJ IDEA, con
**JDK 21** para el proyecto y para Gradle (el *wrapper* incluido descarga la
versión fijada).

- **Dependencias** (`build.gradle.kts`): `org.xerial:sqlite-jdbc`,
  `com.mysql:mysql-connector-j`, JUnit Jupiter y el launcher de JUnit.
- **Tareas propias**: `runSqlite`, `runSqliteMetadata`, `runMysql` y
  `runErrorConexion`, que lanzan los ejemplos del bloque 02.
- **Código**:
  - `bloque02/`: `PrimeraConexionSqlite`, `PrimeraConexionMysql`,
    `MetadatosSqlite`, `CrearBaseDatos`, `ErrorDeConexion`.
  - `bloque03/`: `CrearTabla`, `VariasSentencias`, `ErrorSql`.
  - `src/test/.../ConexionSqliteTest.java`: prueba de conexión SQLite.
- **SQL**: `sql/sqlite/` y `sql/mysql/` con esquemas y datos del Jardín
  Botánico. Los ejemplos SQLite usan `data/jardin-botanico.db` por defecto.
- **Utilidades**: `tools/generar_datos.py`, `preparar_sqlite.py` y
  `validar_sqlite.py`.
- El fichero de base de datos y las carpetas generadas localmente no forman
  parte de la entrega.

---

## Trabajo pendiente / notas para continuar

- Terminar `bbdd.GestorBBDD` (altas, consultas y conversión entre `ResultSet` y
  `Cliente`/`Pagos`) y crear un punto de entrada con base de datos, manteniendo
  las firmas basadas en `List<Object>` para no romper `app.*`.
- Decidir cómo se generarán los IDs con `AUTOINCREMENT` (actualmente los calcula
  `ControladorMenu` con "último ID + 1").
- Si se cambia el formato de almacenamiento, tocar el gestor concreto y el
  punto de entrada donde se instancia.
- Mantener este `CONTEXT.md` al día a medida que avance la práctica de BBDD.
