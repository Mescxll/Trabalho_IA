package implementacao;

import java.util.*;
import java.util.stream.Collectors;

public class Calculo {

    public static ArrayList<String[]> arestasTestadas = new ArrayList<>();
    /*
        Algoritmo A*

        f(n) = g(n) + h(n)
        g(n) = distância acumulada de n ao nó inicial
        h(n) = distância estimada de n ao nó pela função heurística
         */
    public static ArrayList<String> calculaCaminho(String inicio, String fim, int heuristica) {
        ArrayList<String> entradas = new ArrayList<>(Arrays.asList(
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                "N", "O", "P", "Q", "R", "S", "T", "U"
        ));
        if (!entradas.contains(inicio) || !entradas.contains(fim)) {
            System.out.println("Os valores de entrada são inválidos");
            return null;
        }

        ArrayList<String> caminho = new ArrayList<>();
        caminho.add(inicio);

        HashMap<String, ArrayList<String>> direcoes = Mapeamento.mapeiaDirecoes();
        HashSet<String> visitados = new HashSet<>(caminho);

        int g = 0;
        String pontoAtual = inicio;
        String proximoPonto;
        while (!Objects.equals(pontoAtual, fim)) {
            proximoPonto = calculaProximoPonto(direcoes, visitados, pontoAtual, fim, g, heuristica);

            // Caso o algoritmo não encontre um próximo ponto, ele recua um ponto.
            while (proximoPonto == null) {
                String removido = caminho.removeLast();

                if (caminho.isEmpty()) {
                    System.out.println("Caminho não encontrado");
                    return null;
                }

                pontoAtual = caminho.getLast();
                g -= calculaDistancia(pontoAtual, removido);
                proximoPonto = calculaProximoPonto(direcoes, visitados, pontoAtual, fim, g, heuristica);
            }

            g += calculaDistancia(pontoAtual, proximoPonto);
            pontoAtual = proximoPonto;
            caminho.add(pontoAtual);
            visitados.add(pontoAtual);
        }

        return caminho;
    }

    private static String calculaProximoPonto(HashMap<String, ArrayList<String>> direcoes, HashSet<String> visitados, String origem, String destino, int gAtual, int heuristica) {
        ArrayList<String> vizinhos = direcoes.get(origem);
        HashMap<String, Double> resultados = new HashMap<>();

        for (String vizinho : vizinhos) {
            arestasTestadas.clear();
            int gCalculo = gAtual + calculaDistancia(origem, vizinho);
            double hCalculo = 0.0;

            switch (heuristica) {
                case 1:
                    hCalculo = calculaHeuristicaManhattan(vizinho, destino);
                    break;
                case 2:
                    hCalculo = calculaHeuristicaEuclidiana(vizinho, destino);
                    break;
                case 3:
                    hCalculo = calculaHeuristicaChebyshevs(vizinho, destino);
                    break;
                default:
                    break;
            }

            resultados.put(vizinho, gCalculo + hCalculo);
        }

        // Ordena os resultados do menor para o maior
        resultados = resultados.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        for (Map.Entry<String, Double> proximo : resultados.entrySet()) {
            String proximoPonto = proximo.getKey();

            if (!visitados.contains(proximoPonto) && !proximoPonto.isEmpty()) {
                return proximoPonto;
            }
        }

        return null;
    }

    public static Integer calculaDistancia(String origem, String destino) {
        String[][] matriz = Mapeamento.mapeiaDistancia();
        int[] coordenadasAtual = getCoordenadas(matriz, origem);
        int[] coordenadasFim = getCoordenadas(matriz, destino);
        int x1 = coordenadasAtual[0];
        int x2 = coordenadasFim[0];
        int y1 = coordenadasAtual[1];
        int y2 = coordenadasFim[1];

        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    /*
        Heurística - Manhattan

        h(n) = |x1 - x2| + |y1 - y2|
     */
    private static double calculaHeuristicaManhattan(String atual, String fim) {
        String[][] matriz = Mapeamento.mapeiaDistancia();
        int[] coordenadasAtual = getCoordenadas(matriz, atual);
        int[] coordenadasFim = getCoordenadas(matriz, fim);
        int x1 = coordenadasAtual[0];
        int x2 = coordenadasFim[0];
        int y1 = coordenadasAtual[1];
        int y2 = coordenadasFim[1];

        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    /*
        Heurística - Euclidiana

        h(n) = √(x1 − x2)^2 + (y1 − y2)^2
     */
    private static double calculaHeuristicaEuclidiana(String atual, String fim) {
        String[][] matriz = Mapeamento.mapeiaDistancia();
        int[] coordenadasAtual = getCoordenadas(matriz, atual);
        int[] coordenadasFim = getCoordenadas(matriz, fim);
        int x1 = coordenadasAtual[0];
        int x2 = coordenadasFim[0];
        int y1 = coordenadasAtual[1];
        int y2 = coordenadasFim[1];

        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    /*
        Heurística - Chebyshev

        h(n) = max(|x1 − x2|, |y1 − y2|)
     */
    private static double calculaHeuristicaChebyshevs(String atual, String fim) {
        String[][] matriz = Mapeamento.mapeiaDistancia();
        int[] coordenadasAtual = getCoordenadas(matriz, atual);
        int[] coordenadasFim = getCoordenadas(matriz, fim);
        int x1 = coordenadasAtual[0];
        int x2 = coordenadasFim[0];
        int y1 = coordenadasAtual[1];
        int y2 = coordenadasFim[1];

        return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    public static int[] getCoordenadas(String[][] distancias, String valor) {
        for (int lin = 0; lin < distancias.length; lin++) {
            for (int col = 0; col < distancias[lin].length; col++) {
                if (Objects.equals(valor, distancias[lin][col])) {
                    return new int[]{col, lin};
                }
            }
        }

        return null;
    }

    public static void apresentaTrajetoCusto(ArrayList<String> caminhoSimples) {
        int sum=0;
        String trajeto = caminhoSimples.get(0);
        for (int i = 0; i < caminhoSimples.size()-1; i++) {
            String pontoInit= caminhoSimples.get(i);
            String pontoFim=caminhoSimples.get(i+1);
            sum+=calculaDistancia(pontoInit, pontoFim);
            

            trajeto += " -> " + pontoFim + "( "+ sum +" )";
        }
        System.out.println(trajeto);
    }

    static void main() {
        System.out.println(calculaCaminho("H", "I", 2));
    }
}
