package sample;

import java.util.List;

public class Controller {
    public void setStartNode(Visualizer visualizer, List<Integer> next) {
        if(!visualizer.getPathfindingRun().isRunning() && !visualizer.getEnd().equals(next)) {
            Node[][] nodes = visualizer.getNodes();
            List<Integer> start = visualizer.getStart();

            nodes[start.get(0)][start.get(1)].changeState(NodeState.DEFAULT);
            nodes[next.get(0)][next.get(1)].changeState(NodeState.START);

            visualizer.setStart(next);
        }
    }

    public void setEndNode(Visualizer visualizer, List<Integer> next) {
        if(!visualizer.getPathfindingRun().isRunning() && !visualizer.getStart().equals(next)) {
            Node[][] nodes = visualizer.getNodes();
            List<Integer> end = visualizer.getEnd();

            nodes[end.get(0)][end.get(1)].changeState(NodeState.DEFAULT);
            nodes[next.get(0)][next.get(1)].changeState(NodeState.END);

            visualizer.setEnd(next);
        }
    }

    public void setNodeAsBlocked(Visualizer visualizer, List<Integer> next) {
        Node node = visualizer.getNodes()[next.get(0)][next.get(1)];
        if(!visualizer.getPathfindingRun().isRunning() && node.getState().equals(NodeState.DEFAULT)) {
            node.changeState(NodeState.BLOCKED);
        }
    }

    public void addWeight(Visualizer visualizer, List<Integer> next) {
        Node node = visualizer.getNodes()[next.get(0)][next.get(1)];
        if(!visualizer.getPathfindingRun().isRunning() && node.getState().equals(NodeState.DEFAULT)) {
            node.addWeight(1);
        }
    }

    public void visualize(Visualizer visualizer, PathfindingRun pathfindingRun) {
        if(pathfindingRun.isRunning()) {
            pathfindingRun.setRunning(false);
            visualizer.resetNodes();
        } else {
            pathfindingRun.setRunning(true);
            new Thread(pathfindingRun).start();
        }
    }

    public void clearBoard(Visualizer visualizer, PathfindingRun pathfindingRun) {
        pathfindingRun.setRunning(false);
        visualizer.createNodes();
    }
}
