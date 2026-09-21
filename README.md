# SecureLogin PUC

Aplicação web de autenticação e cadastro de usuários desenvolvida com Spring Boot e Thymeleaf, para a Atividade 02 da disciplina.

A interface usa um tema escuro próprio, com fundo em gradiente animado, cartão translúcido (efeito vidro), transições nos campos e botões, e layout responsivo.

**Integrantes:** TODO - nome dos dois integrantes da dupla

---

## Tecnologias

- Java 25
- Spring Boot 4.1.1
- Spring Security (autenticação por formulário e BCrypt)
- Thymeleaf
- Spring Mail (recuperação de senha)
- Jackson (persistência dos usuários em arquivo JSON)

## Funcionalidades

- Login por usuário **ou** email
- Cadastro de novos usuários com validação
- Senhas armazenadas com hash BCrypt
- Bloqueio de acesso a páginas protegidas para usuários não autenticados
- Área administrativa restrita ao perfil ADMIN
- Encerramento de sessão
- Recuperação de senha por email, com link de redefinição

### Validações do cadastro

- Campos obrigatórios não podem ficar vazios
- Formato de email é verificado
- Senha com no mínimo 6 caracteres
- Senha e confirmação devem ser iguais
- Usuário e email não podem estar duplicados

## Estrutura do projeto

```
src/
├── main/
│   ├── java/com/example/SecureLoginPUC/
│   │   ├── SecureLoginPUCApplication.java
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   └── UserConfig.java
│   │   ├── controller/
│   │   │   └── SecureLoginPUCController.java
│   │   ├── model/
│   │   │   └── User.java
│   │   ├── repository/
│   │   │   └── UserJsonRepository.java
│   │   └── service/
│   │       ├── UserService.java
│   │       ├── JsonUserDetailsService.java
│   │       ├── PasswordResetService.java
│   │       └── EmailService.java
│   └── resources/
│       ├── application.yaml
│       ├── static/css/style.css
│       └── templates/
│           ├── login.html
│           ├── register.html
│           ├── recoverpassword.html
│           ├── resetpassword.html
│           ├── home.html
│           ├── admin.html
│           └── error.html
└── test/
```

## Endpoints

| Método | Endpoint           | Descrição                                   | Acesso      |
|--------|--------------------|---------------------------------------------|-------------|
| GET    | `/login`           | Tela de login                               | Público     |
| POST   | `/login`           | Autenticação (tratada pelo Spring Security) | Público     |
| GET    | `/register`        | Tela de cadastro                            | Público     |
| POST   | `/register`        | Processa o cadastro                         | Público     |
| GET    | `/recoverpassword` | Tela de recuperação de senha                | Público     |
| POST   | `/recoverpassword` | Envia o email com o link de redefinição     | Público     |
| GET    | `/resetpassword`   | Tela de nova senha (acessada pelo link)     | Público     |
| POST   | `/resetpassword`   | Grava a nova senha                          | Público     |
| GET    | `/home`            | Área do usuário autenticado                 | Autenticado |
| GET    | `/admin`           | Área administrativa                         | ROLE_ADMIN  |
| POST   | `/logout`          | Encerra a sessão                            | Autenticado |

## Como executar

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Depois acesse http://localhost:8080/login

### Usuários iniciais

Na primeira execução a aplicação cria o arquivo `data/users.json` com dois usuários definidos em `app.seed` no `application.yaml`:

| Usuário  | Senha   | Perfil |
|----------|---------|--------|
| `rafael` | `1234`  | USER   |
| `admin`  | `admin` | ADMIN  |

Os usuários cadastrados pela tela de registro são gravados nesse mesmo arquivo e continuam valendo após reiniciar a aplicação.

## Configuração do ambiente

Nenhuma credencial fica no código-fonte. Os valores sensíveis são lidos de variáveis de ambiente, com valores padrão definidos em `application.yaml`:

| Variável              | Descrição                                      | Padrão                  |
|-----------------------|------------------------------------------------|-------------------------|
| `MAIL_USERNAME`       | Conta Gmail que envia o email de recuperação   | vazio                   |
| `MAIL_PASSWORD`       | Senha de app do Gmail (não é a senha da conta) | vazio                   |
| `SEED_USER_PASSWORD`  | Senha do usuário inicial                       | `1234`                  |
| `SEED_ADMIN_PASSWORD` | Senha do administrador inicial                 | `admin`                 |
| `USERS_FILE`          | Caminho do arquivo JSON de usuários            | `./data/users.json`     |
| `APP_BASE_URL`        | URL base usada no link enviado por email       | `http://localhost:8080` |

### Configurando o envio de email

1. Ative a verificação em duas etapas na conta Google.
2. Gere uma senha de app em https://myaccount.google.com/apppasswords
3. Antes de iniciar a aplicação, defina as variáveis:

```powershell
$env:MAIL_USERNAME = "seuemail@gmail.com"
$env:MAIL_PASSWORD = "sua-senha-de-app"
```

Sem essas variáveis a aplicação continua funcionando: o link de recuperação é impresso no console em vez de ser enviado por email.

O arquivo `data/users.json` está no `.gitignore` porque contém os hashes das senhas.

## Capturas de tela

TODO - adicionar imagens das telas de login e cadastro.

## Licença

MIT
