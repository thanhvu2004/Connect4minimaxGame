package ai;

import game.Board;

public class AIPlayer {
    private final char aiDisc;
    private final char humanDisc;
    private static final int MAX_DEPTH = 4;

    public AIPlayer(char aiDisc, char humanDisc) {
        this.aiDisc = aiDisc;
        this.humanDisc = humanDisc;
    }

    public int getBestMove(Board board) {
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;
    
        for (int col = 1; col <= Board.COLUMNS; col++) {
            if (board.isColumnAvailable(col)) {
                board.addDisc(col, aiDisc); // Simulate dropping a disc
                int score = minimax(board, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                board.removeDisc(col); // Undo the move
    
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = col;
                }
            }
        }
    
        // If no valid moves are available, return -1
        return bestMove;
    }

    private int minimax(Board board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || board.isFull() || board.checkWin(aiDisc) != null || board.checkWin(humanDisc) != null) {
            return evaluateBoard(board);
        }
    
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 1; col <= Board.COLUMNS; col++) {
                if (board.isColumnAvailable(col)) {
                    board.addDisc(col, aiDisc);
                    int eval = minimax(board, depth - 1, alpha, beta, false);
                    board.removeDisc(col);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 1; col <= Board.COLUMNS; col++) {
                if (board.isColumnAvailable(col)) {
                    board.addDisc(col, humanDisc);
                    int eval = minimax(board, depth - 1, alpha, beta, true);
                    board.removeDisc(col);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break;
                }
            }
            return minEval;
        }
    }

    private int evaluateBoard(Board board) {
        // Simple evaluation: prioritize winning moves and blocking opponent's winning moves
        if (board.checkWin(aiDisc) != null) return 1000; // AI wins
        if (board.checkWin(humanDisc) != null) return -1000; // Human wins
        return 0; // Neutral evaluation
    }
}