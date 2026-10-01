# Acceso a Datos-DAM2

## Práctica 1: gestión de clientes y repostajes de una gasolinera

Aplicación de consola en Java para registrar clientes y pagos de repostajes.
Los datos se guardan en ficheros CSV y se conservan entre ejecuciones.

### Requisitos
- JDK: <<25>>

### Compilación y ejecución
Desde la carpeta raíz del proyecto (`Practica_gasolinera`):

    javac -d out src/*.java
    java -cp out Main

### Almacenamiento
- Ubicación: carpeta raíz del proyecto (`Practica_gasolinera`), mediante rutas relativas. - Ficheros: `cliente.csv` y `pagos.csv`.
- Creación: se crean automáticamente en el primer arranque.
- Codificación: UTF-8.

#### Formato de `cliente.csv`
- Separador: coma (`,`) | Cabecera: no
- Columnas en orden: `id, nombre, telefono, matricula`

#### Formato de `pagos.csv`
- Separador: coma (`,`) | Cabecera: no
- Columnas en orden: `id, idCliente, fecha, importe, litros, combustible`
- Fecha: ISO `yyyy-MM-dd` | Importe y litros: número decimal con punto

Los CSV no llevan cabecera; la cabecera solo se muestra al listar por consola.

### Estructura y decisiones de diseño
- `Main`: punto de entrada; muestra el menú y llama a `ControladorMenu`.
- `ControladorMenu`: ejecuta cada opción del menú (alta, listado, búsqueda, pagos) y genera los identificadores automáticos.
- `GestorUsuario`: entrada de datos por consola y validación de lo que escribe el usuario.
- `GestorFichero` (interfaz) y `GestorFicherosEnCSV`: acceso a los ficheros con `Files`. Los serializadores y deserializadores son funciones (`Function`) guardadas como atributos, de modo que el formato de línea está concentrado en esta clase.
- `Cliente` y `Pagos`: entidades con sus datos y su `toString` para mostrarlos en tabla.

### Si cambiara el formato de almacenamiento (p. ej. a JSON)
**Habría que modificar:**
- `GestorFicherosEnCSV` (o crear un `GestorFicherosEnJSON` que implemente `GestorFichero`): es donde está todo lo que depende del formato: los serializadores y deserializadores, la forma de separar los campos (`split(",")`) y la detección del tipo de registro en `leerFichero`, que ahora se basa en contar campos (4 = cliente, 6 = pago).
- `Main`: es donde se crean los gestores, así que ahí se cambia la clase concreta y el nombre de los ficheros (por ejemplo, `.csv` por `.json`).
- Las firmas de `ControladorMenu` y `GestorUsuario`, que reciben el tipo concreto `GestorFicherosEnCSV`. 
