package Telas;
import javax.swing.*;

import Backend.CadastroService;
import Backend.Navegador;
import java.awt.*;

public class TelaCadastro extends JFrame {
    private JTextField campoNome;
    private JTextField campoEmail;
    private JTextField campoCPF;
    private JTextField campoTelefone;
    private JPasswordField campoSenha;
    private CadastroService cadastroService;
    private Navegador navegador; 
   
    

    public TelaCadastro(CadastroService cadastroService, Navegador navegador) {
        this.cadastroService = cadastroService;
        this.navegador = navegador;
      
        
        
        setTitle("Cadastro de Usuário");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.DARK_GRAY);
        
        JPanel painelImagem = new JPanel();
        painelImagem.setBackground(new Color(30, 30, 30));
        JLabel labelImagem = new JLabel();
        ImageIcon imagem = new ImageIcon(getClass().getResource("/utilitarios/Banner0.png"));
        labelImagem.setIcon(new ImageIcon(imagem.getImage().getScaledInstance(600, 200, Image.SCALE_SMOOTH)));
        painelImagem.add(labelImagem);
        painelPrincipal.add(painelImagem, BorderLayout.NORTH);
        
        JPanel painelCadastro = new JPanel();
        painelCadastro.setLayout(new GridBagLayout());
        painelCadastro.setBackground(new Color(45, 45, 45));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelTitulo = new JLabel("Bem-vindo ao Teatro ABC");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        painelCadastro.add(labelTitulo, gbc);

        adicionarCampo(painelCadastro, gbc, "Nome Completo:", campoNome = new JTextField(20), 1);
        adicionarCampo(painelCadastro, gbc, "Email:", campoEmail = new JTextField(20), 2);
        adicionarCampo(painelCadastro, gbc, "CPF:", campoCPF = new JTextField(20), 3);
        adicionarCampo(painelCadastro, gbc, "Telefone:", campoTelefone = new JTextField(15), 4);
        adicionarCampo(painelCadastro, gbc, "Senha:", campoSenha = new JPasswordField(20), 5);

        JButton botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setBackground(new Color(76, 175, 80));
        botaoCadastrar.setForeground(Color.WHITE);
        botaoCadastrar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        painelCadastro.add(botaoCadastrar, gbc);
        
        painelPrincipal.add(painelCadastro, BorderLayout.CENTER);
        add(painelPrincipal);

        botaoCadastrar.addActionListener(e -> cadastrarUsuario());
    }

    private void adicionarCampo(JPanel painel, GridBagConstraints gbc, String label, JTextField campo, int y) {
        JLabel jLabel = new JLabel(label);
        jLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        painel.add(jLabel, gbc);
        gbc.gridx = 1;
        painel.add(campo, gbc);
    }

    private void cadastrarUsuario() {
        try {
            String nome = campoNome.getText();
            String email = campoEmail.getText();
            String cpf = campoCPF.getText();
            String telefone = campoTelefone.getText();
            String senha = new String(campoSenha.getPassword());

            cadastroService.cadastrar(nome, email, cpf, telefone, senha);

            JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new Home(navegador).setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
