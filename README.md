# 🏡 HomeHelper

Aplicativo pessoal para auxiliar na **organização doméstica e financeira em casal**. Desenvolvido em **Java 17** com **Spring Boot**, integrando com **Firebase Firestore** e **Google Sheets API**.

---

## 📦 Funcionalidades

- ✅ Registro e soma de **gastos mensais**
- ✅ Gerenciamento de **lista de compras**
- ✅ **Rotina diária** com controle por dia da semana
- ✅ Criação de **lembretes** visíveis por todos com autor identificado
- ✅ Documentação de API com **Swagger**

---

## 🚀 Como rodar localmente

### 1. Clone o repositório
```bash
git clone https://github.com/RangelMRK/homehelper.git
cd homehelper
```

### 2. Configure variáveis de ambiente

Crie o arquivo `.env` (ou defina no seu sistema) com:

```env
SPREADSHEET_ID=ID_DA_SUA_PLANILHA
GOOGLE_KEY_PATH=src/main/resources/homehelper-key.json
FIREBASE_CONFIG_PATH=src/main/resources/firebase-key.json
```

> 🔒 Os arquivos de chave já estão ignorados no `.gitignore`.

### 3. Rode o projeto

```bash
mvn spring-boot:run
```

---

## 📘 Documentação dos Endpoints

Acesse [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) após iniciar a aplicação para visualizar todos os endpoints disponíveis.

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Maven**
- **Firebase Admin SDK**
- **Firestore**
- **Google Sheets API**
- **Swagger/OpenAPI**
- **Postman/Insomnia para testes**
