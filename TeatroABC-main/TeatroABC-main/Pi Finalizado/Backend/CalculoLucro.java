package Backend;


import Interfaces.ICalculoLucro;
import Interfaces.IControleIngressos;
import Interfaces.IPrecosPorArea;

public class CalculoLucro implements ICalculoLucro {
    private final IControleIngressos controleIngressos;
    private final IPrecosPorArea precosPorArea;

    public CalculoLucro(IControleIngressos controleIngressos, IPrecosPorArea precosPorArea) {
        this.controleIngressos = controleIngressos;
        this.precosPorArea = precosPorArea;
    }

    private double calcularLucro(String tipoPeça) {
        return controleIngressos.getTotalIngressos(tipoPeça) * calcularValorTotalPorPeça();
    }

    private double calcularValorTotalPorPeça() {
        return precosPorArea.getPrecosPorArea().entrySet().stream()
            .mapToDouble(entry -> entry.getValue() * controleIngressos.getTotalIngressos(entry.getKey()))
            .sum();
    }

    @Override
    public double calcularLucroTotal() {
        return calcularLucro("Frozen") + calcularLucro("Michael Jackson") + calcularLucro("Romeu & Julieta");
    }

    @Override
    public String getPecaMaisLucrativa() {
        double lucroFrozen = calcularLucro("Frozen");
        double lucroMJ = calcularLucro("Michael Jackson");
        double lucroRomeu = calcularLucro("Romeu & Julieta");

        if (lucroFrozen > lucroMJ && lucroFrozen > lucroRomeu) return "Frozen o Musical";
        if (lucroMJ > lucroFrozen && lucroMJ > lucroRomeu) return "Musical do Michael Jackson";
        if (lucroRomeu > lucroFrozen && lucroRomeu > lucroMJ) return "Romeu & Julieta";

        return "Nenhuma peça lucrativa";
    }

    @Override
    public String getPecaMenosLucrativa() {
        double lucroFrozen = calcularLucro("Frozen");
        double lucroMJ = calcularLucro("Michael Jackson");
        double lucroRomeu = calcularLucro("Romeu & Julieta");

        if (lucroFrozen < lucroMJ && lucroFrozen < lucroRomeu) return "Frozen o Musical";
        if (lucroMJ < lucroFrozen && lucroMJ < lucroRomeu) return "Musical do Michael Jackson";
        if (lucroRomeu < lucroFrozen && lucroRomeu < lucroMJ) return "Romeu & Julieta";

        return "Nenhuma peça com menor lucro";
    }

    @Override
    public String getLucroMedio() {
        double lucroTotal = calcularLucroTotal();
        return String.format("Lucro Médio: R$ %.2f", lucroTotal / 3);
    }
    @Override
public String getSessaoMaisMovimentada() {
    String[] sessoes = {"Sessão Manhã", "Sessão Tarde", "Sessão Noite"};
    String sessaoMaisMovimentada = sessoes[0];
    int maxIngressos = controleIngressos.getTotalIngressos(sessaoMaisMovimentada);

    for (String sessao : sessoes) {
        int ingressos = controleIngressos.getTotalIngressos(sessao);
        if (ingressos > maxIngressos) {
            maxIngressos = ingressos;
            sessaoMaisMovimentada = sessao;
        }
    }
    
    return sessaoMaisMovimentada;
}

@Override
public String getSessaoMenosMovimentada() {
    String[] sessoes = {"Sessão Manhã", "Sessão Tarde", "Sessão Noite"};
    String sessaoMenosMovimentada = sessoes[0];
    int minIngressos = controleIngressos.getTotalIngressos(sessaoMenosMovimentada);

    for (String sessao : sessoes) {
        int ingressos = controleIngressos.getTotalIngressos(sessao);
        if (ingressos < minIngressos) {
            minIngressos = ingressos;
            sessaoMenosMovimentada = sessao;
        }
    }
    
    return sessaoMenosMovimentada;
}
@Override
public String getPecaMaisMovimentada() {
    String[] pecas = {"Frozen o Musical", "Musical do Michael Jackson", "Romeu & Julieta"};
    String pecaMaisMovimentada = pecas[0];
    int maxIngressos = controleIngressos.getTotalIngressos(pecaMaisMovimentada);

    for (String peca : pecas) {
        int ingressos = controleIngressos.getTotalIngressos(peca);
        if (ingressos > maxIngressos) {
            maxIngressos = ingressos;
            pecaMaisMovimentada = peca;
        }
    }
    
    return pecaMaisMovimentada;
}

@Override
public String getPecaMenosMovimentada() {
    String[] pecas = {"Frozen o Musical", "Musical do Michael Jackson", "Romeu & Julieta"};
    String pecaMenosMovimentada = pecas[0];
    int minIngressos = controleIngressos.getTotalIngressos(pecaMenosMovimentada);

    for (String peca : pecas) {
        int ingressos = controleIngressos.getTotalIngressos(peca);
        if (ingressos < minIngressos) {
            minIngressos = ingressos;
            pecaMenosMovimentada = peca;
        }
    }
    
    return pecaMenosMovimentada;
}

}
