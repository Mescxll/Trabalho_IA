package implementacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Calculo {
    /*
        Algoritmo A*

        f(n) = g(n) + h(n)
        g(n) = distância acumulada de n ao nó inicial
        h(n) = distância estimada de n ao nó pela função heurística
         */
    public static String calculaCaminho(String inicio, String fim, int heuristica) {
        ArrayList<String> entradas = new ArrayList<>(Arrays.asList(
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                "N", "O", "P", "Q", "R", "S", "T", "U"
        ));
        if(!entradas.contains(inicio) || !entradas.contains(fim)){
            return "Os valores de entrada são inválidos";
        }

        //g(n)
        int g = calculaDistancia(inicio, fim);

        HashMap<String, ArrayList<String>> direcoes = Mapeamento.mapeiaDirecoes();
        // Calcular A* para cada direção

        return "Distancia: " + g;
    }

    private static Integer calculaDistancia(String origem, String destino){
        String[][] matriz = Mapeamento.mapeiaDistancia();

        int origemX = -1, origemY = -1, destinoX = -1, destinoY = -1;

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j].equals(origem)) {
                    origemX = i;
                    origemY = j;
                }

                if (matriz[i][j].equals(destino)) {
                    destinoX = i;
                    destinoY = j;
                }
            }
        }

        return Math.abs(origemX-destinoX)+Math.abs(origemY-destinoY);
    }

    /*
        Heurística - Manhattan

        h(n) = |x1 - x2| + |y1 - y2|
     */
    private Integer calculaHeuristicaManhattan(String atual, String fim){
        return 0;
    }

    /*
        Heurística - Euclidiana

        h(n) = √(x1 − x2)^2 + (y1 − y2)^2
     */
    private Integer calculaHeuristicaEuclidiana(String atual, String fim){
        return 0;
    }

    /*
        Heurística - Chebyshev

        h(n) = max(|x1 − x2|, |y1 − y2|)
     */
    private Integer calculaHeuristicaChebyshevs(String atual, String fim){
        return 0;
    }
}
