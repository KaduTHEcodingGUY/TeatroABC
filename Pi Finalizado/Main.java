import javax.swing.*;

import Backend.CadastroService;
import Backend.Navegador;
import Telas.TelaCadastro;

public class Main{
    public static void main(String[] args) {
       
        
        CadastroService cadastroService = new CadastroService();
        Navegador navegador = new Navegador(null);
        SwingUtilities.invokeLater(()-> {
            TelaCadastro telaCadastro = new TelaCadastro(cadastroService, navegador);
            telaCadastro.setVisible(true);

        });

        
    }
}