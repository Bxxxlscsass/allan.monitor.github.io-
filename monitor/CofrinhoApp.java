import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CofrinhoApp {

    // Simulação do ArrayList de moedas do seu projeto POO
    private static ArrayList<String> moedas = new ArrayList<>();
    private static double totalEmReal = 0.0;

    public static void main(String[] args) throws IOException {
        // Vincula o servidor na porta 8087 em todas as interfaces de rede (0.0.0.0)
        HttpServer server = HttpServer.create(new InetSocketAddress(8087), 0);
        server.createContext("/cofrinho", new CofrinhoHandler());
        server.setExecutor(null); 
        System.out.println("[JAVA] Servidor rodando com sucesso na porta 8087...");
        server.start();
    }

    static class CofrinhoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // 1. SOLUÇÃO DO ERRO: Configura os cabeçalhos CORS necessários para o navegador aceitar a requisição
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

            // Trata requisições OPTIONS (Pré-envio que o navegador faz automaticamente antes do POST)
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                // Lê o payload enviado pelo JavaScript (Exemplo: "ADD|1|50.00")
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String payload = br.lines().collect(Collectors.joining());

                // Processa a lógica de POO do Cofrinho baseado no payload recebido
                processarComando(payload);

                // 2. Monta o JSON de resposta exatamente como o seu JS espera ler
                String jsonResposta = construirJsonResposta();

                // Envia a resposta HTTP 200 OK com o JSON formatado
                byte[] responseBytes = jsonResposta.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.sendResponseHeaders(200, responseBytes.length);
                
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();
            } else {
                // Bloqueia outros métodos que não sejam POST
                exchange.sendResponseHeaders(405, -1);
            }
        }

        // Lógica simples para tratar a String separada por "|" (Pipe)
        private void processarComando(String payload) {
            try {
                String[] partes = payload.split("\\|");
                if (partes.length < 3) return;

                String operacao = partes[0]; // ADD ou REM
                String tipoMoeda = partes[1]; // 1, 2, 3 ou 4
                double valor = Double.parseDouble(partes[2]);

                String nomeMoeda = mapearMoeda(tipoMoeda);
                double valorEmReal = converterParaReal(tipoMoeda, valor);

                if ("ADD".equals(operacao)) {
                    moedas.add(nomeMoeda + ": " + valor);
                    totalEmReal += valorEmReal;
                } else if ("REM".equals(operacao)) {
                    // Remove uma ocorrência equivalente se existir
                    if (moedas.remove(nomeMoeda + ": " + valor)) {
                        totalEmReal -= valorEmReal;
                        if (totalEmReal < 0) totalEmReal = 0;
                    }
                }
            } catch (Exception e) {
                System.out.println("[ERRO] Falha ao processar comando: " + e.getMessage());
            }
        }

        private String mapearMoeda(String tipo) {
            switch (tipo) {
                case "1": return "Real";
                case "2": return "Dolar";
                case "3": return "Libra";
                case "4": return "Euro";
                default: return "Desconhecida";
            }
        }

        private double converterParaReal(String tipo, double valor) {
            switch (tipo) {
                case "2": return valor * 5.00; // Cotação fictícia de exemplo
                case "3": return valor * 6.50;
                case "4": return valor * 5.50;
                default: return valor; // Real (1 para 1)
            }
        }

        // Gera a string JSON manualmente para evitar dependências de bibliotecas externas (como Jackson ou Gson)
        private String construirJsonResposta() {
            String moedasJson = moedas.stream()
                .map(m -> "{\"info\":\"" + m + "\"}")
                .collect(Collectors.joining(","));

            return "{"
                + "\"total\":" + totalEmReal + ","
                + "\"moedas\":[" + moedasJson + "]"
                + "}";
        }
    }
}
