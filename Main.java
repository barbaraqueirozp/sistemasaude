import java.util.*;
import java.io.*;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n=== SISTEMA SAÚDE ===");
            System.out.println("1 - Login");
            System.out.println("2 - Cadastro (somente profissionais)");
            System.out.println("3 - Sair");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            if (op == 1) fazerLogin();
            else if (op == 2) cadastrarProfissional();
            else if (op == 3) {
                System.out.println("Saindo...");
                return;
            } else System.out.println("Opção inválida!");
        }
    }
    // LOGIN
    public static void fazerLogin() {
        System.out.print("Login: ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        if (verificarProfissional(login, senha)) {
            menuProfissional(login);
            return;
        }

        if (verificarPaciente(login, senha)) {
            menuPaciente(login);
            return;
        }

        System.out.println("Login ou senha incorretos!");
    }
    // CADASTRO ENFERMEIRO/MEDICO
    public static void cadastrarProfissional() {
        System.out.println("\n=== CADASTRO DE PROFISSIONAL ===");

        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("ID do Conselho: ");
        String crm = sc.nextLine();

        String login;
        while (true) {
            System.out.print("Login: ");
            login = sc.nextLine();
            if (!loginExiste(login)) break;
            System.out.println("Esse login já existe! Digite outro.");
        }

        System.out.print("Senha: ");
        String senha = sc.nextLine();

        try (FileWriter fw = new FileWriter("profissionais.txt", true)) {
            fw.write(nome + ";" + crm + ";" + login + ";" + senha + "\n");
            System.out.println("Profissional cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar profissional: " + e.getMessage());
        }
    }

    // VERIFICAÇÃO (MENU TEM Q IDENTIFICAR SE É PCT OU PROFISSIONAL P/ MOSTRAR O MENU CORRETO)
    public static boolean verificarProfissional(String login, String senha) {
        try (BufferedReader br = new BufferedReader(new FileReader("profissionais.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] p = linha.split(";");
                if (p.length >= 5 && p[3].equals(login) && p[4].equals(senha)) return true;
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar profissional: " + e.getMessage());
        }
        return false;
    }

    public static boolean verificarPaciente(String login, String senha) {
        try (BufferedReader br = new BufferedReader(new FileReader("pacientes.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] p = linha.split(";");
                if (p.length >= 4 && p[2].equals(login) && p[3].equals(senha)) return true;
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar paciente: " + e.getMessage());
        }
        return false;
    }

    public static boolean loginExiste(String login) {
        try (BufferedReader br = new BufferedReader(new FileReader("profissionais.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] p = linha.split(";");
                if (p.length >= 4 && p[3].equals(login)) return true;
            }
        } catch (Exception e) {}

        try (BufferedReader br = new BufferedReader(new FileReader("pacientes.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] p = linha.split(";");
                if (p.length >= 3 && p[2].equals(login)) return true;
            }
        } catch (Exception e) {}

        return false;
    }
    // MENU PROFISSIONAL
    public static void menuProfissional(String login) {
        while (true) {
            System.out.println("\n=== MENU PROFISSIONAL ===");
            System.out.println("1 - Listar pacientes e cadastrar medicação");
            System.out.println("2 - Cadastrar novo paciente");
            System.out.println("3 - Sair");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            if (op == 1) listarPacientesParaEscolha();
            else if (op == 2) cadastrarPaciente();
            else if (op == 3) return;
            else System.out.println("Opção inválida!");
        }
    }
    // CADASTRAR PACIENTE
    public static void cadastrarPaciente() {
        System.out.println("\n=== CADASTRO DE PACIENTE ===");

        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("CPF: ");
        String cpf = sc.nextLine();

        String login;
        while (true) {
            System.out.print("Login: ");
            login = sc.nextLine();
            if (!loginExiste(login)) break;
            System.out.println("Esse login já existe! Digite outro.");
        }

        System.out.print("Senha: ");
        String senha = sc.nextLine();

        try (FileWriter fw = new FileWriter("pacientes.txt", true)) {
            fw.write(nome + ";" + cpf + ";" + login + ";" + senha + "\n");
            System.out.println("Paciente cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar paciente: " + e.getMessage());
        }
    }
    // LISTAR PACIENTES
    public static void listarPacientesParaEscolha() {
        ArrayList<String> pacientes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("pacientes.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) pacientes.add(linha);
        } catch (Exception e) {
            System.out.println("Erro ao ler pacientes.txt: " + e.getMessage());
        }

        if (pacientes.size() == 0) {
            System.out.println("Nenhum paciente cadastrado.");
            return;
        }

        System.out.println("\n=== LISTA DE PACIENTES ===");
        for (int i = 0; i < pacientes.size(); i++) {
            String nome = pacientes.get(i).split(";")[0];
            System.out.println((i+1) + " - " + nome);
        }

        System.out.print("Escolha um paciente (0 para voltar): ");
        int op = sc.nextInt();
        sc.nextLine();

        if (op == 0) return;
        if (op < 1 || op > pacientes.size()) {
            System.out.println("Opção inválida!");
            return;
        }

        String paciente = pacientes.get(op - 1);
        String cpf = paciente.split(";")[1];

        cadastrarMedicacao(cpf);
    }
    // CADASTRO DE MEDICAÇÃO
    public static void cadastrarMedicacao(String cpfPaciente) {
        System.out.println("\n=== CADASTRO DE MEDICAÇÃO ===");

        System.out.print("Nome da medicação: ");
        String nome = sc.nextLine();
        System.out.print("Posologia (mg): ");
        String posologia = sc.nextLine();
        System.out.print("Vezes ao dia: ");
        String vezes = sc.nextLine();

        try (FileWriter fw = new FileWriter("medicacoes.txt", true)) {
            fw.write(cpfPaciente + ";" + nome + ";" + posologia + ";" + vezes + "\n");
            System.out.println("Medicação cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar medicação: " + e.getMessage());
        }
    }
    // MENU PACIENTE
    public static void menuPaciente(String login) {

        String cpfPaciente = pegarCpfPaciente(login);

        while (true) {
            System.out.println("\n=== MENU PACIENTE ===");
            System.out.println("1 - Visualizar medicações");
            System.out.println("2 - Sair");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            if (op == 1) mostrarMedicacoes(cpfPaciente);
            else if (op == 2) return;
            else System.out.println("Opção inválida!");
        }
    }

    public static String pegarCpfPaciente(String login) {
        try (BufferedReader br = new BufferedReader(new FileReader("pacientes.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] v = linha.split(";");
                if (v[2].equals(login)) return v[1];
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar CPF do paciente: " + e.getMessage());
        }
        return "";
    }
    // MOSTRAR MEDICAÇÕES DO PACIENTE
    public static void mostrarMedicacoes(String cpf) {
        System.out.println("\n=== MEDICAÇÕES ===");

        boolean achou = false;

        try (BufferedReader br = new BufferedReader(new FileReader("medicacoes"))) {
            String linha;  
            while ((linha = br.readLine()) != null) {
                String[] m = linha.split(";");
                if (m[0].equals(cpf)) {
                    System.out.println("Medicamento: " + m[1]);
                    System.out.println("Posologia: " + m[2] + " mg");
                    System.out.println("Vezes ao dia: " + m[3]);
                    System.out.println("-------------------------");
                    achou = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler medicações: " + e.getMessage());
        }

        if (!achou) System.out.println("Nenhuma medicação cadastrada.");
    }
}
