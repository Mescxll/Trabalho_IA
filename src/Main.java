import implementacao.Calculo;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("-------------------------");
        System.out.println("     Cidade Virtual      ");
        System.out.println("-------------------------\n");
        System.out.println("De qual ponto deseja partir [A-U]: ");

        String inicio = scanner.nextLine();

        System.out.println("\nQual ponto você deseja alcançar [A-U]: ");
        String fim = scanner.nextLine();

        System.out.println("\nQual função heurística deve ser implementada?");
        System.out.println("1. Distância Manhattan\n2. Distância Euclidiana\n3. Distância Chebyshev\n");
        System.out.println("Escolha [1-3]: ");
        int heuristica = scanner.nextInt();

        System.out.println(" ");
        String caminhoSimples = Calculo.calculaCaminho(inicio, fim, heuristica);
        
        if (caminhoSimples.equals("Caminho não encontrado")|| caminhoSimples.equals("Os valores de entrada são inválidos")) {
            System.out.println(caminhoSimples);
        } else {
            ArrayList<String> caminho = editaString(caminhoSimples);
            Calculo.apresentaTrajetoCusto(caminho);
        }
    
        private static ArrayList<String> editaString(String caminhoSimples) {
            String caminhoEditado = caminhoSimples.replace("[", "").replace("]", "").replace(" ", "");
            return new ArrayList<>(Arrays.asList(caminhoEditado.split(",")));
        }
    }
}