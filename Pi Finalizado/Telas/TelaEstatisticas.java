package Telas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Backend.SistemaTeatro;

import java.awt.*;

public class TelaEstatisticas extends JFrame {

    // Referência ao sistema principal para buscar dados
    private SistemaTeatro sistemaTeatro;

    // UI Components
    private JTable tabelaEstatisticas;
    private JButton btnAtualizar;

    public TelaEstatisticas(SistemaTeatro sistemaTeatro) {
        this.sistemaTeatro = sistemaTeatro;

        // Configuração inicial do frame
        setTitle("Estatísticas do Teatro");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal
        JPanel painelPrincipal = new JPanel(new BorderLayout());

        // Título da tela
        JLabel titulo = new JLabel("ESTATÍSTICAS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // Configuração da tabela
        String[] colunas = { "Critério", "Resultado" };
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaEstatisticas = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaEstatisticas);

        // Estilo da tabela
        tabelaEstatisticas.setRowHeight(30);
        tabelaEstatisticas.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaEstatisticas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Botão para atualizar
        btnAtualizar = new JButton("Atualizar Estatísticas");
        btnAtualizar.addActionListener(e -> atualizarEstatisticas(modeloTabela));
        painelPrincipal.add(btnAtualizar, BorderLayout.SOUTH);

        // Adiciona o painel ao frame
        add(painelPrincipal);
    }

    // Método para atualizar a tabela com estatísticas
    private void atualizarEstatisticas(DefaultTableModel modeloTabela) {
        // Limpa a tabela antes de adicionar novos dados
        modeloTabela.setRowCount(0);

        // Critérios e seus resultados
        modeloTabela.addRow(new Object[] { "Peça com mais ingressos vendidos", pecaMaisIngressos() });
        modeloTabela.addRow(new Object[] { "Peça com menos ingressos vendidos", pecaMenosIngressos() });
        modeloTabela.addRow(new Object[] { "Sessão com maior ocupação", sessaoMaiorOcupacao() });
        modeloTabela.addRow(new Object[] { "Sessão com menor ocupação", sessaoMenorOcupacao() });
        modeloTabela.addRow(new Object[] { "Peça mais lucrativa", pecaMaisLucrativa()});
        modeloTabela.addRow(new Object[] { "Peça menos lucrativa", pecaMenosLucrativa()});
        modeloTabela.addRow(new Object[] { "Lucro médio do teatro por peça", lucroMedioTeatro()});
    }

    // Métodos para calcular estatísticas
    private String pecaMaisIngressos() {
        return sistemaTeatro.getPecaMaisIngressos(); // Implementação na classe SistemaTeatro
    }

    private String pecaMenosIngressos() {
        return sistemaTeatro.getPecaMenosIngressos(); // Implementação na classe SistemaTeatro
    }

    private String sessaoMaiorOcupacao() {
        return sistemaTeatro.getSessaoMaiorOcupacao(); // Implementação na classe SistemaTeatro
    }

    private String sessaoMenorOcupacao() {
        return sistemaTeatro.getSessaoMenorOcupacao(); // Implementação na classe SistemaTeatro
    }
   
    private String pecaMaisLucrativa() {
        return sistemaTeatro.getPecaMaisLucrativa(); // Implementação na classe SistemaTeatro
    }

    private String pecaMenosLucrativa() {
        return sistemaTeatro.getPecaMenosLucrativa(); // Implementação na classe SistemaTeatro
    }
    private String lucroMedioTeatro(){
       return sistemaTeatro.getLucroMedio();
    }

    
}
