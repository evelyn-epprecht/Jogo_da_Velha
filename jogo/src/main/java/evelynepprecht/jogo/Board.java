package evelynepprecht.jogo;

import java.util.Scanner;

public class Board {
    private char[][] board; // Tabuleiro de 3x3

    public Board() {
        board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-'; // Espaços vazios no início
            }
        }
    }

    public void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';  // Limpa a célula, definindo como vazia
            }
        }
    }
    public boolean checkDraw() {
        // Verifica se todas as células estão preenchidas
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    return false;  // Se encontrar uma célula vazia, o jogo não terminou em empate
                }
            }
        }
        // Se todas as células estiverem preenchidas e nenhum vencedor foi encontrado, é empate
        return true;
    }

    // Método para fazer uma jogada no tabuleiro
    public boolean makeMove(int row, int col, char symbol) {
        if (row >= 0 && row < 3 && col >= 0 && col < 3) {
            if (board[row][col] == '-') {  // Se a posição estiver vazia
                board[row][col] = symbol;  // Preenche com o símbolo do jogador
                return true;  // Retorna verdadeiro, indicando que a jogada foi bem-sucedida
            } else {
                return false;  // Se a célula já estiver ocupada, retorna falso
            }
        }
        return false;  // Se a posição estiver fora dos limites do tabuleiro
    }

    // Método para ler uma entrada válida (linha ou coluna)
    public int readValidInput(Scanner scanner, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt + " (0-2): ");
            String input = scanner.nextLine();
            try {
                value = Integer.parseInt(input);
                // Verifica se o valor está dentro do intervalo permitido (0 a 2)
                if (value >= 0 && value <= 2) {
                    return value;
                } else {
                    System.out.println("Valor inválido. Por favor, insira um número entre 0 e 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
            }
        }
    }

    // Método para obter a melhor jogada do bot (para fins de exemplo, escolhe aleatoriamente uma posição válida)
    public int[] getBestMove(char botSymbol, char playerSymbol) {
        // Tenta encontrar uma jogada vencedora ou bloquear o oponente
        int[] bestMove = null;

        // Tenta bloquear ou vencer
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') { // Só tenta a jogada se a célula estiver vazia
                    board[i][j] = botSymbol;
                    if (checkWin(botSymbol)) {
                        bestMove = new int[] {i, j}; // Retorna a jogada vencedora
                        board[i][j] = '-'; // Desfaz a jogada
                        return bestMove;
                    }
                    board[i][j] = playerSymbol;
                    if (checkWin(playerSymbol)) {
                        bestMove = new int[] {i, j}; // Bloqueia o jogador se ele estiver prestes a ganhar
                        board[i][j] = '-'; // Desfaz a jogada
                        return bestMove;
                    }
                    board[i][j] = '-'; // Desfaz a jogada
                }
            }
        }

        // Se não encontrou nenhuma jogada vencedora ou bloqueio, escolhe uma posição aleatória válida
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') { // Posição válida
                    bestMove = new int[] {i, j};
                    return bestMove;
                }
            }
        }

        return bestMove; // Retorna null se não houver jogada válida (impossível no jogo normal)
    }

    // Método para verificar se alguém venceu
    public boolean checkWin(char symbol) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) return true;
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) return true;
        }
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) return true;
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) return true;
        return false;
    }

    // Exibe o tabuleiro no console
    public void displayBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
