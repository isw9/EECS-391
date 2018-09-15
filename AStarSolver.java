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
      node.heuristic = h1(node.board);
    }
    else if (heuristic.equals("h2")) {
      node.heuristic = h2(node.board);
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
        System.out.println("Heuristic: " + currentNode.heuristic);
        System.out.println("Path: " + currentNode.path.substring(2));
        System.out.println("Path: " + currentNode.path);
        System.out.println("COMPLETE");
      }
    }
  }
  
  //When expanding a node, only add its successor to the queue if it has not already been examined
  public static void expandNode(PuzzleSolver node, String heuristic) {
    for (int i = 0; i < 4; i++) {
      int[][] currentBoard = node.board;
      if (validMove(directions[i], currentBoard)) {
        int[][] boardPosition = starMove(directions[i], currentBoard);
        if (!alreadyExpanded(boardPosition)) { 
          StringBuilder path = new StringBuilder();
          path.append(node.path).append(",").append(directions[i].substring(0,1));
          int heuris = 0;
          if (heuristic.equals("h1")) {
            heuris = h1(boardPosition);
          }
          else {
            heuris = h2(boardPosition);
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
  
  public static boolean validMove(String direction, int[][] puzzle) {
    int row = getRow(puzzle, 0);
    int column = getColumn(puzzle, 0);
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
  
  
  public static int getColumn(int[][] puzzle, int target) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (puzzle[i][j] == target) {
          return j;
        }
      }
    }
    return -1;
  }
  
  public static int getRow(int[][] puzzle, int target) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (puzzle[i][j] == target) {
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
    int column = getColumn(board, 0);
    int row = getRow(board, 0);
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
    return set.contains(board);
  }
  
  public static int h2(int[][] board) {
    int count = 0;
    int expected = 0;

    int[][] finalBoard = { {0, 1, 2}, {3, 4, 5}, {6, 7, 8} };
    
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        int value = board[i][j];
        expected++;
        if (value != 0 && value != expected) {
          count = count + Math.abs(i - getRow(finalBoard, value)) + Math.abs(j- getColumn(finalBoard, value));
        }
       }
     }
    return count;
  }
  
  public static int h1(int[][] board) {
    int misplacedTiles = 0;
    int goalTile = 0;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (board[i][j] != goalTile) {
          misplacedTiles++;
        }
      }
      goalTile++;
    }
    return misplacedTiles;
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