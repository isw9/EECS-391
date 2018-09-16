import java.util.PriorityQueue;
import java.util.*;

public class AStarSolver {
  public static String[] directions = {"up", "down", "left", "right"};
  public static PriorityQueue<PuzzleSolver> pQueue = new PriorityQueue<PuzzleSolver>(10000000, new BoardComparator());
  public static HashSet<int[][]> set = new HashSet<int[][]>();
  public static boolean goalFound = false;

  //
  public static void solve(String heuristic, PuzzleSolver node) {
    if (heuristic.equals("h1")) {
      node.heuristic = Util.h1(node.board);
    }
    else if (heuristic.equals("h2")) {
      node.heuristic = Util.h2(node.board);
    }
    else {
      System.out.println("Enter a valid heuristic");
    }
    //clear the priority queue and hash table so we know they are empty
    set.clear();
    pQueue.clear();
    goalFound = false;
    set.add(node.board);
    pQueue.add(node);

    //keep picking a node to expand from the priority queue until the puzzle is solved
    while (pQueue.size() != 0 && goalFound == false) {
      PuzzleSolver currentNode = pQueue.poll();
      if (!isGoalState(currentNode.board)) {
        expandNode(currentNode, heuristic);
      }
      else {
        System.out.println("Path: " + currentNode.path);
        System.out.println("COMPLETE");
      }
    }
  }
  
  //When expanding a node, only add its successor to the queue if it has not already been examined
  public static void expandNode(PuzzleSolver node, String heuristic) {
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
          PuzzleSolver unexploredNode = new PuzzleSolver(boardPosition, 
                                                         heuris, 
                                                         path.toString());
          pQueue.add(unexploredNode);
          set.add(boardPosition);
        }
      }
    }
  }
 
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
 
  public static String hashKey(int[][] board) {
    String key = "";
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        key = key + Integer.toString(board[i][j]);
      }
    }
    return key;
  }
  
  public static boolean alreadyExpanded(int[][] board) {
    return set.contains(board);
  }
}


class BoardComparator implements Comparator<PuzzleSolver> {
  public int compare(PuzzleSolver n1, PuzzleSolver n2) {
    if (n1.heuristic > n2.heuristic) {
      return 1;
    }
    else if (n1.heuristic < n2.heuristic) {
      return -1;
    }
    return 0;
  }
}