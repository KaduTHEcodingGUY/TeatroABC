package Telas;

import javax.swing.*;
import Backend.Navegador;
import java.awt.*;

public class Home extends JFrame {
    private final Navegador navegador;
    private final ImageIcon imagemBanner;

    public Home(Navegador navegador) {
        this.navegador = navegador;
        this.imagemBanner = new ImageIcon("utilitarios/Banner1.png"); // Caminho direto

        setTitle("Teatro ABC");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(30, 30, 30));

        add(criarBarraLateral(), BorderLayout.WEST);
        add(criarContainerBanner(), BorderLayout.CENTER);
    }

    private JPanel criarBarraLateral() {
        JPanel barraLateral = new JPanel();
        barraLateral.setLayout(new BoxLayout(barraLateral, BoxLayout.Y_AXIS));
        barraLateral.setBackground(new Color(45, 45, 45));
        barraLateral.setPreferredSize(new Dimension(250, getHeight()));
        barraLateral.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel tituloLateral = new JLabel("Menu");
        tituloLateral.setFont(new Font("Arial", Font.BOLD, 16));
        tituloLateral.setForeground(Color.WHITE);
        tituloLateral.setAlignmentX(Component.CENTER_ALIGNMENT);

        barraLateral.add(tituloLateral);
        barraLateral.add(Box.createRigidArea(new Dimension(0, 20)));
        barraLateral.add(criarBotao("Comprar ingresso", new Color(55, 71, 79), "TelaCompraIngresso"));
        barraLateral.add(Box.createRigidArea(new Dimension(0, 10)));
        barraLateral.add(criarBotao("Imprimir ingresso", new Color(69, 90, 100), "TelaImprimirIngresso"));
        barraLateral.add(Box.createRigidArea(new Dimension(0, 10)));
        barraLateral.add(criarBotao("Exibir Estatísticas", new Color(97, 97, 97), "TelaEstatisticas"));
        barraLateral.add(Box.createRigidArea(new Dimension(0, 10)));
        barraLateral.add(criarBotao("Sair", new Color(183, 28, 28), "Sair"));

        return barraLateral;
    }

    private JPanel criarContainerBanner() {
        JPanel containerBanner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagemBanner.getImage() != null) {
                    ImageIcon bannerImg = redimensionarImagem(imagemBanner, getWidth(), getHeight());
                    g.drawImage(bannerImg.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        containerBanner.setBackground(new Color(20, 20, 20));
        containerBanner.setLayout(new BorderLayout());
        return containerBanner;
    }

    private JButton criarBotao(String texto, Color cor, String destino) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setOpaque(true);
        botao.addActionListener(e -> navegador.navegar(destino));
        return botao;
    }

    private ImageIcon redimensionarImagem(ImageIcon iconeOriginal, int largura, int altura) {
        return new ImageIcon(iconeOriginal.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH));
    }
}
