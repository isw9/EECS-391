class Util {

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