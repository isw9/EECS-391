import java.util.PriorityQueue;
import java.util.*;

public class AStarSolver {
  public static String[] directions = {"up", "down", "left", "right"};
  public static PriorityQueue<PuzzleSolver> pQueue = new PriorityQueue<PuzzleSolver>(100, new BoardComparator());
  public static Hashtable<String, int[][]> table = new Hashtable<String, int[][]>();
  public static boolean goalFound = false;
  
  public static void solve(String heuristic, PuzzleSolver node) {
    String key = hashKey(node.board);
    table.put(key, node.board);
    pQueue.add(node);
    //System.out.println(isGoalState(node.board));
    while (pQueue.size() != 0 && goalFound == false) {
      PuzzleSolver currentNode = pQueue.poll();
      if (!isGoalState(currentNode.board)) {
        expandNode(currentNode);
      }
      else {
        System.out.println("Heuristic: " + currentNode.heuristic);
        System.out.println("Path: " + currentNode.path);
        System.out.println("COMPLETE");
      }
    }
  }
  
  public static void expandNode(PuzzleSolver node) {
    for (int i = 0; i < 4; i++) {
      int[][] currentBoard = node.board;
      int[][] currentBoardCopy = currentBoard;
      if (validMove(directions[i], currentBoardCopy)) {
        int[][] boardPosition = starMove(directions[i], node.board);
        if (!alreadyExpanded(boardPosition)) { 
          PuzzleSolver unexploredNode = new PuzzleSolver(boardPosition, 
                                                       node.heuristic, 
                                                       //node.path + directions[i]);
                                                         directions[i]);
          pQueue.add(unexploredNode);
          String key = hashKey(boardPosition);
          table.put(key, boardPosition);
        }
      }
    }
  }
  
  public static boolean validMove(String direction, int[][] puzzle) {
    int row = getBlankRow(puzzle);
    int column = getBlankColumn(puzzle);
    if (direction.equals("up")) {
      if (row == 1 || row == 2) {
        return true;
      }
    }
    else if (direction.equals("down")) {
      if (row == 0 || row == 1) {
        return true;
      }
    }
    else if (direction.equals("left")) {
      if (column == 1 || column == 2) {
        return true;
      }
    }
    else if (direction.equals("right")) {
      if (column == 0 || column == 1) {
        return true;                            
      }
    }
    else {
      return false;
    }
    return false;
  }
  
  
  public static int getBlankColumn(int[][] puzzle) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (puzzle[i][j] == 0) {
          return j;
        }
      }
    }
    return -1;
  }
  
  public static int getBlankRow(int[][] puzzle) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (puzzle[i][j] == 0) {
          return i;
        }
      }
    }
    return -1;
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
  
  public static int[][] starMove(String direction, int[][] board) {
    int[][] result = new int[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        result[i][j] = board[i][j];
      }
    }
    int column = getBlankColumn(board);
    int row = getBlankRow(board);
    if (direction.equals("up")) {
      if (row == 1 || row == 2) {
        result[row][column] = board[row-1][column];
        result[row-1][column] = 0;
      }
    }
    else if (direction.equals("down")) {
      if (row == 0 || row == 1) {
        result[row][column] = board[row+1][column];
        result[row+1][column] = 0;
      }
    }
    else if (direction.equals("left")) {
      if (column == 1 || column == 2) {
        result[row][column] = board[row][column-1];
        result[row][column-1] = 0;
      }
    }
    else if (direction.equals("right")) {
      if (column == 0 || column == 1) {
        result[row][column] = board[row][column+1];
        result[row][column+1] = 0;                             
      }
    }
    else {
     System.out.println("Enter a valid direction to move"); 
    }
    return result;
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
    return table.containsValue(board);
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