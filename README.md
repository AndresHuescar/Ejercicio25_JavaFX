# Ejercicio 25 - Buscar empleados en TableView

## Descripción

Este proyecto corresponde al ejercicio 25 del bloque BC5 de JavaFX.

El objetivo del ejercicio es añadir una funcionalidad de búsqueda y filtrado sobre los empleados mostrados en un `TableView`.

La aplicación se conecta a una base de datos SQLite mediante JDBC y muestra los empleados en una tabla. Además, permite buscar empleados por nombre en tiempo real.

## Tecnologías utilizadas

- Java
- JavaFX
- Maven
- SQLite
- JDBC
- IntelliJ IDEA

## Funcionamiento

La aplicación crea una base de datos llamada:

```text
empleados.db
```

Dentro de la base de datos se crea una tabla llamada `empleados` con los campos:

- `id`
- `nombre`
- `salario`

Los datos se muestran en un `TableView`.

Encima de la tabla hay un campo de búsqueda. Cuando el usuario escribe un nombre o parte de un nombre, la tabla se filtra automáticamente y solo muestra los empleados que coinciden con la búsqueda.

También se incluye un botón **Limpiar búsqueda** para borrar el filtro y volver a mostrar todos los empleados.

## Conceptos utilizados

- `Application`: clase base para crear aplicaciones JavaFX.
- `Stage`: ventana principal.
- `Scene`: contenido visual de la ventana.
- `TableView`: tabla visual para mostrar datos.
- `TableColumn`: columnas de la tabla.
- `PropertyValueFactory`: conecta las columnas con los atributos del objeto.
- `ObservableList`: lista observable utilizada por JavaFX.
- `FilteredList`: lista que permite filtrar los elementos mostrados.
- `TextField`: campo de texto utilizado para escribir la búsqueda.
- `Button`: botón utilizado para limpiar la búsqueda.
- `HBox`: layout usado para colocar el campo de búsqueda y el botón en horizontal.
- `VBox`: layout usado para organizar la interfaz principal.
- `JDBC`: conexión entre Java y la base de datos.
- `SQLite`: base de datos local utilizada en el proyecto.

## Estructura del proyecto

```text
Ejercicio25_JavaFX
 ├── pom.xml
 ├── empleados.db
 └── src
     └── main
         └── java
             └── org
                 └── example
                     └── Main.java
```

## Cómo ejecutar el proyecto

Para ejecutar el proyecto desde IntelliJ IDEA:

1. Abrir el proyecto en IntelliJ.
2. Sincronizar el archivo `pom.xml` con Maven.
3. Abrir el panel Maven.
4. Ejecutar:

```bash
mvn javafx:run
```

También se puede ejecutar desde la terminal con:

```bash
mvn javafx:run
```

## Autor

Andrés Huéscar Fernández
