package evelynepprecht.jogo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Objects;


public class GameScreen extends StackPane {
    private Board board;
    private boolean isPlayingWithBot;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Button[][] buttons;
    private Text statusText;
    private Text scoreText;
    private Text turnText;
    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    private int scoreBot = 0;

    private boolean gameInProgress = true;

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean inProgress) {
        this.gameInProgress = inProgress;
    }

    public GameScreen(boolean isPlayingWithBot) {
        this.isPlayingWithBot = isPlayingWithBot;
        this.board = new Board();

        player1 = new Player("Player 1", 'X');
        player2 = new Player(isPlayingWithBot ? "Bot" : "Player 2", 'O');
        currentPlayer = player1;

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);

        // Caixa de Status
        VBox statusBox = new VBox(10);
        statusBox.setAlignment(Pos.CENTER);
        statusBox.setStyle("-fx-background-color: transparent; -fx-border-color: rgb(51, 156, 156); -fx-border-width: 3px; -fx-border-radius: 15px; padding: 20px;");

        statusText = new Text("Modo de Jogo: " + (isPlayingWithBot ? "Bot" : "Multiplayer"));
        statusText.getStyleClass().add("app-title");
        statusText.setFill(Color.WHITE);
        statusText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        scoreText = new Text(updateScoreText());
        scoreText.getStyleClass().add("app-title");
        scoreText.setFill(Color.WHITE);
        scoreText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        statusBox.getChildren().addAll(statusText, scoreText);

        turnText = new Text("Vez de " + currentPlayer.getName() + " (" + currentPlayer.getSymbol() + ")");
        turnText.getStyleClass().add("app-title");
        turnText.setFill(Color.WHITE);
        turnText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Responsividade do quadro de status
        widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double adjustedWidth = Math.min(newWidth.doubleValue() - 100, 400);
            statusBox.setMaxWidth(adjustedWidth);
            scoreText.setWrappingWidth(adjustedWidth);
            statusText.setWrappingWidth(adjustedWidth);
        });

        // Grade do Jogo
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        buttons = new Button[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.getStyleClass().add("board");
                final int row = i;
                final int col = j;

                button.setOnAction(e -> handlePlayerMove(row, col, button));

                buttons[i][j] = button;
                grid.add(button, j, i);
            }
        }

        // Botões de Ação
        Button backButton = Main.buttonHome();
        backButton.setOnAction(e -> showExitConfirmationModal());

        // Layout do Conteúdo
        VBox contentLayout = new VBox(10);
        contentLayout.setAlignment(Pos.CENTER);
        contentLayout.setPadding(new javafx.geometry.Insets(20));
        contentLayout.getChildren().addAll(statusBox, turnText, grid, backButton);

        layout.getChildren().add(contentLayout);
        layout.setAlignment(Pos.CENTER);

        getChildren().add(layout);

        // Responsividade
        widthProperty().addListener((obs, oldWidth, newWidth) -> adjustSizes(newWidth.doubleValue()));
        heightProperty().addListener((obs, oldHeight, newHeight) -> adjustSizes(getWidth()));
    }

    private void adjustSizes(double newWidth) {
        double adjustedWidth = Math.min(newWidth - 40, 500);
        double fontSize = newWidth > 600 ? 20 : 16;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setPrefSize(adjustedWidth / 5, adjustedWidth / 5);
            }
        }

        statusText.setFont(Font.font(fontSize));
        scoreText.setFont(Font.font(fontSize));
        turnText.setFont(Font.font(fontSize));
    }

    private void handlePlayerMove(int row, int col, Button button) {
        if (board.makeMove(row, col, currentPlayer.getSymbol())) {
            button.setText(String.valueOf(currentPlayer.getSymbol()));
            button.setDisable(true);

            if (board.checkWin(currentPlayer.getSymbol())) {
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
                    showResultModal(currentPlayer.getName() + " venceu!");
                    updateScore(currentPlayer.getName());
                }));
                timeline.setCycleCount(1);
                timeline.play();
                return;
            }
            if (board.checkDraw()) {
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
                    showResultModal("Empate!");
                }));
                timeline.setCycleCount(1);
                timeline.play();
                return;
            }

            switchPlayer();
            updateTurnText();

            if (isPlayingWithBot && currentPlayer == player2) {
                pauseAndBotMove();
            }
        }
    }

    private void pauseAndBotMove() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> botMove()));
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    private void updateTurnText() {
        turnText.setText("Vez de " + currentPlayer.getName() + " (" + currentPlayer.getSymbol() + ")");
    }

    private String updateScoreText() {
        return "PLACAR: X: " + scorePlayer1 + " x O: " + (isPlayingWithBot ? scoreBot : scorePlayer2);
    }

    private void botMove() {
        int[] move = board.getBestMove(currentPlayer.getSymbol(), player1.getSymbol());
        if (move != null) {
            int row = move[0];
            int col = move[1];
            Button button = buttons[row][col];
            handlePlayerMove(row, col, button);
        }
    }

    private void showResultModal(String message) {
        StackPane modalBackground = new StackPane();
        modalBackground.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        modalBackground.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        VBox modalLayout = new VBox(20);
        modalLayout.setAlignment(Pos.CENTER);
        modalLayout.setStyle("-fx-background-color: #006064; -fx-padding: 20px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, black, 10, 0.5, 0, 0);");

        Text resultText = new Text(message);
        resultText.getStyleClass().add("app-title");
        resultText.setFill(Color.WHITE);
        resultText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double adjustedWidth = Math.min(newWidth.doubleValue() - 100, 400);
            resultText.setWrappingWidth(adjustedWidth);
        });

        ImageView resultImage = new ImageView();
        resultImage.setFitWidth(120);
        resultImage.setPreserveRatio(true);
        resultImage.getStyleClass().add("image-glow");

        if (message.contains("venceu")) {
            resultImage.setImage(new Image(getClass().getResourceAsStream("/img/trophy.png")));
        } else if (message.contains("Empate")) {
            resultImage.setImage(new Image(getClass().getResourceAsStream("/img/hands.png")));
        }

        // Efeito de pulsação
        Main.efeitoPulsar(resultImage);

        Button playAgainButton = new Button("Jogar Novamente");
        Main.setButtonStyle(playAgainButton);
        playAgainButton.setOnAction(e -> {
            getChildren().remove(modalBackground);
            resetGame();
        });

        Button exitButton = new Button("Sair");
        Main.setButtonStyle(exitButton);
        exitButton.setOnAction(e -> showExitConfirmationModal());

        VBox buttonLayout = new VBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(playAgainButton, exitButton);

        modalLayout.getChildren().addAll(resultImage, resultText, buttonLayout);

        modalBackground.getChildren().add(modalLayout);

        getChildren().add(modalBackground);
    }

    private void updateScore(String winner) {
        if (winner.equals(player1.getName())) {
            scorePlayer1++;
        } else if (winner.equals(player2.getName())) {
            if (isPlayingWithBot) {
                scoreBot++;
            } else {
                scorePlayer2++;
            }
        }
        scoreText.setText(updateScoreText());
    }

    private void resetGame() {
        board.resetBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setDisable(false);
            }
        }
        currentPlayer = player1;
        updateTurnText();
    }

    private StackPane exitConfirmationModal;

    private void showExitConfirmationModal() {
        exitConfirmationModal = new StackPane();
        exitConfirmationModal.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

        VBox modalLayout = new VBox(20);
        modalLayout.setAlignment(Pos.CENTER);
        modalLayout.setStyle("-fx-background-color: #006064; -fx-padding: 20px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, black, 10, 0.5, 0, 0);");

        ImageView exitImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/exit.png"))));
        exitImage.setFitWidth(120);
        exitImage.setPreserveRatio(true);
        exitImage.getStyleClass().add("image-glow");

        // Efeito de pulsação
        Main.efeitoPulsar(exitImage);

        Text confirmationText = new Text("Deseja mesmo abandonar a partida?");
        confirmationText.getStyleClass().add("app-title");
        confirmationText.setFill(Color.WHITE);
        confirmationText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Responsividade no texto
        widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double adjustedWidth = Math.min(newWidth.doubleValue() - 100, 400);
            confirmationText.setWrappingWidth(adjustedWidth);
        });

        Button confirmButton = new Button("Sim");
        Main.setButtonStyle(confirmButton);
        confirmButton.setOnAction(e -> Main.backToMainScreen());

        Button cancelButton = new Button("Não");
        Main.setButtonStyle(cancelButton);
        cancelButton.setOnAction(e -> getChildren().remove(exitConfirmationModal));

        VBox buttonLayout = new VBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(confirmButton, cancelButton);

        modalLayout.getChildren().addAll(exitImage, confirmationText, buttonLayout);

        exitConfirmationModal.getChildren().add(modalLayout);

        getChildren().add(exitConfirmationModal);
    }
}
