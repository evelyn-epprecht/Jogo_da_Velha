package evelynepprecht.jogo;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.util.Objects;


public class WelcomeScreen extends StackPane {

    public WelcomeScreen() {
        // Carregar o arquivo CSS
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
        getStyleClass().add("background");

        // Texto de boas-vindas centralizado e com estilo da classe CSS
        Text appTitle = new Text("Bem-vindo ao Jogo da Velha");
        appTitle.getStyleClass().add("app-title");

        // Ajuste do wrappingWidth com base no tamanho da janela
        appTitle.setWrappingWidth(500);

        // Organize o texto dentro de um TextFlow para controle mais preciso
        TextFlow textFlow = new TextFlow(appTitle);
        textFlow.setPrefWidth(500);
        textFlow.setTextAlignment(TextAlignment.CENTER);
        appTitle.setFill(Color.WHITE);

        // Botões de ação
        Button iniciarJogoButton = new Button("Iniciar Jogo");
        Main.setButtonStyle(iniciarJogoButton);

        VBox buttonsLayout = new VBox(10);
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.getChildren().addAll(iniciarJogoButton);

        // Imagem do logotipo
        Image logoImage = new Image(getClass().getResource("/img/strategic-plan.png").toString());
        ImageView imageView = new ImageView(logoImage);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);

        // Efeito de pulsação
        Main.efeitoPulsar(imageView);

        //efeito brilho
        imageView.getStyleClass().add("image-glow");

        // Organizando os elementos na tela
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(textFlow, imageView, buttonsLayout);

        // Ajuste para responsividade
        widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double newWidthValue = newWidth.doubleValue();
            appTitle.setWrappingWidth(newWidthValue > 600 ? 500 : newWidthValue - 40);
        });

        // Ação do botão Iniciar Jogo
        iniciarJogoButton.setOnAction(e -> showGameModeSelection());

        // Adicionando todos os elementos à tela
        getChildren().addAll(layout);
    }

    private void showGameModeSelection() {
        // Criar um ImageView para a imagem
        Image gameIcon = new Image(Objects.requireNonNull(getClass().getResource("/img/robo-advisor.png")).toString());
        ImageView imageView = new ImageView(gameIcon);
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);

        Main.efeitoPulsar(imageView);

        //efeito brilho
        imageView.getStyleClass().add("image-glow");

        // Tela de fundo para criar o "alert"
        StackPane modalBackground = new StackPane();
        modalBackground.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

        // Caixa de diálogo com opções de jogo
        VBox modalLayout = new VBox(15);
        modalLayout.setAlignment(Pos.CENTER);
        modalLayout.setStyle("-fx-background-color: #006064; -fx-padding: 20px; -fx-border-radius: 10px;");

        Text gameModeTitle = new Text("Escolha o Modo de Jogo:");
        gameModeTitle.setFill(Color.WHITE);
        gameModeTitle.getStyleClass().add("app-title");

        TextFlow textFlow = new TextFlow(gameModeTitle);
        textFlow.setPrefWidth(500);
        textFlow.setTextAlignment(TextAlignment.CENTER);

        widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double newWidthValue = newWidth.doubleValue();
            gameModeTitle.setWrappingWidth(newWidthValue > 600 ? 500 : newWidthValue - 40);
        });

        // Botões de ação
        Button botButton = new Button("Jogar contra o Bot");
        Main.setButtonStyle(botButton);
        botButton.setOnAction(e -> startGameWithBot());

        Button multiplayerButton = new Button("Jogar Multiplayer");
        Main.setButtonStyle(multiplayerButton);
        multiplayerButton.setOnAction(e -> startMultiplayerGame());

        // Botão "home"
        Button homeButton = Main.buttonHome();

        // Organizando os botões
        VBox buttonsLayout = new VBox(10);
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.getChildren().addAll(botButton, multiplayerButton, homeButton);

        // Organizando o layout do modal
        modalLayout.getChildren().addAll(imageView, textFlow, buttonsLayout);

        modalBackground.getChildren().add(modalLayout);

        // Adicionando a tela de seleção de modo de jogo
        getChildren().add(modalBackground);
    }

    private void startGameWithBot() {
        GameScreen gameScreen = new GameScreen(true);
        getChildren().clear();
        getChildren().add(gameScreen);
    }

    private void startMultiplayerGame() {
        GameScreen gameScreen = new GameScreen(false);
        getChildren().clear();
        getChildren().add(gameScreen);
    }

}
