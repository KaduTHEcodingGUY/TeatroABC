package Backend;
import Telas.TelaImprimirIngresso;
import Telas.MapaTeatroScrollavel;
import Telas.TelaCompraIngresso;
import Telas.TelaEstatisticas;

public class Navegador {
    private final SistemaTeatro sistemaTeatro;
    private final MapaTeatroScrollavel mapaTeatro;

    public Navegador(SistemaTeatro sistemaTeatro, MapaTeatroScrollavel mapaTeatro) {
        this.sistemaTeatro = sistemaTeatro;
        this.mapaTeatro = mapaTeatro;
    }

    public void navegar(String destino) {
        switch (destino) {
            case "TelaCompraIngresso":
                new TelaCompraIngresso(sistemaTeatro, mapaTeatro).setVisible(true);
                break;
            case "TelaImprimirIngresso":
                new TelaImprimirIngresso(sistemaTeatro).setVisible(true);
                break;
            case "TelaEstatisticas":
                new TelaEstatisticas(sistemaTeatro).setVisible(true);
                break;
            case "Sair":
                System.exit(0);
                break;
            
        }
    }
}
