import java.util.Scanner;
import java.util.Queue;
import java.util.Arrays;
import java.util.Random;
import java.io.File;

public class Node {
  // global representation of the current state of the puzzle we want to solve
  public static int[][] puzzle = new int[3][3];
  
  public int[][] board = new int[3][3];
  public int heuristic;
  public String path;
  
  // A node will consist of a "board", a heuristic value, and the path that was taken to get to that Node
  public Node(int[][] board, int heuristic, String path) {
    this.board = board;
    this.heuristic = heuristic;
    this.path = path;
  }
  
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String input = "";
    while (!input.equals("quit")) {
      input = scanner.nextLine();
      grabInput(input);
    }
  }
  
  // decide what to do based on the inputted string
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
      setState("b12345678");
      int randomMoves = Integer.parseInt(words[1]);
      randomizeState(randomMoves);
    }
    else if (input.startsWith("printState")) {
      printState(puzzle);
    }
    else if (input.startsWith("move")) {
      String direction = words[1];
      if (Arrays.asList("up", "down", "left", "right").contains(direction)) {
        move(direction, puzzle);
      }
      else {
        System.out.println("Please enter a valid direction (up, down, left, or right)");
      }
    }
    else if (input.startsWith("solve A-star")) {
      String heuristic = words[2];
      System.out.println(heuristic);
      Node board = new Node(puzzle, 0, "");
      System.out.println(board);
      AStarSolver.solve(heuristic, board);
    }
    else if (input.startsWith("solve beam")) {
      int states = Integer.parseInt(words[2]);
      Node board = new Node(puzzle, 0, "");
      BeamSolver.solve(states, board);
    }
    else if (input.startsWith("maxNodes")) {
      int maxNodes = Integer.parseInt(words[1]);
      maxNodes(maxNodes);
    }
    else {       
      System.out.println("Please enter a valid command");
    }
  }
  
  // sets the global puzzle as anything we desire
  // String must follow format "b12 345 678"
  public static int[][] setState(String desiredState) {
    desiredState = desiredState.replace('b', '0');
    String[] integersAsText = desiredState.split("");
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
  
  // Randomly moves the blank tile N spots starting from the goal state
  public static void randomizeState(int randomMoves) {
    String[] possibleDirections = {"up", "down", "left", "right"};
    for (int i = 0; i < randomMoves; i++) {
      String direction = getRandomDirection(possibleDirections);
      move(direction, puzzle);
    }
    System.out.println("Making " + randomMoves + " random moves");
  }
  
  // Prints out a visual representation of the global board at any point in time
  public static void printState(int[][] board) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        System.out.print(board[i][j] + " ");
      }
      System.out.print("\n");
    }
  }
  
  // Method to actually move the blank tile for an inputted board
  // Note this method retuns an int[][] and does NOT modify the global
  // puzzle variable at the top of this class
  public static int[][] move(String direction, int[][] board) {
    System.out.println("Moving in the direction of " + direction);
    int column = findBlankColumn(board);
    int row = findBlankRow(board);
    System.out.println("row is: " + row);
    System.out.println("column is: " + column);
    if (direction.equals("up")) {
      if (row == 1 || row == 2) {
        board[row][column] = board[row-1][column];
        board[row-1][column] = 0;
      }
    }
    else if (direction.equals("down")) {
      if (row == 0 || row == 1) {
        board[row][column] = board[row+1][column];
        board[row+1][column] = 0;
      }
    }
    else if (direction.equals("left")) {
      if (column == 1 || column == 2) {
        board[row][column] = board[row][column-1];
        board[row][column-1] = 0;
      }
    }
    else if (direction.equals("right")) {
      if (column == 0 || column == 1) {
        board[row][column] = board[row][column+1];
        board[row][column+1] = 0;                             
      }
    }
    else {
     System.out.println("Enter a valid direction to move"); 
    }
    return board;
  }

  // Sets a maximum number of nodes to explore
  public static int maxNodes(int maxNodes) {
    System.out.println("Max nodes is " + maxNodes);
    return maxNodes;
  }
  
  // finds the column the blank is located
  public static int findBlankColumn(int[][] board) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (board[i][j] == 0) {
          return j;
        }
      }
    }
    return -1;
  }
  
  // finds the row the blank is located
  public static int findBlankRow(int[][] board) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (board[i][j] == 0) {
          return i;
        }
      }
    }
    return -1;
  }
  
  // Method that randomly gets a new direction (up, down, left, or right)
  public static String getRandomDirection(String[] directions) {
    int rnd = new Random().nextInt(directions.length);
    return directions[rnd];
  }
}