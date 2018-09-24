// This class contains methods that A* search and beam search both use in an attempt to DRY up code
class Util {
  
  // Calculates h2 based on the book definition 
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
  
  // Calculates h1 based on the book defintion 
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
  
  // Gets the column that the int target is in 
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
  
  // Gets the row that the int target is in
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
 
  // Returns an int[][] that reflects the given int[][] with the "blank" moved in the appropriate direction
  public static int[][] solveMove(String direction, int[][] board) {
    int[][] result = new int[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        result[i][j] = board[i][j];
      }
    }
    int column = Util.getColumn(board, 0);
    int row = Util.getRow(board, 0);
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
  
  // Checks if a given move is valid. An example of an invalid move is trying
  // to move the blank to the left when it is already in the leftmost column
  public static boolean validMove(String direction, int[][] puzzle) {
    int row = Util.getRow(puzzle, 0);
    int column = Util.getColumn(puzzle, 0);
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
}
