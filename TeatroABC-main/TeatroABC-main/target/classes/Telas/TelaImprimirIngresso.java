package Telas;

import Utilitarios.ValidadorCPF;
import Interfaces.ProvedorView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.File;

public class TelaImprimirIngresso implements ProvedorView {
    private String arquivo;

    @Override
    public Node getView() {
        // Container principal
        BorderPane containerPrincipal = new BorderPane();
        containerPrincipal.setStyle("-fx-background-color: #1E1E1E;"); // Fundo escuro

        // Painel central (campo de CPF e botão "Imprimir")
        VBox painelCentral = new VBox(20); // 20 pixels de espaçamento entre elementos
        painelCentral.setAlignment(Pos.CENTER);
        painelCentral.setPadding(new Insets(20));
        painelCentral.setStyle("-fx-background-color: #1E1E1E;"); // Fundo escuro

        // Campo para digitar o CPF
        Label labelCPF = new Label("Inserir CPF:");
        labelCPF.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        TextField campoCPF = new TextField();
        campoCPF.setStyle("-fx-font-size: 16px; -fx-background-color: #2D2D2D; -fx-text-fill: white;");
        campoCPF.setPromptText("Digite o CPF");
        campoCPF.setMaxWidth(200);

        // Botão de impressão
        Button botaoImprimir = new Button("Imprimir");
        botaoImprimir.setStyle("-fx-font-size: 16px; -fx-background-color: #455A64; -fx-text-fill: white;");
        botaoImprimir.setOnMouseEntered(e -> botaoImprimir.setStyle("-fx-font-size: 16px; -fx-background-color: #546E7A; -fx-text-fill: white;"));
        botaoImprimir.setOnMouseExited(e -> botaoImprimir.setStyle("-fx-font-size: 16px; -fx-background-color: #455A64; -fx-text-fill: white;"));

        // Lógica do botão de impressão
        botaoImprimir.setOnAction(e -> {
            try {
                String cpf = campoCPF.getText();
        
                // Validação do CPF
                if (cpf.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Aviso", "Por favor, insira um CPF.");
                } else if (!ValidadorCPF.isCPF(cpf)) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "CPF inválido. Por favor, insira um CPF válido.");
                } else {
                    // CPF ok, gera o ingresso
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Ingresso impresso para o CPF: " + cpf);
                    arquivo = cpf + ".txt"; // Nome do arquivo
                    baixarArquivo(); // Gera o arquivo
                }
        
            } catch (Exception err) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao processar o CPF. Por favor, tente novamente.");
                System.out.println("Erro: " + err.getMessage());
            }
        });

        painelCentral.getChildren().addAll(labelCPF, campoCPF, botaoImprimir);

        // Container do banner
        ImageView bannerView = new ImageView(new Image(getClass().getResourceAsStream("/Utilitarios/Banner0.png")));
        bannerView.setFitWidth(600);
        bannerView.setFitHeight(350);
        bannerView.setPreserveRatio(true);

        // Adicionar os componentes ao container principal
        containerPrincipal.setLeft(painelCentral);
        containerPrincipal.setCenter(bannerView);

        return containerPrincipal;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void baixarArquivo() {
        File arquivoExiste = new File(arquivo);

        if (arquivoExiste.exists()) {
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", 
                "Seu arquivo está disponível em: " + arquivoExiste.getAbsolutePath());
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro", "Arquivo não encontrado.");
        }
    }
}