package Backend;

public class SistemaTeatro {
    private final ControleIngressos gerenciadorIngressos;
    private final CalculoLucro calculadoraLucro;
    private final PrecosPorArea gerenciadorPrecos;
    private static SistemaTeatro InstanciaSistema;

    private SistemaTeatro() {
        this.gerenciadorIngressos = new ControleIngressos();
        this.gerenciadorPrecos = new PrecosPorArea();
        this.calculadoraLucro = new CalculoLucro(gerenciadorIngressos, gerenciadorPrecos);
    }
    public static SistemaTeatro getInstancia(){
        if(InstanciaSistema == null){
            InstanciaSistema = new SistemaTeatro();
        }
            return InstanciaSistema;
    }
    public ControleIngressos getGerenciadorIngressos(){
        return gerenciadorIngressos;
    }

    public String getPecaMaisIngressos() {
        return calculadoraLucro.getPecaMaisMovimentada();
    }

    public String getPecaMenosIngressos() {
        return calculadoraLucro.getPecaMenosMovimentada();
    }

    public String getSessaoMaiorOcupacao() {
        return calculadoraLucro.getSessaoMaisMovimentada();
    }

    public String getSessaoMenorOcupacao() {
        return calculadoraLucro.getSessaoMenosMovimentada();
    }

    public String getPecaMaisLucrativa() {
        return calculadoraLucro.getPecaMaisLucrativa();
    }

    public String getPecaMenosLucrativa() {
        return calculadoraLucro.getPecaMenosLucrativa();
    }

    public String getLucroMedio() {
        return calculadoraLucro.getLucroMedio();
    }
    
}
