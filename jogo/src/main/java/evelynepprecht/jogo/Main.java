package evelynepprecht.jogo;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class Main extends Application {
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        primaryStage.setTitle("Jogo da Velha");

        // Adicionando o ícone à janela
        Image icon = new Image(getClass().getResource("/img/tic-tac-toe.png").toString()); // Caminho do ícone
        primaryStage.getIcons().add(icon);

        // Inicializa a tela inicial (WelcomeScreen)
        WelcomeScreen welcomeScreen = new WelcomeScreen();
        Scene welcomeScene = new Scene(welcomeScreen, 700, 600);

        primaryStage.setScene(welcomeScene);
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    // Efeito de pulsação
    public static void efeitoPulsar(ImageView imageView) {
        // Criação da animação de pulsação
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000), imageView);
        scaleTransition.setByX(0.1);  // Aumento de 10% no eixo X
        scaleTransition.setByY(0.1);  // Aumento de 10% no eixo Y
        scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);  // Animação contínua
        scaleTransition.setAutoReverse(true);  // Inverter a animação (voltar ao tamanho original)
        scaleTransition.play();  // Iniciar a animação
    }

    // Função para estilizar os botões
    public static void setButtonStyle(Button button) {
        button.getStyleClass().add("button-style");
        button.setMaxWidth(250);
        button.setOnMouseEntered(e -> button.getStyleClass().add("button-hover"));
        button.setOnMouseExited(e -> button.getStyleClass().remove("button-hover"));
    }

    // Botão home
    public static Button buttonHome() {
        // Criar o botão "home" com imagem
        Image homeIcon = new Image(Main.class.getResource("/img/home.png").toString()); // Caminho da imagem
        ImageView homeImageView = new ImageView(homeIcon);
        homeImageView.setFitHeight(30); // Ajustar o tamanho da imagem
        homeImageView.setFitWidth(30);  // Ajustar o tamanho da imagem

        Button homeButton = new Button();
        homeButton.getStyleClass().add("home-button");
        homeButton.setGraphic(homeImageView); // Define a imagem no botão

        // Ação do botão "home"
        homeButton.setOnAction(e -> backToMainScreen()); // Ao clicar, vai voltar à tela inicial

        return homeButton; // Retorna o botão para ser usado
    }

    // Função de navegação para a tela inicial
    public static void backToMainScreen() {
        // Remove a tela de seleção de modo de jogo e volta à tela inicial
        if (primaryStage != null) {
            // Obtém a largura e altura da cena atual (área visível, sem bordas)
            double sceneWidth = primaryStage.getScene().getWidth();
            double sceneHeight = primaryStage.getScene().getHeight();

            // Cria uma nova instância da tela inicial
            WelcomeScreen welcomeScreen = new WelcomeScreen();
            Scene welcomeScene = new Scene(welcomeScreen, sceneWidth, sceneHeight);

            primaryStage.setScene(welcomeScene);
        }
    }

}
