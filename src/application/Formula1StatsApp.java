package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyDoubleWrapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Formula1StatsApp extends Application {

	private ComboBox<Integer> yearComboBox;
    private TableView<DriverSeason> driverTableView;
    private TableView<ConstructorSeason> constructorTableView;
    private Button chartButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Formula 1 Statistics");

        // Crear ComboBox Seleccionar año
        yearComboBox = new ComboBox<>();
        yearComboBox.setPromptText("Seleccionar Año");
        yearComboBox.setOnAction(e -> updateTableViews());

        HBox yearSelectionBox = new HBox(10);
        yearSelectionBox.setAlignment(Pos.CENTER);
        Label yearLabel = new Label("Año:");
        yearSelectionBox.getChildren().addAll(yearLabel, yearComboBox);

        // Crear botón para abrir la escena de gráficos
        chartButton = new Button("Ver Gráficos");
        chartButton.setOnAction(e -> openChartScene());
        
        // Initialize TableViews
        driverTableView = createDriverTableView();
        constructorTableView = createConstructorTableView();

        // Create layout
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(yearSelectionBox, driverTableView, constructorTableView, chartButton);

        // Create scene
        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load available years
        loadYears();
    }
    
    private void openChartScene() {
        Integer selectedYear = yearComboBox.getValue();
        if (selectedYear == null) {
            showAlert("Por favor, seleccione un año antes de ver los gráficos.");
            return;
        }

        Stage chartStage = new Stage();
        chartStage.setTitle("Gráficos de F1 - Año " + selectedYear);

        BarChart<String, Number> driverChart = createDriverChart(selectedYear);
        BarChart<String, Number> constructorChart = createConstructorChart(selectedYear);

        VBox chartBox = new VBox(20);
        chartBox.setPadding(new Insets(20));
        chartBox.getChildren().addAll(driverChart, constructorChart);

        Scene chartScene = new Scene(chartBox, 800, 600);
        chartStage.setScene(chartScene);
        chartStage.show();
    }
    
    private BarChart<String, Number> createDriverChart(int year) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("Puntos de Conductores - " + year);
        xAxis.setLabel("Conductor");
        yAxis.setLabel("Puntos");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Puntos Totales");

        // Obtener datos de la base de datos
        String query = "SELECT d.forename || ' ' || d.surname AS driver_name, SUM(res.points) AS total_points " +
                       "FROM results res " +
                       "JOIN races r ON res.race_id = r.race_id " +
                       "JOIN drivers d ON res.driver_id = d.driver_id " +
                       "WHERE r.year = ? " +
                       "GROUP BY d.driver_id " +
                       "ORDER BY total_points DESC " +
                       "LIMIT 10";

        try (Connection conn = Main.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("driver_name"), rs.getDouble("total_points")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error al cargar datos de conductores: " + e.getMessage());
        }

        barChart.getData().add(series);
        return barChart;
    }
    
    private BarChart<String, Number> createConstructorChart(int year) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("Puntos de Constructores - " + year);
        xAxis.setLabel("Constructor");
        yAxis.setLabel("Puntos");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Puntos Totales");

        // Obtener datos de la base de datos
        String query = "SELECT c.name AS constructor_name, SUM(cr.points) AS total_points " +
                       "FROM constructor_results cr " +
                       "JOIN races r ON cr.race_id = r.race_id " +
                       "JOIN constructors c ON cr.constructor_id = c.constructor_id " +
                       "WHERE r.year = ? " +
                       "GROUP BY c.constructor_id " +
                       "ORDER BY total_points DESC";

        try (Connection conn = Main.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("constructor_name"), rs.getDouble("total_points")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error al cargar datos de constructores: " + e.getMessage());
        }

        barChart.getData().add(series);
        return barChart;
    }
    
    private TableView<DriverSeason> createDriverTableView() {
        TableView<DriverSeason> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<DriverSeason, String> nameCol = new TableColumn<>("Driver Name");
        nameCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getFullName()));

        TableColumn<DriverSeason, Integer> winsCol = new TableColumn<>("Wins");
        winsCol.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getWins()).asObject());

        TableColumn<DriverSeason, Double> pointsCol = new TableColumn<>("Total Points");
        pointsCol.setCellValueFactory(cellData -> new ReadOnlyDoubleWrapper(cellData.getValue().getTotalPoints()).asObject());

        TableColumn<DriverSeason, Integer> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getSeasonRank()).asObject());

        tableView.getColumns().addAll(nameCol, winsCol, pointsCol, rankCol);

        return tableView;
    }

    private TableView<ConstructorSeason> createConstructorTableView() {
        TableView<ConstructorSeason> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ConstructorSeason, String> nameCol = new TableColumn<>("Constructor Name");
        nameCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getConstructorName()));

        TableColumn<ConstructorSeason, String> nationalityCol = new TableColumn<>("Nationality");
        nationalityCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getConstructorNationality()));

        TableColumn<ConstructorSeason, Integer> winsCol = new TableColumn<>("Wins");
        winsCol.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getWins()).asObject());

        TableColumn<ConstructorSeason, Double> pointsCol = new TableColumn<>("Total Points");
        pointsCol.setCellValueFactory(cellData -> new ReadOnlyDoubleWrapper(cellData.getValue().getTotalPoints()).asObject());

        TableColumn<ConstructorSeason, Integer> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getSeasonRank()).asObject());

        tableView.getColumns().addAll(nameCol, nationalityCol, winsCol, pointsCol, rankCol);

        return tableView;
    }
    
    private void loadYears() {
        List<Integer> years = new ArrayList<>();
        // Asumiendo que los años van desde 1950 hasta 2023
        for (int year = 2009; year <= 2023; year++) {
            years.add(year);
        }
        yearComboBox.setItems(FXCollections.observableArrayList(years));
    }
    
    private void updateTableViews() {
        updateDriverTableView();
        updateConstructorTableView();
    }

    private void updateDriverTableView() {
        Integer selectedYear = yearComboBox.getValue();
        if (selectedYear == null) return;

        String query = "SELECT r.year, d.forename, d.surname, " +
                       "COUNT(CASE WHEN res.position = 1 THEN 1 END) AS wins, " +
                       "SUM(res.points) AS total_points, " +
                       "RANK() OVER (PARTITION BY r.year ORDER BY SUM(res.points) DESC) AS season_rank " +
                       "FROM results res " +
                       "JOIN races r ON res.race_id = r.race_id " +
                       "JOIN drivers d ON res.driver_id = d.driver_id " +
                       "WHERE r.year = ? " +
                       "GROUP BY r.year, d.driver_id, d.forename, d.surname " +
                       "ORDER BY r.year, season_rank";

        ObservableList<DriverSeason> data = FXCollections.observableArrayList();

        try (Connection conn = Main.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, selectedYear);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                data.add(new DriverSeason(
                    rs.getInt("year"),
                    rs.getString("forename"),
                    rs.getString("surname"),
                    rs.getInt("wins"),
                    rs.getDouble("total_points"),
                    rs.getInt("season_rank")
                ));
            }

            driverTableView.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error updating table: " + e.getMessage());
        }
    }
    
    private void updateConstructorTableView() {
        Integer selectedYear = yearComboBox.getValue();
        if (selectedYear == null) return;

        String query = "SELECT " +
                       "r.year, " +
                       "c.name AS constructor_name, " +
                       "c.nationality AS constructor_nationality, " +
                       "COUNT(CASE WHEN cs.position = 1 THEN 1 END) AS wins, " +
                       "SUM(cr.points) AS total_points, " +
                       "RANK() OVER (PARTITION BY r.year ORDER BY SUM(cr.points) DESC) AS season_rank " +
                       "FROM constructor_results cr " +
                       "JOIN races r ON cr.race_id = r.race_id " +
                       "JOIN constructors c ON cr.constructor_id = c.constructor_id " +
                       "JOIN constructor_standings cs ON cr.race_id = cs.race_id AND cr.constructor_id = cs.constructor_id " +
                       "WHERE r.year = ? " +
                       "GROUP BY r.year, c.constructor_id, c.name, c.nationality " +
                       "ORDER BY r.year, season_rank";

        ObservableList<ConstructorSeason> data = FXCollections.observableArrayList();

        try (Connection conn = Main.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, selectedYear);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                data.add(new ConstructorSeason(
                    rs.getInt("year"),
                    rs.getString("constructor_name"),
                    rs.getString("constructor_nationality"),
                    rs.getInt("wins"),
                    rs.getDouble("total_points"),
                    rs.getInt("season_rank")
                ));
            }

            constructorTableView.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error updating constructor table: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}