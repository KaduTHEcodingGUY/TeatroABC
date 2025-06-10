package Telas;

import Interfaces.ProvedorView;
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

public class ClubeFidelidade implements ProvedorView {

    @Override
    public Node getView() {
        BorderPane viewLayout = new BorderPane();

        Image imagemFundo = new Image(getClass().getResourceAsStream("/Utilitarios/TesteHome.jpg"));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, true); // cover
        BackgroundImage backgroundImage = new BackgroundImage(imagemFundo, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        viewLayout.setBackground(new Background(backgroundImage));

        Label titulo = new Label("Clube Fidelidade");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titulo.setTextFill(Color.web("#FFD700"));
        BorderPane.setAlignment(titulo, Pos.CENTER);
        titulo.setPadding(new Insets(50, 0, 0, 0));
        viewLayout.setTop(titulo);
        
        VBox centerContent = new VBox(35);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(0, 20, 20, 20));

        String[] beneficios = {"→ 10% de desconto em todos os ingressos", "→ Acumule pontos e troque por combos de pipoca", "→ Acesso antecipado a pré-estreias exclusivas"};
        for (String texto : beneficios) {
            Label beneficioLabel = new Label(texto);
            beneficioLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 22));
            beneficioLabel.setTextFill(Color.WHITE);
            centerContent.getChildren().add(beneficioLabel);
        }

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
        centerContent.getChildren().add(btnAssinar);
        
        viewLayout.setCenter(centerContent);

        return viewLayout;
    }
}