package Telas;
import javax.swing.*;
import Backend.ControleIngressos;
import Backend.SistemaTeatro;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaCompraIngresso extends JFrame {
    private JLabel posterLabel;
    private SistemaTeatro sistemaTeatro;
    private ControleIngressos controleIngresso;
    private MapaTeatroScrollavel mapaTeatro;
    private String pecaSelecionada;
    private String sessaoSelecionada;
    private List<String> assentosSelecionados;

    private ImageIcon bannerPadrao;
    private ImageIcon posterFrozen;
    private ImageIcon posterMichael;
    private ImageIcon posterRomeu;

    public TelaCompraIngresso(MapaTeatroScrollavel mapaTeatro) {
        this.assentosSelecionados = new ArrayList<>();
        this.sistemaTeatro = SistemaTeatro.getInstancia();
        this.controleIngresso = sistemaTeatro.getGerenciadorIngressos();
        this.mapaTeatro = mapaTeatro;

        carregarImagens();
        configurarJanela();
        inicializarComponentes();
    }

    private void carregarImagens() {
        bannerPadrao = new ImageIcon(getClass().getResource("/utilitarios/Banner0.png"));
        posterFrozen = new ImageIcon(getClass().getResource("/utilitarios/poster1.png"));
        posterMichael = new ImageIcon(getClass().getResource("/utilitarios/poster2.png"));
        posterRomeu = new ImageIcon(getClass().getResource("/utilitarios/poster3.png"));
    }

    private void configurarJanela() {
        setTitle("Compra de Ingresso");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(30, 30, 30));
    }

    private void inicializarComponentes() {
        JPanel painelEsquerdo = criarPainelEsquerdo();
        JPanel painelDireito = criarPainelDireito();

        add(painelEsquerdo, BorderLayout.WEST);
        add(painelDireito, BorderLayout.CENTER);
    }

    private JPanel criarPainelEsquerdo() {
        JPanel painelEsquerdo = new JPanel();
        painelEsquerdo.setLayout(new BoxLayout(painelEsquerdo, BoxLayout.Y_AXIS));
        painelEsquerdo.setBackground(new Color(45, 45, 45));
        painelEsquerdo.setPreferredSize(new Dimension(300, getHeight()));
        painelEsquerdo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel dropDownPeca = criarLabel("Selecione a Peça:");
        JComboBox<String> comboPeca = criarComboBox(new String[]{"Frozen o musical", "Musical do Michael Jackson", "Romeu & Julieta"});
        JLabel dropDownSessao = criarLabel("Selecione a Sessão:");
        JComboBox<String> comboSessao = new JComboBox<>();
        JButton btAssento = criarBotao("Selecionar Assento", new Color(55, 71, 79));

        comboPeca.addActionListener(e -> {
            pecaSelecionada = (String) comboPeca.getSelectedItem();
            atualizarPoster(pecaSelecionada);
            atualizarSessoes(pecaSelecionada, comboSessao);
        });

        btAssento.addActionListener(e -> {
            sessaoSelecionada = (String) comboSessao.getSelectedItem();
            if (pecaSelecionada == null || sessaoSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma peça e uma sessão!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                if (mapaTeatro == null) {
                    mapaTeatro = new MapaTeatroScrollavel(this, controleIngresso);
                }
                mapaTeatro.setVisible(true);
            }
        });

        painelEsquerdo.add(dropDownPeca);
        painelEsquerdo.add(Box.createRigidArea(new Dimension(0, 10)));
        painelEsquerdo.add(comboPeca);
        painelEsquerdo.add(Box.createRigidArea(new Dimension(0, 20)));
        painelEsquerdo.add(dropDownSessao);
        painelEsquerdo.add(Box.createRigidArea(new Dimension(0, 10)));
        painelEsquerdo.add(comboSessao);
        painelEsquerdo.add(Box.createRigidArea(new Dimension(0, 20)));
        painelEsquerdo.add(btAssento);
        return painelEsquerdo;
    }

    private JPanel criarPainelDireito() {
        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.setBackground(new Color(20, 20, 20));
        posterLabel = new JLabel(bannerPadrao);
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        painelDireito.add(posterLabel, BorderLayout.CENTER);
        return painelDireito;
    }

    private void atualizarPoster(String pecaSelecionada) {
        switch (pecaSelecionada) {
            case "Frozen o musical":
                posterLabel.setIcon(posterFrozen);
                break;
            case "Musical do Michael Jackson":
                posterLabel.setIcon(posterMichael);
                break;
            case "Romeu & Julieta":
                posterLabel.setIcon(posterRomeu);
                break;
            default:
                posterLabel.setIcon(bannerPadrao);
                break;
        }
    }

    private void atualizarSessoes(String pecaSelecionada, JComboBox<String> comboSessao) {
        comboSessao.removeAllItems();
        String[] sessoes = {"Manhã", "Tarde", "Noite"};
        for (String sessao : sessoes) {
            comboSessao.addItem(sessao);
        }
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JComboBox<String> criarComboBox(String[] opcoes) {
        JComboBox<String> comboBox = new JComboBox<>(opcoes);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBackground(new Color(60, 60, 60));
        comboBox.setForeground(Color.WHITE);
        return comboBox;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setOpaque(true);
        return botao;
    }

    public String getPecaSelecionada() { return pecaSelecionada; }
    public String getSessaoSelecionada() { return sessaoSelecionada; }
    public List<String> getAssentosSelecionados() { return assentosSelecionados; }
    public void setAssentosSelecionados(List<String> assentos) { this.assentosSelecionados = (assentos != null) ? assentos : new ArrayList<>(); }
}
