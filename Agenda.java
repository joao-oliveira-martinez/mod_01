import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;



class Telefone {
    private Long id;
    private String ddd;
    private Long numero;

    public Telefone(Long id, String ddd, Long numero) {
        this.id = id;
        this.ddd = ddd;
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public String getDdd() {
        return ddd;
    }

    public Long getNumero() {
        return numero;
    }

    public String toString() {
        return "Telefone{" +
                "id=" + id +
                ", ddd='" + ddd + '\'' +
                ", numero=" + numero +
                '}';
    }
}

class Contato {
    private Long id;
    private String nome;
    private String sobreNome;
    private Telefone[] telefones;

    public Contato(Long id, String nome, String sobreNome, Telefone[] telefones) {
        this.id = id;
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.telefones = telefones;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public Telefone[] getTelefones() {
        return telefones;
    }

    public String toString() {
        StringBuilder telefoneStr = new StringBuilder();
        for (Telefone telefone : telefones) {
            telefoneStr.append(telefone.toString()).append(", ");
        }

        return "Contato{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", sobreNome='" + sobreNome + '\'' +
                ", telefones=[" + telefoneStr + "]" +
                '}';
    }
}

public class Agenda {
    private Contato[] contatos;
    private int numContatos;

    public Agenda() {
        this.contatos = new Contato[100];
        this.numContatos = 0;
    }

    public static void main(String[] args) {
        Agenda agenda = new Agenda();


        Scanner scanner = new Scanner(System.in);

        int opcao;
        do {
            System.out.println("##################");
            System.out.println("##### AGENDA #####");
            //System.out.println(">>>> Contatos <<<<");
            //agenda.listarContatos();
            System.out.println(">>>> Menu <<<<");
            System.out.println("1 - Adicionar Contato");
            System.out.println("2 - Remover Contato");
            System.out.println("3 - Editar Contato");
            System.out.println("4 - Mostrar Contatos");
            System.out.println("5 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    agenda.adicionarContato(scanner);
                    break;
                case 2:
                    agenda.removerContato(scanner);
                    break;
                case 3:
                    agenda.editarContato(scanner);
                    break;
                case 4:
                    agenda.listarContatos();
                    break;
                case 5:
                    agenda.salvarContatos();
                    System.out.println("Fechando a agenda. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 5);
    }

    private void listarContatos() {
        System.out.println("ID | Nome | DDD | Número");
        for (int i = 0; i < numContatos; i++) {
            System.out.print(contatos[i] != null ? contatos[i].getId() + " | " + contatos[i].getNome() + " | " : "");
            Telefone[] telefones = contatos[i] != null ? contatos[i].getTelefones() : null;
            if (telefones != null && telefones.length > 0) {
                System.out.print(telefones[0].getDdd() + " | " + telefones[0].getNumero());
            }
            System.out.println();
        }
    }
    private boolean validarIdUnico(Long novoId) {
        for (int i = 0; i < numContatos; i++) {
            if (contatos[i].getId().equals(novoId)) {
                return false;
            }
        }
        return true;
    }

    private Long obterProximoId() {
        Long ultimoId = 0L;

        for (int i = 0; i < numContatos; i++) {
            if (contatos[i] != null && contatos[i].getId() > ultimoId) {
                ultimoId = contatos[i].getId();
            }
        }

        return ultimoId + 1;
    }

    private void adicionarContato(Scanner scanner) {
        System.out.println("Adicionar Contato");

        Long novoId = obterProximoId();

        // Informações de contato
        System.out.print("Nome: ");
        String nome = scanner.next();
        System.out.print("Sobrenome: ");
        String sobrenome = scanner.next();

        Long numeroTelefone;
        String dddTelefone;
        boolean numeroExistente;
        do {
            System.out.print("Informe o número do telefone: ");
            numeroTelefone = scanner.nextLong();
            System.out.print("DDD: ");
            dddTelefone = scanner.next();

            numeroExistente = validarNumeroExistente(numeroTelefone, dddTelefone);

            if (numeroExistente) {
                System.out.println("Este número de telefone já está cadastrado. Por favor, informe outro número.");
            }

        } while (numeroExistente);

        Telefone[] telefones = {new Telefone(novoId, dddTelefone, numeroTelefone)};
        Contato novoContato = new Contato(novoId, nome, sobrenome, telefones);
        contatos[numContatos] = novoContato;
        numContatos++;

        System.out.println("Contato adicionado com sucesso!");
    }

    private boolean validarNumeroExistente(Long numero, String ddd) {
        for (int i = 0; i < numContatos; i++) {
            Telefone[] telefones = contatos[i].getTelefones();
            if (telefones != null && telefones.length > 0 &&
                    telefones[0].getNumero().equals(numero) &&
                    telefones[0].getDdd().equals(ddd)) {
                return true;
            }
        }
        return false;
    }


    private void removerContato(Scanner scanner) {
        System.out.println("Remover Contato");

        System.out.print("Informe o ID do contato a ser removido: ");
        Long idRemover = scanner.nextLong();

        int indiceRemover = -1;
        for (int i = 0; i < numContatos; i++) {
            if (contatos[i] != null && contatos[i].getId().equals(idRemover)) {
                indiceRemover = i;
                break;
            }
        }

        if (indiceRemover != -1) {
            contatos[indiceRemover] = criarContatoExcluido(idRemover);
            System.out.println("Contato removido com sucesso!");
        } else {
            System.out.println("Contato não encontrado.");
        }
    }

    private Contato criarContatoExcluido(Long id) {
        return new Contato(id, "Contato excluído", "", new Telefone[0]);
    }

    private void editarContato(Scanner scanner) {
        System.out.println("Editar Contato");

        System.out.print("Informe o ID do contato a ser editado: ");
        Long idEditar = scanner.nextLong();

        int indiceEditar = -1;
        for (int i = 0; i < numContatos; i++) {
            if (contatos[i] != null && contatos[i].getId().equals(idEditar)) {
                indiceEditar = i;
                break;
            }
        }

        if (indiceEditar != -1) {
            System.out.print("Novo Nome: ");
            String novoNome = scanner.next();
            System.out.print("Novo Sobrenome: ");
            String novoSobrenome = scanner.next();

            contatos[indiceEditar].setNome(novoNome);
            contatos[indiceEditar].setSobreNome(novoSobrenome);

            System.out.println("Contato editado com sucesso!");
        } else {
            System.out.println("Contato não encontrado.");
        }
    }

    private void salvarContatos() {
        System.out.println("Salvando Contatos...");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("contatos.txt"))) {
            bw.write("Id | Nome e Sobrenome | DDD + Número");
            bw.newLine();

            for (int i = 0; i < numContatos; i++) {
                Contato contato = contatos[i];
                // Escreve os dados do contato
                bw.write(contato.getId() + " | " + contato.getNome() + " " + contato.getSobreNome() + " | "
                        + "(" + contato.getTelefones()[0].getDdd() + ") " + contato.getTelefones()[0].getNumero());
                bw.newLine();
            }

            System.out.println("Contatos salvos com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar contatos: " + e.getMessage());
        }
    }

}


