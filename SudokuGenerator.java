import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {
    private static final int SIZE = 9;
    private static final Random random = new Random();
    private static final int EASY_CLUES = 36;
    private static final int MEDIUM_CLUES = 32;
    private static final int HARD_CLUES = 28;

    public static int[][][] generatePuzzleWithSolution(Difficulty difficulty) {
        int[][] puzzle = generatePuzzle(difficulty);
        int[][] solution = new int[SIZE][SIZE];
        solveSudoku(puzzle, solution);
        removeNumbers(puzzle, difficulty);
        return new int[][][]{puzzle, solution};
    }

    public static int[][] generatePuzzle(Difficulty difficulty) {
        int[][] grid = createEmptyGrid();
        generateRandomGrid(grid);
        removeNumbers(grid, difficulty);
        return grid;
    }

    public static int[][] createEmptyGrid() {
        return new int[SIZE][SIZE];
    }

    public static void generateRandomGrid(int[][] grid) {
        solveSudoku(grid, new int[SIZE][SIZE]);
    }

    public static void solveSudoku(int[][] puzzle, int[][] solution) {
        solveSudokuRecursive(puzzle, solution, 0, 0);
    }

    private static boolean solveSudokuRecursive(int[][] puzzle, int[][] solution, int row, int col) {
        if (row == SIZE) {
            return true;
        }
        if (col == SIZE) {
            return solveSudokuRecursive(puzzle, solution, row + 1, 0);
        }
        if (puzzle[row][col] != 0) {
            solution[row][col] = puzzle[row][col];
            return solveSudokuRecursive(puzzle, solution, row, col + 1);
        }
        List<Integer> numbers = new ArrayList<>();
        for (int num = 1; num <= SIZE; num++) {
            numbers.add(num);
        }
        Collections.shuffle(numbers);
        for (int num : numbers) {
            if (isValid(puzzle, row, col, num)) {
                puzzle[row][col] = num;
                solution[row][col] = num;
                if (solveSudokuRecursive(puzzle, solution, row, col + 1)) {
                    return true;
                }
                puzzle[row][col] = 0;
                solution[row][col] = 0;
            }
        }
        return false;
    }

    public static boolean isValid(int[][] grid, int row, int col, int num) {
        // Check row, column, and box constraints
        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] == num || grid[i][col] == num || grid[row - row % 3 + i / 3][col - col % 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }

    private static void removeNumbers(int[][] grid, Difficulty difficulty) {
        int targetClues = getTargetClues(difficulty);
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);
        for (int pos : positions) {
            int row = pos / SIZE;
            int col = pos % SIZE;
            int backup = grid[row][col];
            grid[row][col] = 0;
            if (!hasUniqueSolution(grid) || countClues(grid) < targetClues) {
                grid[row][col] = backup;
            }
            if (countClues(grid) == targetClues) {
                break;
            }
        }
    }

    public static boolean hasUniqueSolution(int[][] grid) {
        return countSolutions(grid) == 1;
    }

    public static int countSolutions(int[][] grid) {
        return countSolutionsRecursive(grid);
    }

    private static int countSolutionsRecursive(int[][] grid) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(grid, row, col, num)) {
                            grid[row][col] = num;
                            if (countSolutionsRecursive(grid) > 1) {
                                grid[row][col] = 0;
                                return 2; // More than one solution
                            }
                            grid[row][col] = 0;
                        }
                    }
                    return 1; // Found a solution
                }
            }
        }
        return 0; // No solution found
    }

    public static int countClues(int[][] grid) {
        int count = 0;
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private static int getTargetClues(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return EASY_CLUES;
            case MEDIUM:
                return MEDIUM_CLUES;
            case HARD:
                return HARD_CLUES;
            default:
                return EASY_CLUES;
        }
    }

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
}
