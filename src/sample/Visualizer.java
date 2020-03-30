package sample;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
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
    private HBox hbxInstructions;
    private GridPane matrix;
    private GridPane gridInstructions;
    private Label lblStart;
    private Label lblEnd;
    private Label lblBlock;
    private Label lblWeight;
    private ComboBox cbxAlgo;
    private Button btnVisualize;
    private Button btnClear;

    public Visualizer(Controller controller) {
        this.controller = controller;
        this.nodes = new Node[ROW_COUNT][COL_COUNT];
        this.hbxMenu = new HBox();
        this.hbxInstructions = new HBox();
        this.matrix = new GridPane();

        // Create the menu
        this.hbxMenu.setAlignment(Pos.CENTER);
        this.hbxMenu.setSpacing(24);
        this.cbxAlgo = new ComboBox();
        this.btnVisualize = new Button("Visualize");
        this.btnClear = new Button("Clear board");
        hbxMenu.setPadding(new Insets(10));

        // Create instructions
        this.lblStart = new Label("LMB | Set start");
        this.lblEnd = new Label("RMB | Set end");
        this.lblBlock = new Label("LMB + DRAG | Add wall");
        this.lblWeight = new Label("RMB + DRAG | Add weight");
        hbxInstructions.setPadding(new Insets(10));
        hbxInstructions.setSpacing(50);
        hbxInstructions.setAlignment(Pos.CENTER);

        // Add the items to the cbx
        btnVisualize.setOnMouseClicked((event) -> controller.visualize(this, pathfindingRun));
        cbxAlgo.setItems(FXCollections.observableArrayList(
                Algorithm.Variation.DIJKSTRA.getStringEquiv(),
                Algorithm.Variation.ASTAR.getStringEquiv(),
                Algorithm.Variation.BFS.getStringEquiv(),
                Algorithm.Variation.DFS.getStringEquiv()
        ));
        btnClear.setOnMouseClicked((event) -> controller.clearBoard(this, pathfindingRun));
        cbxAlgo.getSelectionModel().selectFirst();

        // Set start and end nodes
        start = List.of((ROW_COUNT - 1) / 2, COL_COUNT / 3 / 2);
        end = List.of((ROW_COUNT - 1) / 2, COL_COUNT - (COL_COUNT / 3 / 2));

        // Create the nodes
        createNodes();

        // Create the runnable
        this.pathfindingRun = new PathfindingRun(this);

        // Add the child controls
        hbxMenu.getChildren().addAll(cbxAlgo, btnVisualize, btnClear);
        hbxInstructions.getChildren().addAll(lblStart, lblEnd, lblBlock, lblWeight);
        this.getChildren().addAll(hbxMenu, hbxInstructions, matrix);

        // Add stylesheet
        getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    }

    public void resetNodes() {
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                Node node = nodes[row][col];
                if (
                    node.getState().equals(NodeState.BLOCKED) ||
                    List.of(row, col).equals(start) ||
                    List.of(row, col).equals(end)
                ) continue;
                else node.changeState(NodeState.DEFAULT);
            }
        }
    }

    public void createNodes() {
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                int finalRow = row;
                int finalCol = col;
                Node temp = new Node();

                temp.addEventHandler(MouseEvent.DRAG_DETECTED, (event) -> {
                    temp.startFullDrag();
                    if (event.isPrimaryButtonDown()) {
                        controller.setNodeAsBlocked(this, List.of(finalRow, finalCol));
                    } else {
                        controller.addWeight(this, List.of(finalRow, finalCol));
                    }
                });

                temp.addEventHandler(MouseDragEvent.MOUSE_DRAG_ENTERED, (event) -> {
                    if (event.isPrimaryButtonDown()) {
                        controller.setNodeAsBlocked(this, List.of(finalRow, finalCol));
                    } else {
                        controller.addWeight(this, List.of(finalRow, finalCol));
                    }
                });

                temp.setOnMouseClicked((event) -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        controller.setStartNode(this, List.of(finalRow, finalCol));
                    } else {
                        controller.setEndNode(this, List.of(finalRow, finalCol));
                    }
                });

                nodes[row][col] = temp;
                matrix.add(temp, col, row);
            }
        }

        // Set the start and end nodes
        nodes[start.get(0)][start.get(1)].changeState(NodeState.START);
        nodes[end.get(0)][end.get(1)].changeState(NodeState.END);
    }

    public Algorithm.Variation getAlgorithmVariation() {
        String selectedValue = String.valueOf(cbxAlgo.getValue());

        if (selectedValue == Algorithm.Variation.DFS.getStringEquiv()) {
            return Algorithm.Variation.DFS;
        } else if (selectedValue == Algorithm.Variation.ASTAR.getStringEquiv()) {
            return Algorithm.Variation.ASTAR;
        } else if (selectedValue == Algorithm.Variation.DIJKSTRA.getStringEquiv()) {
            return Algorithm.Variation.DIJKSTRA;
        } else {
            return Algorithm.Variation.BFS;
        }
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
}
