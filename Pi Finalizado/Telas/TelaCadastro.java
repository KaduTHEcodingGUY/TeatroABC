package Telas;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TelaCadastro {

    public Parent getRoot(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: black;");

        ImageView backgroundImageView = createBackgroundImage();
        Pane gradientOverlay = createGradientOverlay();
        VBox registrationForm = createRegistrationForm(primaryStage);

        registrationForm.maxWidthProperty().bind(root.widthProperty().multiply(0.6));
        registrationForm.maxHeightProperty().bind(root.heightProperty().multiply(0.8));

        root.getChildren().addAll(backgroundImageView, gradientOverlay, registrationForm);
        return root;
    }
    
    private ImageView createBackgroundImage() {
        ImageView imageView = new ImageView();
        try {
            Image backgroundImage = new Image(getClass().getResourceAsStream("/Imagens/TesteLogin.jpg"));
            imageView.setImage(backgroundImage);
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem de fundo /Utilitarios/Banner1.png. A tela ficará preta.");
            e.printStackTrace();
        }
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(1920);
        return imageView;
    }

    private Pane createGradientOverlay() {
        Pane gradientPane = new Pane();
        gradientPane.setStyle("-fx-background-color: linear-gradient(from 0% 100% to 0% 0%, rgba(0,0,0,1) 30%, rgba(0,0,0,0) 100%);");
        return gradientPane;
    }

    private VBox createRegistrationForm(Stage primaryStage) {
        VBox formVBox = new VBox(20);
        formVBox.setAlignment(Pos.CENTER);
        formVBox.setPadding(new Insets(30, 50, 30, 50));
        formVBox.setStyle("-fx-background-color: rgba(25, 25, 25, 0.9); -fx-background-radius: 25;");

        Label titleLabel = new Label("Crie sua Conta");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
        
        GridPane fieldsGrid = new GridPane();
        fieldsGrid.setAlignment(Pos.CENTER);
        fieldsGrid.setHgap(20);
        fieldsGrid.setVgap(15);

        VBox nomeBox = createFormField("Nome Completo", "Digite seu nome completo");
        VBox cpfBox = createFormField("CPF", "000.000.000-00");
        VBox senhaBox = createPasswordField("Senha", "Digite uma senha forte");
        VBox emailBox = createFormField("Email", "seu.email@exemplo.com");
        VBox telefoneBox = createFormField("Telefone", "(00) 00000-0000");
        VBox confirmarSenhaBox = createPasswordField("Confirmar Senha", "Digite a senha novamente");

        fieldsGrid.add(nomeBox, 0, 0);
        fieldsGrid.add(emailBox, 1, 0);
        fieldsGrid.add(cpfBox, 0, 1);
        fieldsGrid.add(telefoneBox, 1, 1);
        fieldsGrid.add(senhaBox, 0, 2);
        fieldsGrid.add(confirmarSenhaBox, 1, 2);

        Button registerButton = new Button("Cadastrar");
        registerButton.setCursor(Cursor.HAND);
        registerButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 25; -fx-pref-width: 200px; -fx-pref-height: 45px;");
        
        registerButton.setOnAction(e -> {
            PasswordField senhaField = (PasswordField) senhaBox.getChildren().get(1);
            PasswordField confirmarSenhaField = (PasswordField) confirmarSenhaBox.getChildren().get(1);

            if (senhaField.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Campo Vazio", "O campo de senha não pode estar vazio.");
                return;
            }
            if (!senhaField.getText().equals(confirmarSenhaField.getText())) {
                showAlert(Alert.AlertType.ERROR, "Erro de Senha", "As senhas não coincidem. Tente novamente.");
                return;
            }

            // Extrai os dados dos campos
            TextField nomeField = (TextField) nomeBox.getChildren().get(1);
            TextField emailField = (TextField) emailBox.getChildren().get(1);
            TextField cpfField = (TextField) cpfBox.getChildren().get(1);
            TextField telefoneField = (TextField) telefoneBox.getChildren().get(1);

            String nome = nomeField.getText();
            String email = emailField.getText();
            String cpf = cpfField.getText();
            String telefone = telefoneField.getText();
            String senha = senhaField.getText();


            if (!Backend.Utilitarios.ValidadorCPF.isCPF(cpf)) {
                showAlert(Alert.AlertType.ERROR, "CPF Inválido", "O CPF inserido não é válido. Por favor, verifique os dados e tente novamente.");
                return; // Interrompe o processo de cadastro se o CPF for inválido
            }


            // Se todas as validações passaram, tenta realizar o cadastro
            if (Backend.Servicos.AuthService.signUp(email, senha, nome, cpf, telefone)) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Cadastro realizado com sucesso!");
                
                double currentWidth = primaryStage.getScene().getWidth();
                double currentHeight = primaryStage.getScene().getHeight();

                Telalogin telaLogin = new Telalogin();
                Parent loginRoot = telaLogin.getRoot(primaryStage);

                Scene loginScene = new Scene(loginRoot, currentWidth, currentHeight);
                primaryStage.setScene(loginScene);
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro no Cadastro", "Não foi possível realizar o cadastro. Verifique os dados ou tente novamente.");
            }
        });
        
        DropShadow glowEffect = new DropShadow(20, Color.rgb(70, 130, 255, 0.7));
        registerButton.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                registerButton.setEffect(glowEffect);
            } else {
                registerButton.setEffect(null);
            }
        });

        Button btnVoltar = new Button("← Voltar para o Login");
        btnVoltar.setCursor(Cursor.HAND);
        btnVoltar.setStyle("-fx-background-color: transparent; -fx-text-fill: #A0A0A0; -fx-font-weight: bold; -fx-font-size: 14px; -fx-border-color: transparent; -fx-underline: false;");
        VBox.setMargin(btnVoltar, new Insets(10, 0, 0, 0));

        btnVoltar.setOnAction(e -> {
            double currentWidth = primaryStage.getScene().getWidth();
            double currentHeight = primaryStage.getScene().getHeight();

            Telalogin telaLogin = new Telalogin();
            Parent loginRoot = telaLogin.getRoot(primaryStage);

            Scene loginScene = new Scene(loginRoot, currentWidth, currentHeight);
            primaryStage.setScene(loginScene);
        });
        
        formVBox.getChildren().addAll(titleLabel, fieldsGrid, registerButton, btnVoltar);
        return formVBox;
    }

    private VBox createFormField(String labelText, String promptText) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 13px; -fx-text-fill: #A0A0A0;");
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle("-fx-background-color: #333333; -fx-text-fill: #E0E0E0; -fx-background-radius: 8; -fx-border-width: 0; -fx-font-size: 14px; -fx-pref-height: 40px;");
        return new VBox(5, label, textField);
    }
    
    private VBox createPasswordField(String labelText, String promptText) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 13px; -fx-text-fill: #A0A0A0;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(promptText);
        passwordField.setStyle("-fx-background-color: #333333; -fx-text-fill: #E0E0E0; -fx-background-radius: 8; -fx-border-width: 0; -fx-font-size: 14px; -fx-pref-height: 40px;");
        return new VBox(5, label, passwordField);
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}