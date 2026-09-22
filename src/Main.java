import implementacao.Calculo;
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
        System.out.println(Calculo.calculaCaminho(inicio, fim, heuristica));
    }
}