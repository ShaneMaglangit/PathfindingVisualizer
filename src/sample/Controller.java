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

    public void visualize(Visualizer visualizer, PathfindingRun pathfindingRun) {
        if(pathfindingRun.isRunning()) {
            pathfindingRun.setRunning(false);
            visualizer.createNodes();
        } else {
            pathfindingRun.setRunning(true);
            new Thread(pathfindingRun).start();
        }
    }
}
