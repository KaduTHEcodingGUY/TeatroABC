package Telas;

import Interfaces.ProvedorView;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TelaCompraIngresso implements ProvedorView {

    // --- VARIÁVEIS DE ESTADO RENOMEADAS ---
    private ImageView bannerSelecionado = null;
    private String pecaSel = null;          // RENOMEADO
    private String sessaoSel = null;        // RENOMEADO

    private final double TAMANHO_NORMAL = 1.0;
    private final double TAMANHO_EXPANDIDO = 1.1;

    private StackPane mainLayout;
    private Pane overlayBackground;
    private VBox horarioSelectionContainer;
    private Button btnContinuar;

    @Override
    public Node getView() {
        mainLayout = new StackPane();
        BorderPane viewLayout = createMainContent();
        
        criarOverlaySelecaoHorario();

        mainLayout.getChildren().addAll(viewLayout, overlayBackground, horarioSelectionContainer);
        return mainLayout;
    }

    private BorderPane createMainContent() {
        BorderPane viewLayout = new BorderPane();
        Image imagemFundo = new Image(getClass().getResourceAsStream("/Utilitarios/Banner1.png"));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        BackgroundImage backgroundImage = new BackgroundImage(imagemFundo, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        viewLayout.setBackground(new Background(backgroundImage));

        HBox bannersContainer = new HBox(30);
        bannersContainer.setAlignment(Pos.CENTER);
        ImageView banner1 = createBanner("/Utilitarios/FrozenPoster.jpg", "Frozen");
        ImageView banner2 = createBanner("/Utilitarios/MJ-Musical-.jpg", "Musical do Michael Jackson");
        ImageView banner3 = createBanner("/Utilitarios/Romeu&Julietta.png", "Romeu & Julieta");
        bannersContainer.getChildren().addAll(banner1, banner2, banner3);
        viewLayout.setCenter(bannersContainer);

        Button btnComprar = new Button("Comprar Ingresso");
        btnComprar.setDisable(true);
        btnComprar.setCursor(Cursor.HAND);
        btnComprar.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 25; -fx-padding: 10px 25px;");
        
        btnComprar.setOnAction(e -> {
            if (pecaSel != null) { // MODIFICADO para usar a variável renomeada
                mostrarSelecaoHorario();
            }
        });

        HBox buttonContainer = new HBox(btnComprar);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(0, 0, 40, 0));
        viewLayout.setBottom(buttonContainer);

        addSelectionListener(banner1, btnComprar);
        addSelectionListener(banner2, btnComprar);
        addSelectionListener(banner3, btnComprar);

        return viewLayout;
    }
    
    private void criarOverlaySelecaoHorario() {
        overlayBackground = new Pane();
        overlayBackground.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        overlayBackground.setOnMouseClicked(event -> ocultarSelecaoHorario());

        horarioSelectionContainer = new VBox(25);
        horarioSelectionContainer.setAlignment(Pos.CENTER);
        horarioSelectionContainer.setStyle("-fx-background-color: #2D2D2D; -fx-padding: 40px; -fx-background-radius: 10px;");
        horarioSelectionContainer.setMaxSize(400, 300);

        Label titulo = new Label("Selecione um Horário");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.WHITE);
        VBox.setMargin(titulo, new Insets(0, 0, 10, 0));

        HBox choiceChips = new HBox(15);
        choiceChips.setAlignment(Pos.CENTER);
        Button chip1 = criarChoiceChip("18:00");
        Button chip2 = criarChoiceChip("20:30");
        Button chip3 = criarChoiceChip("22:00");
        choiceChips.getChildren().addAll(chip1, chip2, chip3);

        btnContinuar = new Button("Continuar");
        btnContinuar.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 25; -fx-padding: 10px 25px;");
        btnContinuar.setDisable(true);
        
        // Ação de clique do botão "Continuar" usa as variáveis renomeadas
        btnContinuar.setOnAction(e -> {
    // A lógica de navegação agora está dentro do IF, como deveria ser
    if (sessaoSel != null) {
        System.out.println("Navegando para o Mapa de Assentos...");
        System.out.println("Peça: " + pecaSel + ", Sessão selecionada: " + sessaoSel);

        // 1. Pega a janela (Stage) atual a partir do próprio botão
        Stage stage = (Stage) btnContinuar.getScene().getWindow();

        // 2. Cria a nova tela do mapa de assentos
        MapaAssentos mapaView = new MapaAssentos();
        Parent mapaRoot = (Parent) mapaView.getView(); // O getView() monta a interface

        // 3. Cria a nova cena
        Scene mapaScene = new Scene(mapaRoot);
        
        // 4. Troca a cena na janela principal
        stage.setScene(mapaScene);
        stage.setFullScreen(true); // Mantém o modo de tela cheia
    }
});
        
        horarioSelectionContainer.getChildren().addAll(titulo, choiceChips, btnContinuar);
        
        ocultarSelecaoHorario();
    }

    private Button criarChoiceChip(String horario) {
        Button chip = new Button(horario);
        String styleNormal = "-fx-background-color: #555555; -fx-text-fill: #E0E0E0; -fx-background-radius: 20px; -fx-padding: 8px 20px; -fx-font-weight: bold; -fx-font-size: 14px;";
        String styleSelecionado = "-fx-background-color:rgb(192, 20, 20); -fx-text-fill: white; -fx-background-radius: 20px; -fx-padding: 8px 20px; -fx-font-weight: bold; -fx-font-size: 14px;";
        
        chip.setStyle(styleNormal);
        chip.setCursor(Cursor.HAND);

        chip.setOnAction(e -> {
            HBox parent = (HBox) chip.getParent();
            for (Node node : parent.getChildren()) {
                node.setStyle(styleNormal);
            }
            chip.setStyle(styleSelecionado);
            // Armazena na variável renomeada
            sessaoSel = horario; 
            btnContinuar.setDisable(false);
        });
        return chip;
    }
    
    private void addSelectionListener(ImageView banner, Button buyButton) {
        banner.setOnMouseClicked(event -> {
            if (bannerSelecionado != null && bannerSelecionado != banner) {
                animateBanner(bannerSelecionado, TAMANHO_NORMAL);
            }
            animateBanner(banner, TAMANHO_EXPANDIDO);
            
            bannerSelecionado = banner;
            // Armazena na variável renomeada
            this.pecaSel = (String) banner.getUserData(); 
            
            buyButton.setDisable(false);
        });
    }

    private void ocultarSelecaoHorario() {
        overlayBackground.setVisible(false);
        horarioSelectionContainer.setVisible(false);
        
        // Reseta a variável renomeada
        sessaoSel = null; 
        
        if(btnContinuar != null) btnContinuar.setDisable(true);

        if (horarioSelectionContainer != null && horarioSelectionContainer.getChildren().size() > 1) {
             Node chipsNode = horarioSelectionContainer.getChildren().get(1);
            if (chipsNode instanceof HBox) {
                HBox choiceChips = (HBox) chipsNode;
                String styleNormal = "-fx-background-color: #555555; -fx-text-fill: #E0E0E0; -fx-background-radius: 20px; -fx-padding: 8px 20px; -fx-font-weight: bold; -fx-font-size: 14px;";
                for(Node chip : choiceChips.getChildren()){
                    chip.setStyle(styleNormal);
                }
            }
        }
    }
    
    // O resto dos métodos (createBanner, animateBanner, etc.) continua igual.
    private ImageView createBanner(String imagePath, String nomeDaPeca) {
        Image bannerImage = new Image(getClass().getResourceAsStream(imagePath));
        ImageView bannerView = new ImageView(bannerImage);
        bannerView.setFitWidth(300);
        bannerView.setFitHeight(450);
        bannerView.setPreserveRatio(false);
        bannerView.setCursor(Cursor.HAND);
        Rectangle clip = new Rectangle(300, 450);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        bannerView.setClip(clip);
        bannerView.setEffect(new DropShadow(10, Color.BLACK));
        bannerView.setUserData(nomeDaPeca);
        return bannerView;
    }

    private void animateBanner(ImageView banner, double targetScale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), banner);
        st.setToX(targetScale);
        st.setToY(targetScale);
        st.play();
    }
    
    private void mostrarSelecaoHorario() {
        overlayBackground.setVisible(true);
        horarioSelectionContainer.setVisible(true);
    }
}