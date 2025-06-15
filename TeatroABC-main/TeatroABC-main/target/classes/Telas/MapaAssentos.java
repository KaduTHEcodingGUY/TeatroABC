package Telas; // Ou o seu pacote de views

import Interfaces.ProvedorView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import Backend.Servicos.AssentoService;
import Backend.Servicos.AssentoService.AreaAssento;
import Backend.Servicos.AssentoService.Assento;
import Backend.Servicos.AssentoService.AssentoReservado;
import java.util.stream.Collectors; // Adicionado para Collectors

// MODIFICADO: O nome da classe foi alterado para MapaAssentos
public class MapaAssentos implements ProvedorView {

    // MODIFICADO: Adicionado campo id ao record Setor
    private record Setor(String id, String nome, double preco) {}

    private BorderPane rootLayout;
    private Button btnComprar;
    private List<String> assentosSelecionadosIds = new ArrayList<>(); // MODIFICADO: Armazena IDs de assentos selecionados
    
    private VBox setorSelecionadoBox = null;
    private Runnable onBackAction;
    private String sessaoId;
    // NOVO: Adicionado viewSwitcher para navegação.
    private java.util.function.BiConsumer<Node, Boolean> viewSwitcher;

    private String selectedSubCategory; // NOVO: Campo para armazenar a subclasse selecionada (ex: "C1", "F2")
    private String currentAreaId; // NOVO: Campo para armazenar o ID da área atualmente exibida (ex: "Camarote", "Frisa")
    private String currentAreaName; // NOVO: Campo para armazenar o NOME da área atualmente exibida

    private final String STYLE_ASSENTO_DISPONIVEL = "-fx-background-color: #555555; -fx-background-radius: 3px;";
    private final String STYLE_ASSENTO_SELECIONADO = "-fx-background-color: #DC3545; -fx-background-radius: 3px;";
    
    private final String STYLE_SETOR_NORMAL = "-fx-background-color: transparent; -fx-padding: 8px; -fx-border-color: transparent;";
    private final String STYLE_SETOR_HOVER = "-fx-background-color: #404040; -fx-padding: 8px; -fx-background-radius: 5px;";
    private final String STYLE_SETOR_SELECIONADO = "-fx-background-color: #005A9E; -fx-padding: 8px; -fx-background-radius: 5px;";
    private final String STYLE_ASSENTO_RESERVADO = "-fx-background-color: #8B0000; -fx-background-radius: 3px;"; // NOVO: Estilo para assentos reservados

    // MODIFICADO: Construtor padrão que chama o novo construtor com sessaoId e viewSwitcher nulos
    public MapaAssentos() {
        this(null, null, null);
    }

    // MODIFICADO: Construtor para receber a ação de retorno, o sessaoId e o viewSwitcher
    public MapaAssentos(Runnable onBackAction, String sessaoId, java.util.function.BiConsumer<Node, Boolean> viewSwitcher) {
        System.out.println("[DEBUG MapaAssentos] Construtor MapaAssentos chamado com sessaoId: " + sessaoId);
        this.onBackAction = onBackAction;
        this.sessaoId = sessaoId; // Inicializa o sessaoId
        this.viewSwitcher = viewSwitcher; // Inicializa o viewSwitcher
        this.selectedSubCategory = null; // Inicializa sem subcategoria selecionada
        this.currentAreaId = null; // Inicializa sem área atual
        this.currentAreaName = null; // Inicializa sem nome de área atual
    }

    @Override
    public Node getView() {
        System.out.println("[DEBUG MapaAssentos] getView() chamado.");
        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: black;");

        VBox sidebar = createSidebar(this);
        rootLayout.setLeft(sidebar);

        btnComprar = new Button("Efetuar Compra");
        btnComprar.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 25; -fx-padding: 10px 25px;");
        btnComprar.setOnAction(e -> {
            System.out.println("[DEBUG MapaAssentos] Botão Efetuar Compra clicado.");
            if (assentosSelecionadosIds.isEmpty()) {
                System.out.println("[DEBUG MapaAssentos] Nenhum assento selecionado.");
                // Adicionar um alerta ou mensagem para o usuário aqui
                return;
            }
            
            System.out.println("[DEBUG MapaAssentos] Assentos selecionados (IDs): " + assentosSelecionadosIds);

            if (viewSwitcher != null) {
                System.out.println("[DEBUG MapaAssentos] Navegando para TelaResumoCompra...");
                TelaResumoCompra resumoView = new TelaResumoCompra(sessaoId, assentosSelecionadosIds, viewSwitcher);
                viewSwitcher.accept(resumoView.getView(), false); // Ocultar sidebar para o resumo
            } else {
                System.err.println("[ERRO MapaAssentos] viewSwitcher é nulo. Não é possível navegar para o resumo da compra.");
            }
        });
        
        updateMap(null, null, null); // MODIFICADO: Passa null para areaId, subCategory e areaName inicialmente

        return rootLayout;
    }

    private VBox createSidebar(MapaAssentos viewManager) {
        System.out.println("[DEBUG MapaAssentos] createSidebar() chamado.");
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: #2D2D2D;");
        sidebar.setAlignment(Pos.TOP_LEFT);
        
        Button btnVoltar = new Button("← Voltar para seleção");
        btnVoltar.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        btnVoltar.setCursor(Cursor.HAND);
        btnVoltar.setOnAction(e -> {
            System.out.println("[DEBUG MapaAssentos] Botão Voltar para seleção clicado.");
            if (viewSwitcher != null) {
                TelaCompraIngresso compraView = new TelaCompraIngresso(viewSwitcher);
                viewSwitcher.accept(compraView.getView(), true);
            } else {
                System.out.println("[DEBUG MapaAssentos] viewSwitcher é nulo. Não é possível voltar.");
            }
        });
        
        VBox mapaTeatroBox = createSetorOption(null, "Mapa Teatro", "Visão geral", viewManager); // MODIFICADO: id nulo para Mapa Teatro

        // MODIFICADO: Busca áreas de assentos do serviço
        List<AssentoService.AreaAssento> areasBanco = AssentoService.listarAreasAssentos();
        System.out.println("[DEBUG MapaAssentos] Áreas de assentos carregadas do banco: " + areasBanco.size());

        // Converte AreaAssento para Setor
        List<Setor> setores = new ArrayList<>();
        for (AreaAssento area : areasBanco) {
            setores.add(new Setor(area.getId(), area.getNomeArea(), area.getPreco()));
        }

        setores.sort(Comparator.comparingDouble(Setor::preco));

        sidebar.getChildren().add(btnVoltar);
        sidebar.getChildren().add(mapaTeatroBox);

        for (Setor setor : setores) {
            // MODIFICADO: Passa o ID e o nome do setor para createSetorOption
            VBox setorBox = createSetorOption(setor.id(), setor.nome(), String.format("R$ %.2f", setor.preco()), viewManager);
            sidebar.getChildren().add(setorBox);
        }

        // NOVO: A seleção inicial do setor é manipulada no updateMap
        // setorSelecionadoBox = mapaTeatroBox;
        // setorSelecionadoBox.setStyle(STYLE_SETOR_SELECIONADO);

        return sidebar;
    }

    private VBox createSetorOption(String id, String nome, String preco, MapaAssentos viewManager) {
        System.out.println("[DEBUG MapaAssentos] createSetorOption() chamado para: " + nome + " (ID: " + id + ").");
        VBox container = new VBox(-2);
        container.setCursor(Cursor.HAND);
        
        // Define um user data para o ID da área, para fácil acesso no clique
        container.setUserData(id);

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
            System.out.println("[DEBUG MapaAssentos] Setor clicado: " + nome + " (ID: " + id + ").");
            if (setorSelecionadoBox != null) {
                setorSelecionadoBox.setStyle(STYLE_SETOR_NORMAL);
            }
            setorSelecionadoBox = container;
            setorSelecionadoBox.setStyle(STYLE_SETOR_SELECIONADO);
            viewManager.updateMap((String) container.getUserData(), null, nome); // MODIFICADO: Passa null para subCategory e o nome da área
        });

        return container;
    }
    
    // MODIFICADO: Agora updateMap aceita um parâmetro para subcategoria e gerencia a limpeza de assentos selecionados
    public void updateMap(String newAreaId, String newSubCategory, String areaName) {
        System.out.println("[DEBUG MapaAssentos] updateMap() chamado com newAreaId: " + newAreaId + ", newSubCategory: " + newSubCategory + ", areaName: " + areaName);

        // Limpa os assentos selecionados apenas se estiver voltando para o mapa geral
        if (newAreaId == null) { // Caso seja "Mapa Teatro (Visão geral)"
             System.out.println("[DEBUG MapaAssentos] Limpando assentos selecionados (voltando ao mapa geral). Assentos antes da limpeza: " + assentosSelecionadosIds);
             assentosSelecionadosIds.clear();
             this.selectedSubCategory = null;
        }

        this.currentAreaId = newAreaId;
        this.currentAreaName = areaName; // NOVO: Atribui o nome da área
        this.selectedSubCategory = newSubCategory; // Atualiza a subcategoria selecionada

        // Se for Camarote ou Frisa e não tiver subcategoria selecionada, seleciona a primeira
        if (("Camarote".equalsIgnoreCase(areaName) || "Frisa".equalsIgnoreCase(areaName)) && selectedSubCategory == null) {
            if ("Camarote".equalsIgnoreCase(areaName)) {
                selectedSubCategory = "C1";
            } else if ("Frisa".equalsIgnoreCase(areaName)) {
                selectedSubCategory = "F1";
            }
            System.out.println("[DEBUG MapaAssentos] Selecionando primeira subcategoria automaticamente: " + selectedSubCategory);
        }

        System.out.println("[DEBUG MapaAssentos] Estado atual: currentAreaId=" + currentAreaId + ", currentAreaName=" + currentAreaName + ", selectedSubCategory=" + selectedSubCategory + ", assentosSelecionadosIds.size()=" + assentosSelecionadosIds.size());

        // NOVO: Atualiza a seleção visual na sidebar
        if (rootLayout != null) { // Ensure rootLayout is initialized
            Node sidebarNode = rootLayout.getLeft();
            if (sidebarNode instanceof VBox) {
                VBox sidebar = (VBox) sidebarNode;
                for (Node child : sidebar.getChildren()) {
                    if (child instanceof VBox && child.getUserData() != null) {
                        // A comparação deve ser feita com o ID para selecionar o item correto na sidebar
                        if (child.getUserData().equals(currentAreaId)) {
                            if (setorSelecionadoBox != null) {
                                setorSelecionadoBox.setStyle(STYLE_SETOR_NORMAL);
                            }
                            setorSelecionadoBox = (VBox) child;
                            setorSelecionadoBox.setStyle(STYLE_SETOR_SELECIONADO);
                        } else {
                            child.setStyle(STYLE_SETOR_NORMAL);
                        }
                    }
                }
            }
        }


        GridPane seatMap;
        List<Assento> assentosDaArea = new ArrayList<>();
        List<AssentoReservado> assentosReservadosSessao = new ArrayList<>();

        if (currentAreaId == null) { // Caso seja "Mapa Teatro"
            System.out.println("[DEBUG MapaAssentos] Exibindo mapa geral do teatro.");
            seatMap = createSeatGrid(currentAreaId, assentosDaArea, assentosReservadosSessao);
            btnComprar.setVisible(false); // Oculta o botão de comprar no mapa geral
        } else {
            System.out.println("[DEBUG MapaAssentos] Carregando assentos para a área: " + currentAreaId);
            assentosDaArea = AssentoService.listarAssentosPorArea(currentAreaId);
            System.out.println("[DEBUG MapaAssentos] Assentos carregados para área " + currentAreaId + ": " + assentosDaArea.size());

            if (sessaoId != null) {
                assentosReservadosSessao = AssentoService.listarAssentosReservados(sessaoId);
                System.out.println("[DEBUG MapaAssentos] Assentos reservados para a sessão " + sessaoId + ": " + assentosReservadosSessao.size());
            } else {
                System.out.println("[DEBUG MapaAssentos] sessaoId é nulo, não buscando assentos reservados.");
            }

            // MODIFICADO: createSeatGrid agora usa a subcategoria armazenada em `selectedSubCategory`
            seatMap = createSeatGrid(currentAreaId, assentosDaArea, assentosReservadosSessao);
            btnComprar.setVisible(true); // Exibe o botão de comprar para áreas específicas
        }

        VBox centerContainer = new VBox(20);
        centerContainer.setAlignment(Pos.CENTER);

        // NOVO: Adiciona ChoiceChips se for Camarote ou Frisa, usando currentAreaName
        boolean shouldShowChips = "Camarote".equalsIgnoreCase(currentAreaName) || "Frisa".equalsIgnoreCase(currentAreaName);
        System.out.println("[DEBUG MapaAssentos] Verificando se deve mostrar ChoiceChips: areaName=" + currentAreaName + ", shouldShowChips=" + shouldShowChips);
        if (shouldShowChips) {
            HBox chipContainer = createSubCategoryChips(currentAreaName);
            centerContainer.getChildren().add(chipContainer);
        }
        
        centerContainer.getChildren().addAll(seatMap, btnComprar); // Adiciona o botão de comprar aqui
        
        rootLayout.setCenter(centerContainer);
    }
    
    // NOVO: Método para criar os ChoiceChips
    private HBox createSubCategoryChips(String areaType) {
        System.out.println("[DEBUG MapaAssentos] createSubCategoryChips() chamado para areaType: " + areaType);
        HBox chipsBox = new HBox(10);
        chipsBox.setAlignment(Pos.CENTER);
        chipsBox.setPadding(new Insets(10));

        List<String> subCategories = new ArrayList<>();
        if ("Camarote".equalsIgnoreCase(areaType)) {
            for (int i = 1; i <= 5; i++) {
                subCategories.add("C" + i);
            }
        } else if ("Frisa".equalsIgnoreCase(areaType)) {
            for (int i = 1; i <= 6; i++) {
                subCategories.add("F" + i);
            }
        }

        // Determine if a default selection needs to be made for this area type
        // This handles cases where user clicks a main area (Camarote/Frisa) for the first time
        // or comes back to it and no sub-category was previously active, or the previous sub-category is not in the current list.
        boolean needsDefaultSelection = (selectedSubCategory == null || !subCategories.contains(selectedSubCategory));
        String defaultSubCategory = null;
        if (needsDefaultSelection && !subCategories.isEmpty()) {
            defaultSubCategory = subCategories.get(0);
        }

        for (String sub : subCategories) {
            ToggleButton chip = new ToggleButton(sub);
            chip.setUserData(sub);
            chip.setStyle("-fx-background-color: #444444; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 5 10;");
            
            chip.setOnMouseEntered(e -> {
                if (!chip.isSelected()) {
                    chip.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 5 10;");
                }
            });
            chip.setOnMouseExited(e -> {
                if (!chip.isSelected()) {
                    chip.setStyle("-fx-background-color: #444444; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 5 10;");
                }
            });
            
            // Set initial selection for the chip
            if (sub.equals(selectedSubCategory) || (needsDefaultSelection && sub.equals(defaultSubCategory))) {
                chip.setSelected(true);
                chip.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 5 10;");
            }

            chip.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                if (isSelected) {
                    // Deselect other chips in the same group
                    for (Node node : chipsBox.getChildren()) {
                        if (node instanceof ToggleButton && node != chip) {
                            ((ToggleButton) node).setSelected(false);
                            ((ToggleButton) node).setStyle("-fx-background-color: #444444; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 5 10;");
                        }
                    }
                    chip.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 5 10;");
                    // Only call updateMap if the subCategory actually changed to avoid unnecessary re-renders
                    String newSelectedSub = (String) chip.getUserData();
                    if (!newSelectedSub.equals(selectedSubCategory)) {
                        System.out.println("[DEBUG MapaAssentos] Chip selecionado: " + newSelectedSub + ". Chamando updateMap com currentAreaId: " + currentAreaId + ", currentAreaName: " + currentAreaName);
                        updateMap(currentAreaId, newSelectedSub, currentAreaName); // MODIFICADO: Passa currentAreaId, areaType e currentAreaName
                    }
                } else {
                    // Prevent deselection if it's the only selected chip
                    boolean anyOtherChipSelected = chipsBox.getChildren().stream()
                                                            .filter(node -> node instanceof ToggleButton && node != chip)
                                                            .anyMatch(node -> ((ToggleButton) node).isSelected());
                    if (!anyOtherChipSelected && sub.equals(selectedSubCategory)) {
                        // This means the user tried to deselect the last active chip.
                        // Re-select it to enforce always having one selected.
                        chip.setSelected(true);
                        chip.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 5 10;");
                    } else {
                        chip.setStyle("-fx-background-color: #444444; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 5 10;");
                    }
                }
            });
            chipsBox.getChildren().add(chip);
        }

        // If a default selection was made and it's different from the current selectedSubCategory,
        // trigger a map update to show the correct sub-category initially.
        if (needsDefaultSelection && defaultSubCategory != null && (selectedSubCategory == null || !selectedSubCategory.equals(defaultSubCategory))) {
            // This is crucial for the first time entering Camarote/Frisa area.
            // We need to set the internal state and trigger a re-render.
            // The listener will also call updateMap, but this ensures the initial state is correct.
            // To avoid double re-render, we can set selectedSubCategory and call createSeatGrid directly.
            // However, calling updateMap handles the full lifecycle.
            // The check `if (!newSelectedSub.equals(selectedSubCategory))` in the listener prevents redundant calls.
            // So, setting chip.setSelected(true) will trigger the listener, which will call updateMap.
            // No explicit updateMap call needed here after setting chip.setSelected(true).
            // The `if (sub.equals(selectedSubCategory) || (needsDefaultSelection && sub.equals(defaultSubCategory)))`
            // block above handles this.
        }

        return chipsBox;
    }

    // MODIFICADO: Agora aceita listas de assentos e assentos reservados e filtra por subcategoria
    private GridPane createSeatGrid(String areaId, List<Assento> assentosDaArea, List<AssentoReservado> assentosReservadosSessao) {
        System.out.println("[DEBUG MapaAssentos] createSeatGrid() chamado para areaId: " + areaId + ", subCategory: " + selectedSubCategory + ", assentos: " + assentosDaArea.size() + ", reservados: " + assentosReservadosSessao.size() + ", currentAreaName=" + currentAreaName);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);

        if (areaId == null) { // Se for o mapa geral, exibe a imagem
            System.out.println("[DEBUG MapaAssentos] Exibindo imagem do mapa geral.");
            try {
                Image image = new Image("file:C:/Users/norma/Downloads/SappensV2Git/TeatroABC/TeatroABC/TeatroABC-main/TeatroABC-main/Pi Finalizado/Utilitarios/PALCO.png");
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(600); // Ajuste o tamanho conforme necessário
                imageView.setPreserveRatio(true);
                grid.add(imageView, 0, 0, 12, 12); // Adiciona a imagem ao grid
            } catch (Exception e) {
                System.err.println("[ERRO MapaAssentos] Erro ao carregar a imagem do mapa do teatro: " + e.getMessage());
                // Fallback para a tela e label existentes, caso a imagem não carregue
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
            }
        } else { // Se for uma área específica, exibe os assentos
            System.out.println("[DEBUG MapaAssentos] Exibindo assentos para a área: " + areaId);
            Arc telaShape = new Arc(0, 0, 200, 50, 0, -180);
            telaShape.setType(ArcType.OPEN);
            telaShape.setStroke(Color.WHITE);
            telaShape.setStrokeWidth(2);
            telaShape.setFill(Color.TRANSPARENT);
            Label telaLabel = new Label("TELA");
            telaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            telaLabel.setTextFill(Color.WHITE);
            StackPane telaContainer = new StackPane(telaShape, telaLabel);
            grid.add(telaContainer, 0, 0, 10, 1); // A "TELA" sempre aparece

            // NOVO: Filtrar assentos se uma subcategoria estiver selecionada e for Camarote ou Frisa
            List<Assento> assentosFiltrados = new ArrayList<>();
            boolean shouldFilterBySubcategory = selectedSubCategory != null && ("Camarote".equalsIgnoreCase(currentAreaName) || "Frisa".equalsIgnoreCase(currentAreaName));
            System.out.println("[DEBUG MapaAssentos] Should filter by subcategory: " + shouldFilterBySubcategory + ", selectedSubCategory: " + selectedSubCategory);
            if (shouldFilterBySubcategory) {
                for (Assento assento : assentosDaArea) {
                    if (assento.getFileira().equalsIgnoreCase(selectedSubCategory)) {
                        assentosFiltrados.add(assento);
                    }
                }
                System.out.println("[DEBUG MapaAssentos] Assentos filtrados para subcategoria " + selectedSubCategory + ": " + assentosFiltrados.size());
            } else {
                assentosFiltrados.addAll(assentosDaArea); // Se não houver subcategoria selecionada, usa todos
            }
            
            // Mapeamento de fileiras para índices de linha (ajuste conforme necessário)
            // Assumindo que as fileiras são letras e queremos uma ordem alfabética
            List<String> fileirasOrdenadas = assentosFiltrados.stream()
                                                    .map(Assento::getFileira)
                                                    .distinct()
                                                    .sorted()
                                                    .collect(Collectors.toList()); // Usando Collectors.toList()
            
            // Adicionar mais debug aqui
            System.out.println("[DEBUG MapaAssentos] Fileiras ordenadas (após filtro): " + fileirasOrdenadas);

            for (Assento assento : assentosFiltrados) {
                int col = assento.getNumero() - 1; // Ajuste para índice 0-base
                int row = fileirasOrdenadas.indexOf(assento.getFileira()) + 2; // +2 para compensar a "TELA"

                if (col >= 0 && row >= 2) { // Validação básica de posição
                    ToggleButton seat = createSeatButton();
                    seat.setUserData(assento.getId()); // Armazena o ID do assento
                    
                    // NOVO: Verificar se o assento já está na lista global de selecionados
                    boolean isAlreadySelected = assentosSelecionadosIds.contains(assento.getId());

                    boolean isReserved = assentosReservadosSessao.stream()
                                                        .anyMatch(r -> r.getAssentoId().equals(assento.getId()));
                    
                    if (isReserved) {
                        System.out.println("[DEBUG MapaAssentos] Assento " + assento.getFileira() + assento.getNumero() + " (ID: " + assento.getId() + ") está RESERVADO.");
                        seat.setStyle(STYLE_ASSENTO_RESERVADO);
                        seat.setDisable(true); // Desabilita assentos reservados
                    } else {
                        System.out.println("[DEBUG MapaAssentos] Assento " + assento.getFileira() + assento.getNumero() + " (ID: " + assento.getId() + ") está DISPONÍVEL.");
                        if (isAlreadySelected) {
                            seat.setSelected(true); // Marca como selecionado se já estiver na lista global
                            seat.setStyle(STYLE_ASSENTO_SELECIONADO);
                        } else {
                            seat.setStyle(STYLE_ASSENTO_DISPONIVEL);
                        }
                    }
                    grid.add(seat, col, row);
                } else {
                    System.err.println("[ERRO MapaAssentos] Posição de assento inválida: Fileira " + assento.getFileira() + ", Número " + assento.getNumero());
                }
            }
        }
        return grid;
    }

    private ToggleButton createSeatButton() {
        ToggleButton seat = new ToggleButton();
        seat.setPrefSize(25, 25);
        // Initial style is set by the calling method based on isReserved or isAlreadySelected.
        seat.setCursor(Cursor.HAND);
        seat.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            String assentoId = (String) seat.getUserData();
            if (isSelected) {
                if (!assentosSelecionadosIds.contains(assentoId)) { // Avoid duplicates
                    assentosSelecionadosIds.add(assentoId);
                }
            } else {
                assentosSelecionadosIds.remove(assentoId);
            }
            // Update style immediately after selection change
            seat.setStyle(isSelected ? STYLE_ASSENTO_SELECIONADO : STYLE_ASSENTO_DISPONIVEL);
        });

        return seat;
    }
}