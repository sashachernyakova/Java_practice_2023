package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.net.URL;
import java.util.*;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.jgrapht.graph.DefaultWeightedEdge;

public class HelloController {


    @FXML
    public AnchorPane anchor;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addEdgeButton;

    @FXML
    private Button addVertexButton;

    @FXML
    private Label commentLabel;

    @FXML
    private Label costLabel;

    @FXML
    private Label cost;

    @FXML
    private Button deleteEdgeButton;

    @FXML
    private Button deleteVertexButton;

    @FXML
    private Button importGraphButton;

    @FXML
    private Button startButton;

    @FXML
    private AnchorPane anchorInside;

    @FXML
    private Button stepBackButton;

    @FXML
    private Label stepCommentLabel;

    @FXML
    private Button stepForwardButton;

    @FXML
    private Label treeCostLabel;

    @FXML
    void initialize() {
        deleteVertexButton.setOnMouseClicked(event -> {
            handleDeleteButtonAction();
        });
        addVertexButton.setOnMouseClicked(event -> {
            handleAddButtonAction();
        });
        deleteEdgeButton.setOnMouseClicked(event -> {
            handleDeleteEdgeAction();
        });
        addEdgeButton.setOnMouseClicked(event -> {
            handleAddEdgeAction();
        });
        importGraphButton.setOnMouseClicked(event -> {
            handleImportGraph();
        });
        stepForwardButton.setOnMouseClicked(event -> {
            handleStepForwardAction();
        });
        startButton.setOnMouseClicked(event -> {
            handleStartAgainAction();
        });
        stepBackButton.setOnMouseClicked(event -> {
            handleStepBackAction();
        });
    }
    private HelloApplication application = new HelloApplication();

    public void handleDeleteButtonAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Удаление вершины");
        alert.setHeaderText("Выберите вершину");
        ChoiceBox choice = new ChoiceBox();
        choice.setMaxWidth(Double.MAX_VALUE);
        choice.getItems().addAll(HelloApplication.vertexes);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(choice);
        alert.getDialogPane().setContent(vBox);
        alert.showAndWait();
        HelloApplication.vertexes.remove(HelloApplication.vertexes.indexOf(choice.getValue()));
        HelloApplication.deleteVertex((choice.getValue()).toString());
    }
    public void handleAddButtonAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Добавление вершины");
        alert.setHeaderText("Введите вершину");
        TextField textAddVertex = new TextField();
        textAddVertex.setMaxWidth(Double.MAX_VALUE);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(textAddVertex);
        alert.getDialogPane().setContent(vBox);
        alert.showAndWait();
        if (HelloApplication.vertexes.contains(textAddVertex.getText())){
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Добавление вершины");
            warning.setHeaderText("Данная вершина уже существует в графе");
            GridPane gridPane2 = new GridPane();
            warning.getDialogPane().setContent(gridPane2);
            warning.showAndWait();
        } else{
            HelloApplication.vertexes.add(textAddVertex.getText());
            HelloApplication.addVertex(textAddVertex.getText());
        }
    }
    public void handleDeleteEdgeAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Удаление ребра");
        alert.setHeaderText("Выберите смежные вершины");

        ChoiceBox choiceVertex1 = new ChoiceBox();
        ChoiceBox choiceVertex2 = new ChoiceBox();
        choiceVertex1.setMaxWidth(Double.valueOf(40));
        choiceVertex1.getItems().addAll(HelloApplication.vertexes);
        choiceVertex2.setMaxWidth(Double.valueOf(40));
        choiceVertex2.getItems().addAll(HelloApplication.vertexes);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(25); // Set horizontal gap between columns
        gridPane.setVgap(10); // Set vertical gap between rows
        gridPane.addColumn(0, new Label("Вершина 1:"), choiceVertex1);
        gridPane.addColumn(1, new Label("Вершина 2:"), choiceVertex2);
        alert.getDialogPane().setContent(gridPane);
        alert.showAndWait();
        if (choiceVertex1.getValue().toString().equals(choiceVertex2.getValue().toString())){
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Удаление ребра");
            warning.setHeaderText("Нельзя удалить отсутсвующее ребро");
            GridPane gridPane2 = new GridPane();
            warning.getDialogPane().setContent(gridPane2);
            warning.showAndWait();
        }
        HelloApplication.deleteEdge(choiceVertex1.getValue().toString(),choiceVertex2.getValue().toString());
    }


    public void handleAddEdgeAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Добавление ребра");
        alert.setHeaderText("Выберите значения для добавления ребра");
        ChoiceBox choiceVertex1 = new ChoiceBox();
        ChoiceBox choiceVertex2 = new ChoiceBox();
        choiceVertex1.setMaxWidth(Double.valueOf(40));
        choiceVertex1.getItems().addAll(HelloApplication.vertexes);
        choiceVertex2.setMaxWidth(Double.valueOf(40));
        choiceVertex2.getItems().addAll(HelloApplication.vertexes);
        TextField textAddEdgeCost = new TextField();
        textAddEdgeCost.setMaxWidth(Double.valueOf(40));
        GridPane gridPane = new GridPane();
        gridPane.setHgap(25); // Set horizontal gap between columns
        gridPane.setVgap(10); // Set vertical gap between rows
        gridPane.addColumn(0, new Label("Вершина 1:"), choiceVertex1);
        gridPane.addColumn(1, new Label("Вершина 2:"), choiceVertex2);
        gridPane.addColumn(2, new Label("Вес ребра"), textAddEdgeCost);
        alert.getDialogPane().setContent(gridPane);
        alert.showAndWait();

        application.checkAddCostEdge(choiceVertex1, choiceVertex2, textAddEdgeCost); //!!!!!!!!!

    }

    public void handleImportGraph(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Импорт графа");
        alert.setHeaderText("Выберите путь к файлу с графом");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы графа", "*.txt"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            HelloApplication.importGraph(file);
        }
    }


    public void  handleStepForwardAction(){
        importGraphButton.setDisable(true);
        addVertexButton.setDisable(true);
        addEdgeButton.setDisable(true);
        deleteVertexButton.setDisable(true);
        deleteEdgeButton.setDisable(true);


        if(application.checkConnectivityComponents()){
            HelloApplication.stepForward(commentLabel, cost);
        } else{
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Граф несвязный");
            warning.setHeaderText("Запрещено использование несвязных графов, введите связный граф");
            GridPane gridPane = new GridPane();
            warning.getDialogPane().setContent(gridPane);
            warning.showAndWait();
        }


    }

    public void handleStartAgainAction(){
        HelloApplication.clearWindow(commentLabel, cost);
        importGraphButton.setDisable(false);
        addVertexButton.setDisable(false);
        addEdgeButton.setDisable(false);
        deleteVertexButton.setDisable(false);
        deleteEdgeButton.setDisable(false);

    }

    public void handleStepBackAction(){

    }
}
