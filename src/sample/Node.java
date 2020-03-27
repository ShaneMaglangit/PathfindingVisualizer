package sample;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

public class Node extends Pane {
    private NodeState state;

    public Node() {
        getStyleClass().add("default-border");
        changeState(NodeState.DEFAULT);
    }

    public void changeState(NodeState state) {
        this.state = state;

        Background background;
        BackgroundFill backgroundFill;
        Paint paint;

        switch (state) {
            case VISITED: paint = Paint.valueOf("#ffeaa7"); break;
            case QUEUED: paint = Paint.valueOf("#f1c40f"); break;
            case START: paint = Paint.valueOf("#55efc4"); break;
            case END: paint = Paint.valueOf("#ee5253"); break;
            case PATH: paint = Paint.valueOf("#5352ed"); break;
            case BLOCKED: paint = Paint.valueOf("#000000"); break;
            default: paint = Paint.valueOf("#f1f2f6");
        }

        backgroundFill = new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY);
        background = new Background(backgroundFill);
        setBackground(background);
    }

    public NodeState getState() {
        return state;
    }
}
