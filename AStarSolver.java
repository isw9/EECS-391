import java.util.PriorityQueue;
import java.util.*;

// This class is called from the main Node class
// AStarSolver is passed a Node and a heuristic and solves the 8 puzzle using A* search
public class AStarSolver {
  public static String[] directions = {"up", "down", "left", "right"};
  public static PriorityQueue<Node> pQueue = new PriorityQueue<Node>(10000000, new BoardComparator());
  public static HashSet<String> set = new HashSet<String>();
  public static boolean goalFound = false;

  // The Node class enters here
  public static void solve(String heuristic, Node node) {
    System.out.println(" STAR");
    if (heuristic.equals("h1")) {
      node.heuristic = Util.h1(node.board);
    }
    else if (heuristic.equals("h2")) {
      node.heuristic = Util.h2(node.board);
    }
    else {
      System.out.println("Enter a valid heuristic");
    }
    // clear the priority queue and hash table so we know they are empty and THEN add the first node
    set.clear();
    pQueue.clear();
    goalFound = false;
    set.add(stringify(node.board));
    pQueue.add(node);

    // keep picking a node to expand from the priority queue until the puzzle is solved
    while (pQueue.size() != 0 && goalFound == false) {
      Node currentNode = pQueue.poll();
      if (!isGoalState(currentNode.board)) {
        expandNode(currentNode, heuristic);
      }
      else {
        System.out.println("Path:" + currentNode.path);
        System.out.println("COMPLETE");
      }
    }
  }
  
  //When expanding a node, only add its successor to the queue if it has not already been examined
  public static void expandNode(Node node, String heuristic) {
    for (int i = 0; i < 4; i++) {
      int[][] currentBoard = node.board;
      if (Util.validMove(directions[i], currentBoard)) {
        int[][] boardPosition = Util.solveMove(directions[i], currentBoard);
        if (!alreadyExpanded(boardPosition)) { 
          StringBuilder path = new StringBuilder();
          path.append(node.path).append(" ").append(directions[i].substring(0,1));
          int heuris = 0;
          if (heuristic.equals("h1")) {
            heuris = Util.h1(boardPosition);
          }
          else {
            heuris = Util.h2(boardPosition);
          }
          Node unexploredNode = new Node(boardPosition, 
                                         heuris, 
                                         path.toString(), node.costSoFar + 1);
          System.out.println("\n");
          Node.printState(boardPosition);
          pQueue.add(unexploredNode);
          set.add(stringify(boardPosition));
        }
      }
    }
  }
 
  // returns true if the inputted int[][] is the goal state
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
    goalFound = true;
    return true;
  }
 
  // returns true if the board in question has already been part of a node that has been expanded
  public static boolean alreadyExpanded(int[][] board) {
    return set.contains(stringify(board));
  }
  
  public static String stringify(int[][] board) {
    StringBuilder representation = new StringBuilder();
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        representation.append(board[i][j]);
      }
    }
    return representation.toString();
  }
}

// I had to override the compare method for the priority queue so I could put Nodes into queue
class BoardComparator implements Comparator<Node> {
  public int compare(Node n1, Node n2) {
    int f1 = n1.heuristic + n1.costSoFar;
    int f2 = n2.heuristic + n2.costSoFar;
    if (f1 > f2) {
      return 1;
    }
    else if (f1 < f2) {
      return -1;
    }
    return 0;
  }
}