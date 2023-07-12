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
    public Label stepCommentLabel;
    public Label treeCostLabel;
    public AnchorPane anchorInside;


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
        stepBackButton.setDisable(true);
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
        stepBackButton.setOnMouseClicked(event -> {
            handleStepBackAction();
        });
    }
    private KruscalApplication application = new KruscalApplication();

    public void handleDeleteVertexAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("vertex removal");
        alert.setHeaderText("Select a vertex to delete");
        ChoiceBox choice = new ChoiceBox();
        choice.setMaxWidth(Double.MAX_VALUE);
        choice.getItems().addAll(KruscalApplication.vertexes);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(choice);
        alert.getDialogPane().setContent(vBox);
        alert.showAndWait();
        KruscalApplication.vertexes.remove(KruscalApplication.vertexes.indexOf(choice.getValue()));
        KruscalApplication.deleteVertex((choice.getValue()).toString());
    }
    public void handleAddVertexAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Adding a vertex");
        alert.setHeaderText("Enter a vertex to add");
        TextField textAddVertex = new TextField();
        textAddVertex.setMaxWidth(Double.MAX_VALUE);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(textAddVertex);
        alert.getDialogPane().setContent(vBox);
        alert.showAndWait();
        if (KruscalApplication.vertexes.contains(textAddVertex.getText())){
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Adding a vertex");
            warning.setHeaderText("This vertex already exists in the graph");
            GridPane gridPane2 = new GridPane();
            warning.getDialogPane().setContent(gridPane2);
            warning.showAndWait();
        } else{
            KruscalApplication.vertexes.add(textAddVertex.getText());
            KruscalApplication.addVertex(textAddVertex.getText());
        }
    }
    public void handleDeleteEdgeAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Removing an edge");
        alert.setHeaderText("Select adjacent vertices");

        ChoiceBox choiceVertex1 = new ChoiceBox();
        ChoiceBox choiceVertex2 = new ChoiceBox();
        choiceVertex1.setMaxWidth(Double.valueOf(40));
        choiceVertex1.getItems().addAll(KruscalApplication.vertexes);
        choiceVertex2.setMaxWidth(Double.valueOf(40));
        choiceVertex2.getItems().addAll(KruscalApplication.vertexes);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(25); // Set horizontal gap between columns
        gridPane.setVgap(10); // Set vertical gap between rows
        gridPane.addColumn(0, new Label("Vertex 1:"), choiceVertex1);
        gridPane.addColumn(1, new Label("Vertex 2:"), choiceVertex2);
        alert.getDialogPane().setContent(gridPane);
        alert.showAndWait();
        if (choiceVertex1.getValue().toString().equals(choiceVertex2.getValue().toString())){
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Removing an edge");
            warning.setHeaderText("Cannot delete missing edge");
            GridPane gridPane2 = new GridPane();
            warning.getDialogPane().setContent(gridPane2);
            warning.showAndWait();
        }
        KruscalApplication.deleteEdge(choiceVertex1.getValue().toString(),choiceVertex2.getValue().toString());
    }

    public void handleAddEdgeAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Adding an edge");
        alert.setHeaderText("Select values to add an edge");
        ChoiceBox choiceVertex1 = new ChoiceBox();
        ChoiceBox choiceVertex2 = new ChoiceBox();
        choiceVertex1.setMaxWidth(Double.valueOf(40));
        choiceVertex1.getItems().addAll(KruscalApplication.vertexes);
        choiceVertex2.setMaxWidth(Double.valueOf(40));
        choiceVertex2.getItems().addAll(KruscalApplication.vertexes);
        TextField textAddEdgeCost = new TextField();
        textAddEdgeCost.setMaxWidth(40.0);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(25); // Set horizontal gap between columns
        gridPane.setVgap(10); // Set vertical gap between rows
        gridPane.addColumn(0, new Label("Vertex 1:"), choiceVertex1);
        gridPane.addColumn(1, new Label("Vertex 2:"), choiceVertex2);
        gridPane.addColumn(2, new Label("Edge weight"), textAddEdgeCost);
        alert.getDialogPane().setContent(gridPane);
        alert.showAndWait();
        try {
            double w = Double.parseDouble(textAddEdgeCost.getText());
            if (w <= 0){
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Adding an edge");
                warning.setHeaderText("Edge weight must be a positive number");
                GridPane gridPane2 = new GridPane();
                warning.getDialogPane().setContent(gridPane2);
                warning.showAndWait();
            }
            else if (choiceVertex1.getValue().toString().equals(choiceVertex2.getValue().toString())){
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Adding an edge");
                warning.setHeaderText("You can't create loops in a graph");
                GridPane gridPane2 = new GridPane();
                warning.getDialogPane().setContent(gridPane2);
                warning.showAndWait();
            }
            else if(KruscalApplication.edgeExist(choiceVertex1.getValue().toString(), choiceVertex2.getValue().toString())){
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Adding an edge");
                warning.setHeaderText("This edge already exists");
                GridPane gridPane2 = new GridPane();
                warning.getDialogPane().setContent(gridPane2);
                warning.showAndWait();
            }
            else{
                KruscalApplication.addEdge(choiceVertex1.getValue().toString(),choiceVertex2.getValue().toString(),w);
            }
        } catch (NumberFormatException e) {
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Adding an edge");
            warning.setHeaderText("Edge weight must be a positive number");
            GridPane gridPane2 = new GridPane();
            warning.getDialogPane().setContent(gridPane2);
            warning.showAndWait();
        }

    }

    public void handleImportGraphAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Graph import");
        alert.setHeaderText("Select the path to the graph file");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Graph files", "*.txt"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            KruscalApplication.importGraph(file);
        }
    }

    public void  handleStepForwardAction(){
        importGraphButton.setDisable(true);
        addVertexButton.setDisable(true);
        addEdgeButton.setDisable(true);
        deleteVertexButton.setDisable(true);
        deleteEdgeButton.setDisable(true);
        stepBackButton.setDisable(false);
//        KruscalApplication.stepForward(commentLabel, costLabel);
        if(application.checkConnectivityComponents()){
            application.stepForward(commentLabel, costLabel);
        } else{
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Graph is not connected");
            warning.setHeaderText("You cannot use disconnected graphs, enter a connected graph");
            GridPane gridPane = new GridPane();
            warning.getDialogPane().setContent(gridPane);
            warning.showAndWait();
            importGraphButton.setDisable(false);
            addVertexButton.setDisable(false);
            addEdgeButton.setDisable(false);
            deleteVertexButton.setDisable(false);
            deleteEdgeButton.setDisable(false);
            stepBackButton.setDisable(true);
        }
    }

    public void handleStartAgainAction(){
        KruscalApplication.clearWindow(commentLabel, costLabel);
        importGraphButton.setDisable(false);
        addVertexButton.setDisable(false);
        addEdgeButton.setDisable(false);
        deleteVertexButton.setDisable(false);
        deleteEdgeButton.setDisable(false);
        stepBackButton.setDisable(true);
    }

    public void handleStepBackAction(){
        int flag = KruscalApplication.stepBack(commentLabel, costLabel);
        if(flag == 1){
            stepBackButton.setDisable(true);
        }
    }


}
