package game;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Board {
    private final char[][] board;
    public static final int ROWS = 6;
    public static final int COLUMNS = 7;
    public static final char EMPTY_SLOT = '.';
    private final GridPane grid;

    public Board(GridPane grid) {
        this.grid = grid;

        // Set gaps between rows and columns
        grid.setHgap(10); // Horizontal gap
        grid.setVgap(10); // Vertical gap

        board = new char[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = EMPTY_SLOT;
            }
        }
    }

    public GridPane getGrid() {
        return grid;
    }

    public int addDisc(int column, char disc) {
        if (column < 1 || column > COLUMNS) {
            System.out.println("Column out of bounds");
            return -1; // Invalid column
        }
        column--; // Adjust for 0-based index
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][column] == EMPTY_SLOT) {
                board[i][column] = disc;

                // Update the UI
                Circle cell = (Circle) getNodeFromGridPane(column, i);
                cell.setFill(disc == 'R' ? Color.RED : Color.YELLOW);
                return i; // Return the row index where the disc was added
            }
        }
        System.out.println("Column is full");
        return -1; // Column is full
    }

    public boolean isFull() {
        for (int j = 0; j < COLUMNS; j++) {
            if (board[0][j] == EMPTY_SLOT) {
                return false;
            }
        }
        return true;
    }

    public int[] checkWin(char disc) {
        // Horizontal
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                if (board[row][col] == disc && board[row][col + 1] == disc &&
                    board[row][col + 2] == disc && board[row][col + 3] == disc) {
                    return new int[]{row, col, row, col + 3}; // Start and end points
                }
            }
        }
    
        // Vertical
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (board[row][col] == disc && board[row + 1][col] == disc &&
                    board[row + 2][col] == disc && board[row + 3][col] == disc) {
                    return new int[]{row, col, row + 3, col}; // Start and end points
                }
            }
        }
    
        // Diagonal (\)
        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                if (board[row][col] == disc && board[row - 1][col + 1] == disc &&
                    board[row - 2][col + 2] == disc && board[row - 3][col + 3] == disc) {
                    return new int[]{row, col, row - 3, col + 3}; // Start and end points
                }
            }
        }
    
        // Diagonal (/)
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                if (board[row][col] == disc && board[row + 1][col + 1] == disc &&
                    board[row + 2][col + 2] == disc && board[row + 3][col + 3] == disc) {
                    return new int[]{row, col, row + 3, col + 3}; // Start and end points
                }
            }
        }
    
        return null; // No win
    }

    public boolean isColumnAvailable(int column) {
        if (column < 1 || column > COLUMNS) {
            return false;
        }
        column--;
        return board[0][column] == EMPTY_SLOT;
    }

    public void removeDisc(int column) {
        if (column < 1 || column > COLUMNS) {
            return;
        }
        column--; // Adjust for 0-based index
        for (int i = 0; i < ROWS; i++) {
            if (board[i][column] != EMPTY_SLOT) {
                board[i][column] = EMPTY_SLOT;

                // Update the UI
                Circle cell = (Circle) getNodeFromGridPane(column, i);
                cell.setFill(Color.LIGHTGRAY);
                return;
            }
        }
    }

    private javafx.scene.Node getNodeFromGridPane(int col, int row) {
        for (javafx.scene.Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}