//package sample;
//
//import java.util.LinkedList;
//
//public final class Algorithm {
//    public static void breadthFirstSearch(boolean isRunning, Node[][] nodes, Node start, Node end) {
//        LinkedList<Node> queue = new LinkedList();
//        int[] rowDir = {0, -1, 0, 1};
//        int[] colDir = {1, 0, -1, 0};
//
//        // Put start node in the queue
//        queue.add(start);
//
//        // DO WHILE: Queue is not empty
//        while(!queue.isEmpty()) {
//            try {
//                if(!isRunning) return;
//                // Pop the next node in the queue
//                Node currentNode = queue.poll();
//                currentNode.setIsVisited(true);
//                // If the next node is equal to the end node; return
//                if(currentNode.getIsEnd()) return;
//                // For rowDir, colDir
//                for(int i = 0; i < 4; i++) {
//                    Node nextNode;
//                    // Let x = nextRow, y = nextCol
//                    int x = currentNode.getX() + rowDir[i];
//                    int y = currentNode.getY() + colDir[i];
//                    // If nextRow < 0 or nextRow >= nodes.length then skip
//                    if(x < 0 || x >= 25) continue;
//                    // If nextCol < 0 or nextCol >= nodes[nextRow].length then skip
//                    if(y < 0 || y >= 50) continue;
//                    // If already in queue then skip
//                    nextNode = nodes[x][y];
//                    if(nextNode.getOnQueue()) continue;
//                    else nextNode.setOnQueue(true);
//                    // Add to queue
//                    queue.add(nextNode);
//                }
//                Thread.sleep(8);
//            } catch (InterruptedException interruptedException) {
//                interruptedException.printStackTrace();
//            }
//        }
//    }
//}