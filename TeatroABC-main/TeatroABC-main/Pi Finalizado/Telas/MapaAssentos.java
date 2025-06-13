package Telas; // Ou o seu pacote de views

import Interfaces.ProvedorView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// MODIFICADO: O nome da classe foi alterado para MapaAssentos
public class MapaAssentos implements ProvedorView {

    private record Setor(String nome, double preco) {}

    private BorderPane rootLayout;
    private Button btnComprar;
    private List<ToggleButton> assentosSelecionados = new ArrayList<>();
    
    private VBox setorSelecionadoBox = null;

    private final String STYLE_ASSENTO_DISPONIVEL = "-fx-background-color: #555555; -fx-background-radius: 3px;";
    private final String STYLE_ASSENTO_SELECIONADO = "-fx-background-color: #DC3545; -fx-background-radius: 3px;";
    
    private final String STYLE_SETOR_NORMAL = "-fx-background-color: transparent; -fx-padding: 8px; -fx-border-color: transparent;";
    private final String STYLE_SETOR_HOVER = "-fx-background-color: #404040; -fx-padding: 8px; -fx-background-radius: 5px;";
    private final String STYLE_SETOR_SELECIONADO = "-fx-background-color: #005A9E; -fx-padding: 8px; -fx-background-radius: 5px;";

    @Override
    public Node getView() {
        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: black;");

        // Passamos 'this' para a sidebar para que ela possa chamar o método updateMap
        VBox sidebar = createSidebar(this);
        rootLayout.setLeft(sidebar);

        btnComprar = new Button("Buy Ticket");
        btnComprar.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 25; -fx-padding: 10px 25px;");
        btnComprar.setOnAction(e -> {
            System.out.println("Comprando " + assentosSelecionados.size() + " ingressos.");
        });
        
        updateMap("Mapa Teatro");

        return rootLayout;
    }

    private VBox createSidebar(MapaAssentos viewManager) {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: #2D2D2D;");
        sidebar.setAlignment(Pos.TOP_LEFT);
        
        Button btnVoltar = new Button("← Voltar para seleção");
        btnVoltar.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        btnVoltar.setCursor(Cursor.HAND);
        btnVoltar.setOnAction(e -> System.out.println("Ação de voltar clicada"));
        
        VBox mapaTeatroBox = createSetorOption("Mapa Teatro", "Visão geral", viewManager);

        List<Setor> setores = new ArrayList<>(List.of(
            new Setor("Plateia A", 40.00),
            new Setor("Plateia B", 60.00),
            new Setor("Frisa", 120.00),
            new Setor("Camarote", 80.00),
            new Setor("Balcão Nobre", 250.00)
        ));

        setores.sort(Comparator.comparingDouble(Setor::preco));

        sidebar.getChildren().add(btnVoltar);
        sidebar.getChildren().add(mapaTeatroBox);

        for (Setor setor : setores) {
            VBox setorBox = createSetorOption(setor.nome(), String.format("R$ %.2f", setor.preco()), viewManager);
            sidebar.getChildren().add(setorBox);
        }

        setorSelecionadoBox = mapaTeatroBox;
        setorSelecionadoBox.setStyle(STYLE_SETOR_SELECIONADO);

        return sidebar;
    }

    private VBox createSetorOption(String nome, String preco, MapaAssentos viewManager) {
        VBox container = new VBox(-2);
        container.setCursor(Cursor.HAND);

        Label nomeLabel = new Label(nome);
        nomeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        nomeLabel.setTextFill(Color.WHITE);

        Label precoLabel = new Label(preco);
        precoLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        precoLabel.setTextFill(Color.LIGHTGRAY);

        container.getChildren().addAll(nomeLabel, precoLabel);
        container.setStyle(STYLE_SETOR_NORMAL);

        container.setOnMouseEntered(e -> {
            if (container != setorSelecionadoBox) {
                container.setStyle(STYLE_SETOR_HOVER);
            }
        });
        container.setOnMouseExited(e -> {
            if (container != setorSelecionadoBox) {
                container.setStyle(STYLE_SETOR_NORMAL);
            }
        });
        container.setOnMouseClicked(e -> {
            if (setorSelecionadoBox != null) {
                setorSelecionadoBox.setStyle(STYLE_SETOR_NORMAL);
            }
            setorSelecionadoBox = container;
            setorSelecionadoBox.setStyle(STYLE_SETOR_SELECIONADO);
            viewManager.updateMap(nome);
        });

        return container;
    }
    
    public void updateMap(String sectionName) {
        assentosSelecionados.clear();
        GridPane seatMap = createSeatGrid(sectionName);
        VBox centerContainer = new VBox(20);
        centerContainer.setAlignment(Pos.CENTER);
        
        if (!"Mapa Teatro".equals(sectionName)) {
            centerContainer.getChildren().addAll(seatMap, btnComprar);
        } else {
            centerContainer.getChildren().add(seatMap);
        }
        rootLayout.setCenter(centerContainer);
    }
    
    private GridPane createSeatGrid(String section) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);

        Arc telaShape = new Arc(0, 0, 200, 50, 0, -180);
        telaShape.setType(ArcType.OPEN);
        telaShape.setStroke(Color.WHITE);
        telaShape.setStrokeWidth(2);
        telaShape.setFill(Color.TRANSPARENT);
        Label telaLabel = new Label("TELA");
        telaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        telaLabel.setTextFill(Color.WHITE);
        StackPane telaContainer = new StackPane(telaShape, telaLabel);
        grid.add(telaContainer, 0, 0, 10, 1);

        int rows = "Mapa Teatro".equals(section) || "Plateia A".equals(section) ? 10 : 5;
        int cols = 12;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (col == 2 || col == 9) continue;
                if (row > 5 && (col < 2 || col > 9)) continue;
                ToggleButton seat = createSeatButton();
                grid.add(seat, col, row + 2);
            }
        }
        return grid;
    }

    private ToggleButton createSeatButton() {
        ToggleButton seat = new ToggleButton();
        seat.setPrefSize(25, 25);
        seat.setStyle(STYLE_ASSENTO_DISPONIVEL);
        seat.setCursor(Cursor.HAND);
        seat.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            seat.setStyle(isSelected ? STYLE_ASSENTO_SELECIONADO : STYLE_ASSENTO_DISPONIVEL);
            if (isSelected) {
                assentosSelecionados.add(seat);
            } else {
                assentosSelecionados.remove(seat);
            }
        });
        return seat;
    }
}