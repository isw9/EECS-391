import java.util.PriorityQueue;
import java.util.*;
public class BeamSolver {

  public static String[] directions = {"up", "down", "left", "right"};
  public static PriorityQueue<Node> beamQueue = new PriorityQueue<Node>(10000000, new BoardComparator());
  public static PriorityQueue<Node> beamQueueTwo = new PriorityQueue<Node>(10000000, new BoardComparator());
  public static HashSet<int[][]> beamSet = new HashSet<int[][]>();
  public static boolean puzzleSolved = false;

  // This class is called from the main node class
  // Given a node and a k value, this solves the 8 puzzle using beam search
  public static void solve(int k, Node node, int maxNodes) {
    node.heuristic = Util.h2(node.board);
    //clear the priority queue and hash table so we know they are empty and THEN add the first node
    beamSet.clear();
    beamQueue.clear();
    beamQueueTwo.clear();
    puzzleSolved = false;
    beamSet.add(node.board);
    beamQueue.add(node);

    // My beam search works by utilizing two queues: beamQueue and beamQueueTwo
    // 1) We examine each node in beamQueue and put it in beamQueueTwo. After this step, beamQueue is empty
    // 2) Take the k best nodes from beamQueueTwo (based on the heuristic) and put them in beamQueue
    // 3) Clear beamQueueTwo
    // 4) Repeat until the goal is found


    int counter = 0;
    while (puzzleSolved == false && counter <= maxNodes) {
    //keep picking a node to expand from the priority queue beamQueue until the puzzle is solved or beamQueue is empty
      while (beamQueue.size() != 0) {
        Node currentNode = beamQueue.poll();
        if (!isGoalState(currentNode.board)) {
          expandNode(currentNode);
          counter++;
        }
        else {
          System.out.println("Path: " + currentNode.path);
          System.out.println("Number of moves:" + currentNode.costSoFar);
        }
      }
      //take k best nodes from beamQueueTwo and put them in beamQueue
      int i = 0;
      while (i < k) {
        Node cNode = beamQueueTwo.poll();
        if (cNode != null) {
          beamQueue.add(cNode);
        }
        i++;
      }
      beamQueueTwo.clear();
    }
    if (!(counter < maxNodes)) {
      System.out.println("Maximum node limit was exceeded during search");
    }
  }

  //When expanding a node, only add its successor to beamQueueTwo if it has not already been examined
  public static void expandNode(Node node) {
    for (int i = 0; i < 4; i++) {
      int[][] currentBoard = node.board;
      if (Util.validMove(directions[i], currentBoard)) {
        int[][] boardPosition = Util.solveMove(directions[i], currentBoard);
        if (!alreadyExpanded(boardPosition)) {
          StringBuilder path = new StringBuilder();
          path.append(node.path).append(" ").append(directions[i].substring(0,1));
          int heuris = 0;
          heuris = Util.h2(boardPosition);
          Node unexploredNode = new Node(boardPosition,
                                         heuris,
                                         path.toString(),
                                         node.costSoFar + 1);
          beamQueueTwo.add(unexploredNode);
          beamSet.add(boardPosition);
        }
      }
    }
  }

  // checks if the given int[][] has already been part of a node that has been expanded
  public static boolean alreadyExpanded (int[][] board) {
    return beamSet.contains(board);
  }

  // checks if the given int[][] is the goal state
  public static boolean isGoalState(int[][] puzzle) {
    int count = 0;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (puzzle[i][j] != count) {
          return false;
        }
        count++;
      }
    }
    puzzleSolved = true;
    return true;
  }
}
