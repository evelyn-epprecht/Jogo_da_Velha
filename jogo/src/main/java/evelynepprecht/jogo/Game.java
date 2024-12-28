package evelynepprecht.jogo;

import java.util.Scanner;

public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public Game(Player player1, Player player2) {
        this.board = new Board();
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
    }

    public void playGame() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            board.displayBoard();
            System.out.println("Vez de " + currentPlayer.getName() + " (" + currentPlayer.getSymbol() + ")");
            int row = board.readValidInput(scanner, "Linha");
            int col = board.readValidInput(scanner, "Coluna");

            if (board.makeMove(row, col, currentPlayer.getSymbol())) {
                if (board.checkWin(currentPlayer.getSymbol())) {
                    board.displayBoard();
                    System.out.println(currentPlayer.getName() + " venceu!");
                    break;
                }
                if (board.checkDraw()) {
                    board.displayBoard();
                    System.out.println("Empate!");
                    break;
                }
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
            } else {
                System.out.println("Essa posição já está ocupada, tente novamente.");
            }
        }
        scanner.close();
    }
}
