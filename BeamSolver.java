import java.util.PriorityQueue;
import java.util.*;
public class BeamSolver {
    
  public static String[] directions = {"up", "down", "left", "right"};
  public static PriorityQueue<PuzzleSolver> beamQueue = new PriorityQueue<PuzzleSolver>(10000000, new BoardComparator());
  public static PriorityQueue<PuzzleSolver> beamQueueTwo = new PriorityQueue<PuzzleSolver>(10000000, new BoardComparator());
  public static HashSet<int[][]> beamSet = new HashSet<int[][]>();
  public static boolean puzzleSolved = false;

  //
  public static void solve(int k, PuzzleSolver node) {
    node.heuristic = Util.h2(node.board);
    //clear the priority queue and hash table so we know they are empty
    beamSet.clear();
    beamQueue.clear();
    beamQueueTwo.clear();
    puzzleSolved = false;
    beamSet.add(node.board);
    beamQueue.add(node);

    while (puzzleSolved == false) {
    //keep picking a node to expand from the priority queue until the puzzle is solved
      while (beamQueue.size() != 0) {
        PuzzleSolver currentNode = beamQueue.poll();
        if (!isGoalState(currentNode.board)) {
          expandNode(currentNode);
        }
        else {
          System.out.println("Path: " + currentNode.path);
          System.out.println("COMPLETE");
        }
      }
      //take k best nodes from beamQueueTwo and put them in beamQueue
      int i = 0;
      while (i < k) {
        PuzzleSolver cNode = beamQueueTwo.poll();
        if (cNode != null) {
          beamQueue.add(cNode);
        }
        i++;
      }
      beamQueueTwo.clear();
    } 
  }
  
  //When expanding a node, only add its successor to the queue if it has not already been examined
  public static void expandNode(PuzzleSolver node) {
    for (int i = 0; i < 4; i++) {
      int[][] currentBoard = node.board;
      if (Util.validMove(directions[i], currentBoard)) {
        int[][] boardPosition = Util.solveMove(directions[i], currentBoard);
        if (!alreadyExpanded(boardPosition)) { 
          StringBuilder path = new StringBuilder();
          path.append(node.path).append(" ").append(directions[i].substring(0,1));
          int heuris = 0;
          heuris = Util.h2(boardPosition);
          PuzzleSolver unexploredNode = new PuzzleSolver(boardPosition, 
                                                         heuris, 
                                                         path.toString());
          beamQueueTwo.add(unexploredNode);
          beamSet.add(boardPosition);
        }
      }
    }
  }
  
  public static boolean alreadyExpanded (int[][] board) {
    return beamSet.contains(board);
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
    puzzleSolved = true;
    return true;
  }
  
}