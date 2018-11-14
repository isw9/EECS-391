import java.util.Scanner;
import java.util.Queue;
import java.util.Arrays;
import java.util.Random;
import java.io.*;

public class Node {
  // global representation of the current state of the puzzle we want to solve
  public static int[][] puzzle = new int[3][3];
  //start the maxNodes as really high if we are not explicitly given a max number of nodes to consider
  public static int globalMaxNodes = 100000000;
  
  public static Random rnd = new Random();


  public int[][] board = new int[3][3];
  public int heuristic;
  public String path;
  int costSoFar;

  // A node will consist of a "board", a heuristic value,
  // the path that was taken to get to that Node, and the total number of moved made so far to get to the current state
  public Node(int[][] board, int heuristic, String path, int costSoFar) {
    this.board = board;
    this.heuristic = heuristic;
    this.path = path;
    this.costSoFar = costSoFar;
  }

  public static void main(String[] args) throws FileNotFoundException {

    rnd.setSeed(10);
    Scanner scanner = new Scanner(System.in);
    File file = new File(args[0].toString());
    Scanner fileScanner = new Scanner(file);
    while (fileScanner.hasNextLine()) {
      String command = fileScanner.nextLine();
      grabInput(command);
    }
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
      Node board = new Node(puzzle, 0, "", 0);
      AStarSolver.solve(heuristic, board, globalMaxNodes);
    }
    else if (input.startsWith("solve beam")) {
      int states = Integer.parseInt(words[2]);
      Node board = new Node(puzzle, 0, "", 0);
      BeamSolver.solve(states, board);
    }
    else if (input.startsWith("maxNodes")) {
      int maxNodes = Integer.parseInt(words[1]);
      maxNodes(maxNodes);
    }
    else if (input.startsWith("experiment_a")) {
      experiment_a();
    }
    else if (input.startsWith("experiment_b")) {
      experiment_b();
    }
    else if (input.startsWith("experiment_c")) {
      experiment_c();
    }
    else if (input.startsWith("experiment_d")) {
      experiment_d();
    }
    else {
      System.out.println("Please enter a valid command");
    }
  }

  // sets the global puzzle as anything we desire
  // String must follow format "b12345678"
  public static int[][] setState(String desiredState) {
    desiredState = desiredState.replace('b', '0');
    String[] integersAsText = desiredState.split("");

    puzzle[0][0] = Integer.parseInt(integersAsText[0]);
    puzzle[0][1] = Integer.parseInt(integersAsText[1]);
    puzzle[0][2] = Integer.parseInt(integersAsText[2]);
    puzzle[1][0] = Integer.parseInt(integersAsText[3]);
    puzzle[1][1] = Integer.parseInt(integersAsText[4]);
    puzzle[1][2] = Integer.parseInt(integersAsText[5]);
    puzzle[2][0] = Integer.parseInt(integersAsText[6]);
    puzzle[2][1] = Integer.parseInt(integersAsText[7]);
    puzzle[2][2] = Integer.parseInt(integersAsText[8]);
    return puzzle;
  }

  // Randomly moves the blank tile N spots starting from the goal state
  public static void randomizeState(int randomMoves) {
    String[] possibleDirections = {"up", "down", "left", "right"};
    for (int i = 0; i < randomMoves; i++) {
      String direction = getRandomDirection(possibleDirections);
      move(direction, puzzle);
    }
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
    int column = findBlankColumn(board);
    int row = findBlankRow(board);
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
  public static void maxNodes(int maxNodes) {
    globalMaxNodes = maxNodes;
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
  
  // experiment_a tests
  public static void experiment_a() {
    for (int j = 5; j <= 100; j = j + 5) {
      int solveCounter = 0;
      int failCounter = 0;
      for (int i = 5; i < 15; i++) {
        int counter = 0;
        maxNodes(j);
        while (counter < 5) {
          setState("b12345678");
          randomizeState(i);
          Node board = new Node(puzzle, 0, "", 0);
          if (AStarSolver.solve("h1", board, globalMaxNodes) > 0) {
            solveCounter++;
          }
          else {
            failCounter++;
          }
          if (AStarSolver.solve("h2", board, globalMaxNodes) > 0) {
            solveCounter++;
          }
          else {
            failCounter++;
          }
          counter++;
        }
      }
        System.out.println("Max Nodes was: " + j);
        System.out.println("Successful: " + solveCounter);
        System.out.println("Failure: " + failCounter);
    }
  }
  
  // experiment_b tests  
  public static void experiment_b() {
    for (int j = 5; j <= 100; j = j + 5) {
      int solveCounterH1 = 0;
      int failCounterH1 = 0;
      int solveCounterH2 = 0;
      int failCounterH2 = 0;
      for (int i = 5; i < 15; i++) {
        int counter = 0;
        maxNodes(j);
        while (counter < 5) {
          setState("b12345678");
          randomizeState(i);
          Node board = new Node(puzzle, 0, "", 0);
          if (AStarSolver.solve("h1", board, globalMaxNodes) > 0) {
            solveCounterH1++;
          }
          else {
            failCounterH1++;
          }
          if (AStarSolver.solve("h2", board, globalMaxNodes) > 0) {
            solveCounterH2++;
          }
          else {
            failCounterH2++;
          }
          counter++;
        }
      }
        System.out.println("Max Nodes was: " + j);
        System.out.println("Successful H1: " + solveCounterH1);
        System.out.println("Failure H1: " + failCounterH1);
        System.out.println("Successful H2: " + solveCounterH2);
        System.out.println("Failure H2: " + failCounterH2);
    }   
  }
  
  // experiment_c tests
  public static void experiment_c() {
   double solveCounterH1 = 0;
   double h1Total = 0;
   double solveCounterH2 = 0;
   double h2Total = 0;
   double beamCounter = 0;
   double beamTotal = 0;
    for (int j = 5; j <= 100; j = j + 5) {
      for (int i = 5; i < 15; i++) {
        int counter = 0;
        maxNodes(j);
        while (counter < 5) {
          setState("b12345678");
          randomizeState(i);
          Node board = new Node(puzzle, 0, "", 0);
          int h1 = AStarSolver.solve("h1", board, globalMaxNodes);
          if (h1 > 0) {
            solveCounterH1++;
            h1Total = h1Total + h1;
          }
          int h2 = AStarSolver.solve("h2", board, globalMaxNodes);
          if (h2 > 0) {
            solveCounterH2++;
            h2Total = h2Total + h2;
          }
          int beam = BeamSolver.solve(4, board);
          if (beam > 0) {
            beamCounter++;
            beamTotal = beamTotal + beam;
          }
          counter++;
        }
      }
    }
    System.out.println("H1 average length: " + h1Total / solveCounterH1);
    System.out.println("H2 average length: " + h2Total / solveCounterH2);
    System.out.println("beam average length: " + beamTotal / beamCounter);  
  }
  
  // experiment_d tests
  public static void experiment_d() {
    int solveCounter = 0;
    int failCounter = 0;
    for (int j = 5; j <= 100; j = j + 5) {
      for (int i = 5; i < 15; i++) {
        int counter = 0;
        while (counter < 5) {
          setState("b12345678");
          randomizeState(i);
          Node board = new Node(puzzle, 0, "", 0);
          if (BeamSolver.solve(4, board) > 0) {
            solveCounter++;
          }
          else {
            failCounter++;
          }
          counter++;
        }
      }
    }
    System.out.println("Successful H1: " + solveCounter);
    System.out.println("Failure H1: " + failCounter);
  }

  // Method that randomly gets a new direction (up, down, left, or right)
  public static String getRandomDirection(String[] directions) {
    int num = rnd.nextInt(directions.length);
    return directions[num];
  }
}
