package Backend;
import java.util.HashMap;
import java.util.Map;

import Interfaces.IPrecosPorArea;

public class PrecosPorArea implements IPrecosPorArea {
    private final Map<String, Double> precos;

    public PrecosPorArea() {
        precos = new HashMap<>();
        precos.put("Plateia A", 40.0);
        precos.put("Plateia B", 60.0);
        precos.put("Frisa", 80.0);
        precos.put("Camarote", 120.0);
        precos.put("Balcão Nobre", 250.0);
    }

    @Override
    public Map<String, Double> getPrecosPorArea() {
        return precos;
    }
}
