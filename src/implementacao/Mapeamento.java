package implementacao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Mapeamento {
    static HashMap<String, ArrayList<String>> mapaDirecoes = new HashMap<>();

    public static String[][] mapeiaDistancia(){
        String[][] matrizDistancia = {
                {"A", "B", "0", "0", "C", "D", "0", "E"},
                {"0", "0", "0", "0", "0", "0", "0", "0"},
                {"F", "G", "0", "0", "H", "0", "0", "I"},
                {"0", "J", "0", "K", "L", "M", "0", "N"},
                {"0", "0", "0", "0", "O", "0", "0", "P"},
                {"Q", "R", "0", "S", "T", "0", "0", "U"}
        };
        return matrizDistancia;
    }

    public static HashMap<String, ArrayList<String>> mapeiaDirecoes(){
        mapaDirecoes.put("A", new ArrayList<>(Arrays.asList("B", "F")));
        mapaDirecoes.put("B", new ArrayList<>(Arrays.asList("G", "A", "C")));
        mapaDirecoes.put("C", new ArrayList<>(Arrays.asList("B", "D")));
        mapaDirecoes.put("D", new ArrayList<>(Arrays.asList("C", "E", "M")));
        mapaDirecoes.put("E", new ArrayList<>(Arrays.asList("D", "I")));
        mapaDirecoes.put("F", new ArrayList<>(Arrays.asList("A", "G", "Q")));
        mapaDirecoes.put("G", new ArrayList<>(Arrays.asList("F", "H", "J")));
        mapaDirecoes.put("H", new ArrayList<>(Arrays.asList("G", "C")));
        mapaDirecoes.put("I", new ArrayList<>(List.of("N")));
        mapaDirecoes.put("J", new ArrayList<>(List.of("R")));
        mapaDirecoes.put("K", new ArrayList<>(Arrays.asList("J", "S")));
        mapaDirecoes.put("L", new ArrayList<>(Arrays.asList("H", "K")));
        mapaDirecoes.put("M", new ArrayList<>(Arrays.asList("D", "L")));
        mapaDirecoes.put("N", new ArrayList<>(Arrays.asList("P", "M")));
        mapaDirecoes.put("O", new ArrayList<>(List.of("L")));
        mapaDirecoes.put("P", new ArrayList<>(Arrays.asList("U", "O")));
        mapaDirecoes.put("Q", new ArrayList<>(Arrays.asList("R", "F")));
        mapaDirecoes.put("R", new ArrayList<>(List.of("S")));
        mapaDirecoes.put("S", new ArrayList<>(Arrays.asList("K", "T")));
        mapaDirecoes.put("T", new ArrayList<>(Arrays.asList("O", "U")));
        mapaDirecoes.put("U", new ArrayList<>(List.of("T")));
        return mapaDirecoes;
    }
}
