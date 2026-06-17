package src;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
// VERIFICA SE O LOGIN É DE PACIENTE
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
// VERIFICAÇÃO PARA VER SE O LOGIN EXISTE NA HORA DE CADASTRAR UM NOVO PROFISSIONAL OU PACIENTE (NÃO PODE TER DOIS LOGINS IGUAIS)
    public static boolean loginExiste(String login) {
        String sql = "SELECT * FROM profissionais WHERE login = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, login);
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
            System.out.println("1 - Listar pacientes");
            System.out.println("2 - Cadastrar novo paciente");
            System.out.println("3 - Buscar paciente por CPF");
            System.out.println("4 - Buscar paciente por nome");
            System.out.println("5 - Cadastro de medicação");
            System.out.println("6 - Sair");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            if (op == 1) listarPacientesParaEscolha();
            else if (op == 2) cadastrarPaciente();
            else if (op == 3) buscarPacientePorCPF();
            else if (op == 4) buscarPacientePorNome();
            else if (op == 5) cadastrarMedicacao();
            else if (op == 6) return;
            else System.out.println("Opção inválida!");
        }
    }
    // Cadastro geral de medicações
    public static void cadastrarMedicacao() {
        System.out.println("\n=== CADASTRO DE MEDICACAO ===");
        System.out.print("Digite a medicacao a ser cadastrada: ");
        String nome = sc.nextLine();

        Integer idExistente = buscarIdMedicacaoPorNome(nome);

        if (idExistente != null) {
            System.out.println("Medicacao ja cadastrada.");
            cadastrarPosologiasDaMedicacao(idExistente, nome);
            return;
        }

        String sql = "INSERT INTO medicacoes(nome) VALUES (?)";
        int idMedicacao;

        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nome);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) {
                    System.out.println("Medicacao cadastrada, mas nao foi possivel recuperar o codigo.");
                    return;
                }

                idMedicacao = rs.getInt(1);
            }

            System.out.println("Medicacao cadastrada!");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return;
        }

        cadastrarPosologiasDaMedicacao(idMedicacao, nome);
    }
// BUSCAR ID DA MEDICAÇÃO POR NOME PARA VER SE ELA JÁ EXISTE NO BANCO E PARA PEGAR O ID NA HORA DE CADASTRAR AS POSOLOGIAS
    public static Integer buscarIdMedicacaoPorNome(String nome) {
        String sql = "SELECT id FROM medicacoes WHERE nome = ?";

        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nome);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar medicacao: " + e.getMessage());
        }

        return null;
    }
// CADASTRO DE POSOLOGIAS DE UMA MEDICAÇÃO (EXEMPLO: 500 MG, 750 MG, 1 G)
    public static void cadastrarPosologiasDaMedicacao(int idMedicacao, String nomeMedicacao) {
        System.out.println("\n=== POSOLOGIAS DE " + nomeMedicacao + " ===");
        System.out.println("Cadastre as opcoes de posologia. Exemplo: 500 mg, 750 mg, 1 g.");

        while (true) {
            System.out.print("Digite uma posologia (ENTER para finalizar): ");
            String descricao = sc.nextLine();

            if (descricao.trim().isEmpty()) {
                break;
            }

            inserirPosologia(idMedicacao, descricao);
        }

        System.out.println("A quantidade de vezes ao dia serao informadas ao fazer a prescricao para o paciente.");
    }
// INSERIR POSOLOGIA NO BANCO
    public static void inserirPosologia(int idMedicacao, String descricao) {
        String sql = "INSERT INTO posologias(medicacao_id, descricao) VALUES (?, ?)";

        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMedicacao);
            ps.setString(2, descricao);
            ps.executeUpdate();

            System.out.println("Posologia cadastrada!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar posologia: " + e.getMessage());
        }
    }
    // BUSCAR PACIENTE POR CPF
    public static void buscarPacientePorCPF() {
        System.out.print("Digite o CPF do paciente: ");
        String cpf = sc.nextLine();

        String sql = "SELECT * FROM pacientes WHERE cpf = ?";

        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Nome: " + rs.getString("nome"));
                System.out.println("CPF: " + rs.getString("cpf"));
                menuPacienteProfissional(cpf);
            } else {
                System.out.println("Paciente não encontrado.");
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    // BUSCAR PACIENTE POR NOME
    public static void buscarPacientePorNome() {
        System.out.print("Digite o nome do paciente: ");
        String nome = sc.nextLine();

        String sql = "SELECT * FROM pacientes WHERE nome LIKE ?";

        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nome + "%");
            ResultSet rs = ps.executeQuery();

            boolean achou = false;

            while (rs.next()) {
                System.out.println("Nome: " + rs.getString("nome"));
                System.out.println("CPF: " + rs.getString("cpf"));
                menuPacienteProfissional(rs.getString("cpf"));
                System.out.println("--------------------");
                achou = true;
            }

            if (!achou) {
                System.out.println("Nenhum paciente encontrado.");
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    // MENU PROFISISONAL DO PACIENTE
    public static void menuPacienteProfissional(String cpf) {

    while (true) {

        System.out.println("\n=== MENU DO PACIENTE ===");
        System.out.println("1 - Ver prescrições do paciente");
        System.out.println("2 - Fazer prescrição");
        System.out.println("3 - Excluir prescrição");
        System.out.println("4 - Voltar");
        System.out.print("Escolha: ");

        int op = sc.nextInt();
        sc.nextLine();

        switch (op) {

            case 1:
                mostrarMedicacoes(cpf);
                    while (true) {
                        System.out.println("1 - Alterar prescrição");
                        System.out.println("2 - Voltar");
                        System.out.print("Escolha: ");
                        int subOp = sc.nextInt();
                        sc.nextLine();

                        if (subOp == 1) {
                            alterarMedicacao(cpf);
                            break;
                        } else if (subOp == 2) {
                            break;
                        } else {
                            System.out.println("Opção inválida!");
                        }
                    }
                break;

            case 2:
                cadastrarPrescricao(cpf);
                break;

            case 3:
                excluirMedicacao(cpf);
                break;

            case 4:
                return;

            default:
                System.out.println("Opção inválida!");
        }
    }
}
    // CADASTRO DE PRESCRIÇÃO DO PACIENTE (ESCOLHER MEDICAÇÃO, POSOLOGIA E VEZES AO DIA)
    public static void cadastrarPrescricao(String cpf) {
        System.out.println("\n=== CADASTRO DE PRESCRICAO ===");
        List<Medicacao> medicacoes = buscarMedicacoesCadastradas();

        if (medicacoes.isEmpty()) {
            System.out.println("Nenhuma medicacao cadastrada no banco.");
            return;
        }

        System.out.println("\nMedicacoes disponiveis:");
        for (int i = 0; i < medicacoes.size(); i++) {
            System.out.println((i + 1) + " - " + medicacoes.get(i).getNome());
        }

        System.out.print("Escolha a medicacao (0 para voltar): ");
        int opMedicacao = sc.nextInt();
        sc.nextLine();

        if (opMedicacao == 0) return;

        if (opMedicacao < 1 || opMedicacao > medicacoes.size()) {
            System.out.println("Opcao invalida!");
            return;
        }

        Medicacao medicacao = medicacoes.get(opMedicacao - 1);
        List<Posologia> posologias = buscarPosologias(medicacao.getId());

        if (posologias.isEmpty()) {
            System.out.println("Nenhuma posologia cadastrada para essa medicacao.");
            return;
        }

        System.out.println("\nPosologias disponiveis para " + medicacao.getNome() + ":");
        for (int i = 0; i < posologias.size(); i++) {
            System.out.println((i + 1) + " - " + posologias.get(i).getDescricao());
        }

        System.out.print("Escolha a posologia (0 para voltar): ");
        int opPosologia = sc.nextInt();
        sc.nextLine();

        if (opPosologia == 0) return;

        if (opPosologia < 1 || opPosologia > posologias.size()) {
            System.out.println("Opcao invalida!");
            return;
        }

        System.out.print("Vezes ao dia: ");
        int vezesAoDia = sc.nextInt();
        sc.nextLine();

        Posologia posologia = posologias.get(opPosologia - 1);
        registrarPrescricao(cpf, medicacao.getId(), posologia.getId(), vezesAoDia);
    }
// BUSCAR NO BANCO AS MEDICAÇÕES CADASTRADAS PARA MOSTRAR NA HORA DE CADASTRAR A PRESCRIÇÃO
    public static List<Medicacao> buscarMedicacoesCadastradas() {
        List<Medicacao> medicacoes = new ArrayList<>();
        String sql = "SELECT id, nome FROM medicacoes ORDER BY nome";

        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                medicacoes.add(new Medicacao(rs.getInt("id"), rs.getString("nome")));
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar medicacoes: " + e.getMessage());
        }

        return medicacoes;
    }
// BUSCAR NO BANCO POSOLOGIAS DE UMA MEDICAÇÃO PARA MOSTRAR NA HORA DE CADASTRAR A PRESCRIÇÃO E NA HORA DE ALTERAR A PRESCRIÇÃO
    public static List<Posologia> buscarPosologias(int idMedicacao) {
        List<Posologia> posologias = new ArrayList<>();
        String sql = "SELECT id, descricao FROM posologias WHERE medicacao_id = ? ORDER BY descricao";

        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMedicacao);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    posologias.add(new Posologia(rs.getInt("id"), rs.getString("descricao")));
                }
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar posologias: " + e.getMessage());
        }

        return posologias;
    }
// REGISTRAR PRESCRIÇÃO NO BANCO
    public static void registrarPrescricao(String cpf, int idMedicacao, int idPosologia, int vezesAoDia) {
        String sql = "INSERT INTO prescricoes(paciente_cpf, medicacao_id, posologia_id, vezes_ao_dia) VALUES (?,?,?,?)";

        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cpf);
            ps.setInt(2, idMedicacao);
            ps.setInt(3, idPosologia);
            ps.setInt(4, vezesAoDia);

            ps.executeUpdate();

            System.out.println("Prescricao cadastrada!");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    //ALTERAR MEDICACAO
    public static void alterarMedicacao(String cpf) {
        System.out.print("Digite o codigo da prescricao a alterar: ");
        int idPrescricao = sc.nextInt();
        sc.nextLine();

        Integer idMedicacao = buscarIdMedicacaoDaPrescricao(cpf, idPrescricao);

        if (idMedicacao == null) {
            System.out.println("Prescricao nao encontrada para esse paciente.");
            return;
        }

        List<Posologia> posologias = buscarPosologias(idMedicacao);

        if (posologias.isEmpty()) {
            System.out.println("Nenhuma posologia cadastrada para essa medicacao.");
            return;
        }

        System.out.println("\nNovas posologias disponiveis:");
        for (int i = 0; i < posologias.size(); i++) {
            System.out.println((i + 1) + " - " + posologias.get(i).getDescricao());
        }

        System.out.print("Escolha a nova posologia: ");
        int opPosologia = sc.nextInt();
        sc.nextLine();

        if (opPosologia < 1 || opPosologia > posologias.size()) {
            System.out.println("Opcao invalida!");
            return;
        }

        System.out.print("Novas vezes ao dia: ");
        int vezes = sc.nextInt();
        sc.nextLine();

        String sql = "UPDATE prescricoes SET posologia_id = ?, vezes_ao_dia = ? WHERE paciente_cpf = ? AND id_prescricao = ?";

        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, posologias.get(opPosologia - 1).getId());
            ps.setInt(2, vezes);
            ps.setString(3, cpf);
            ps.setInt(4, idPrescricao);

            int affected = ps.executeUpdate();

            if (affected > 0) System.out.println("Prescricao alterada!");
            else System.out.println("Prescricao nao encontrada para esse paciente.");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
// BUSCAR O ID DA MEDICAÇÃO DE UMA PRESCRIÇÃO PARA PODER PEGAR AS POSOLOGIAS DISPONÍVEIS NA ALTERAÇÃO
    public static Integer buscarIdMedicacaoDaPrescricao(String cpf, int idPrescricao) {
        String sql = "SELECT medicacao_id FROM prescricoes WHERE paciente_cpf = ? AND id_prescricao = ?";

        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cpf);
            ps.setInt(2, idPrescricao);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("medicacao_id");
                }
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return null;
    }
// EXCLUIR MEDICAÇÃO
    private static void excluirMedicacao(String cpf) {
        System.out.print("Digite o codigo da prescricao a excluir: ");
        int idPrescricao = sc.nextInt();
        sc.nextLine();
        String sql = "DELETE FROM prescricoes WHERE paciente_cpf = ? AND id_prescricao = ?";

        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cpf);
            ps.setInt(2, idPrescricao);

            int affected = ps.executeUpdate();

            if (affected > 0) System.out.println("Prescricao excluida!");
            else System.out.println("Prescricao nao encontrada para esse paciente.");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
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

    menuPacienteProfissional(cpfPaciente);
    }
    // MENU QUE PACIENTE VÊ AO SER AUTENTICADO (VER MEDICAÇÕES)
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
    // BUSCA NO BANCO MEDICAÇÕES DO PACIENTE EM FORMATO DE LISTA (NOME, POSOLOGIA, VEZES AO DIA)
   public static List<Medicacao> buscarMedicacoes(String cpf) {
    String sql =
        "SELECT p.id_prescricao, m.nome, po.descricao, p.vezes_ao_dia " +
        "FROM prescricoes p " +
        "INNER JOIN medicacoes m ON m.id = p.medicacao_id " +
        "INNER JOIN posologias po ON po.id = p.posologia_id " +
        "WHERE p.paciente_cpf = ? " +
        "ORDER BY p.data_prescricao DESC";
    List<Medicacao> lista = new ArrayList<>();

    try (Connection con = Conexao.conectar(); 
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, cpf);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Criando o objeto com os dados do banco
                Medicacao med = new Medicacao(
                    rs.getInt("id_prescricao"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getInt("vezes_ao_dia")
                );
                // Adicionando o objeto na lista
                lista.add(med);
            }
        }
    } catch (Exception e) {
        System.out.println("Erro ao buscar medicações: " + e.getMessage());
    }

    // Retorna a lista (vazia se não encontrar nada ou cheia se encontrar)
    return lista;
}
// IMPRIME AS MEDICAÇÕES DO PACIENTE COM NOME, POSOLOGIA E VEZES AO DIA
    public static void mostrarMedicacoes(String cpf) {
        List<Medicacao> medicacoes = buscarMedicacoes(cpf);

        if (medicacoes.isEmpty()) {
            System.out.println("Nenhuma medicação cadastrada para este paciente.");
            return;
        }

        System.out.println("\n=== MEDICAÇÕES DO PACIENTE ===");
        for (Medicacao med : medicacoes) {
            System.out.println("Codigo da prescricao: " + med.getId());
            System.out.println("Nome: " + med.getNome());
            System.out.println("Posologia: " + med.getPosologia());
            System.out.println("Vezes ao dia: " + med.getVezesAoDia());
            System.out.println("--------------------");
        }
    }
}
