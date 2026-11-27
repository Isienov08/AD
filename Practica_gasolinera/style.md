# style.md

## 1. Para qué sirve
Lee esto antes de generar código Java. Que parezca escrito por mí. Si hay duda, pregunta antes.

## 2. Quién soy
DAM, 2º curso. Mi código de referencia: `AD/Practica_gasolinera`.
El resto del repo (exámenes, soluciones, Jardín Botánico) no es mío.
Manejo bien: consola, ficheros CSV/JSON, listas y validaciones.
Estoy aprendiendo: JDBC/SQL, programación funcional, colecciones, excepciones propias, herencia y composición.

## 3. Nombres y formato
- Español. 4 espacios. Comentarios con `//`, sin Javadoc.
- Clases `Cliente`, `ControladorMenu`, `GestorFicherosEnCSV`; métodos `darAltaCliente`, `leerFichero`; variables `clientesNuevos`; constantes `FORMATO`.
- A veces escribo `cliente=new Cliente()` sin espacios en la `=`. Manténlo en código nuevo; no reescribas código viejo.
- Orden: constantes, atributos, constructor, getters/setters, métodos, `toString`.

## 4. Lo que sé (úsalos libremente)
Setters con `IllegalArgumentException`; `do-while` + `switch` + `Scanner`; `while (true)` para repetir; `List<Object>` con `instanceof`; interfaces y su implementación; `Files` con `try-with-resources`; `LocalDate`, `String.format`, `Function<...>`.

## 5. Lo que aprendo (con comentario)
JDBC, lambdas y `stream()`, colecciones, excepciones propias, `record`, `switch ->`, genéricos.
Lo nuevo se explica línea a línea; lo que ya manejo, por bloques.

## 6. Prohibido salvo que yo lo pida
Hilos, `BigDecimal`, patrones y librerías nuevas (excepto JUnit), one-liners, `var`, elegir herencia/composición sin preguntar.
Alternativas: bucles normales, `double` con `Math.round`, clases simples, escribir siempre el tipo.

## 7. Organización y errores
Una clase por archivo; la estructura la decido yo. Validar en setters, capturar y avisar con `System.out`/`System.err`, volver a preguntar si el dato no vale, no sobrescribir ficheros sin permiso. JUnit siempre.

## 8. Explicaciones
Comentarios dentro del código y resumen corto después. Cada término técnico explicado en una frase.

## 9. Si algo no encaja con mi nivel
Para y ofrece: versión sencilla, explicación mínima, o versión avanzada solo si yo la elijo. Pregunta antes sobre diseño, arquitectura y cualquier tema nuevo.

## 10. Ejemplo de mi repo
Sí: reutilizar `Cliente.FORMATO` y `c.toString()` en `listaClientes`.
No: montar las columnas a mano con `+ "\t" +` en `buscarClientes`.
