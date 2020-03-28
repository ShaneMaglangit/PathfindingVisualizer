package sample;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class Visualizer extends VBox {
    public static final int ROW_COUNT = 25;
    public static final int COL_COUNT = 50;
    private Controller controller;
    private Node[][] nodes;
    private List<Integer> start;
    private List<Integer> end;
    private PathfindingRun pathfindingRun;
    private HBox hbxMenu;
    private GridPane matrix;
    private ComboBox cbxAlgo;
    private Button btnVisualize;

    public Visualizer(Controller controller) {
        this.controller = controller;
        this.nodes = new Node[ROW_COUNT][COL_COUNT];
        this.hbxMenu = new HBox();
        this.matrix = new GridPane();

        // Create the menu
        this.hbxMenu.setAlignment(Pos.CENTER);
        this.hbxMenu.setSpacing(24);
        this.cbxAlgo = new ComboBox();
        this.btnVisualize = new Button("Visualize");

        // Add the items to the cbx
        btnVisualize.setOnMouseClicked((event) -> controller.visualize(this, pathfindingRun));
        cbxAlgo.setItems(FXCollections.observableArrayList(
                Algorithm.Variation.DIJKSTRA.getStringEquiv(),
                Algorithm.Variation.ASTAR.getStringEquiv(),
                Algorithm.Variation.BFS.getStringEquiv(),
                Algorithm.Variation.DFS.getStringEquiv()
        ));
        cbxAlgo.getSelectionModel().selectFirst();

        // Set start and end nodes
        start = List.of((ROW_COUNT - 1) / 2, COL_COUNT / 3 / 2);
        end = List.of((ROW_COUNT - 1) / 2, COL_COUNT - (COL_COUNT / 3 / 2));

        // Create the nodes
        createNodes();

        // Create the runnable
        this.pathfindingRun = new PathfindingRun(this);

        // Add the child controls
        hbxMenu.getChildren().addAll(cbxAlgo, btnVisualize);
        this.getChildren().addAll(hbxMenu, matrix);

        // Add stylesheet
        getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    }

    public void createNodes() {
        matrix.getChildren().clear();
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                int finalRow = row;
                int finalCol = col;
                Node temp = new Node();
                temp.setOnMouseClicked((event) -> {
                    controller.setStartNode(this, List.of(finalRow, finalCol));
                });
                nodes[row][col] = temp;
                matrix.add(temp, col, row);
            }
        }

        // Set the start and end nodes
        nodes[start.get(0)][start.get(1)].changeState(NodeState.START);
        nodes[end.get(0)][end.get(1)].changeState(NodeState.END);
    }

    public Node[][] getNodes() {
        return nodes;
    }

    public List<Integer> getStart() {
        return start;
    }

    public void setStart(List<Integer> start) {
        this.start = start;
    }

    public List<Integer> getEnd() {
        return end;
    }

    public void setEnd(List<Integer> end) {
        this.end = end;
    }

    public PathfindingRun getPathfindingRun() {
        return pathfindingRun;
    }

    public Algorithm.Variation getAlgorithmVariation() {
        String selectedValue = String.valueOf(cbxAlgo.getValue());

        if(selectedValue == Algorithm.Variation.DFS.getStringEquiv()) {
            return Algorithm.Variation.DFS;
        } else if(selectedValue == Algorithm.Variation.ASTAR.getStringEquiv()) {
            return Algorithm.Variation.ASTAR;
        } else if(selectedValue == Algorithm.Variation.DIJKSTRA.getStringEquiv()) {
            return Algorithm.Variation.DIJKSTRA;
        } else {
            return  Algorithm.Variation.BFS;
        }
    }
}
