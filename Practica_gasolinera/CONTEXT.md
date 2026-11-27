# Contexto del proyecto `Practica_gasolinera`

Aplicación de consola en Java de la asignatura **Acceso a Datos** (DAM2) para
registrar clientes y pagos de repostajes de una gasolinera. El proyecto ha
evolucionado de ficheros CSV a JSON y, actualmente, está migrando su
persistencia a SQLite.

Este documento describe **únicamente este proyecto**: su estructura, su
funcionalidad, el estado de cada fase y cómo compilarlo y ejecutarlo.

---

## Estructura del proyecto

```
Practica_gasolinera/
├── CONTEXT.md                  ← este documento
├── Practica_gasolinera.iml
├── src/
│   ├── MainConFicheros.java    ← punto de entrada actual (JSON)
│   ├── app/                    ← entidades, controlador y entrada de usuario
│   ├── practicaConFicheros/    ← CSV, JSON y migración entre formatos
│   └── bbdd/                   ← conexión y gestión SQLite (en curso)
├── sql/
│   └── 01-esquma.sql           ← esquema de las tablas clientes y pagos
├── cliente.csv / pagos.csv     ← almacenamiento de la Práctica 1
├── cliente.json / pagos.json   ← almacenamiento de la Práctica 2
├── gasolinera.sqlite           ← base de datos de la Práctica 3
└── out/                        ← clases compiladas (generado, ignorado por Git)
```

---

## Dominio y funcionalidad

El programa muestra un menú con las opciones `0`–`5`:

1. Dar de alta un cliente.
2. Listar clientes.
3. Buscar clientes (por nombre, teléfono o matrícula).
4. Registrar un pago de repostaje (validando que el cliente exista).
5. Consultar pagos.
0. Salir (guarda los datos creados durante la sesión).

**`Cliente`**: `id, nombre, telefono, matricula` (matrícula única y normalizada
a mayúsculas).

**`Pagos`**: `id, idCliente, fecha (LocalDate), importe, litros, combustible`.
Importe y litros deben ser mayores que cero y tener como máximo dos decimales.

Las validaciones están repartidas entre los *setters* de las entidades y los
métodos de `GestorUsuario` (matrícula duplicada, existencia del cliente, formato
de fecha, etc.). Los identificadores se generan en `ControladorMenu` como
"último ID + 1".

---

## Evolución / estado

| Fase | Estado | Persistencia |
| --- | --- | --- |
| Práctica 1 | Terminada | `GestorFicherosEnCSV` → `cliente.csv`, `pagos.csv` |
| Práctica 2 | Terminada | `GestorFicherosEnJSON` + `MigraCSVtoJson` → `cliente.json`, `pagos.json` |
| Práctica 3 (BBDD) | **En curso** | `ConexionBBDD` listo; `GestorBBDD` aún sin implementar → `gasolinera.sqlite` |

`MainConFicheros` es el arranque actual: si no existen los ficheros JSON,
pregunta al usuario si desea migrar desde CSV; después carga todas las entidades
en listas `List<Object>` y solo escribe al salir los objetos creados en la
sesión.

---

## Arquitectura

| Clase / paquete | Responsabilidad |
| --- | --- |
| `MainConFicheros` | Arranca la app, lanza la migración si hace falta, carga los datos, muestra el menú y guarda al salir. |
| `app.ControladorMenu` | Ejecuta las opciones del menú y genera los IDs automáticos. Conserva las firmas con `List<Object>`. |
| `app.GestorUsuario` | Lectura y validación por consola, búsqueda, validación de matrícula duplicada y de cliente existente, y diálogo de migración (`record Rutas`). |
| `app.Cliente`, `app.Pagos` | Entidades con validación en *setters* y `toString` con formato de tabla (`FORMATO`). |
| `practicaConFicheros.GestorFichero` | Interfaz: `guardarEnFichero(Object)` y `leerFichero(): List<Object>`. |
| `practicaConFicheros.GestorFicherosEnCSV` | Acceso a CSV con `Files`; serializadores/deserializadores guardados como `Function`. Detecta el tipo por número de campos (4 = cliente, 6 = pago). |
| `practicaConFicheros.GestorFicherosEnJSON` | Igual con JSON: escribe un array de objetos, uno por línea; lee detectando el tipo por número de campos. Usa `clienteId` en los pagos. |
| `practicaConFicheros.MigraCSVtoJson` | Migración de datos entre CSV y JSON. |
| `bbdd.ConexionBBDD` | Abre `jdbc:sqlite:gasolinera.sqlite`, activa `PRAGMA foreign_keys = ON` en cada conexión y crea las tablas (`inicializarTablas`). Tiene un `main` de prueba de conexión. |
| `bbdd.GestorBBDD` | **En construcción**. Declara `SELECT_CLIENTES`, `SELECT_PAGOS`, `INSERT_CLIENTE` e `INSERT_PAGO`; la idea es gestionar clientes y pagos devolviendo `List<Object>` para no cambiar `ControladorMenu` ni `GestorUsuario`. |

La clave de la arquitectura es que todas las capas comparten `List<Object>` con
instancias de `Cliente` y `Pagos`. Eso permite sustituir el gestor de
persistencia (CSV → JSON → BBDD) sin modificar la lógica del menú ni la entrada
de usuario.

---

## Formatos de datos

### CSV

Sin cabecera, separador `,`, codificación UTF-8 y rutas relativas a la carpeta
del proyecto (se crean automáticamente en el primer arranque):

- `cliente.csv`: `id, nombre, telefono, matricula`
- `pagos.csv`: `id, idCliente, fecha (yyyy-MM-dd), importe, litros, combustible`

Importe y litros usan punto como separador decimal.

### JSON

Un array con un objeto por línea; si no existe, se crea con `[]`:

```json
{"id": 1, "nombre": "Idaira", "telefono": "124578963", "matricula": "1478MNC"}
{"id": 1, "clienteId": 2, "fecha": "2026-10-11", "importe": 15.99, "litros": 45.0, "combustible": "Diesel"}
```

Ojo: en JSON el campo del cliente en un pago se llama `clienteId`, mientras que
en CSV es `idCliente` y en la base de datos `id_cliente`.

### SQLite

Definido en `sql/01-esquma.sql` y replicado en `ConexionBBDD`:

- `clientes(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(100) NOT NULL,
  telefono VARCHAR(20) NOT NULL, matricula VARCHAR(15) NOT NULL UNIQUE)`
- `pagos(id INTEGER PRIMARY KEY AUTOINCREMENT, id_cliente INTEGER NOT NULL,
  fecha DATE NOT NULL, importe DECIMAL(10,2) CHECK (importe > 0),
  litros DECIMAL(10,2) CHECK (litros > 0), combustible VARCHAR(50) NOT NULL,
  FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE)`
- `PRAGMA foreign_keys = ON` debe activarse en cada conexión nueva.

---

## Compilación y ejecución

Desde la carpeta `Practica_gasolinera/` (el código usa paquetes, `record` y
*text blocks*; se recomienda JDK 21 o superior):

```
javac -d out src/MainConFicheros.java src/app/*.java src/practicaConFicheros/*.java src/bbdd/*.java
java -cp out MainConFicheros
```

Para ejecutar las clases del paquete `bbdd` hace falta añadir al *classpath* el
driver JDBC de SQLite (`org.xerial:sqlite-jdbc`).

`.idea/`, `out/` y los artefactos generados localmente están ignorados por Git.

---

## Material de referencia

En la carpeta raíz del repositorio están el enunciado en PDF de la Práctica 1 y
`DiagramaClaseV2.drawio`, con el diagrama de clases de la gasolinera.

---

## Trabajo pendiente

- 2026-11-27: Leído el enunciado hasta «El personal y la organización del trabajo»; entidades identificadas: Estación (equivalente a Gasolinera), Surtidor y configuración de facturación.
- 2026-11-27: Decisión pendiente: representar la configuración de facturación en una tabla propia o como campos de Estación; todavía no se ha elegido una opción.
- Terminar `bbdd.GestorBBDD`: altas, consultas y conversión de `ResultSet` a
  `Cliente`/`Pagos`, manteniendo `List<Object>` para no romper `app.*`.
- Crear un punto de entrada que use la base de datos en lugar de los ficheros.
- Decidir cómo generar los IDs con `AUTOINCREMENT` (ahora los calcula
  `ControladorMenu` con "último ID + 1").
- Si se cambia el formato de almacenamiento, modificar el gestor concreto y el
  punto de entrada donde se instancia.
- Mantener este `CONTEXT.md` al día a medida que avance la práctica de BBDD.
