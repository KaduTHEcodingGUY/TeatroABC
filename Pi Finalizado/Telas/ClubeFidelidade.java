package Telas;

import Interfaces.ProvedorView;
import Backend.Servicos.AuthService;
import com.google.gson.JsonObject;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class ClubeFidelidade implements ProvedorView {

    private java.util.function.Consumer<javafx.scene.Node> onNavigateToPayment;

    public ClubeFidelidade() {
    }

    public ClubeFidelidade(java.util.function.Consumer<javafx.scene.Node> onNavigateToPayment) {
        this.onNavigateToPayment = onNavigateToPayment;
    }

    @Override
    public Node getView() {
        BorderPane viewLayout = new BorderPane();

        try {
            Image imagemFundo = new Image(getClass().getResourceAsStream("/Imagens/BG1BR.png"));
            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
            BackgroundImage backgroundImage = new BackgroundImage(imagemFundo, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
            viewLayout.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem de fundo (ClubeFidelidade). Usando cor de fundo sólida.");
            e.printStackTrace();
            viewLayout.setBackground(new Background(new BackgroundFill(Color.web("#1E1E1E"), CornerRadii.EMPTY, Insets.EMPTY)));
        }

        Label titulo = new Label("Clube Fidelidade");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titulo.setTextFill(Color.web("#FFD700"));
        BorderPane.setAlignment(titulo, Pos.CENTER);
        titulo.setPadding(new Insets(50, 0, 0, 0));
        viewLayout.setTop(titulo);
        
        VBox centerContent = new VBox(60);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(0, 20, 20, 20));

        // Lógica para verificar o status do membro fidelidade
        String userId = AuthService.getCurrentUserId();
        boolean isMembroFidelidade = false;
        if (userId != null) {
            JsonObject userDetails = AuthService.getUserDetails(userId);
            if (userDetails != null && userDetails.has("membro_fidelidade")) {
                isMembroFidelidade = userDetails.get("membro_fidelidade").getAsBoolean();
            }
        }

        // MODIFICADO: A tela agora tem duas aparências, dependendo se o usuário é membro ou não.
        if (isMembroFidelidade) {
            // --- NOVO: TELA PARA QUEM JÁ É MEMBRO ---
            Label aproveiteLabel = new Label("Aproveite o teatro!");
            aproveiteLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
            aproveiteLabel.setTextFill(Color.WHITE);

            Label membroStatusLabel = new Label("Você já faz parte do Clube de Fidelidade!");
            membroStatusLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 20));
            membroStatusLabel.setTextFill(Color.web("#28a745"));

            centerContent.getChildren().addAll(aproveiteLabel, membroStatusLabel);
            
        } else {
            // --- TELA PARA QUEM AINDA NÃO É MEMBRO ---
            Label beneficioLabel = new Label("Tenha 10% de desconto em todos os ingressos");
            beneficioLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
            beneficioLabel.setTextFill(Color.WHITE);
            beneficioLabel.setTextAlignment(TextAlignment.CENTER);
            beneficioLabel.setWrapText(true);

            // MODIFICADO: Preço atualizado para R$ 15,00
            Label precoLabel = new Label("Apenas R$ 15,00 / mês");
            precoLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
            precoLabel.setTextFill(Color.web("#28a745"));
            
            Button btnAssinar = new Button("Assinar clube");
            btnAssinar.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
            btnAssinar.setTextFill(Color.WHITE);
            btnAssinar.setBackground(new Background(new BackgroundFill(Color.web("#28a745"), new CornerRadii(20), Insets.EMPTY)));
            btnAssinar.setPadding(new Insets(10, 25, 10, 25));
            
            DropShadow blueGlow = new DropShadow(20, Color.rgb(0, 191, 255, 0.8));
            blueGlow.setSpread(0.5);
            btnAssinar.setOnMousePressed(event -> btnAssinar.setEffect(blueGlow));
            btnAssinar.setOnMouseReleased(event -> btnAssinar.setEffect(null));
            
            VBox.setMargin(btnAssinar, new Insets(20, 0, 0, 0)); 
            
            centerContent.getChildren().addAll(beneficioLabel, precoLabel, btnAssinar);
            
            btnAssinar.setOnAction(event -> {
                if (onNavigateToPayment != null) {
                    TelaPagamentoCartao telaPagamento = new TelaPagamentoCartao(node -> {
                        onNavigateToPayment.accept(new ClubeFidelidade(onNavigateToPayment).getView());
                    });
                    onNavigateToPayment.accept(telaPagamento.getView());
                }
            });
        }
        
        viewLayout.setCenter(centerContent);

        return viewLayout;
    }
}