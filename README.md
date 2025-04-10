# 🏡 HomeHelper - Aplicativo de Organização Doméstica

Este é um backend Java com Spring Boot desenvolvido para um aplicativo que auxilia casais (especialmente com TDAH) na organização financeira, de rotinas e lembretes da casa. O sistema é modular, seguro, e utiliza planilhas do Google como fonte para os gastos mensais.

---

## ✅ Funcionalidades

### 📊 Gastos
- Integração com Google Sheets
- Atualização e soma de valores por categoria e mês

### 🛒 Lista de Compras
- CRUD completo
- Sincronizado via Firebase Firestore

### 📆 Rotina Diária
- Tarefas por dia da semana
- Reset automático das rotinas todos os dias às 7h

### 🔔 Lembretes
- Visíveis para todos os usuários
- Autor identificado em cada lembrete

### 📋 Dashboard
- `/dashboard/hoje`: lista de tarefas de rotina e lembretes do dia
- Agrupado por tipo, com status de conclusão e autor

### 🔐 Segurança
- Autenticação via JWT
- Registro e login com senha criptografada (BCrypt)
- Todos os endpoints protegidos (exceto `/auth/**`)
- Swagger com botão de autorização via token

---

## 🔧 Configuração da Planilha

A estrutura da planilha de gastos é modular. Você pode copiar a planilha base e criar seu próprio arquivo de configuração.

### 📎 Planilha Exemplo (readonly)
Copie este modelo para seu Google Drive:
👉 [https://docs.google.com/spreadsheets/d/1ESBhM42BT44FDfl1bYqAOmkWQtrOjn8eFS_hhpvJTEE](https://docs.google.com/spreadsheets/d/1ESBhM42BT44FDfl1bYqAOmkWQtrOjn8eFS_hhpvJTEE)

### 🛠️ Configurando o `planilha-config.json`
No diretório `src/main/resources/`, você deve criar um arquivo `planilha-config.json` com os campos:

```json
{
  "colunasPorMes": {
    "Janeiro": "F",
    "Fevereiro": "G",
    "Março": "H",
    "...": "...",
    "Dezembro": "Q"
  },
  "linhasPorCategoria": {
    "Luz": 24,
    "Água": 25,
    "Internet": 26,
    "Mercado": 27,
    "...": 59
  }
}
```

Essas colunas e linhas devem bater com a posição dos dados na sua planilha copiada. A lógica de leitura usa essa estrutura para atualizar e somar valores diretamente nas células corretas.

### 🌐 Outras variáveis de ambiente obrigatórias
No seu sistema ou `.env`, configure:
```bash
SPREADSHEET_ID=<ID da sua planilha Google>
GOOGLE_KEY_PATH=src/main/resources/homehelper-key.json
```

---

## 🚀 Testando com Swagger
Acesse `http://localhost:8080/swagger-ui.html`

1. Clique em **Authorize** e cole seu token JWT:
   ```
   Bearer eyJhbGciOiJIUz...
   ```

2. Teste os endpoints:
    - `POST /auth/register`
    - `POST /auth/login`
    - `GET /dashboard/hoje`
    - `GET /gastos/valores?mes=Janeiro`

---

## 📌 Próximos passos
- `/dashboard/resumo` com tarefas concluídas vs pendentes
- Histórico e agendamentos personalizados
- Integração com app Android nativo

---

## 👥 Feito para casais que querem mais clareza no dia a dia com leveza e organização ✨
