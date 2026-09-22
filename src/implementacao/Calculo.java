package implementacao;

import java.util.ArrayList;
import java.util.HashMap;

public class Calculo {
    /*
        Algoritmo A*

        f(n) = g(n) + h(n)
        g(n) = distância acumulada de n ao nó inicial
        h(n) = distância estimada de n ao nó pela função heurística
         */
    public static String calculaCaminho(String inicio, String fim) {
        HashMap<String, ArrayList<String>> direcoes = Mapeamento.mapeiaDirecoes();

        // Calcular A* para cada direção
        return null;
    }

    private Integer calculaDistancia(String origem, String destino){
        return 0;
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
