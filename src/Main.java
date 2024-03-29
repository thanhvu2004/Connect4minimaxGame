import game.Board;
import java.util.InputMismatchException;
import java.util.Scanner;
import ai.AIPlayer;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Connect 4!");
        System.out.println("Select game mode:");
        System.out.println("1. Two Player");
        System.out.println("2. Single Player vs AI");
        int gameMode = sc.nextInt();

        AIPlayer aiPlayer = null;
        char aiDisc = 'Y';
        char currPlayer = 'R';

        switch (gameMode) {
            case 1:
                // Two Player mode
                System.out.println("Starting a game for two players...");
                break;
            case 2:
                // Single-player vs AI mode
                System.out.println("Starting a game against the AI...");
                aiPlayer = new AIPlayer(aiDisc, currPlayer);
                break;
            default:
                System.out.println("Invalid selection! Defaulting to two-player mode.");
                break;
        }

        Board board = new Board();
        boolean endGame = false;

        while (!endGame) {
            board.printBoard();
            if (gameMode == 2 && currPlayer == aiDisc) {    // AI's turn
                int aiMove = aiPlayer.getBestMove(board);
                if (aiMove != -1) {  // Ensure AI made a valid move
                    System.out.println("AI selects column " + (aiMove + 1)); // Adjust column index for user display
                    System.out.println("Board state after AI's move:");
                    board.printBoard();
                    board.addDisc(aiMove + 1, aiDisc);  // Adjust column index for board
                    System.out.println("AI (Player " + aiDisc + ") places a disc in column " + (aiMove + 1)); // Adjust column index for user display
                    currPlayer = 'R';  // Switch to human player
                } else {
                    System.out.println("AI couldn't make a move!");  // Handle case when AI can't make a move
                }
            } else {   // Human player's turn
                System.out.println("Player " + currPlayer + ", choose a column (1-7): ");
                try {
                    int column = sc.nextInt();
                    if (board.addDisc(column, currPlayer)) {
                        System.out.println("Adding " + currPlayer + "'s disc to column " + column);
                        board.printBoard();
                        if (board.checkWin(currPlayer)) {
                            endGame = true;
                            board.printBoard();  // Show final board
                            System.out.println("Player " + currPlayer + " WINS!");
                        } else if (board.isFull()) {
                            endGame = true;
                            board.printBoard();
                            System.out.println("The game is a draw!!");
                        } else {
                            currPlayer = (gameMode == 1) ? (currPlayer == 'R' ? 'Y' : 'R') : aiDisc;
                        }
                    } else {
                        System.out.println("Column is full or invalid! Please choose another one");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a number between 1 and 7");
                    sc.next();  // Clear invalid input
                }
            }
        }
        sc.close();
    }
}
