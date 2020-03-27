package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class Algorithm {
    public enum Variation {
        BFS("Breath-first search"), DFS("Depth-first search");

        private String stringEquiv;

        Variation(String stringEquiv) {
            this.stringEquiv = stringEquiv;
        }

        public String getStringEquiv() {
            return this.stringEquiv;
        }
    }

    public static LinkedList<List<Integer>> depthFirstSearch(PathfindingRun pathfindingRun, Visualizer visualizer, List<List<Integer>> visited, List<Integer> current) {
        int[] rowDir = {-1, 0, 1, 0};
        int[] colDir = {0, 1, 0, -1};

        for(int i = 0; i < 4; i++) {
            if(!pathfindingRun.isRunning()) break;
            LinkedList<List<Integer>> result;
            List<Integer> next;
            int nextRow = current.get(0) + rowDir[i];
            int nextCol = current.get(1) + colDir[i];

            // If exceeds then skip
            if (nextRow < 0 || nextRow >= visualizer.ROW_COUNT) continue;
            if (nextCol < 0 || nextCol >= visualizer.COL_COUNT) continue;

            // If visited or start then skip
            if (nextRow == visualizer.getStart().get(0) && nextCol == visualizer.getStart().get(1)) continue;
            next = List.of(nextRow, nextCol);
            if (visited.contains(next)) continue;

            // If end found return
            if (next.equals(visualizer.getEnd())) {
                result = new LinkedList<>();
                result.add(current);
                return result;
            }

            // Set node as visit
            visited.add(next);
            visualizer.getNodes()[next.get(0)][next.get(1)].changeState(NodeState.VISITED);
            pathfindingRun.sleep(8L);

            // Recurse
            result = depthFirstSearch(pathfindingRun, visualizer, visited, next);

            if(result != null) {
                if(!current.equals(visualizer.getStart())) result.add(0, current);
                return result;
            }
        }

        return null;
    }

    public static List<List<Integer>> breadthFirstSearch(PathfindingRun pathfindingRun, Visualizer visualizer) {
        List<List<Integer>> visited = new ArrayList<>();
        List<List<Integer>> prev = new ArrayList<>();
        List<Integer> start = visualizer.getStart();
        List<Integer> end = visualizer.getEnd();
        LinkedList<List<Integer>> queue = new LinkedList();
        int[] rowDir = {0, 0, 1, -1};
        int[] colDir = {1, -1, 0, 0};

        queue.add(visualizer.getStart());

        while (!queue.isEmpty() && pathfindingRun.isRunning()) {
            List<Integer> current = queue.poll();

            // Add neighbors / children to queue
            for (int i = 0; i < 4; i++) {
                List<Integer> temp;
                int nextRow = current.get(0) + rowDir[i];
                int nextCol = current.get(1) + colDir[i];

                // If exceeds then skip
                if (nextRow < 0 || nextRow >= visualizer.ROW_COUNT) continue;
                if (nextCol < 0 || nextCol >= visualizer.COL_COUNT) continue;

                // If visited or start then skip
                if (nextRow == start.get(0) && nextCol == start.get(1)) continue;
                temp = List.of(nextRow, nextCol);
                if (visited.contains(temp)) continue;

                prev.add(current);
                visited.add(temp);
                queue.add(temp);

                // If end node is found, trace and return path.
                if (temp.equals(visualizer.getEnd())) {
                    return traceBFSPath(visited, prev, start, end);
                }

                visualizer.getNodes()[temp.get(0)][temp.get(1)].changeState(NodeState.VISITED);
            }
            pathfindingRun.sleep(8L);
        }

        // If end node is not found, return an empty path.
        return new ArrayList<>();
    }

    private static List<List<Integer>> traceBFSPath(List<List<Integer>> visited, List<List<Integer>> prev, List<Integer> start, List<Integer> end) {
        List<List<Integer>> path = new ArrayList<>();
        List<Integer> trackedNode = prev.get(visited.indexOf(end));

        // Trace
        while (!trackedNode.equals(start)) {
            path.add(trackedNode);
            trackedNode = prev.get(visited.indexOf(trackedNode));
        }

        // Reverse
        Collections.reverse(path);

        return path;
    }
}