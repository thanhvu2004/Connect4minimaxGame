package ai;

import game.Board;

public class AIPlayer {
    private final char aiDisc;
    private final char humanDisc;
    private static final int maxDepth = 4;

    public AIPlayer(char aiDisc, char humanDisc) {
        this.aiDisc = aiDisc;
        this.humanDisc = humanDisc;
    }

    public int getBestMove(Board board) {
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;

        for (int col = 0; col < Board.COLUMNS; col++) {
            Board boardCopy = board.copy();
            if (boardCopy.isColumnAvailable(col)) {
                boardCopy.addDisc(col, aiDisc);
                int score = alphabeta(boardCopy, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                boardCopy.removeDisc(col);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = col;
                }
            }
        }
        return bestMove;
    }

    private int alphabeta(Board board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || board.isGameOver()) { // Corrected base case condition
            return scorePosition(board, maximizingPlayer ? aiDisc : humanDisc);
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < Board.COLUMNS; col++) {
                if (board.isColumnAvailable(col)) {
                    board.addDisc(col, aiDisc);
                    int eval = alphabeta(board, depth - 1, alpha, beta, false);
                    board.removeDisc(col);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break; // Beta cut-off
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < Board.COLUMNS; col++) {
                if (board.isColumnAvailable(col)) {
                    board.addDisc(col, humanDisc);
                    int eval = alphabeta(board, depth - 1, alpha, beta, true);
                    board.removeDisc(col);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break; // Alpha cut-off
                }
            }
            return minEval;
        }
    }

    private int evaluateWindow(char[] window, char playerDisc) {
        int score = 0;
        int playerCount = 0;
        int opponentCount = 0;
        int emptyCount = 0;

        for (char cell : window) {
            if (cell == playerDisc) {
                playerCount++;
            } else if (cell == Board.EMPTY_SLOT) {
                emptyCount++;
            } else {
                opponentCount++;
            }
        }

        if (playerCount == 4) {
            score += 100;
        } else if (playerCount == 3 && emptyCount == 1) {
            score += 10;
        } else if (playerCount == 2 && emptyCount == 2) {
            score += 5;
        }

        // Adjust penalties for opponent's potential moves
        if (opponentCount == 3 && emptyCount == 1) {
            score -= 50; // Opponent has 3 in a row, block it
        } else if (opponentCount == 2 && emptyCount == 2) {
            score -= 10; // Opponent has 2 in a row, slightly prioritize blocking
        }

        return score;
    }
    private int scorePosition(Board board, char playerDisc) {
        int score = 0;
        // Score center column more highly, as it offers more opportunities for winning
        for (int row = 0; row < Board.ROWS; row++) {
            if (board.getCell(row, Board.COLUMNS / 2) == playerDisc) {
                score += 3; // Arbitrary value to prefer center column
            }
        }

        // Horizontal score
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLUMNS - 3; col++) {
                char[] window = new char[4];
                for (int k = 0; k < 4; k++) {
                    window[k] = board.getCell(row, col + k);
                }
                score += evaluateWindow(window, playerDisc);
            }
        }

        // Vertical score
        for (int col = 0; col < Board.COLUMNS; col++) {
            for (int row = 0; row < Board.ROWS - 3; row++) {
                char[] window = new char[4];
                for (int k = 0; k < 4; k++) {
                    window[k] = board.getCell(row + k, col);
                }
                score += evaluateWindow(window, playerDisc);
            }
        }

        // Ascending Diagonal score
        for (int row = 3; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLUMNS - 3; col++) {
                char[] window = new char[4];
                for (int k = 0; k < 4; k++) {
                    window[k] = board.getCell(row - k, col + k);
                }
                score += evaluateWindow(window, playerDisc);
            }
        }

        // Descending Diagonal score
        for (int row = 0; row < Board.ROWS - 3; row++) {
            for (int col = 0; col < Board.COLUMNS - 3; col++) {
                char[] window = new char[4];
                for (int k = 0; k < 4; k++) {
                    window[k] = board.getCell(row + k, col + k);
                }
                score += evaluateWindow(window, playerDisc);
            }
        }

        return score;
    }
}