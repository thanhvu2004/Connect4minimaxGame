package ai;

import game.Board;

public class AIPlayer {
    private final char aiDisc;
    private final char humanDisc;
    private static final int MAX_DEPTH = 4;
    private static final int WINNING_LENGTH = 4;
    private static final int IMMEDIATE_WIN_SCORE = 10000;
    private static final int ONE_MOVE_WIN_SCORE = 100;
    private static final int TWO_MOVES_WIN_SCORE = 10;
    private static final int EARLY_GAME_ADVANTAGE_SCORE = 5;

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
                    if (beta <= alpha) break; // Beta cut-off
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
                    if (beta <= alpha) break; // Alpha cut-off
                }
            }
            return minEval;
        }
    }

    private int evaluateBoardForPlayer(Board board, char player) {
        int score = 0;
        int boardRows = Board.ROWS;
        int boardCols = Board.COLUMNS;

        for (int row = 0; row < boardRows; row++) {
            for (int col = 0; col < boardCols; col++) {
                for (int[] dir : new int[][]{{0, 1}, {1, 0}, {1, 1}, {1, -1}}) {
                    int countPlayerPieces = 0;
                    int countEmpty = 0;

                    for (int i = 0; i < WINNING_LENGTH; i++) {
                        int newRow = row + dir[0] * i;
                        int newCol = col + dir[1] * i;

                        if (newRow >= 0 && newRow < boardRows && newCol >= 0 && newCol < boardCols) {
                            if (board.getCell(newRow, newCol) == player) {
                                countPlayerPieces++;
                            } else if (board.getCell(newRow, newCol) == Board.EMPTY_SLOT) {
                                countEmpty++;
                            }
                        }
                    }

                    if (countPlayerPieces == WINNING_LENGTH) {
                        score += IMMEDIATE_WIN_SCORE;
                    } else if (countPlayerPieces == WINNING_LENGTH - 1 && countEmpty == 1) {
                        score += ONE_MOVE_WIN_SCORE;
                    } else if (countPlayerPieces == WINNING_LENGTH - 2 && countEmpty == 2) {
                        score += TWO_MOVES_WIN_SCORE;
                    } else if (countPlayerPieces > 0 && countEmpty == (WINNING_LENGTH - countPlayerPieces)) {
                        score += EARLY_GAME_ADVANTAGE_SCORE;
                    }
                }
            }
        }

        return score;
    }

    private int scorePosition(Board board) {
        int playerScore = evaluateBoardForPlayer(board, aiDisc);
        int opponentScore = evaluateBoardForPlayer(board, humanDisc);
        return playerScore - opponentScore;
    }
}
