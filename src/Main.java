package src;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

    try (
        Connection con = Conexao.conectar();
    ) {
        System.out.println("Conexão bem-sucedida!");

    } catch (Exception e) {
        System.out.println("Erro: " + e.getMessage());
        throw new RuntimeException(e);
    }

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
    String conselho = sc.nextLine();

    String login;

    while (true) {

        System.out.print("Login: ");
        login = sc.nextLine();

        if (!loginExiste(login))
            break;

        System.out.println("Login já existe.");
    }

    System.out.print("Senha: ");
    String senha = sc.nextLine();

    String sql =
        "INSERT INTO profissionais(nome,id_conselho,login,senha) VALUES (?,?,?,?)";

    try (
        Connection con = Conexao.conectar();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {

        ps.setString(1, nome);
        ps.setString(2, conselho);
        ps.setString(3, login);
        ps.setString(4, senha);

        ps.executeUpdate();

        System.out.println("Profissional cadastrado!");

    } catch (Exception e) {
        System.out.println("Erro: " + e.getMessage());
    }
}

    // VERIFICAÇÃO (MENU TEM Q IDENTIFICAR SE É PCT OU PROFISSIONAL P/ MOSTRAR O MENU CORRETO)
    public static boolean verificarProfissional(String login, String senha) {
        String sql = "SELECT * FROM profissionais WHERE login = ? AND senha = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, login);
                stmt.setString(2, senha);
                ResultSet rs = stmt.executeQuery();
                return rs.next(); 
        } catch (Exception e) {
            if (e instanceof SQLException) {
                System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
            } else {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
           
        }
        return false;
    }

    public static boolean verificarPaciente(String login, String senha) {
        String sql = "SELECT * FROM pacientes WHERE login = ? AND senha = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, login);
                stmt.setString(2, senha);
                ResultSet rs = stmt.executeQuery();
                return rs.next(); 
        } catch (Exception e) {
            System.out.println("Erro ao verificar paciente: " + e.getMessage());
        }
        return false;
    }

    public static boolean loginExiste(String login) {
        String sql = "SELECT * FROM profissionais WHERE login = ? UNION SELECT * FROM pacientes WHERE login = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, login);
                stmt.setString(2, login);
                ResultSet rs = stmt.executeQuery();
                return rs.next(); 
        } catch (Exception e) {
            if (e instanceof SQLException) {
                System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
            } else {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
        }

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

        if (!loginExiste(login))
            break;

        System.out.println("Login já existe.");
    }

    System.out.print("Senha: ");
    String senha = sc.nextLine();

    String sql =
        "INSERT INTO pacientes(nome,cpf,login,senha) VALUES (?,?,?,?)";

    try (
        Connection con = Conexao.conectar();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {

        ps.setString(1, nome);
        ps.setString(2, cpf);
        ps.setString(3, login);
        ps.setString(4, senha);

        ps.executeUpdate();

        System.out.println("Paciente cadastrado!");

    } catch (Exception e) {
        System.out.println("Erro: " + e.getMessage());
    }
}
    // LISTAR PACIENTES
  public static void listarPacientesParaEscolha() {

    ArrayList<Paciente> pacientes = new ArrayList<>();

    String sql =
            "SELECT nome, cpf FROM pacientes ORDER BY nome";

    try (
            Connection con = Conexao.conectar();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
    ) {

        while (rs.next()) {

            pacientes.add(
                    new Paciente(
                            rs.getString("nome"),
                            rs.getString("cpf")
                    )
            );
        }

    } catch (Exception e) {
        System.out.println("Erro ao buscar pacientes: "
                + e.getMessage());
        return;
    }

    if (pacientes.isEmpty()) {
        System.out.println("Nenhum paciente cadastrado.");
        return;
    }

    System.out.println("\n=== LISTA DE PACIENTES ===");

    for (int i = 0; i < pacientes.size(); i++) {

        System.out.println(
                (i + 1) + " - " +
                        pacientes.get(i).getNome()
        );
    }

    System.out.print(
            "Escolha um paciente (0 para voltar): ");

    int op = sc.nextInt();
    sc.nextLine();

    if (op == 0)
        return;

    if (op < 1 || op > pacientes.size()) {

        System.out.println("Opção inválida!");
        return;
    }

    String cpfPaciente =
            pacientes.get(op - 1).getCpf();

    cadastrarMedicacao(cpfPaciente);

    }
    // CADASTRO DE MEDICAÇÃO
    public static void cadastrarMedicacao(String cpfPaciente) {

    System.out.println("\n=== CADASTRO DE MEDICAÇÃO ===");

    System.out.print("Nome da medicação: ");
    String nome = sc.nextLine();

    System.out.print("Posologia (mg): ");
    String posologia = sc.nextLine();

    System.out.print("Vezes ao dia: ");
    int vezes = sc.nextInt();
    sc.nextLine();

    String sql =
        "INSERT INTO medicacoes(paciente_cpf,nome_medicacao,posologia,vezes_ao_dia) VALUES (?,?,?,?)";

    try (
        Connection con = Conexao.conectar();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {

        ps.setString(1, cpfPaciente);
        ps.setString(2, nome);
        ps.setString(3, posologia);
        ps.setInt(4, vezes);

        ps.executeUpdate();

        System.out.println("Medicação cadastrada!");

    } catch (Exception e) {
        System.out.println("Erro: " + e.getMessage());
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

    String sql =
        "SELECT cpf FROM pacientes WHERE login = ?";

    try (
        Connection con = Conexao.conectar();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {

        ps.setString(1, login);

        ResultSet rs = ps.executeQuery();

        if (rs.next())
            return rs.getString("cpf");

    } catch (Exception e) {
        System.out.println("Erro: " + e.getMessage());
    }

    return "";
}
    // MOSTRAR MEDICAÇÕES DO PACIENTE
    public static void mostrarMedicacoes(String cpf) {

    String sql =
        "SELECT * FROM medicacoes WHERE paciente_cpf = ?";

    boolean achou = false;

    try (
        Connection con = Conexao.conectar();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {

        ps.setString(1, cpf);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            System.out.println(
                "Medicamento: " +
                rs.getString("nome_medicacao"));

            System.out.println(
                "Posologia: " +
                rs.getString("posologia"));

            System.out.println(
                "Vezes ao dia: " +
                rs.getInt("vezes_ao_dia"));

            System.out.println("--------------------");

            achou = true;
        }

    } catch (Exception e) {
        System.out.println("Erro: " + e.getMessage());
    }

    if (!achou)
        System.out.println("Nenhuma medicação cadastrada.");
}
}
