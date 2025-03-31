package Telas;
import javax.swing.*;


import Backend.ControleIngressos;
import Backend.PrecosPorArea;
import Backend.SistemaTeatro;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MapaTeatroScrollavel extends JFrame  {
    private List<String> assentosSelecionados;
    private SistemaTeatro sistemaTeatro;
    private TelaCompraIngresso telaCompraIngresso;
    private ControleIngressos controleIngressos;
    private JLabel totalLabel;
    private double total;
    private PrecosPorArea precosPorArea;

    public MapaTeatroScrollavel(SistemaTeatro sistemaTeatro, TelaCompraIngresso telaCompraIngresso, ControleIngressos controleIngressos) {
        this.sistemaTeatro = sistemaTeatro;
        this.telaCompraIngresso = telaCompraIngresso;
        this.controleIngressos = controleIngressos;
        this.assentosSelecionados = new ArrayList<>();
        this.precosPorArea = new PrecosPorArea();
        this.total = 0;
        
        configurarJanela();
    }

    private void configurarJanela() {
        setTitle("Mapa de Assentos - Teatro");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.DARK_GRAY);
        
        JPanel containerPrinc = new JPanel(new BorderLayout());
        containerPrinc.setBackground(Color.DARK_GRAY);
        containerPrinc.add(criarPalcoComImagem(), BorderLayout.NORTH);
        containerPrinc.add(criarMapaAssentos(), BorderLayout.CENTER);
        containerPrinc.add(criarPainelInferior(), BorderLayout.SOUTH);
        
        JScrollPane scroll = new JScrollPane(containerPrinc);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scroll);
    }

    private JPanel criarPalcoComImagem() {
        JPanel containerMapa = new JPanel(new BorderLayout());
        containerMapa.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        containerMapa.setBackground(Color.DARK_GRAY);
        JLabel labelImagem = new JLabel(new ImageIcon("PALCO.png"), SwingConstants.CENTER);
        containerMapa.add(labelImagem, BorderLayout.CENTER);
        containerMapa.setPreferredSize(new Dimension(800, 300));
        return containerMapa;
    }

    private JPanel criarPainelInferior() {
        JPanel painelInferior = new JPanel();
        painelInferior.setBackground(Color.DARK_GRAY);
        
        JButton botaoConfirmar = new JButton("Confirmar Seleção");
        botaoConfirmar.setBackground(new Color(76, 175, 80));
        botaoConfirmar.setForeground(Color.WHITE);
        botaoConfirmar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoConfirmar.addActionListener(e -> confirmarSelecao());
        
        totalLabel = new JLabel("Preço Total: R$ 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(Color.WHITE);
        painelInferior.add(totalLabel);
        painelInferior.add(botaoConfirmar);
        return painelInferior;
    }

    private void confirmarSelecao() {
        String pecaSelecionada = telaCompraIngresso.getPecaSelecionada();
        String sessaoSelecionada = telaCompraIngresso.getSessaoSelecionada();
        System.out.println(telaCompraIngresso.getPecaSelecionada());
        System.out.println(telaCompraIngresso.getSessaoSelecionada());

        for (String assento : assentosSelecionados) {
            controleIngressos.incrementarIngresso(assento);
        }
        
        controleIngressos.incrementarIngresso(pecaSelecionada);
        controleIngressos.incrementarIngresso(sessaoSelecionada);

        telaCompraIngresso.setAssentosSelecionados(assentosSelecionados);
        
        JOptionPane.showMessageDialog(this, "Seleção confirmada!");
        dispose();
        
       
    }

    private JPanel criarMapaAssentos() {
        JPanel painelMapa = new JPanel();
        painelMapa.setLayout(new BoxLayout(painelMapa, BoxLayout.Y_AXIS));
        painelMapa.setBackground(Color.DARK_GRAY);
        
        painelMapa.add(criarSecao("Plateia A", 5, 5, Color.YELLOW));
        painelMapa.add(criarSecao("Plateia B", 10, 10, Color.GREEN));
        
        for (int i = 1; i <= 6; i++) {
            painelMapa.add(criarSecao("Frisa " + i, 1, 5, Color.ORANGE));
        }
        
        for (int i = 1; i <= 5; i++) {
            painelMapa.add(criarSecao("Camarote " + i, 2, 5, Color.CYAN));
        }
        
        painelMapa.add(criarSecao("Balcão Nobre", 5, 10, Color.PINK));
        return painelMapa;
    }

    private JPanel criarSecao(String nome, int linhas, int colunas, Color cor) {
        JPanel painelSecao = new JPanel(new BorderLayout());
        painelSecao.setBackground(Color.DARK_GRAY);
        JPanel assentos = new JPanel(new GridLayout(linhas, colunas, 5, 5));
        assentos.setBackground(Color.GRAY);
        
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                String posicao = (i + 1) + "-" + (j + 1) + " da " + nome;
                JButton botaoAssento = new JButton((i + 1) + "-" + (j + 1));
                botaoAssento.setBackground(cor);
                botaoAssento.addActionListener(e -> toggleAssento(botaoAssento, posicao, cor));
                assentos.add(botaoAssento);
            }
        }
        painelSecao.setBorder(BorderFactory.createTitledBorder(nome));
        painelSecao.add(assentos, BorderLayout.CENTER);
        return painelSecao;
    }

    private void toggleAssento(JButton botao, String assento, Color cor) {
        if (assentosSelecionados.contains(assento)) {
            assentosSelecionados.remove(assento);
            botao.setBackground(cor);
        } else {
            assentosSelecionados.add(assento);
            botao.setBackground(Color.RED);
        }
        atualizarPrecoTotal();
    }

    public void atualizarPrecoTotal() {
        total = assentosSelecionados.stream().mapToDouble(assento ->
            precosPorArea.getPrecosPorArea().entrySet().stream().filter(e -> assento.contains(e.getKey()))
                .mapToDouble(Map.Entry::getValue).sum()).sum();
        totalLabel.setText(String.format("Preço Total: R$ %.2f", total));
    }

    public double getTotal() {
        return total;
    }
}
