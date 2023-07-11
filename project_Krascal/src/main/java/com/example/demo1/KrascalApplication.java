package com.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.graph.*;

public class HelloApplication extends Application {
    private static Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

    static Map<String, List<Double>> map = new HashMap();

    public static LinkedList<String> vertexes = new LinkedList<String>();

    public static LinkedList<String> vertOfOstovTree = new LinkedList<String>();

    public static LinkedList<DefaultWeightedEdge>  edgeColorList = new LinkedList<DefaultWeightedEdge>();

    private static Stage mainStage;

    private static Scene mainScene;

    static int step = 0;

    static int totalEdgesInOstovTree;

    static double cost = 0;

    public static boolean edgeExist(String v1, String v2) {
        DefaultWeightedEdge edge = graph.getEdge(v1, v2);
        if (edge == null){
            return false;
        }
        return true;
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 550);
        stage.setTitle("Алгоритм Краскала");
        stage.setScene(scene);
        mainScene = scene;
        // Создание панели и добавление компонента Canvas
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
    };

    public static void deleteEdge(String vertex1, String vertex2){
        DefaultWeightedEdge edge = graph.getEdge(vertex1, vertex2);
        if (edge == null){
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Удаление ребра");
            warning.setHeaderText("Нельзя удалить отсутсвующее ребро");
            GridPane gridPane2 = new GridPane();
            warning.getDialogPane().setContent(gridPane2);
            warning.showAndWait();
        }
        else{
            graph.removeEdge(vertex1, vertex2);
            drawGraph(null);
        }
    };

    public void checkAddCostEdge(ChoiceBox choiceVertex1, ChoiceBox choiceVertex2, TextField textAddEdgeCost){ /// !!!!!!!!!!!!!
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
            else if(HelloApplication.edgeExist(choiceVertex1.getValue().toString(), choiceVertex2.getValue().toString())){
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Добавление ребра");
                warning.setHeaderText("Данное ребро уже существует");
                GridPane gridPane2 = new GridPane();
                warning.getDialogPane().setContent(gridPane2);
                warning.showAndWait();
            }
            else{
                HelloApplication.addEdge(choiceVertex1.getValue().toString(),choiceVertex2.getValue().toString(),w);
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

    public static void importGraph(File file){
        try (FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] elements = line.split(" ");
                if (elements.length != 3) {
                    Alert warning = new Alert(Alert.AlertType.WARNING);
                    warning.setTitle("Импортирование графа");
                    warning.setHeaderText("В каждой строке должно быть 3 параметра, разделенных пробелом");
                    GridPane gridPane2 = new GridPane();
                    warning.getDialogPane().setContent(gridPane2);
                    warning.showAndWait();
                    return;
                }
                try {
                    double w = Double.parseDouble(elements[2]);
                    if (w <= 0){
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    Alert warning = new Alert(Alert.AlertType.WARNING);
                    warning.setTitle("Импортирование графа");
                    warning.setHeaderText("Третий параметр в каждой строке должен быть положительным числом");
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
        graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        vertexes.clear();
        drawGraph(null);
        label.setText("");
        costLabel.setText("0");
    }

    public static void drawGraph(DefaultWeightedEdge edgeColor){
        // Создание компонента Canvas для отображения графа
        Canvas canvas = new Canvas(400, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Настройка параметров отображения графа
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;
        double radius = 50;
        double angle = 2 * Math.PI / graph.vertexSet().size();

        // Отображение вершин
        int i = 0;
        for (String vertex : graph.vertexSet()) {
            double x = centerX + radius * Math.cos(i * angle);
            double y = centerY + radius * Math.sin(i * angle);
            List<Double> list1 = new ArrayList<>();
            list1.add(x);
            list1.add(y);
            HelloApplication.map.put(vertex, list1);

            gc.setFill(Color.web("#5B71D7"));
            gc.fillOval(x, y, 20, 20);
            gc.setFill(Color.web("#5B71D7"));
            gc.fillText(vertex, x + 25, y + 15);

            i++;
        }

        // Отображение ребер
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            String source = graph.getEdgeSource(edge);
            String target = graph.getEdgeTarget(edge);
            double weight = graph.getEdgeWeight(edge);

            double x1 = map.get(source).get(0);
            double y1 = map.get(source).get(1);
            double x2 = map.get(target).get(0);
            double y2 = map.get(target).get(1);
            double labelX = (x1 + x2) / 2;
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

            i++;
        }
        // Создание панели и добавление компонента Canvas
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
        String[][] ostovTreeCost = new String[graph.edgeSet().size()][3];
        int i = 0;
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            String source = graph.getEdgeSource(edge);
            String target = graph.getEdgeTarget(edge);
            double weight = graph.getEdgeWeight(edge);
            String[] list = {String.valueOf(weight), source, target};
            ostovTreeCost[i] = list;
            i +=1 ;
        }
        Arrays.sort(ostovTreeCost, Comparator.comparing(row -> Double.parseDouble(row[0])));
        return ostovTreeCost;
    }

    public static void stepForward(Label label, Label costLabel) {
        doAlgorithm(label, sortEdge(), costLabel);
    };

    public static void doAlgorithm(Label label, String[][] ostovTreeCost, Label costLabel){
        // проверка на конец алгоритма
        if (totalEdgesInOstovTree == graph.vertexSet().size() - 1){
            label.setText("Остовное дерево построено");
            return;
        }
        System.out.println(step);
        label.setText("");
        costLabel.setText("");
        String v1 = ostovTreeCost[step][1];
        String v2 = ostovTreeCost[step][2];
        // проверка на цикл
        if (vertOfOstovTree.contains(v1) && vertOfOstovTree.contains(v2)){
            step += 1;
            doAlgorithm(label, ostovTreeCost, costLabel);
        }
        vertOfOstovTree.add(v1);
        vertOfOstovTree.add(v2);
        // комментарии к построению
        totalEdgesInOstovTree += 1;
        String str = "В остовное дерево\n включено ребро " + v1 + v2;
        DefaultWeightedEdge edge = graph.getEdge(v1, v2);
        double weight = graph.getEdgeWeight(edge);
        cost += weight;
        Double strCost = cost;
        label.setText(str);
        costLabel.setText(strCost.toString());
        step += 1;

        // красим ребро
        drawGraph(edge);

    };

    public static boolean checkConnectivityComponents(){
        Set<String> setVertexofGraph = new HashSet();
        Set<String> setVertexofConnectivityComponent = new HashSet();

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

        while (true) {
            int sizeSetVertexPrev = setVertexofConnectivityComponent.size();
            for (String vertex : setVertexofConnectivityComponent) {
                for (DefaultWeightedEdge edge : graph.edgeSet()) {
                    if (graph.getEdgeTarget(edge).equals(vertex)) {
                        setVertexofConnectivityComponent.add(graph.getEdgeSource(edge));
                    }
                    if (graph.getEdgeSource(edge).equals(vertex)) {
                        setVertexofConnectivityComponent.add(graph.getEdgeTarget(edge));
                    }
                }
            }
            int sizeSetVertexNext = setVertexofConnectivityComponent.size();
            if (sizeSetVertexPrev == sizeSetVertexNext){
                break;
            }
        }
        if (setVertexofGraph.size() != setVertexofConnectivityComponent.size()) {
            return false;
        } else{
            return true;
        }

    }


    public static void main(String[] args) {
        launch();
    }
}