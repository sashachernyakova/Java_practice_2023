package com.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.graph.*;
import javafx.scene.text.Font;

public class KruscalApplication extends Application {
    private static Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

    static Map<String, List<Double>> map = new HashMap<>();

    public static LinkedList<String> vertexes = new LinkedList<>();

    public static LinkedList<String> vertOfOstovTree = new LinkedList<>();

    public static LinkedList<DefaultWeightedEdge>  edgeColorList = new LinkedList<>();

    public static LinkedList<Double>  costList = new LinkedList<>();

    public static LinkedList<String>  commentList = new LinkedList<>();

    private static Stage mainStage;

    private static Scene mainScene;

    static int step = 0;

    static int totalEdgesInOstovTree;

    static double cost = 0;

    public static boolean checkConnectivityComponents(){
        Set<String> setVertexofGraph = new TreeSet<>();
        Set<String> setVertexofConnectivityComponent = new TreeSet<>();

        for (String vertex : graph.vertexSet()){
            setVertexofGraph.add(vertex);
        }

        try {
            setVertexofConnectivityComponent.add(graph.vertexSet().iterator().next());
        } catch (NoSuchElementException e){
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Обработка графа");
            warning.setHeaderText("Нельзя вводить пустой граф, введите связный граф с вершинами");
            GridPane gridPane2 = new GridPane();
            warning.getDialogPane().setContent(gridPane2);
            warning.showAndWait();
        }
        int flag = 0;
        int sizeSetVertexNext = 0;
        while (true) {
            int sizeSetVertexPrev = setVertexofConnectivityComponent.size();
            for (String vertex : setVertexofConnectivityComponent) {
                for (DefaultWeightedEdge edge : graph.edgeSet()) {
                    if (graph.getEdgeTarget(edge).equals(vertex)) {
                        setVertexofConnectivityComponent.add(graph.getEdgeSource(edge));
                        sizeSetVertexNext = setVertexofConnectivityComponent.size();
                        if (setVertexofGraph.size() == sizeSetVertexNext){
                            flag = 1;
                            break;
                        }
                    }
                    if (graph.getEdgeSource(edge).equals(vertex)) {
                        setVertexofConnectivityComponent.add(graph.getEdgeTarget(edge));
                        sizeSetVertexNext = setVertexofConnectivityComponent.size();
                        if (setVertexofGraph.size() == sizeSetVertexNext) {
                            flag = 1;
                            break;
                        }
                    }
                }
                if (flag == 1){
                    break;
                }
            }

            if (flag == 1){
                break;
            }
            else if (sizeSetVertexPrev == sizeSetVertexNext){
                break;
            }
        }
        if (flag == 1){
            return true;
        }
        return setVertexofGraph.size() == setVertexofConnectivityComponent.size();

    }

    public static boolean edgeExist(String v1, String v2) {
        DefaultWeightedEdge edge = graph.getEdge(v1, v2);
        return edge != null;
    }

    public static int stepBack(Label label, Label costLabel) {
        commentList.removeLast();

        DefaultWeightedEdge edge = edgeColorList.getLast();
        vertOfOstovTree.remove(graph.getEdgeSource(edge));
        vertOfOstovTree.remove(graph.getEdgeTarget(edge));
        edgeColorList.removeLast();
        step -= 1;
        cost -= graph.getEdgeWeight(edge);
        costList.removeLast();
        if (step == 0){
            label.setText("");
            costLabel.setText("0.0");
            totalEdgesInOstovTree -= 1;
            drawGraph(null);
            return 1;
        }
        else{
            label.setText(commentList.getLast());
            costLabel.setText(costList.getLast().toString());
            totalEdgesInOstovTree -= 1;
            drawGraph(null);
            return 0;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(KruscalApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 550);
        stage.setTitle("Kruskal's algorithm");
        stage.setScene(scene);
        mainScene = scene;
        // Creating a Panel and Adding a Canvas Component
        Pane pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(Color.web("#D1E2F4"), null,null)));
        pane.setLayoutX(220);
        pane.setLayoutY(130);
        AnchorPane anchorPane = (AnchorPane) scene.lookup("#anchor");
        anchorPane.getChildren().add(pane);
        stage.show();
    }

    public static void addVertex(String vert){
        graph.addVertex(vert);
        drawGraph(null);
    }

    public static void deleteVertex(String vert){
        graph.removeVertex(vert);
        drawGraph(null);
    }

    public static void addEdge(String vertex1, String vertex2, Double cost){
        DefaultWeightedEdge edge = graph.addEdge(vertex1, vertex2);
        graph.setEdgeWeight(edge, cost);
        drawGraph(null);
    }

    public static void deleteEdge(String vertex1, String vertex2){
        DefaultWeightedEdge edge = graph.getEdge(vertex1, vertex2);
        if (edge == null){
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Removing an edge");
            warning.setHeaderText("Cannot delete missing edge");
            GridPane gridPane2 = new GridPane();
            warning.getDialogPane().setContent(gridPane2);
            warning.showAndWait();
        }
        else{
            graph.removeEdge(vertex1, vertex2);
            drawGraph(null);
        }
    }

    public static void importGraph(File file){
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            Alert warning = new Alert(Alert.AlertType.WARNING);
            while ((line = bufferedReader.readLine()) != null) {
                String[] elements = line.split(" ");
                if (elements.length != 3) {
                    warning.setTitle("Graph import");
                    warning.setHeaderText("Each line should have 3 parameters separated by a space");
                    GridPane gridPane2 = new GridPane();
                    warning.getDialogPane().setContent(gridPane2);
                    warning.showAndWait();
                    return;
                }
                try {
                    double w = Double.parseDouble(elements[2]);
                    if (w <= 0){
                        warning.setTitle("Graph import");
                        warning.setHeaderText("The third parameter on each line must be a positive number");
                        GridPane gridPane2 = new GridPane();
                        warning.getDialogPane().setContent(gridPane2);
                        warning.showAndWait();
                        return;
                    }
                } catch (NumberFormatException e) {
                    warning.setTitle("Graph import");
                    warning.setHeaderText("The third parameter on each line must be a positive number");
                    GridPane gridPane2 = new GridPane();
                    warning.getDialogPane().setContent(gridPane2);
                    warning.showAndWait();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] elements = line.split(" ");
                if (!vertexes.contains(elements[0])){
                    vertexes.add(elements[0]);
                }
                if (!vertexes.contains(elements[1])){
                    vertexes.add(elements[1]);
                }
                addVertex(elements[0]);
                addVertex(elements[1]);
                addEdge(elements[0], elements[1], Double.parseDouble(elements[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearWindow(Label label, Label costLabel){
        cost = 0;
        step = 0;
        totalEdgesInOstovTree = 0;
        edgeColorList.clear();
        vertOfOstovTree.clear();
        vertexes.clear();
        map.clear();
        costList.clear();
        commentList.clear();
        graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        vertexes.clear();
        drawGraph(null);
        label.setText("");
        costLabel.setText("0.0");
    }

    public static void drawGraph(DefaultWeightedEdge edgeColor){
        // Creating a Canvas Component to Display a Graph
        Canvas canvas = new Canvas(400, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Setting graph display options
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;
        double radius = 110;
        double angle = 2 * Math.PI / graph.vertexSet().size();

        // Vertices display
        int i = 0;
        for (String vertex : graph.vertexSet()) {
            double x = centerX + radius * Math.cos(i * angle);
            double y = centerY + radius * Math.sin(i * angle);
            List<Double> list1 = new ArrayList<>();
            list1.add(x);
            list1.add(y);
            KruscalApplication.map.put(vertex, list1);

            gc.setFill(Color.web("#5B71D7"));
            gc.fillOval(x, y, 20, 20);
            gc.setFill(Color.web("#5B71D7"));
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 15)); // Задать жирность шрифта
            gc.fillText(vertex, x + 25, y + 15);

            i++;
        }

        // Edge display
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            String source = graph.getEdgeSource(edge);
            String target = graph.getEdgeTarget(edge);
            double weight = graph.getEdgeWeight(edge);

            double x1 = map.get(source).get(0);
            double y1 = map.get(source).get(1);
            double x2 = map.get(target).get(0);
            double y2 = map.get(target).get(1);
            double labelX = (x1 + x2) / 2 ;
            double labelY = (y1 + y2) / 2;
            if (edgeColor != null && graph.getEdgeSource(edge).equals(graph.getEdgeSource(edgeColor)) && graph.getEdgeTarget(edge).equals(graph.getEdgeTarget(edgeColor))){
                gc.setStroke(Color.RED);
                gc.setLineWidth(2.0);
                edgeColorList.add(edgeColor);
            }
            else if (edgeColorList.contains(edge)){
                gc.setStroke(Color.RED);
                gc.setLineWidth(2.0);
            }
            else {
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(2.0);
            }
            gc.strokeLine(x1 + 10, y1 + 10, x2 + 10, y2 + 10);
            gc.fillText(Double.toString(weight), labelX, labelY);

            gc.restore();

            i++;
        }
        // Creating a Panel and Adding a Canvas Component
        Pane pane = new Pane();
        pane.getChildren().add(canvas);
        pane.setBackground(new Background(new BackgroundFill(Color.web("#D1E2F4"), null,null)));
        pane.setLayoutX(220);
        pane.setLayoutY(130);
        AnchorPane anchorPane = (AnchorPane) mainScene.lookup("#anchor");
        anchorPane.getChildren().add(pane);
        mainStage.show();
    }

    public static String[][] sortEdge(){
        String[][] spanningTreeCost = new String[graph.edgeSet().size()][3];
        int i = 0;
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            String source = graph.getEdgeSource(edge);
            String target = graph.getEdgeTarget(edge);
            double weight = graph.getEdgeWeight(edge);
            String[] list = {String.valueOf(weight), source, target};
            spanningTreeCost[i] = list;
            i +=1 ;
        }
        Arrays.sort(spanningTreeCost, Comparator.comparing(row -> Double.parseDouble(row[0])));
        return spanningTreeCost;
    }

    public static void stepForward(Label label, Label costLabel) {
        doAlgorithm(label, sortEdge(), costLabel);
    }

    public static void doAlgorithm(Label label, String[][] ostovTreeCost, Label costLabel){
        // checking for the end of the algorithm
        if (totalEdgesInOstovTree == graph.vertexSet().size() - 1) {
            label.setText("Spanning tree built");
            return;
        }
        label.setText("");
        costLabel.setText("");
        String v1 = ostovTreeCost[step][1];
        String v2 = ostovTreeCost[step][2];
        // cycle check
        int n = edgeColorList.size();
//        if (vertOfOstovTree.contains(v1) && vertOfOstovTree.contains(v2)){
//            step += 1;
//            doAlgorithm(label, ostovTreeCost, costLabel);
//        }
        int n_vertex = vertOfOstovTree.size();
        if (!vertOfOstovTree.contains(v1)){
            n_vertex += 1;
        }
        if (!vertOfOstovTree.contains(v2)){
            n_vertex += 1;
        }
        if (n + 1 >= n_vertex){
            step += 1;
            doAlgorithm(label, ostovTreeCost, costLabel);
        }

        // construction comments
        if (!vertOfOstovTree.contains(v1)){
            vertOfOstovTree.add(v1);
        }
        if (!vertOfOstovTree.contains(v2)){
            vertOfOstovTree.add(v2);
        }
        totalEdgesInOstovTree += 1;
        DefaultWeightedEdge edge = graph.getEdge(v1, v2);
        double weight = graph.getEdgeWeight(edge);
        cost += weight;
        costList.add(cost);
        double strCost = cost;
        String str = "In a spanning tree included\nedge " + v1 + v2 + " with min edge " + weight;
        label.setText(str);
        costLabel.setText(Double.toString(strCost));
        commentList.add(str);
        step += 1;

        // paint the edge
        drawGraph(edge);

    }

    public static void main(String[] args) {
        launch();
    }
}