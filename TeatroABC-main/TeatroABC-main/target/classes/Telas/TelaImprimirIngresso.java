package Telas;
import javax.swing.*;

import Backend.SistemaTeatro;
import Utilitarios.ValidadorCPF;

import java.awt.*;
import java.io.File;

public class TelaImprimirIngresso extends JFrame {
    private SistemaTeatro sistemaTeatro;
    String arquivo;

    public TelaImprimirIngresso() {
        this.sistemaTeatro = SistemaTeatro.getInstancia();

        setTitle("Imprimir Ingresso");
        setSize(900, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        inicializarComponentes(); // Inicializar a interface
    }

    public void inicializarComponentes() {
        // Configuração do layout principal
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(30, 30, 30)); // Fundo escuro

        // Barra lateral removida

        // Container principal
        JPanel containerPrincipal = new JPanel();
        containerPrincipal.setLayout(new BorderLayout());
        containerPrincipal.setBackground(new Color(30, 30, 30)); // Fundo escuro

        // Painel central (campo de CPF e botão "Imprimir")
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new GridBagLayout());
        painelCentral.setBackground(new Color(30, 30, 30)); // Fundo escuro
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre os componentes

        // Campo de texto para CPF
        JLabel labelCPF = new JLabel("Inserir CPF:");
        labelCPF.setFont(new Font("Arial", Font.PLAIN, 16));
        labelCPF.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelCentral.add(labelCPF, gbc);

        JTextField campoCPF = new JTextField(15);
        campoCPF.setFont(new Font("Arial", Font.PLAIN, 16));
         arquivo = campoCPF.getText();
        gbc.gridx = 1;
        gbc.gridy = 0;
        painelCentral.add(campoCPF, gbc);

        // Botão para Imprimir
        JButton botaoImprimir = new JButton("Imprimir");
        botaoImprimir.setFont(new Font("Arial", Font.BOLD, 16));
        botaoImprimir.setBackground(new Color(69, 90, 100)); // Cor cinza
        botaoImprimir.setForeground(Color.WHITE);
        botaoImprimir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Ação do botão "Imprimir"
        botaoImprimir.addActionListener(e -> {
            try {
                String cpf = campoCPF.getText();
        
                // Verifica se o CPF é válido e não está vazio
                if (cpf.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor, insira um CPF.");
                } else if (!ValidadorCPF.isCPF(cpf)) {
                    JOptionPane.showMessageDialog(this, "CPF inválido. Por favor, insira um CPF válido.");
                } else {
                    // CPF válido
                    JOptionPane.showMessageDialog(this, "Ingresso impresso para o CPF: " + cpf);
                    String arquivo = cpf + ".txt"; // Definindo o nome do arquivo
                    BaixarArq(); // Método que deve ser chamado para baixar o arquivo
                }
        
            } catch (Exception err) {
                // Captura qualquer outra exceção e exibe uma mensagem genérica
                JOptionPane.showMessageDialog(this, "Ocorreu um erro ao processar o CPF. Por favor, tente novamente.");
                System.out.println("Erro: " + err.getMessage()); // Exibe a mensagem de erro no console para debug
            }
        });
        

        gbc.gridx = 1;
        gbc.gridy = 1;
        painelCentral.add(botaoImprimir, gbc);

        // Adicionar o painel central ao container principal
        containerPrincipal.add(painelCentral, BorderLayout.CENTER);

        // Container do banner (imagem ou conteúdo adicional)
        JPanel containerBanner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bannerImg = redimensionarImagem("Banner0.png", getWidth(), getHeight());
                g.drawImage(bannerImg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        containerBanner.setBackground(new Color(20, 20, 20)); // Fundo escuro
        containerBanner.setLayout(new BorderLayout());

        // Adicionar o banner ao lado direito
        add(containerPrincipal, BorderLayout.WEST);
        add(containerBanner, BorderLayout.CENTER);
    }

    // Método auxiliar para redimensionar imagens
    private ImageIcon redimensionarImagem(String caminho, int largura, int altura) {
        ImageIcon iconeOriginal = new ImageIcon(caminho);
        Image imagemOriginal = iconeOriginal.getImage();
        Image imagemRedimensionada = imagemOriginal.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
        return new ImageIcon(imagemRedimensionada);
    }
    //Método baixar arquivo

    private void BaixarArq(){
        File arquivoexiste = new File(arquivo);

        if (arquivoexiste.exists()) {
            // Mensagem informando o local do arquivo
            JOptionPane.showMessageDialog(null, "Seu arquivo está disponível em: " + arquivoexiste.getAbsolutePath());
        } else {
            JOptionPane.showMessageDialog(null, "Arquivo não encontrado.");
        }
    }
    

}