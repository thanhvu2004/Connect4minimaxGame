package ai;

import game.Board;

public class AIPlayer {
    private final char aiDisc;
    private final char humanDisc;
    private static final int MAX_DEPTH = 4;
    private static final int WIN_SCORE = 10000;
    private static final int THREE_IN_A_ROW_SCORE = 400;
    private static final int TWO_IN_A_ROW_SCORE = 10;

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
            if (boardCopy.isColumnAvailable(col + 1)) {
                boardCopy.addDisc(col + 1, aiDisc);
                int score = alphabeta(boardCopy, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
                boardCopy.removeDisc(col + 1);
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
        if (depth == 0 || board.isGameOver()) {
            return scorePosition(board);
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < Board.COLUMNS; col++) {
                if (board.isColumnAvailable(col + 1)) {
                    board.addDisc(col + 1, aiDisc);
                    int eval = alphabeta(board, depth - 1, alpha, beta, false);
                    board.removeDisc(col + 1);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < Board.COLUMNS; col++) {
                if (board.isColumnAvailable(col + 1)) {
                    board.addDisc(col + 1, humanDisc);
                    int eval = alphabeta(board, depth - 1, alpha, beta, true);
                    board.removeDisc(col + 1);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break;
                }
            }
            return minEval;
        }
    }

    public static int evaluateBoardForPlayer(Board board, char player) {
        int score = 0;

        // Check for 4 in a row, 3 in a row, and 2 in a row.
        score += checkLines(board, player, 4) * WIN_SCORE;
        score += checkLines(board, player, 3) * THREE_IN_A_ROW_SCORE;
        score += checkLines(board, player, 2) * TWO_IN_A_ROW_SCORE;

        return score;
    }

    private static int checkLines(Board board, char player, int length) {
        int count = 0;

        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLUMNS; col++) {
                if (col + length <= Board.COLUMNS) { // Check horizontal
                    boolean match = true;
                    for (int i = 0; i < length; i++) {
                        if (board.getCell(row, col + i) != player) {
                            match = false;
                            break;
                        }
                    }
                    if (match) count++;
                }
                if (row + length <= Board.ROWS) { // Check vertical
                    boolean match = true;
                    for (int i = 0; i < length; i++) {
                        if (board.getCell(row + i, col) != player) {
                            match = false;
                            break;
                        }
                    }
                    if (match) count++;
                }
                if (row + length <= Board.ROWS && col + length <= Board.COLUMNS) { // Check diagonal (\)
                    boolean match = true;
                    for (int i = 0; i < length; i++) {
                        if (board.getCell(row + i, col + i) != player) {
                            match = false;
                            break;
                        }
                    }
                    if (match) count++;
                }
                if (row + length <= Board.ROWS && col - length + 1 >= 0) { // Check anti-diagonal (/)
                    boolean match = true;
                    for (int i = 0; i < length; i++) {
                        if (board.getCell(row + i, col - i) != player) {
                            match = false;
                            break;
                        }
                    }
                    if (match) count++;
                }
            }
        }

        return count;
    }

    private int scorePosition(Board board) {
        int playerScore = evaluateBoardForPlayer(board, aiDisc);
        int opponentScore = evaluateBoardForPlayer(board, humanDisc);
        return playerScore - opponentScore;
    }
}
