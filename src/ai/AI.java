/*
package ai;

import game.Board;

import java.util.EmptyStackException;

public class AIPlayer {
    private final char aiDisc;
    private final char humanDisc;
    private static final int maxDepth = 4;

    public AIPlayer(char aiDisc, char humanDisc) {
        this.aiDisc = aiDisc;
        this.humanDisc = humanDisc;
    }

    public int getBestMove(Board board) {
        int bestMove = 0;
        int bestValue = Integer.MIN_VALUE;

        for (int col = 1; col <= Board.COLUMNS; col++) {
            // Check for valid move
            if (board.isColumnAvailable(col)) {
                // Make the move
                board.addDisc(col, aiDisc);

                // Compute the value of the move
                int moveValue = minimax(board, 0, false);

                // Undo the move
                board.removeDisc(col);

                // Update the best move
                if (moveValue > bestValue) {
                    bestMove = col;
                    bestValue = moveValue;
                }
            }
        }
        return bestMove;
    }

    private int scorePosition(Board board, char playerDisc) {
        int score = 0;
        char opponentDisc = (playerDisc == aiDisc) ? humanDisc : aiDisc;

        // Score center column
        score += countCenterColumn(board, playerDisc) * 3;

        // Score Horizontal, Vertical, and Diagonal
        score += evaluateLines(board, playerDisc);

        // If opponent wins and block them
        score -= evaluateLines(board, opponentDisc);

        return score;
    }

    private int countCenterColumn(Board board, char playerDisc) {
        int centerCount = 0;
        for (int row = 0; row < Board.ROWS; row++) {
            if (board.getCell(row, Board.COLUMNS / 2) == playerDisc) {
                centerCount++;
            }
        }
        return centerCount;
    }

    private int evaluateLines(Board board, char playerDisc) {
        int score = 0;

        // Score horizontal lines
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLUMNS - 3; col++) {
                char[] window = new char[4];
                for (int i = 0; i < 4; i++) {
                    window[i] = board.getCell(row, col + i);
                }
                score += evaluateWindow(window, playerDisc);
            }
        }
        return score;
    }

    // Score a window of four cells
    private int evaluateWindow(char[] window, char playerDisc) {
        int score = 0;
        int countDisc = 0;
        int countEmpty = 0;

        for (char cell : window) {
            if (cell == playerDisc) {
                countDisc++;
            } else if (cell == Board.EMPTY_SLOT) {
                countEmpty++;
            }
        }

        // Scoring based on the number of disc and empty slots in the window
        if (countDisc == 4) {
            score += 100;
        } else if (countDisc == 3 && countEmpty == 1) {
            score += 5;
        } else if (countDisc == 2 && countEmpty == 2) {
            score += 2;
        }
        return score;
    }
    private int minimax(Board board, int depth, boolean isMaximizing) {

        if (board.checkWin(aiDisc)) {
            return 10 - depth;   // Win: positive score for AI
        } else if (board.checkWin(humanDisc)) {
            return depth - 10;   // Loss : negative score for AI
        } else if (board.isFull()) {
            return 0;  // Draw
        }
        if (depth == maxDepth) {
            return scorePosition(board, isMaximizing ? aiDisc : humanDisc);
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int col = 1; col <= Board.COLUMNS; col++) {
                // Check valid move, make the move, recurse, undo the move
                int score = minimax(board, depth + 1, false);
                bestScore = Math.max(score, bestScore);
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int col = 1; col <= Board.COLUMNS; col++) {
                // Check valid move, make the move, recurse, undo the move...
                int score = minimax(board, depth + 1, true);
                bestScore = Math.min(score, bestScore);
            }
            return bestScore;
        }
    }
}
*/
