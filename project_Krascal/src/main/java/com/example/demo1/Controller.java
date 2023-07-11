package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class Controller {
    @FXML
    public AnchorPane anchor;


    @FXML
    private Label costLabel;

    @FXML
    private Label commentLabel;


    @FXML
    private Button addEdgeButton;

    @FXML
    private Button addVertexButton;

    @FXML
    private Button deleteEdgeButton;

    @FXML
    private Button deleteVertexButton;

    @FXML
    private Button importGraphButton;

    @FXML
    private Button startButton;

    @FXML
    private Button stepBackButton;

    @FXML
    private Button stepForwardButton;

    @FXML
    void initialize() {
        deleteVertexButton.setOnMouseClicked(event -> {
            handleDeleteVertexAction();
        });
        addVertexButton.setOnMouseClicked(event -> {
            handleAddVertexAction();
        });
        deleteEdgeButton.setOnMouseClicked(event -> {
            handleDeleteEdgeAction();
        });
        addEdgeButton.setOnMouseClicked(event -> {
            handleAddEdgeAction();
        });
        importGraphButton.setOnMouseClicked(event -> {
            handleImportGraphAction();
        });
        stepForwardButton.setOnMouseClicked(event -> {
            handleStepForwardAction();
        });
        startButton.setOnMouseClicked(event -> {
            handleStartAgainAction();
        });
    }
    private KrascalApplication application = new KrascalApplication();

    public void handleDeleteVertexAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Удаление вершины");
        alert.setHeaderText("Выберите вершину");
        ChoiceBox choice = new ChoiceBox();
        choice.setMaxWidth(Double.MAX_VALUE);
        choice.getItems().addAll(KrascalApplication.vertexes);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(choice);
        alert.getDialogPane().setContent(vBox);
        alert.showAndWait();
        KrascalApplication.vertexes.remove(KrascalApplication.vertexes.indexOf(choice.getValue()));
        KrascalApplication.deleteVertex((choice.getValue()).toString());
    }
    public void handleAddVertexAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Добавление вершины");
        alert.setHeaderText("Введите вершину");
        TextField textAddVertex = new TextField();
        textAddVertex.setMaxWidth(Double.MAX_VALUE);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(textAddVertex);
        alert.getDialogPane().setContent(vBox);
        alert.showAndWait();
        if (KrascalApplication.vertexes.contains(textAddVertex.getText())){
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Добавление вершины");
            warning.setHeaderText("Данная вершина уже существует в графе");
            GridPane gridPane2 = new GridPane();
            warning.getDialogPane().setContent(gridPane2);
            warning.showAndWait();
        } else{
            KrascalApplication.vertexes.add(textAddVertex.getText());
            KrascalApplication.addVertex(textAddVertex.getText());
        }
    }
    public void handleDeleteEdgeAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Удаление ребра");
        alert.setHeaderText("Выберите смежные вершины");

        ChoiceBox choiceVertex1 = new ChoiceBox();
        ChoiceBox choiceVertex2 = new ChoiceBox();
        choiceVertex1.setMaxWidth(Double.valueOf(40));
        choiceVertex1.getItems().addAll(KrascalApplication.vertexes);
        choiceVertex2.setMaxWidth(Double.valueOf(40));
        choiceVertex2.getItems().addAll(KrascalApplication.vertexes);
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
        KrascalApplication.deleteEdge(choiceVertex1.getValue().toString(),choiceVertex2.getValue().toString());
    }

    public void handleAddEdgeAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Добавление ребра");
        alert.setHeaderText("Выберите значения для добавления ребра");
        ChoiceBox choiceVertex1 = new ChoiceBox();
        ChoiceBox choiceVertex2 = new ChoiceBox();
        choiceVertex1.setMaxWidth(Double.valueOf(40));
        choiceVertex1.getItems().addAll(KrascalApplication.vertexes);
        choiceVertex2.setMaxWidth(Double.valueOf(40));
        choiceVertex2.getItems().addAll(KrascalApplication.vertexes);
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
        try {
            double w = Double.parseDouble(textAddEdgeCost.getText());
            if (w <= 0){
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Добавление ребра");
                warning.setHeaderText("Вес ребра должен быть положительным числом");
                GridPane gridPane2 = new GridPane();
                warning.getDialogPane().setContent(gridPane2);
                warning.showAndWait();
            }
            else if (choiceVertex1.getValue().toString().equals(choiceVertex2.getValue().toString())){
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Добавление ребра");
                warning.setHeaderText("Нельзя создавать петли");
                GridPane gridPane2 = new GridPane();
                warning.getDialogPane().setContent(gridPane2);
                warning.showAndWait();
            }
            else if(KrascalApplication.edgeExist(choiceVertex1.getValue().toString(), choiceVertex2.getValue().toString())){
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Добавление ребра");
                warning.setHeaderText("Данное ребро уже существует");
                GridPane gridPane2 = new GridPane();
                warning.getDialogPane().setContent(gridPane2);
                warning.showAndWait();
            }
            else{
                KrascalApplication.addEdge(choiceVertex1.getValue().toString(),choiceVertex2.getValue().toString(),w);
            }
        } catch (NumberFormatException e) {
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Добавление ребра");
            warning.setHeaderText("Вес ребра должен быть положительным числом");
            GridPane gridPane2 = new GridPane();
            warning.getDialogPane().setContent(gridPane2);
            warning.showAndWait();
        }

    }

    public void handleImportGraphAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Импорт графа");
        alert.setHeaderText("Выберите путь к файлу с графом");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы графа", "*.txt"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            KrascalApplication.importGraph(file);
        }
    }

    public void  handleStepForwardAction(){
        importGraphButton.setDisable(true);
        addVertexButton.setDisable(true);
        addEdgeButton.setDisable(true);
        deleteVertexButton.setDisable(true);
        deleteEdgeButton.setDisable(true);
        KrascalApplication.stepForward(commentLabel, costLabel);
    }

    public void handleStartAgainAction(){
        KrascalApplication.clearWindow(commentLabel, costLabel);
        importGraphButton.setDisable(false);
        addVertexButton.setDisable(false);
        addEdgeButton.setDisable(false);
        deleteVertexButton.setDisable(false);
        deleteEdgeButton.setDisable(false);
    }
}
