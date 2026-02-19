# 🎓 EduManager - Sistema de Gestão Escolar

Sistema completo de gerenciamento escolar desenvolvido em React para controle de alunos, turmas e professores.

## 📋 Características

- ✅ **Gestão de Alunos** - Cadastro completo com matrícula, dados pessoais e vinculação a turmas
- ✅ **Gestão de Turmas** - Controle de turmas com capacidade, período e professor responsável
- ✅ **Gestão de Professores** - Registro de professores com especialidades
- ✅ **Interface Moderna** - Design profissional e responsivo
- ✅ **Busca em Tempo Real** - Sistema de filtros para todas as entidades
- ✅ **CRUD Completo** - Criar, Ler, Atualizar e Deletar para todas as entidades
- ✅ **Validação de Formulários** - Campos obrigatórios e validações
- ✅ **Feedback Visual** - Indicadores de capacidade e status

## 🚀 Tecnologias Utilizadas

- **React** - Biblioteca JavaScript para construção de interfaces
- **Lucide React** - Ícones modernos e elegantes
- **CSS3** - Estilização com variáveis CSS e animações
- **Fetch API** - Comunicação com backend REST

## 📁 Estrutura do Projeto

```
frontend/
├── src/
│   ├── components/
│   │   ├── Header/
│   │   │   ├── Header.jsx
│   │   │   └── Header.css
│   │   └── Footer/
│   │       ├── Footer.jsx
│   │       └── Footer.css
│   ├── pages/
│   │   ├── AlunosPage.jsx
│   │   ├── TurmasPage.jsx
│   │   ├── ProfessoresPage.jsx
│   │   └── PagesStyle.css
│   ├── styles/
│   │   └── GlobalStyle.css
│   ├── App.jsx
│   └── main.jsx
└── package.json
```

## 🔧 Instalação

### Pré-requisitos

- Node.js (versão 14 ou superior)
- npm ou yarn
- Backend API rodando (ajuste as URLs conforme necessário)

### Passos para Instalação

1. **Clone o repositório ou crie a estrutura de pastas**

2. **Instale as dependências**

```bash
npm install
```

ou

```bash
yarn install
```

3. **Dependências necessárias**

```bash
npm install lucide-react
```

4. **Configure a URL da API**

Edite os arquivos de página e ajuste a constante `API_URL` para apontar para seu backend:

```javascript
// Em AlunosPage.jsx, TurmasPage.jsx e ProfessoresPage.jsx
const API_URL = 'http://localhost:3000/api/alunos'; // Ajuste conforme seu backend
```

5. **Execute o projeto**

```bash
npm run dev
```

ou

```bash
yarn dev
```

## 🔌 Integração com Backend

O frontend espera as seguintes rotas da API:

### Alunos
- `GET /api/alunos` - Lista todos os alunos
- `POST /api/alunos` - Cria novo aluno
- `PUT /api/alunos/:id` - Atualiza aluno
- `DELETE /api/alunos/:id` - Deleta aluno

**Estrutura de dados do Aluno:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "(11) 98765-4321",
  "dataNascimento": "2005-03-15",
  "matricula": "ALU2024001",
  "turmaId": 1
}
```

### Turmas
- `GET /api/turmas` - Lista todas as turmas
- `POST /api/turmas` - Cria nova turma
- `PUT /api/turmas/:id` - Atualiza turma
- `DELETE /api/turmas/:id` - Deleta turma

**Estrutura de dados da Turma:**
```json
{
  "id": 1,
  "nome": "1º Ano A",
  "ano": "2024",
  "periodo": "Manhã",
  "professorId": 1,
  "capacidadeMaxima": 30,
  "alunosMatriculados": 28
}
```

### Professores
- `GET /api/professores` - Lista todos os professores
- `POST /api/professores` - Cria novo professor
- `PUT /api/professores/:id` - Atualiza professor
- `DELETE /api/professores/:id` - Deleta professor

**Estrutura de dados do Professor:**
```json
{
  "id": 1,
  "nome": "Prof. Carlos Mendes",
  "email": "carlos@escola.com",
  "telefone": "(11) 99876-5432",
  "especialidade": "Matemática",
  "registro": "PROF2024001"
}
```

## 🎨 Personalização do Design

### Cores
Edite as variáveis CSS em `GlobalStyle.css`:

```css
:root {
  --primary: #2B5F9E;        /* Cor principal */
  --secondary: #F39C12;      /* Cor secundária */
  --accent: #E74C3C;         /* Cor de destaque */
  --success: #27AE60;        /* Cor de sucesso */
  /* ... outras cores */
}
```

### Tipografia
As fontes utilizadas são:
- **DM Serif Display** - Títulos e headings
- **Lexend** - Corpo do texto

Para alterar, modifique o import no `GlobalStyle.css` e as variáveis:

```css
--font-display: 'SuaFonteDisplay', serif;
--font-body: 'SuaFonteBody', sans-serif;
```

## 📱 Responsividade

O sistema é totalmente responsivo e se adapta a diferentes tamanhos de tela:
- **Desktop** - Layout completo com todas as funcionalidades
- **Tablet** - Layout adaptado com navegação otimizada
- **Mobile** - Interface simplificada e touch-friendly

## 🔒 Funcionalidades de Segurança

- Confirmação antes de deletar registros
- Validação de campos obrigatórios
- Tratamento de erros na comunicação com API
- Feedback visual para ações do usuário

## 🐛 Modo de Desenvolvimento

O sistema inclui dados mock para desenvolvimento sem backend:
- Dados de exemplo são carregados automaticamente se a API falhar
- Permite testar a interface sem configurar o backend completo
- Útil para desenvolvimento e demonstrações

## 📝 Como Usar

### Adicionar Novo Aluno
1. Clique em "Novo Aluno"
2. Preencha os dados do formulário
3. Clique em "Cadastrar"

### Editar Aluno
1. Clique no botão de editar (lápis) na linha do aluno
2. Modifique os dados desejados
3. Clique em "Atualizar"

### Excluir Aluno
1. Clique no botão de deletar (lixeira)
2. Confirme a exclusão

O mesmo processo se aplica para Turmas e Professores.

## 🎯 Próximas Funcionalidades (Sugestões)

- [ ] Dashboard com estatísticas
- [ ] Relatórios em PDF
- [ ] Sistema de autenticação
- [ ] Upload de fotos de perfil
- [ ] Gestão de notas e frequência
- [ ] Calendário escolar
- [ ] Sistema de mensagens
- [ ] Exportação de dados (Excel/CSV)

## 📧 Suporte

Para dúvidas ou problemas, ajuste o código conforme necessário para seu caso de uso específico.

## 📄 Licença

Este projeto é open source e está disponível para uso livre.

---

**Desenvolvido com ❤️ para educação**
