package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class Main extends Application {

    // Ruta de la base de datos SQLite
    private static final String URL = "jdbc:sqlite:empleados.db";

    private TableView<Empleado> tabla;
    private ObservableList<Empleado> listaEmpleados;

    @Override
    public void start(Stage stage) {

        // Creo la base de datos y la tabla si no existen
        crearBaseDatos();

        // Inserto datos iniciales si la tabla está vacía
        insertarDatosIniciales();

        Label titulo = new Label("Buscar empleados en TableView");

        // Campo de texto para escribir la búsqueda
        TextField campoBusqueda = new TextField();
        campoBusqueda.setPromptText("Buscar por nombre...");
        campoBusqueda.setPrefWidth(250);

        // Botón para limpiar la búsqueda
        Button botonLimpiar = new Button("Limpiar búsqueda");

        // Creo el TableView
        tabla = new TableView<>();

        // Columna ID
        TableColumn<Empleado, Integer> columnaId = new TableColumn<>("ID");
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Columna Nombre
        TableColumn<Empleado, String> columnaNombre = new TableColumn<>("Nombre");
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // Columna Salario
        TableColumn<Empleado, Double> columnaSalario = new TableColumn<>("Salario");
        columnaSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));

        columnaId.setPrefWidth(80);
        columnaNombre.setPrefWidth(250);
        columnaSalario.setPrefWidth(120);

        tabla.getColumns().addAll(columnaId, columnaNombre, columnaSalario);

        // Cargo los empleados desde la base de datos
        listaEmpleados = cargarEmpleados();

        // FilteredList permite filtrar los datos que se muestran en el TableView
        FilteredList<Empleado> empleadosFiltrados = new FilteredList<>(listaEmpleados, empleado -> true);

        // Cada vez que el usuario escribe en el campo de búsqueda, se actualiza el filtro
        campoBusqueda.textProperty().addListener((observable, textoAnterior, textoNuevo) -> {
            empleadosFiltrados.setPredicate(empleado -> {

                // Si el campo está vacío, se muestran todos los empleados
                if (textoNuevo == null || textoNuevo.isEmpty()) {
                    return true;
                }

                // Paso el texto a minúsculas para que la búsqueda no distinga mayúsculas
                String filtro = textoNuevo.toLowerCase();

                // Compruebo si el nombre del empleado contiene el texto buscado
                return empleado.getNombre().toLowerCase().contains(filtro);
            });
        });

        // El TableView muestra la lista filtrada
        tabla.setItems(empleadosFiltrados);

        // Botón para borrar el texto de búsqueda
        botonLimpiar.setOnAction(event -> campoBusqueda.clear());

        // HBox para colocar el buscador y el botón en la misma fila
        HBox zonaBusqueda = new HBox(10);
        zonaBusqueda.getChildren().addAll(campoBusqueda, botonLimpiar);
        zonaBusqueda.setStyle("-fx-alignment: center;");

        // Contenedor principal
        VBox root = new VBox(15);
        root.getChildren().addAll(titulo, zonaBusqueda, tabla);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Creo la escena principal
        Scene scene = new Scene(root, 600, 400);

        // Configuro y muestro la ventana
        stage.setTitle("Ejercicio 25 - Buscar empleados");
        stage.setScene(scene);
        stage.show();
    }

    // Crea la tabla empleados si no existe
    private void crearBaseDatos() {
        String sql = """
                CREATE TABLE IF NOT EXISTS empleados (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    salario REAL NOT NULL
                );
                """;

        try (Connection conexion = DriverManager.getConnection(URL);
             Statement sentencia = conexion.createStatement()) {

            sentencia.execute(sql);

        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo crear la base de datos: " + e.getMessage());
        }
    }

    // Inserta datos iniciales solo si la tabla está vacía
    private void insertarDatosIniciales() {
        String comprobar = "SELECT COUNT(*) FROM empleados";
        String insertar = "INSERT INTO empleados (nombre, salario) VALUES (?, ?)";

        try (Connection conexion = DriverManager.getConnection(URL);
             Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(comprobar)) {

            if (resultado.next() && resultado.getInt(1) == 0) {

                try (PreparedStatement ps = conexion.prepareStatement(insertar)) {

                    ps.setString(1, "Andrés");
                    ps.setDouble(2, 1500.00);
                    ps.executeUpdate();

                    ps.setString(1, "María");
                    ps.setDouble(2, 1800.50);
                    ps.executeUpdate();

                    ps.setString(1, "Carlos");
                    ps.setDouble(2, 2100.75);
                    ps.executeUpdate();

                    ps.setString(1, "Lucía");
                    ps.setDouble(2, 1950.25);
                    ps.executeUpdate();

                    ps.setString(1, "Pedro");
                    ps.setDouble(2, 1700.00);
                    ps.executeUpdate();
                }
            }

        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron insertar los datos iniciales: " + e.getMessage());
        }
    }

    // Carga los empleados de la base de datos
    private ObservableList<Empleado> cargarEmpleados() {
        ObservableList<Empleado> empleados = FXCollections.observableArrayList();

        String sql = "SELECT id, nombre, salario FROM empleados";

        try (Connection conexion = DriverManager.getConnection(URL);
             Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(sql)) {

            while (resultado.next()) {
                empleados.add(new Empleado(
                        resultado.getInt("id"),
                        resultado.getString("nombre"),
                        resultado.getDouble("salario")
                ));
            }

        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los empleados: " + e.getMessage());
        }

        return empleados;
    }

    // Método para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }

    // Clase modelo que representa un empleado
    public static class Empleado {

        private int id;
        private String nombre;
        private double salario;

        public Empleado(int id, String nombre, double salario) {
            this.id = id;
            this.nombre = nombre;
            this.salario = salario;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public double getSalario() {
            return salario;
        }
    }
}