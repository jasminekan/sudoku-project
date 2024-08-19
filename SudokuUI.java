import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuUI extends JFrame {
    private static final int GRID_SIZE = 9;
    private static final int CELL_SIZE = 50;
    private int[][] solution;

    private JPanel sudokuPanel;
    private JComboBox<String> difficultyComboBox;
    private JButton generateButton;
    private JButton checkButton;

    private Timer timer;
    private JLabel timerLabel;
    private int secondsElapsed;

    private JLabel congratulationLabel;

    public SudokuUI() {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the Sudoku panel
        sudokuPanel = new JPanel();
        sudokuPanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Choose a level to start");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        sudokuPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Create the difficulty combo box
        difficultyComboBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        difficultyComboBox.setSelectedIndex(0);

        // Create the generate button
        generateButton = new JButton("Generate Puzzle");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
                int[][][] puzzleAndSolution = generateSudokuPuzzle(selectedDifficulty);
                int[][] puzzle = puzzleAndSolution[0]; // Extract the puzzle
                solution = puzzleAndSolution[1]; // Extract the solution

                // Start the timer
                timer.start();

                // Display the Sudoku puzzle
                displaySudokuPuzzle(puzzle);
            }
        });

        // Create the check button
checkButton = new JButton("Check Solution");
checkButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean isComplete = true; // Flag to track if the game is complete

        // Iterate through each cell in the Sudoku grid
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JTextField textField = (JTextField) sudokuPanel.getComponent(i * GRID_SIZE + j);
                int value;
                try {
                    value = Integer.parseInt(textField.getText());
                } catch (NumberFormatException ex) {
                    // Handle non-numeric input (optional)
                    continue; // Skip to the next cell
                }

                // Compare the value with the correct solution value
                if (value == solution[i][j]) {
                    // If the value matches, mark the cell as green
                    textField.setBackground(Color.GREEN);
                } else {
                    // If the value does not match, mark the cell as red
                    textField.setBackground(Color.RED);
                    isComplete = false; // Set the flag to false if any cell is incorrect
                }
            }
        }

        // If all cells are filled correctly, show congratulatory message
        if (isComplete) {
            timer.stop(); // Stop the timer
            showCongratulatoryMessage();
        }
    }
});

        // Initialize the congratulation label
        congratulationLabel = new JLabel("");
        congratulationLabel.setHorizontalAlignment(JLabel.CENTER);

        // Initialize the timer
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsElapsed++;
                updateTimerLabel();
            }
        });

        // Create the timer label
        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text

        // Add components to the content pane
Container contentPane = getContentPane();
contentPane.setLayout(new BorderLayout());

contentPane.add(sudokuPanel, BorderLayout.CENTER); // Add the welcome message panel

JPanel controlsPanel = new JPanel(new FlowLayout());
controlsPanel.add(difficultyComboBox);
controlsPanel.add(generateButton);
controlsPanel.add(checkButton);

contentPane.add(controlsPanel, BorderLayout.SOUTH); // Add the controls panel to the SOUTH position

contentPane.add(timerLabel, BorderLayout.NORTH); // Add the timer label to the NORTH position



        // Set the window size
        setSize(400, 200); // Adjust the size as needed
        setLocationRelativeTo(null); // Center the window
    }

    private void updateTimerLabel() {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        String timeString = String.format("Time: %02d:%02d", minutes, seconds);
        timerLabel.setText(timeString);
    }

    private int[][][] generateSudokuPuzzle(String difficulty) {
        SudokuGenerator.Difficulty enumDifficulty = null;
        switch (difficulty) {
            case "Easy":
                enumDifficulty = SudokuGenerator.Difficulty.EASY;
                break;
            case "Medium":
                enumDifficulty = SudokuGenerator.Difficulty.MEDIUM;
                break;
            case "Hard":
                enumDifficulty = SudokuGenerator.Difficulty.HARD;
                break;
            default:
                // Default to easy difficulty
                enumDifficulty = SudokuGenerator.Difficulty.EASY;
                break;
        }
        return SudokuGenerator.generatePuzzleWithSolution(enumDifficulty);
    }

    private void displaySudokuPuzzle(int[][] puzzle) {
        // Clear the Sudoku grid panel
        sudokuPanel.removeAll();
        sudokuPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));

        // Populate the Sudoku grid panel with the puzzle
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JTextField textField = new JTextField();
                textField.setEditable(true); // Make text fields editable
                textField.setHorizontalAlignment(JTextField.CENTER); // Center-align text
                textField.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE)); // Set size of text field

                // Set thicker borders for section divisions
                if ((i + 1) % 3 == 0 && (j + 1) % 3 == 0) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.BLACK));
                } else if ((i + 1) % 3 == 0) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.BLACK));
                } else if ((j + 1) % 3 == 0) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.BLACK));
                } else {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
                }

                if (puzzle[i][j] != 0) {
                    textField.setText(String.valueOf(puzzle[i][j])); // Set the cell value
                    textField.setEditable(false); // Disable editing for pre-filled cells
                }
                sudokuPanel.add(textField);
            }
        }

        // Adjust the size of the window
        int panelWidth = GRID_SIZE * CELL_SIZE;
        int panelHeight = GRID_SIZE * CELL_SIZE;
        int windowWidth = panelWidth + 50; // Adjust as needed
        int windowHeight = panelHeight + 100; // Adjust as needed
        setSize(windowWidth, windowHeight);

        // Repaint the Sudoku grid panel
        sudokuPanel.revalidate();
        sudokuPanel.repaint();
    }

    // Method to show congratulatory message with time
    private void showCongratulatoryMessage() {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        String timeTaken = String.format("%02d:%02d", minutes, seconds);
        congratulationLabel.setText("Congratulations! Your time was: " + timeTaken);
        getContentPane().removeAll();
        getContentPane().add(congratulationLabel, BorderLayout.CENTER);
        validate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SudokuUI sudokuUI = new SudokuUI();
                sudokuUI.setVisible(true);
            }
        });
    }
}
