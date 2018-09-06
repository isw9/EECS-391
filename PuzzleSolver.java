import java.util.Scanner;
import java.util.Queue;
import java.util.Arrays;

public class PuzzleSolver {
  
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String input = "";
    while (!input.equals("quit")) {
      input = scanner.nextLine();
      grabInput(input);
    }
  }
  
  public static void grabInput(String input) {
    String[] words = input.split("\\s+");
    if (input.startsWith("setState")) {
      String rowOne = words[1];
      String rowTwo = words[2];
      String rowThree = words[3];
      String desiredState = rowOne + rowTwo + rowThree;
      setState(desiredState);
    }
    else if (input.startsWith("randomizeState")) {
      int randomMoves = Integer.parseInt(words[1]);
      randomizeState(randomMoves);
    }
    else if (input.startsWith("printState")) {
      printState();
    }
    else if (input.startsWith("move")) {
      String direction = words[1];
      if (Arrays.asList("up", "down", "left", "right").contains(direction)) {
        int[][] puzzle = setState("b12543678");
        move(direction, puzzle);
      }
      else {
        System.out.println("Please enter a valid direction (up, down, left, or right)");
      }
    }
    else if (input.startsWith("solve A-star")) {
      String heuristic = words[2];
      AStarSolver.solve(heuristic);
    }
    else if (input.startsWith("solve beam")) {
      int states = Integer.parseInt(words[2]);
      BeamSolver.solve(states);
    }
    else if (input.startsWith("maxNodes")) {
      int maxNodes = Integer.parseInt(words[1]);
      maxNodes(maxNodes);
    }
    else {       
      System.out.println("Please enter a valid command");
    }
  }
  
  public static int[][] setState(String desiredState) {
    desiredState = desiredState.replace('b', '0');
    String[] integersAsText = desiredState.split("");
    int puzzle[][] = new int[3][3];
    int row = 0;
    int column = 0;
    for (int i = 1; i < integersAsText.length; i++) {
      if (row > 2) {
        row = 0;
      }
      column = column % 3;
      puzzle[row][column] = Integer.parseInt(integersAsText[i]);
      if (column == 2) {
        row++;
      }
      column++;
    }
    return puzzle;
  }
  
  public static void randomizeState(int randomMoves) {
    System.out.println("Making " + randomMoves + " random moves");
  }
  
  public static String printState() {
    System.out.println("Printing State");
    return "representation of board";
  }
  public static String move(String direction, int[][] puzzle) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        System.out.print(puzzle[i][j]);
      }
    }
    System.out.println("Moving in the direction of " + direction);
    int column = findBlankColumn(puzzle);
    int row = findBlankRow(puzzle);
    if (direction.equals("up")) {
      if (row == 1 || row == 2) {
        puzzle[row][column] = puzzle[row-1][column];
        puzzle[row-1][column] = 0;
      }
    }
    else if (direction.equals("down")) {
      if (row == 0 || row == 1) {
        puzzle[row][column] = puzzle[row+1][column];
        puzzle[row+1][column] = 0;
      }
    }
    else if (direction.equals("left")) {
      if (column == 1 || column == 2) {
        puzzle[row][column] = puzzle[row][column-1];
        puzzle[row][column-1] = 0;
      }
    }
    else if (direction.equals("right")) {
      if (column == 0 || column == 1) {
        puzzle[row][column] = puzzle[row][column+1];
        puzzle[row][column+1] = 0;                             
      }
    }
    else {
     System.out.println("Enter a valid direction to move"); 
    }
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        System.out.print(puzzle[i][j]);
      }
    }
    return direction;
  }

  public static int maxNodes(int maxNodes) {
    System.out.println("Max nodes is " + maxNodes);
    return maxNodes;
  }
  
  public static int findBlankColumn(int[][] puzzle) {
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        if (puzzle[i][j] == 0) {
          return j;
        }
      }
    }
    return -1;
  }
  
  public static int findBlankRow(int[][] puzzle) {
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        if (puzzle[i][j] == 0) {
          return i;
        }
      }
    }
    return -1;
  }
}