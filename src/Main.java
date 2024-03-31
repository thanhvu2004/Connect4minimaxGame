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
        char player1Disc = 'R';
        char player2Disc = 'Y';
        String player1Name = "Player 1";
        String player2Name = "Player 2";
        int currPlayer = 0;

        switch (gameMode) {
            case 1:
                // Two Player mode
                System.out.println("Starting a game for two players...");
                System.out.print("Enter Player 1 name: ");
                player1Name = sc.next();
                System.out.print("Choose your symbol (‘R’ or ‘Y’): ");
                player1Disc = sc.next().charAt(0);
                System.out.print("Enter Player 2 name: ");
                player2Name = sc.next();
                player2Disc = (player1Disc == 'R') ? 'Y' : 'R';
                System.out.println(player2Name + " will play as " + player2Disc);
                break;
            case 2:
                // Single-player vs AI mode
                System.out.println("Starting a game against the AI...");
                System.out.print("Enter your name: ");
                player1Name = sc.next();
                System.out.print("Choose your symbol (‘R’ or ‘Y’): ");
                player1Disc = sc.next().charAt(0);
                player2Disc = (player1Disc == 'R') ? 'Y' : 'R';
                System.out.print("Who should go first? (player/computer): ");
                String firstTurn = sc.next();
                if (firstTurn.equalsIgnoreCase("player")) {
                    currPlayer = 1;
                }
                aiPlayer = new AIPlayer(player2Disc, player1Disc);
                break;
            default:
                System.out.println("Invalid selection! Defaulting to two-player mode.");
                break;
        }

        Board board = new Board();
        boolean endGame = false;

        while (!endGame) {
            if (gameMode == 2 && currPlayer == 0) {    // AI's turn
                int aiMove = aiPlayer.getBestMove(board);
                if (board.addDisc(aiMove + 1, player2Disc)) {  // Adjust column index for board
                    System.out.println("AI (Player " + player2Disc + ") places a disc in column " + (aiMove + 1)); // Adjust column index for user display
                }
                if (board.checkWin(player2Disc)) {
                    endGame = true;
                    board.printBoard();  // Show final board
                    System.out.println("AI WINS!");
                } else if (board.isFull()) {
                    endGame = true;
                    board.printBoard();
                    System.out.println("The game is a draw!!");
                } else {
                    currPlayer = 1;  // Switch to human player
                }
            } else {   // Human player's turn
                board.printBoard();
                System.out.println("Player " + (currPlayer == 1 ? player1Name : player2Name) + ", choose a column (1-7): ");
                try {
                    int column = sc.nextInt();
                    if (board.addDisc(column, currPlayer == 1 ? player1Disc : player2Disc)) {
                        System.out.println("Adding " + (currPlayer == 1 ? player1Name : player2Name) + "'s disc to column " + column);
                        if (board.checkWin(currPlayer == 1 ? player1Disc : player2Disc)) {
                            endGame = true;
                            board.printBoard();  // Show final board
                            System.out.println("Player " + (currPlayer == 1 ? player1Name : player2Name) + " WINS!");
                        } else if (board.isFull()) {
                            endGame = true;
                            board.printBoard();
                            System.out.println("The game is a draw!!");
                        } else {
                            currPlayer = (gameMode == 1) ? (currPlayer == 1 ? 2 : 1) : 0;
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
