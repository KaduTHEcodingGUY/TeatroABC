package Backend.Servicos;

import Backend.Servicos.SupabaseConfig;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngressoService {
    private static final String API_URL = SupabaseConfig.SUPABASE_URL + "/rest/v1";
    private static final String API_KEY = SupabaseConfig.SUPABASE_ANON_KEY;
    private static final Gson gson = new Gson();
    private static final Map<String, Integer> ingressosPorTipo = new HashMap<>();

    static {
        // Inicialização dos contadores de ingressos
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

    public static class DetalheIngresso {
        private String reserva_id;
        private String usuario_id;
        private String codigo_ingresso;
        private LocalDateTime data_compra;
        private String peca_titulo;
        private String peca_imagem_url;
        private LocalDateTime data_hora_sessao;
        private String assentos_comprados;
        private double preco;
        private double pagamento;
        private boolean membro_fidelidade;
        private double desconto_fidelidade;

        // Getters
        public String getReservaId() { return reserva_id; }
        public String getUsuarioId() { return usuario_id; }
        public String getCodigoIngresso() { return codigo_ingresso; }
        public LocalDateTime getDataCompra() { return data_compra; }
        public String getPecaTitulo() { return peca_titulo; }
        public String getPecaImagemUrl() { return peca_imagem_url; }
        public LocalDateTime getDataHoraSessao() { return data_hora_sessao; }
        public String getAssentosComprados() { return assentos_comprados; }
        public double getPreco() { return preco; }
        public double getPagamento() { return pagamento; }
        public boolean isMembroFidelidade() { return membro_fidelidade; }
        public double getDescontoFidelidade() { return desconto_fidelidade; }
    }

    public static List<DetalheIngresso> listarIngressosPorUsuario(String userId) {
        List<DetalheIngresso> ingressos = new ArrayList<>();
        try {
            System.out.println("[DEBUG IngressoService] Iniciando listagem de ingressos para usuário: " + userId);
            // A URL aponta para a view recém-criada e filtra pelo usuario_id
            URL url = new URL(API_URL + "/vw_detalhes_ingressos_usuarios?usuario_id=eq." + userId);
            System.out.println("[DEBUG IngressoService] URL da API: " + url.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("apikey", API_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);

            int responseCode = conn.getResponseCode();
            System.out.println("[DEBUG IngressoService] Código de resposta: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                String response = responseBuilder.toString();
                System.out.println("[DEBUG IngressoService] Resposta do servidor: " + response);

                JsonArray jsonArray = gson.fromJson(response, JsonArray.class);
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME; // Assumindo formato ISO para datas/horas

                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject json = jsonArray.get(i).getAsJsonObject();
                    DetalheIngresso ingresso = new DetalheIngresso();
                    ingresso.reserva_id = json.get("reserva_id").getAsString();
                    ingresso.usuario_id = json.get("usuario_id").getAsString();
                    ingresso.codigo_ingresso = json.get("codigo_ingresso").getAsString();
                    ingresso.data_compra = LocalDateTime.parse(json.get("data_compra").getAsString(), formatter);
                    ingresso.peca_titulo = json.get("peca_titulo").getAsString();
                    
                    String imageUrl = json.get("peca_imagem_url").getAsString();
                    if (imageUrl != null && !imageUrl.trim().isEmpty() && !imageUrl.startsWith("http")) {
                        imageUrl = SupabaseConfig.SUPABASE_URL + "/storage/v1/object/public/" + imageUrl;
                    }
                    ingresso.peca_imagem_url = imageUrl;
                    
                    ingresso.data_hora_sessao = LocalDateTime.parse(json.get("data_hora_sessao").getAsString(), formatter);
                    ingresso.assentos_comprados = json.get("assentos_comprados").getAsString();
                    ingresso.preco = json.get("preco").getAsDouble();
                    
                    if (json.has("pagamento") && !json.get("pagamento").isJsonNull()) {
                        ingresso.pagamento = json.get("pagamento").getAsDouble();
                    } else {
                        ingresso.pagamento = ingresso.preco;
                    }
                    
                    if (json.has("membro_fidelidade") && !json.get("membro_fidelidade").isJsonNull()) {
                        ingresso.membro_fidelidade = json.get("membro_fidelidade").getAsBoolean();
                    } else {
                        ingresso.membro_fidelidade = false;
                    }
                    
                    if (json.has("desconto_fidelidade") && !json.get("desconto_fidelidade").isJsonNull()) {
                        ingresso.desconto_fidelidade = json.get("desconto_fidelidade").getAsDouble();
                    } else {
                        ingresso.desconto_fidelidade = 0.0;
                    }
                    
                    ingressos.add(ingresso);
                    System.out.println("[DEBUG IngressoService] Ingresso carregado: " + ingresso.getCodigoIngresso() + " - " + ingresso.getPecaTitulo());
                }
            } else {
                System.err.println("[ERRO IngressoService] Erro ao listar ingressos. Código: " + responseCode);
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String errorResponse = errorReader.readLine();
                System.err.println("[ERRO IngressoService] Resposta de erro: " + errorResponse);
            }
        } catch (Exception e) {
            System.err.println("[ERRO IngressoService] Exceção ao listar ingressos: " + e.getMessage());
            e.printStackTrace();
        }
        return ingressos;
    }

    public static void incrementarIngresso(String tipo) {
        ingressosPorTipo.put(tipo, ingressosPorTipo.getOrDefault(tipo, 0) + 1);
    }

    public static int getTotalIngressos(String tipo) {
        return ingressosPorTipo.getOrDefault(tipo, 0);
    }
} 