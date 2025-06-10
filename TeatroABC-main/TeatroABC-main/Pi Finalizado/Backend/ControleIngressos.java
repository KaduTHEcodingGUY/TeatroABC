package Backend;
import java.util.HashMap;
import java.util.Map;

import Interfaces.IControleIngressos;

public class ControleIngressos implements IControleIngressos {
    private final Map<String, Integer> ingressosPorTipo;

    public ControleIngressos() {
        ingressosPorTipo = new HashMap<>();
        ingressosPorTipo.put("Frozen", 0);
        ingressosPorTipo.put("Michael Jackson", 0);
        ingressosPorTipo.put("Romeu & Julieta", 0);
        ingressosPorTipo.put("Sessão Manhã", 0);
        ingressosPorTipo.put("Sessão Tarde", 0);
        ingressosPorTipo.put("Sessão Noite", 0);
        ingressosPorTipo.put("Plateia A", 0);
        ingressosPorTipo.put("Plateia B", 0);
        ingressosPorTipo.put("Frisa", 0);
        ingressosPorTipo.put("Camarote", 0);
        ingressosPorTipo.put("Balcão Nobre", 0);
    }

    @Override
    public void incrementarIngresso(String tipo) {
        ingressosPorTipo.put(tipo, ingressosPorTipo.getOrDefault(tipo, 0) + 1);
    }

    @Override
    public int getTotalIngressos(String tipo) {
        return ingressosPorTipo.getOrDefault(tipo, 0);
    }
}
