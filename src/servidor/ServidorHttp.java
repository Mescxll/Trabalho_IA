package servidor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import implementacao.Calculo;
import implementacao.Mapeamento;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServidorHttp {

    private static final int PORTA = 8000;
    private static final String PASTA_WEB = "web";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORTA), 0);

        server.createContext("/api/caminho", ServidorHttp::tratarCaminho);
        server.createContext("/", ServidorHttp::tratarEstatico);

        server.setExecutor(null);
        server.start();

        System.out.println("Servidor rodando em http://localhost:" + PORTA);
    }

    private static void tratarCaminho(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            enviarResposta(exchange, 405, "{\"erro\":\"Método não permitido\"}", "application/json");
            return;
        }

        try {
            Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
            String inicio = params.get("inicio");
            String fim = params.get("fim");
            String heuristicaStr = params.get("heuristica");

            if (inicio == null || fim == null || heuristicaStr == null) {
                enviarResposta(exchange, 400,
                        "{\"erro\":\"Parâmetros obrigatórios: inicio, fim, heuristica\"}",
                        "application/json");
                return;
            }

            inicio = inicio.toUpperCase();
            fim = fim.toUpperCase();
            int heuristica = Integer.parseInt(heuristicaStr);

            ArrayList<String> caminho = Calculo.calculaCaminho(inicio, fim, heuristica);

            String json;
            if (caminho == null) {
                json = "{\"erro\":\"Caminho não encontrado\"}";
            } else {
                Calculo.apresentaTrajetoCusto(caminho);
                json = montaJsonResultado(caminho);
            }

            enviarResposta(exchange, 200, json, "application/json");

        } catch (NumberFormatException e) {
            enviarResposta(exchange, 400, "{\"erro\":\"Heurística inválida\"}", "application/json");
        } catch (Exception e) {
            enviarResposta(exchange, 500, "{\"erro\":\"Erro interno: " + e.getMessage() + "\"}", "application/json");
        }
    }

    private static String montaJsonResultado(ArrayList<String> caminho) {
        String pontosJson = montaJsonCaminho(caminho);
        String testadasJson = montaJsonArestasTestadas();
        return "{\"caminho\":[" + pontosJson + "],\"testadas\":" + testadasJson + "}";
    }

    private static String montaJsonCaminho(ArrayList<String> caminho) {
        String[][] matriz = Mapeamento.mapeiaDistancia();
        StringBuilder pontosJson = new StringBuilder();
        int acumulado = 0;

        for (int i = 0; i < caminho.size(); i++) {
            String ponto = caminho.get(i);
            int[] coordenadas = getCoordenadas(matriz, ponto);

            if (i > 0) {
                acumulado += calculaDistancia(matriz, caminho.get(i - 1), ponto);
                pontosJson.append(",");
            }

            pontosJson.append("{")
                    .append("\"ponto\":\"").append(ponto).append("\",")
                    .append("\"acumulado\":").append(acumulado).append(",")
                    .append("\"x\":").append(coordenadas[0]).append(",")
                    .append("\"y\":").append(coordenadas[1])
                    .append("}");
        }

        return pontosJson.toString();
    }

    private static String montaJsonArestasTestadas() {
        StringBuilder sb = new StringBuilder();
        ArrayList<String[]> arestas = Calculo.arestasTestadas;

        for (int i = 0; i < arestas.size(); i++) {
            String[] aresta = arestas.get(i);
            if (i > 0) sb.append(",");
            sb.append("{\"de\":\"").append(aresta[0]).append("\",\"para\":\"").append(aresta[1]).append("\"}");
        }

        return "[" + sb + "]";
    }

    private static int calculaDistancia(String[][] matriz, String origem, String destino) {
        int[] c1 = getCoordenadas(matriz, origem);
        int[] c2 = getCoordenadas(matriz, destino);
        return Math.abs(c1[0] - c2[0]) + Math.abs(c1[1] - c2[1]);
    }

    private static int[] getCoordenadas(String[][] matriz, String valor) {
        for (int lin = 0; lin < matriz.length; lin++) {
            for (int col = 0; col < matriz[lin].length; col++) {
                if (valor.equals(matriz[lin][col])) {
                    return new int[]{col, lin};
                }
            }
        }
        return new int[]{-1, -1};
    }

    private static void tratarEstatico(HttpExchange exchange) throws IOException {
        String caminhoPedido = exchange.getRequestURI().getPath();
        if (caminhoPedido.equals("/")) {
            caminhoPedido = "/index.html";
        }

        Path arquivo = Paths.get(PASTA_WEB, caminhoPedido);

        if (!Files.exists(arquivo) || Files.isDirectory(arquivo)) {
            enviarResposta(exchange, 404, "404 - Arquivo não encontrado", "text/plain");
            return;
        }

        byte[] bytes = Files.readAllBytes(arquivo);
        exchange.getResponseHeaders().add("Content-Type", tipoConteudo(caminhoPedido));
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String tipoConteudo(String path) {
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".svg")) return "image/svg+xml";
        return "text/html; charset=utf-8";
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) return map;

        for (String par : query.split("&")) {
            String[] kv = par.split("=", 2);
            String chave = kv[0];
            String valor = kv.length > 1 ? kv[1] : "";
            map.put(chave, valor);
        }
        return map;
    }

    private static void enviarResposta(HttpExchange exchange, int status, String corpo, String tipo) throws IOException {
        byte[] bytes = corpo.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", tipo + "; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}