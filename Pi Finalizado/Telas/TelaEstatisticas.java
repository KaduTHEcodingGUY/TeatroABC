package Telas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Backend.SistemaTeatro;
import java.awt.*;

public class TelaEstatisticas extends JFrame {
    private SistemaTeatro sistemaTeatro;
    private JTable tabelaEstatisticas;
    private DefaultTableModel modeloTabela;
    private JButton btnAtualizar;

    public TelaEstatisticas() {
        this.sistemaTeatro = SistemaTeatro.getInstancia();
        
        configurarJanela();
        inicializarComponentes();
        atualizarUI();
    }

    private void configurarJanela() {
        setTitle("Estatísticas do Teatro");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        JLabel titulo = new JLabel("ESTATÍSTICAS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        add(titulo, BorderLayout.NORTH);

        String[] colunas = {"Critério", "Resultado"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaEstatisticas = new JTable(modeloTabela);
        tabelaEstatisticas.setRowHeight(30);
        tabelaEstatisticas.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaEstatisticas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(tabelaEstatisticas);
        add(scrollPane, BorderLayout.CENTER);

        btnAtualizar = new JButton("Atualizar Estatísticas");
        btnAtualizar.addActionListener(e -> atualizarUI());
        add(btnAtualizar, BorderLayout.SOUTH);
    }

    private void atualizarUI() {
        modeloTabela.setRowCount(0); 
        modeloTabela.addRow(new Object[]{"Peça com mais ingressos vendidos", sistemaTeatro.getPecaMaisIngressos()});
        modeloTabela.addRow(new Object[]{"Peça com menos ingressos vendidos", sistemaTeatro.getPecaMenosIngressos()});
        modeloTabela.addRow(new Object[]{"Sessão com maior ocupação", sistemaTeatro.getSessaoMaiorOcupacao()});
        modeloTabela.addRow(new Object[]{"Sessão com menor ocupação", sistemaTeatro.getSessaoMenorOcupacao()});
        modeloTabela.addRow(new Object[]{"Peça mais lucrativa", sistemaTeatro.getPecaMaisLucrativa()});
        modeloTabela.addRow(new Object[]{"Peça menos lucrativa", sistemaTeatro.getPecaMenosLucrativa()});
        modeloTabela.addRow(new Object[]{"Lucro médio do teatro por peça", sistemaTeatro.getLucroMedio()});
    }
}
