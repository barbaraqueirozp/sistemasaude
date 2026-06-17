# Sistema Saúde

Aplicação Java simples para gerenciamento de pacientes e medicações.

**Visão geral:**

- **Projeto:** console Java que permite cadastro/login de profissionais e pacientes, além de registro de medicações.
- **Código:** está em [src](src) e usa o pacote `src`.

**Pré-requisitos:**

- Java JDK 17+ instalado.
- MySQL (ou MariaDB) em execução.
- MySQL Connector/J (coloque o JAR em `lib/` ou adicione ao classpath).

**Configurar banco de dados:**

- Execute o arquivo [init-db.sql](init-db.sql) para criar o banco `sistema_saude` e as tabelas necessárias.
- Atualize as credenciais em [src/Conexao.java](src/Conexao.java) se necessário.

**Compilar e executar (linha de comando, Windows):**

1. Criar pasta para dependências e colocar o connector JDBC (ex.: `mysql-connector-java.jar`) em `lib/`.

2. Compilar:

```powershell
mkdir out
javac -cp "lib/*" -d out src\*.java
```

3. Executar:

```powershell
java -cp "out;lib/*" src.Main
```

Observações:

- Os arquivos fontes usam `package src;`. O comando acima compila os arquivos dentro de `src` e gera classes em `out\src`.
- Se preferir, abra o projeto em uma IDE (IntelliJ IDEA, Eclipse, VS Code) e configure o classpath para incluir o driver JDBC.
