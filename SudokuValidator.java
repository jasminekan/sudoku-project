public class SudokuValidator {
    private static final int SIZE = 9;

    // Method to check the validity of a Sudoku solution
    public static boolean isValidSolution(int[][] solution) {
        return solve(solution);
    }

    // Method to solve the Sudoku puzzle using backtracking
    private static boolean solve(int[][] grid) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValidMove(grid, row, col, num)) {
                            grid[row][col] = num;
                            if (solve(grid)) {
                                return true;
                            }
                            grid[row][col] = 0; // Backtrack
                        }
                    }
                    return false; // No valid move found
                }
            }
        }
        return true; // All cells filled (solution found)
    }

    // Method to check if a move is valid
    private static boolean isValidMove(int[][] grid, int row, int col, int num) {
        return isValidInRow(grid, row, num) &&
               isValidInColumn(grid, col, num) &&
               isValidInSubgrid(grid, row - row % 3, col - col % 3, num);
    }

    // Method to check if a number is valid in the given row
    private static boolean isValidInRow(int[][] grid, int row, int num) {
        for (int col = 0; col < SIZE; col++) {
            if (grid[row][col] == num) {
                return false;
            }
        }
        return true;
    }

    // Method to check if a number is valid in the given column
    private static boolean isValidInColumn(int[][] grid, int col, int num) {
        for (int row = 0; row < SIZE; row++) {
            if (grid[row][col] == num) {
                return false;
            }
        }
        return true;
    }

    // Method to check if a number is valid in the given 3x3 subgrid
    private static boolean isValidInSubgrid(int[][] grid, int startRow, int startCol, int num) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (grid[row + startRow][col + startCol] == num) {
                    return false;
                }
            }
        }
        return true;
    }
}