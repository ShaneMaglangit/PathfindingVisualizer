package sample;

import java.util.ArrayList;
import java.util.List;

public class PathfindingRun implements Runnable {
    private static volatile boolean isRunning;
    private Visualizer visualizer;

    public PathfindingRun(Visualizer visualizer) {
        this.visualizer = visualizer;
        this.isRunning = false;
    }

    @Override
    public void run() {
        if(isRunning) {
            List<List<Integer>> path;
            switch(visualizer.getAlgorithmVariation()) {
                case DIJKSTRA: path = Algorithm.dijkstra(this, visualizer); break;
                case ASTAR: path = Algorithm.aStar(this, visualizer); break;
                case DFS: path = Algorithm.depthFirstSearch(this, visualizer, new ArrayList<List<Integer>>(), visualizer.getStart()); break;
                default: path = Algorithm.breadthFirstSearch(this, visualizer);
            }
            if(!path.isEmpty()) showPath(path);
        }
    }


    private void showPath(List<List<Integer>> path) {
        for(List<Integer> node : path) {
            if(!isRunning) break;
            visualizer.getNodes()[node.get(0)][node.get(1)].changeState(NodeState.PATH);
            sleep(20L);
        }
    }

    /**
     * Pause the execution (delay)
     */
    public void sleep(Long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public static boolean isRunning() {
        return isRunning;
    }
}
