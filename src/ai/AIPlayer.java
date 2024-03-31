package ai;

import game.Board;

public class AIPlayer {
    private final char aiDisc;
    private final char humanDisc;
    private static final int maxDepth = 100;

    public AIPlayer(char aiDisc, char humanDisc) {
        this.aiDisc = aiDisc;
        this.humanDisc = humanDisc;
    }

    public int getBestMove(Board board) {
        System.out.println("\nAI is thinking...");
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;
        Board boardCopy = board.copy();
        for (int col = 0; col < Board.COLUMNS; col++) {
            if (boardCopy.isColumnAvailable(col+1)) {
                boardCopy.addDisc(col+1, aiDisc);
                int score = alphabeta(boardCopy, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
                boardCopy.removeDisc(col+1);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = col;
                }
            }
        }
        // If bestMove is still -1 here, it means all columns are full and no valid move can be made
        return bestMove;
    }

    private int alphabeta(Board board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || board.isGameOver()) { // Corrected base case condition
            return scorePosition(board, maximizingPlayer ? aiDisc : humanDisc) - scorePosition(board, maximizingPlayer ? humanDisc : aiDisc);
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < Board.COLUMNS; col++) {
                if (board.isColumnAvailable(col+1)) {
                    board.addDisc(col+1, aiDisc);
                    int eval = alphabeta(board, depth - 1, alpha, beta, false);
                    board.removeDisc(col+1);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break; // Beta cut-off
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < Board.COLUMNS; col++) {
                if (board.isColumnAvailable(col+1)) {
                    board.addDisc(col+1, humanDisc);
                    int eval = alphabeta(board, depth - 1, alpha, beta, true);
                    board.removeDisc(col+1);
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

        // Count player's discs, opponent's discs, and empty slots in the window
        for (char cell : window) {
            if (cell == playerDisc) {
                playerCount++;
            } else if (cell == Board.EMPTY_SLOT) {
                emptyCount++;
            } else {
                opponentCount++;
            }
        }

        // Assign scores based on player's potential moves
        if (playerCount == 4) {
            score += 100;
        } else if (playerCount == 3 && emptyCount == 1) {
            score += 10;
        } else if (playerCount == 2 && emptyCount == 2) {
            score += 5;
        }

        // Adjust penalties for opponent's potential moves
        if (opponentCount == 3 && emptyCount == 1) {
            score -= 80; // Opponent has 3 in a row, block it
        } else if (opponentCount == 2 && emptyCount == 2) {
            score -= 20; // Opponent has 2 in a row, slightly prioritize blocking
        }

        if (playerCount == 3 && emptyCount == 1 && window[0] == Board.EMPTY_SLOT && window[3] == Board.EMPTY_SLOT) {
            score += 100; // AI has 3 in a row with empty slots on both ends
        }
        if (opponentCount == 3 && emptyCount == 1 && window[0] == Board.EMPTY_SLOT && window[3] == Board.EMPTY_SLOT) {
            score -= 200; // Opponent has 3 in a row with empty slots on both ends, block it
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
