package Telas;

import Interfaces.ProvedorView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.function.BiConsumer;

public class TelaConfirmacaoPagamento implements ProvedorView {
    private String codigoIngresso;
    private BiConsumer<Node, Boolean> viewSwitcher;

    public TelaConfirmacaoPagamento(String codigoIngresso, BiConsumer<Node, Boolean> viewSwitcher) {
        this.codigoIngresso = codigoIngresso;
        this.viewSwitcher = viewSwitcher;
    }

    @Override
    public Node getView() {
        BorderPane rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: #1E1E1E;");
        rootLayout.setPadding(new Insets(30));

        // Título da página
        Label title = new Label("Pagamento Confirmado!");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.WHITE);
        BorderPane.setAlignment(title, Pos.CENTER);
        rootLayout.setTop(title);

        // Mensagem de confirmação
        VBox centerContent = new VBox(20);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(40));

        Label message = new Label("Seu pagamento foi processado com sucesso!");
        message.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        message.setTextFill(Color.WHITE);

        // Botão para voltar ao início
        Button btnVoltar = new Button("Voltar ao Início");
        btnVoltar.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 25; -fx-padding: 10px 25px;");
        btnVoltar.setOnAction(e -> {
            if (viewSwitcher != null) {
                viewSwitcher.accept(new TelaCompraIngresso(viewSwitcher).getView(), true);
            }
        });

        centerContent.getChildren().addAll(message, btnVoltar);
        rootLayout.setCenter(centerContent);

        return rootLayout;
    }
} 