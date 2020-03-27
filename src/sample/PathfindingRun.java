package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
            List<List<Integer>> visited = new ArrayList<>();
            List<List<Integer>> prev = new ArrayList<>();
            List<Integer> start = visualizer.getStart();
            List<Integer> end = visualizer.getEnd();
            LinkedList<List<Integer>> queue = new LinkedList();
            int[] rowDir = {0, 0, 1, -1};
            int[] colDir = {1, -1, 0, 0};

            queue.add(visualizer.getStart());

            loop:
            while(!queue.isEmpty() && isRunning) {
                List<Integer> current = queue.poll();

                // Add child to queue
                for(int i = 0; i < 4; i++) {
                    List<Integer> temp;
                    int nextRow = current.get(0) + rowDir[i];
                    int nextCol = current.get(1) + colDir[i];

                    // If exceeds then skip
                    if(nextRow < 0 || nextRow >= visualizer.ROW_COUNT) continue;
                    if(nextCol < 0 || nextCol >= visualizer.COL_COUNT) continue;

                    // If visited or start then skip
                    if(nextRow == start.get(0) && nextCol == start.get(1)) continue;
                    temp = List.of(nextRow, nextCol);
                    if(visited.contains(temp)) continue;

                    prev.add(current);
                    visited.add(temp);
                    queue.add(temp);

                    if(temp.equals(visualizer.getEnd())) {
                        tracePath(visited, prev, start, end);
                        break loop;
                    }

                    visualizer.getNodes()[temp.get(0)][temp.get(1)].changeState(NodeState.VISITED);
                }
                sleep(10L);
            }
        }
    }

    private void tracePath(List<List<Integer>> visited, List<List<Integer>> prev, List<Integer> start, List<Integer> end) {
        List<List<Integer>> path = new ArrayList<>();
        List<Integer> trackedNode = end;

        // Trace
        while(!trackedNode.equals(start)) {
            path.add(trackedNode);
            trackedNode = prev.get(visited.indexOf(trackedNode));
        }

        // Reverse
        Collections.reverse(path);

        // Show path
        showPath(path);
    }

    private void showPath(List<List<Integer>> path) {
        for(List<Integer> node : path) {
            visualizer.getNodes()[node.get(0)][node.get(1)].changeState(NodeState.PATH);
            sleep(20L);
        }
    }

    /**
     * Pause the execution (delay)
     */
    private void sleep(Long delay) {
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
