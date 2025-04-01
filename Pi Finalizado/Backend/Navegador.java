package Backend;
import Telas.TelaImprimirIngresso;
import Telas.MapaTeatroScrollavel;
import Telas.TelaCompraIngresso;
import Telas.TelaEstatisticas;

public class Navegador {
    
    private final MapaTeatroScrollavel mapaTeatro;

    public Navegador(MapaTeatroScrollavel mapaTeatro) {
        this.mapaTeatro = mapaTeatro;
    }

    public void navegar(String destino) {
        switch (destino) {
            case "TelaCompraIngresso":
                new TelaCompraIngresso(mapaTeatro).setVisible(true);
                break;
            case "TelaImprimirIngresso":
                new TelaImprimirIngresso().setVisible(true);
                break;
            case "TelaEstatisticas":
                new TelaEstatisticas().setVisible(true);
                break;
            case "Sair":
                System.exit(0);
                break;
            
        }
    }
}
