package game;

public class Board {
    // 2D array for the game board
    private final char[][] board;
    // Size of the board
    public static final int ROWS = 6;
    public static final int COLUMNS = 7;
    // Empty slots on the board
    public static final char EMPTY_SLOT = '.';

    public Board() {
        board = new char[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = EMPTY_SLOT;
            }
        }
    }

    public Board copy() {
        Board newBoard = new Board();
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(this.board[i], 0, newBoard.board[i], 0, COLUMNS);
        }
        return newBoard;
    }

    public void printBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("1 2 3 4 5 6 7");    // Column numbers for user reference
    }

    // Adding disc to the board
    public boolean addDisc(int column, char disc) {
        if (column < 1 || column > COLUMNS) {
            System.out.println("Column out of bounds");
            return false;
        }
        column--; // Adjust for 0-based index
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][column] == EMPTY_SLOT) {
                board[i][column] = disc;
                return true;
            }
        }
        System.out.println("Column is full");
        return false;
    }

    // Checking if the board is full
    public boolean isFull() {
        for (int j = 0; j < COLUMNS; j++) {
            if (board[0][j] == EMPTY_SLOT) {
                return false;   // At least one slot is available
            }
        }
        return true;   // No top slots available
    }

    // Win condition
    public boolean checkWin(char disc) {
        // Horizontal
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                if (board[row][col] == disc && board[row][col + 1] == disc &&
                board[row][col + 2] == disc && board[row][col + 3] == disc) {
                    return true;
                }
            }
        }

        // Vertical
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (board[row][col] == disc && board[row + 1][col] == disc &&
                board[row + 2][col] == disc && board[row + 3][col] == disc) {
                    return true;
                }
            }
        }

        // Diagonal , Asc
        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                if (board[row][col] == disc && board[row - 1][col + 1] == disc &&
                board[row - 2][col + 2] == disc && board[row - 3][col + 3] == disc) {
                    return true;
                }
            }
        }

        // Diagonal , Desc
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                if (board[row][col] == disc && board[row + 1][col + 1] == disc &&
                board[row + 2][col + 2] == disc && board[row + 3][col + 3] == disc) {
                    return true;
                }
            }
        }
        return false; // No win condition met
    }

    public boolean isGameOver() {
        return checkWin('Y') || checkWin('R') || isFull();
    }

    // Available column for a move
    public boolean isColumnAvailable(int column) {
        if (column < 1 || column > COLUMNS) {
            return false;
        }
        column--;
        return board[0][column] == EMPTY_SLOT;
    }

    // Remove last disc (Minimax backtracking)
    public void removeDisc(int column) {
        if (column < 1 || column > COLUMNS) {
            return;
        }
        column--; // Adjust for 0-based index
        for (int i = 0; i < ROWS; i++) {
            if (board[i][column] != EMPTY_SLOT) {
                board[i][column] = EMPTY_SLOT;
                return;
            }
        }
    }

    public char getCell(int row, int column) {
        return board[row][column];
    }
}