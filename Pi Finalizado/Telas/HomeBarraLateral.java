package Telas;

import Interfaces.ProvedorView;
import Backend.Servicos.AuthService;
import Backend.Servicos.UsuarioService;
import Backend.Servicos.UsuarioService.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HomeBarraLateral {

    private BorderPane rootLayout;
    private Button botaoSelecionado;
    private ProvedorView clubeFidelidadeViewProvider;
    private BorderPane sidebarPane;
    private Label userName;

    private static final Background BG_BOTAO_NORMAL = new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background BG_BOTAO_HOVER = new Background(new BackgroundFill(Color.web("#404040"), new CornerRadii(5), Insets.EMPTY));
    private static final Background BG_BOTAO_SELECIONADO = new Background(new BackgroundFill(Color.web("#005A9E"), new CornerRadii(5), Insets.EMPTY));

    public Parent getRoot(Stage primaryStage) {
        clubeFidelidadeViewProvider = new ClubeFidelidade(node -> setCenterView(node, true)); 
        rootLayout = new BorderPane();
        rootLayout.setBackground(new Background(new BackgroundFill(Color.web("#1E1E1E"), CornerRadii.EMPTY, Insets.EMPTY)));

        sidebarPane = createSidebar(primaryStage);
        rootLayout.setLeft(sidebarPane);
        
        return rootLayout;
    }

    private BorderPane createSidebar(Stage primaryStage) { 
        BorderPane sidebarPane = new BorderPane();
        sidebarPane.setPadding(new Insets(20));
        sidebarPane.setPrefWidth(250);
        sidebarPane.setBackground(new Background(new BackgroundFill(Color.web("#2D2D2D"), CornerRadii.EMPTY, Insets.EMPTY)));

        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/masks.png")));
        logoView.setFitHeight(60);
        logoView.setFitWidth(60);
        HBox logoContainer = new HBox(logoView);
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.setPadding(new Insets(0, 0, 40, 0));
        sidebarPane.setTop(logoContainer);
        
        VBox navButtons = new VBox(15);
        navButtons.setAlignment(Pos.TOP_CENTER);
        
        String userId = AuthService.getCurrentUserId();
        Usuario usuario = UsuarioService.buscarUsuarioPorId(userId);
        String tipoUsuario = (usuario != null) ? usuario.getTipoUsuario() : "cliente";

        Button btnComprar = createNavButtonWithIcon("Comprar Ingressos", "/Imagens/ticket.png", new TelaCompraIngresso(this::setCenterView));
        Button btnMeusIngressos = createNavButtonWithIcon("Meus Ingressos", "/Imagens/tickets.png", new MeusIngressos());
        Button btnClube = createNavButtonWithIcon("Clube Fidelidade", "/Imagens/ABCard-removebg-preview.png", new ClubeFidelidade(node -> setCenterView(node, true)));
        Button btnEstatisticas = createNavButtonWithIcon("Estatisticas", "/Imagens/ABCard-removebg-preview.png", new TelaEstatisticas());

        if ("gestor".equalsIgnoreCase(tipoUsuario)) {
            navButtons.getChildren().addAll(btnEstatisticas);
            botaoSelecionado = btnEstatisticas;
            setCenterView(new TelaEstatisticas().getView(), true);
        } else {
            navButtons.getChildren().addAll(btnComprar, btnMeusIngressos, btnClube);
            botaoSelecionado = btnClube;
            setCenterView(clubeFidelidadeViewProvider.getView(), true);
        }

        if (botaoSelecionado != null) {
            botaoSelecionado.setBackground(BG_BOTAO_SELECIONADO);
        }
        
        sidebarPane.setCenter(navButtons);

        VBox profileSection = new VBox(10);
        profileSection.setAlignment(Pos.CENTER);
        HBox userBox = new HBox(10);
        userBox.setAlignment(Pos.CENTER_LEFT);
        Image avatarImage = new Image(getClass().getResourceAsStream("/Imagens/avatar.jpg"));
    
        ImageView avatarView = new ImageView(avatarImage);
    
        avatarView.setFitHeight(40);
        avatarView.setFitWidth(40);
    
        Circle clip = new Circle(20, 20, 20);
        avatarView.setClip(clip);
    
        userName = new Label(usuario != null ? usuario.getNomeCompleto() : "Usuário");
        userName.setTextFill(Color.WHITE);
        userName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        userBox.getChildren().addAll(avatarView, userName);
        
        Button btnLogout = new Button("Logout");
        btnLogout.setPrefWidth(180);
        btnLogout.setCursor(Cursor.HAND);
        btnLogout.setTextFill(Color.WHITE);
        btnLogout.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        btnLogout.setBackground(new Background(new BackgroundFill(Color.web("#DC3545"), new CornerRadii(5), Insets.EMPTY)));
        
      
        btnLogout.setOnAction(event -> {
            // Captura as dimensões da janela atual
            double currentWidth = primaryStage.getScene().getWidth();
            double currentHeight = primaryStage.getScene().getHeight();

            Telalogin telaLogin = new Telalogin();
            Parent loginRoot = telaLogin.getRoot(primaryStage);

            // Cria a nova cena com as dimensões exatas da janela
            Scene loginScene = new Scene(loginRoot, currentWidth, currentHeight);
            
            primaryStage.setScene(loginScene);
    
        });

        profileSection.getChildren().addAll(userBox, btnLogout);
        sidebarPane.setBottom(profileSection);

        return sidebarPane;
    }

    private Button createNavButtonWithIcon(String text, String iconPath, ProvedorView provider) {
        Image iconImage = new Image(getClass().getResourceAsStream(iconPath));
        ImageView iconView = new ImageView(iconImage);
        
        if ("Clube Fidelidade".equals(text)) {
            iconView.setFitHeight(30);
            iconView.setFitWidth(35);
        } else {
            iconView.setFitHeight(30);
            iconView.setFitWidth(35);
        }

        Button button = new Button(text);
        button.setGraphic(iconView);
        
        button.setBackground(BG_BOTAO_NORMAL);
        button.setTextFill(Color.web("#BDBDBD"));
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPrefWidth(200);
        button.setGraphicTextGap(15);
        button.setPadding(new Insets(8, 12, 8, 12));
        button.setContentDisplay(ContentDisplay.LEFT);
        button.setCursor(Cursor.HAND);

        button.hoverProperty().addListener((observable, oldValue, isHovering) -> {
            if (button != botaoSelecionado) {
                button.setBackground(isHovering ? BG_BOTAO_HOVER : BG_BOTAO_NORMAL);
            }
        });

        button.setOnAction(event -> {
            if (provider != null) {
                if (botaoSelecionado != null) {
                    botaoSelecionado.setBackground(BG_BOTAO_NORMAL);
                }
                botaoSelecionado = button;
                botaoSelecionado.setBackground(BG_BOTAO_SELECIONADO);
                setCenterView(provider.getView(), true);
            } else {
                System.out.println("Tela para '" + button.getText() + "' ainda não implementada.");
            }
        });

        return button;
    }

    public void setCenterView(Node view, boolean showSidebar) {
        rootLayout.setCenter(view);
        if (showSidebar) {
            rootLayout.setLeft(sidebarPane);
        } else {
            rootLayout.setLeft(null);
        }
    }
}