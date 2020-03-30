package sample;

import java.util.*;

public final class Algorithm {
    public enum Variation {
        BFS("Breath-first search"), DFS("Depth-first search"),
        DIJKSTRA("Dijkstra"), ASTAR("A-Star Search (A*)");

        private String stringEquiv;

        Variation(String stringEquiv) {
            this.stringEquiv = stringEquiv;
        }

        public String getStringEquiv() {
            return this.stringEquiv;
        }
    }

    public static LinkedList<List<Integer>> depthFirstSearch(PathfindingRun pathfindingRun, Visualizer visualizer, List<List<Integer>> visited, List<Integer> current) {
        LinkedList<List<Integer>> result = new LinkedList<>();
        int[] rowDir = {-1, 0, 1, 0};
        int[] colDir = {0, 1, 0, -1};

        loop:
        for(int i = 0; i < 4; i++) {
            if(!pathfindingRun.isRunning()) break;
            List<Integer> next;
            int nextRow = current.get(0) + rowDir[i];
            int nextCol = current.get(1) + colDir[i];

            // If exceeds then skip
            if (nextRow < 0 || nextRow >= visualizer.ROW_COUNT) continue;
            if (nextCol < 0 || nextCol >= visualizer.COL_COUNT) continue;

            // If visited or start then skip
            if (visualizer.getNodes()[nextRow][nextCol].getState() == NodeState.BLOCKED) continue;
            if (nextRow == visualizer.getStart().get(0) && nextCol == visualizer.getStart().get(1)) continue;
            next = List.of(nextRow, nextCol);
            if (visited.contains(next)) continue;

            // If end found return
            if (next.equals(visualizer.getEnd())) {
                result.add(current);
                break loop;
            }

            // Set node as visit
            visited.add(next);
            visualizer.getNodes()[next.get(0)][next.get(1)].changeState(NodeState.VISITED);
            pathfindingRun.sleep(8L);

            // Recurse
            result = depthFirstSearch(pathfindingRun, visualizer, visited, next);

            if(!result.isEmpty()) {
                if(!current.equals(visualizer.getStart())) result.add(0, current);
                    break loop;
            }
        }

        return result;
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
            List<List<Integer>> neigbors = neighbors(current, visualizer.ROW_COUNT, visualizer.COL_COUNT);
            for (List<Integer> neighbor : neigbors) {
                // If visited or start then skip
                if (visualizer.getNodes()[neighbor.get(0)][neighbor.get(1)].getState() == NodeState.BLOCKED) continue;
                if (neighbor.equals(visualizer.getStart())) continue;
                if (visited.contains(neighbor)) continue;

                prev.add(current);
                visited.add(neighbor);
                queue.add(neighbor);

                // If end node is found, trace and return path.
                if (neighbor.equals(visualizer.getEnd())) {
                    return tracePath(visited, prev, start, end);
                }

                visualizer.getNodes()[neighbor.get(0)][neighbor.get(1)].changeState(NodeState.VISITED);
            }
            pathfindingRun.sleep(8L);
        }

        // If end node is not found, return an empty path.
        return new ArrayList<>();
    }

    public static List<List<Integer>> dijkstra(PathfindingRun pathfindingRun, Visualizer visualizer) {
        PriorityQueue<List<Object>> heap = new PriorityQueue<>(new DistanceComparator());
        List<List<Integer>> visited = new ArrayList<>();
        List<List<Integer>> prev = new ArrayList<>();

        heap.add(List.of(0, visualizer.getStart(), new ArrayList<>()));

        while(!heap.isEmpty() && pathfindingRun.isRunning()) {
            List<Integer> current = (List<Integer>) heap.peek().get(1);
            List<Integer> tempPrev = (List<Integer>) heap.peek().get(2);
            int distance = (int) heap.poll().get(0);

            if(!visited.contains(current)) {
                Node currentNode = visualizer.getNodes()[current.get(0)][current.get(1)];
                List<List<Integer>> neighbors;
                visited.add(current);
                prev.add(tempPrev);

                if(List.of(current.get(0), current.get(1)).equals(visualizer.getEnd())) {
                    return tracePath(visited, prev, visualizer.getStart(), visualizer.getEnd());
                }

                if(!currentNode.getState().equals(NodeState.START)) currentNode.changeState(NodeState.VISITED);
                pathfindingRun.sleep(8L);

                neighbors = neighbors(current, visualizer.ROW_COUNT, visualizer.COL_COUNT);
                for(List<Integer> neighbor : neighbors) {
                    if(visited.contains(neighbor)) continue;
                    Node node = visualizer.getNodes()[neighbor.get(0)][neighbor.get(1)];
                    if(node.getState().equals(NodeState.BLOCKED)) continue;
                    int nextDistance = distance + node.getWeight();
                    heap.add(List.of(nextDistance, neighbor, current));
                }
            }
        }

        // If end node is not found, return an empty path.
        return new ArrayList<>();
    }

    public static List<List<Integer>> aStar(PathfindingRun pathfindingRun, Visualizer visualizer) {
        PriorityQueue<List<Object>> heap = new PriorityQueue<>(new CostComparator());
        List<List<Integer>> visited = new ArrayList<>();
        List<List<Integer>> prev = new ArrayList<>();

        int gCost = 0;
        int hCost = getHeuristicDistance(visualizer.getStart(), visualizer.getEnd());
        int fCost = hCost + 0;
        heap.add(List.of(gCost, hCost, fCost, visualizer.getStart(), new ArrayList<>()));

        while(!heap.isEmpty() && pathfindingRun.isRunning()) {
            List<Integer> current = (List<Integer>) heap.peek().get(3);
            List<Integer> tempPrev = (List<Integer>) heap.peek().get(4);
            int distance = (int) heap.poll().get(0);

            if(!visited.contains(current)) {
                Node currentNode = visualizer.getNodes()[current.get(0)][current.get(1)];
                List<List<Integer>> neighbors;
                visited.add(current);
                prev.add(tempPrev);

                if(List.of(current.get(0), current.get(1)).equals(visualizer.getEnd())) {
                    return tracePath(visited, prev, visualizer.getStart(), visualizer.getEnd());
                }

                if(!currentNode.getState().equals(NodeState.START)) currentNode.changeState(NodeState.VISITED);
                pathfindingRun.sleep(8L);

                neighbors = neighbors(current, visualizer.ROW_COUNT, visualizer.COL_COUNT);
                for(List<Integer> neighbor : neighbors) {
                    if(visited.contains(neighbor)) continue;
                    Node node = visualizer.getNodes()[neighbor.get(0)][neighbor.get(1)];
                    if(node.getState().equals(NodeState.BLOCKED)) continue;
                    gCost = distance + node.getWeight();
                    hCost = getHeuristicDistance(neighbor, visualizer.getEnd());
                    fCost = hCost + gCost;
                    heap.add(List.of(gCost, hCost, fCost, neighbor, current));
                }
            }
        }

        // If end node is not found, return an empty path.
        return new ArrayList<>();
    }

    public static int getHeuristicDistance(List<Integer> a, List<Integer> b) {
        return ((int) Math.sqrt(Math.pow(b.get(0) - a.get(0), 2) + Math.pow(b.get(1) - a.get(1), 2))) * 4;
    }

    public static List<List<Integer>> neighbors(List<Integer> origin, int maxRow, int maxCol) {
        List<List<Integer>> neighbors = new ArrayList<>();
        int[] rowDir = {1, 0, -1, 0};
        int[] colDir = {0, 1, 0, -1};

        for(int i = 0; i < 4; i++) {
            int nextRow = origin.get(0) + rowDir[i];
            int nextCol = origin.get(1) + colDir[i];

            if(nextRow < 0 || nextRow >= maxRow) continue;
            if(nextCol < 0 || nextCol >= maxCol) continue;

            neighbors.add(List.of(nextRow, nextCol));
        }

        return neighbors;
    }

    private static List<List<Integer>> tracePath(List<List<Integer>> visited, List<List<Integer>> prev, List<Integer> start, List<Integer> end) {
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