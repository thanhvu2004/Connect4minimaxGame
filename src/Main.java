import ai.AIPlayer;
import game.Board;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final int CELL_SIZE = 80;

    private Board board;
    private char currentPlayerDisc = 'R';
    private AIPlayer aiPlayer = null;
    private boolean isSinglePlayer = false;

    @Override
    public void start(Stage primaryStage) {
        // Set the application icon
        System.out.println(getClass().getResource("/resources/icon.png"));
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/resources/icon.png")));

        // Create the start screen
        VBox startScreen = new VBox(20);
        startScreen.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Title
        javafx.scene.text.Text title = new javafx.scene.text.Text("Connect 4 Game");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-margin-bottom: 20px;");

        Button playWithHumanButton = new Button("Play with Human");
        Button playWithAIButton = new Button("Play with AI");

        // Footer
        javafx.scene.text.Text footer = new javafx.scene.text.Text("By Thanh Vu Le - 2025");
        footer.setStyle("-fx-font-size: 12px; -fx-margin-top: 20px;");

        // website
        javafx.scene.text.Text website = new javafx.scene.text.Text("https://www.thanhhvu.com");
        website.setStyle("-fx-font-size: 12px;");

        // Set button actions
        playWithHumanButton.setOnAction(e -> {
            isSinglePlayer = false; // Set to two-player mode
            showGameScreen(primaryStage);
        });

        playWithAIButton.setOnAction(e -> {
            isSinglePlayer = true; // Set to single-player mode
            aiPlayer = new AIPlayer('Y', 'R'); // Initialize AI player
            showGameScreen(primaryStage);
        });

        startScreen.getChildren().addAll(title, playWithHumanButton, playWithAIButton, footer, website);

        Scene startScene = new Scene(startScreen, COLUMNS * CELL_SIZE, (ROWS + 1) * CELL_SIZE);
        primaryStage.setTitle("Connect 4");
        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    private void showGameScreen(Stage primaryStage) {
        GridPane grid = new GridPane();
    
        // Pass the GridPane to the Board constructor
        board = new Board(grid);
    
        // Create the game board UI
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Circle cell = new Circle(CELL_SIZE / 2 - 5);
                cell.setFill(Color.LIGHTGRAY);
                cell.setStroke(Color.BLACK);
                grid.add(cell, col, row);
            }
        }
    
        // Add buttons for each column
        StackPane stackPane = new StackPane(); // Wrap the grid in a StackPane
        for (int col = 0; col < COLUMNS; col++) {
            Button dropButton = new Button("Drop");
            dropButton.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5px 10px;");
            dropButton.setPrefWidth(CELL_SIZE); // Match button width to cell size
            dropButton.setPrefHeight(30); // Set button height
            int column = col; // Capture column index for event handler
            dropButton.setOnAction(e -> handleMove(column, grid, stackPane, primaryStage)); // Pass stackPane
            GridPane.setHalignment(dropButton, javafx.geometry.HPos.CENTER); // Center the button horizontally
            grid.add(dropButton, col, ROWS);
        }
    
        // Add a Restart button
        Button restartButton = new Button("Restart");
        restartButton.setStyle("-fx-font-size: 14px; -fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 5px 10px;");
        restartButton.setOnAction(e -> restartGame(primaryStage)); // Restart the game when clicked
        GridPane.setHalignment(restartButton, javafx.geometry.HPos.CENTER); // Center the button horizontally
        grid.add(restartButton, COLUMNS / 2, ROWS + 1); // Add the Restart button below the grid
    
        // Center the entire grid
        grid.setAlignment(javafx.geometry.Pos.CENTER);
    
        // Wrap the grid in a StackPane to allow overlaying
        stackPane.getChildren().add(grid);
    
        Scene gameScene = new Scene(stackPane, COLUMNS * CELL_SIZE, (ROWS + 2) * CELL_SIZE); // Adjust height for the Restart button
        primaryStage.setScene(gameScene);
    }

    private void handleMove(int column, GridPane grid, StackPane stackPane, Stage primaryStage) {
        int row = board.addDisc(column + 1, currentPlayerDisc); // Add disc to the board
        if (row != -1) {
            // Update the UI
            Circle cell = (Circle) getNodeFromGridPane(grid, column, row);
            cell.setFill(currentPlayerDisc == 'R' ? Color.RED : Color.YELLOW);
    
            // Check for win or draw
            int[] winningLine = board.checkWin(currentPlayerDisc);
            if (winningLine != null) {
                highlightWinningDiscs(grid, winningLine); // Highlight the winning discs
                showWinnerPopup("Player " + (currentPlayerDisc == 'R' ? "Red" : "Yellow") + " WINS!", primaryStage);
                disableButtons(grid);
            } else if (board.isFull()) {
                showWinnerPopup("The game is a draw!", primaryStage);
                disableButtons(grid);
            } else {
                // Switch player
                currentPlayerDisc = (currentPlayerDisc == 'R') ? 'Y' : 'R';
    
                // If single-player mode and AI's turn
                if (isSinglePlayer && currentPlayerDisc == 'Y') {
                    handleAIMove(grid, stackPane, primaryStage);
                }
            }
        } else {
            System.out.println("Column is full! Choose another column.");
        }
    }

    private void handleAIMove(GridPane grid, StackPane stackPane, Stage primaryStage) {
        int aiMove = aiPlayer.getBestMove(board);
    
        if (aiMove == -1) {
            System.out.println("AI has no valid moves. The game is a draw!");
            disableButtons(grid);
            showWinnerPopup("The game is a draw!", primaryStage);
            return;
        }
    
        handleMove(aiMove - 1, grid, stackPane, primaryStage);
    }

    private void disableButtons(GridPane grid) {
        for (int col = 0; col < COLUMNS; col++) {
            Button button = (Button) getNodeFromGridPane(grid, col, ROWS);
            button.setDisable(true);
        }
    }

    private void highlightWinningDiscs(GridPane grid, int[] winningLine) {
        int startRow = winningLine[0];
        int startCol = winningLine[1];
        int endRow = winningLine[2];
        int endCol = winningLine[3];

        // Determine the direction of the winning line
        int rowStep = Integer.compare(endRow, startRow); // 1, -1, or 0
        int colStep = Integer.compare(endCol, startCol); // 1, -1, or 0

        // Highlight all discs in the winning line
        int currentRow = startRow;
        int currentCol = startCol;
        while (currentRow != endRow + rowStep || currentCol != endCol + colStep) {
            Circle cell = (Circle) getNodeFromGridPane(grid, currentCol, currentRow);
            if (cell != null) {
                // Get the original color of the disc
                Color originalColor = (Color) cell.getFill();

                // Create a radial gradient with half green and half the original color
                RadialGradient gradient = new RadialGradient(
                    0, 0, 0.5, 0.5, 0.5, true, // Center and radius
                    CycleMethod.NO_CYCLE,
                    new Stop(0.5, originalColor), // Original color for the first half
                    new Stop(1.0, Color.GREEN)    // Green for the second half
                );

                // Apply the gradient to the disc
                cell.setFill(gradient);
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
    }

    private javafx.scene.Node getNodeFromGridPane(GridPane grid, int col, int row) {
        for (javafx.scene.Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    private void showWinnerPopup(String message, Stage primaryStage) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
    
        // Add a "Restart" button
        javafx.scene.control.ButtonType restartButton = new javafx.scene.control.ButtonType("Restart");
        alert.getButtonTypes().setAll(restartButton, javafx.scene.control.ButtonType.CLOSE);
    
        // Show the alert and handle the user's choice
        alert.showAndWait().ifPresent(response -> {
            if (response == restartButton) {
                restartGame(primaryStage); // Pass the primaryStage directly
            }
        });
    }

    private void restartGame(Stage primaryStage) {
        start(primaryStage); // Call the start method to show the start screen
    }

    public static void main(String[] args) {
        launch(args);
    }
}