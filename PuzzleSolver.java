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
      String state = words[1];
      setState(state);
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
        move(direction);
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
  
  public static void setState(String desiredState) {
    System.out.println("Setting state to be " + desiredState);  
  }
  
  public static void randomizeState(int randomMoves) {
    System.out.println("Making " + randomMoves + " random moves");
  }
  
  public static String printState() {
    System.out.println("Printing State");
    return "representation of board";
  }
  public static String move(String direction) {
    System.out.println("Moving in the direction of " + direction);
    return direction;
  }

  public static int maxNodes(int maxNodes) {
    System.out.println("Max nodes is " + maxNodes);
    return maxNodes;
  }
}