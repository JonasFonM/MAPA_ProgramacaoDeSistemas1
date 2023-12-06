import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

/**
 * Programa para leitura e manipulação de registros em um arquivo CSV de doações de sangue.
 */
public class LeitorCSV {

    /**
     * Método principal para execução do programa.
     *
     * @param args Os argumentos da linha de comando (não utilizados neste exemplo).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Informe o caminho e nome do arquivo CSV:");
        String caminhoArquivo = scanner.nextLine();

        try {
            int escolha;
            do {
                exibirConteudoArquivo(caminhoArquivo);
                exibirMenu();
                escolha = scanner.nextInt();
                scanner.nextLine();

                switch (escolha) {
                    case 1:
                        adicionarDoacao(caminhoArquivo);
                        break;
                    case 2:
                        deletarDoacao(caminhoArquivo);
                        break;
                    case 3:
                        System.out.println("Encerrando o programa.");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }

            } while (escolha != 3);

        } catch (IOException e) {
            System.err.println("Erro ao manipular o arquivo: " + e.getMessage());
        }
    }

    /**
     * Exibe o conteúdo do arquivo CSV na tela.
     *
     * @param caminhoArquivo O caminho do arquivo CSV.
     * @throws IOException Se houver um erro ao ler o arquivo.
     */
    private static void exibirConteudoArquivo(String caminhoArquivo) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            System.out.println("\n");
            while ((linha = reader.readLine()) != null) {
                System.out.println(linha);
            }
        }
    }

    /**
     * Adiciona uma nova doação ao final do arquivo CSV.
     *
     * @param caminhoArquivo O caminho do arquivo CSV.
     * @throws IOException Se houver um erro ao escrever no arquivo.
     */
    private static void adicionarDoacao(String caminhoArquivo) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Informe os detalhes da nova doação:");
        System.out.print("Código: ");
        int codigo = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Data de Nascimento (YYYY-MM-DD): ");
        String dataNascimento = scanner.nextLine();

        System.out.print("Tipo Sanguíneo: ");
        String tipoSanguineo = scanner.nextLine();

        System.out.print("MLS Doados: ");
        int mlsDoados = scanner.nextInt();
        scanner.nextLine(); 

        String novaDoacao = String.format("%d,%s,%s,%s,%s,%d", codigo, nome, cpf, dataNascimento, tipoSanguineo, mlsDoados);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            writer.newLine();
            writer.write(novaDoacao);
            System.out.println("Nova doação adicionada com sucesso!");
        }
    }

    /**
     * Deleta uma doação com base no código especificado.
     *
     * @param caminhoArquivo O caminho do arquivo CSV.
     * @throws IOException Se houver um erro ao escrever no arquivo.
     */
    private static void deletarDoacao(String caminhoArquivo) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Informe o código da doação a ser removida:");
        int codigoRemover = scanner.nextInt();
        scanner.nextLine();

        Path arquivoOriginal = Paths.get(caminhoArquivo);
        Path arquivoTemp = Paths.get(caminhoArquivo + ".tmp");

        try (BufferedReader reader = Files.newBufferedReader(arquivoOriginal);
             BufferedWriter writer = Files.newBufferedWriter(arquivoTemp, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] campos = linha.split(",");
                int codigo = Integer.parseInt(campos[0].trim());

                if (codigo != codigoRemover) {
                    writer.write(linha);
                    writer.newLine();
                }
            }
        }

        try {
            Files.move(arquivoTemp, arquivoOriginal, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Doação removida com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao mover o arquivo temporário: " + e.getMessage());
        }
    }

    /**
     * Exibe o menu de opções.
     */
    private static void exibirMenu() {
        System.out.println("\nSelecione uma opção:");
        System.out.println("1 - Adicionar nova doação");
        System.out.println("2 - Deletar doação por código");
        System.out.println("3 - Sair");
    }
}
