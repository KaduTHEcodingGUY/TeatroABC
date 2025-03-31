import javax.swing.*;

import Backend.CadastroService;
import Backend.Navegador;
import Backend.SistemaTeatro;
import Telas.TelaCadastro;

public class Main{
    public static void main(String[] args) {
       
        SistemaTeatro sistemaTeatro = new SistemaTeatro();
        
        CadastroService cadastroService = new CadastroService(null);
        Navegador navegador = new Navegador(sistemaTeatro, null);
        SwingUtilities.invokeLater(()-> {
            TelaCadastro telaCadastro = new TelaCadastro(cadastroService, navegador);
            telaCadastro.setVisible(true);

        });

        
    }
}