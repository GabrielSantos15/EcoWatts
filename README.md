# EcoWatts

### Gestão inteligente de energia residencial, unindo tecnologia e consciência ESG

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose"/>
  <img src="https://img.shields.io/badge/Android%20Studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white" alt="Android Studio"/>
  <img src="https://img.shields.io/badge/Room%20Database-4CAF50?style=for-the-badge&logo=sqlite&logoColor=white" alt="Room Database"/>
  <img src="https://img.shields.io/badge/Retrofit-48B983?style=for-the-badge&logo=square&logoColor=white" alt="Retrofit"/>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/status-conclu%C3%ADdo-brightgreen?style=flat-square" alt="status"/>
  <img src="https://img.shields.io/badge/plataforma-Android-blue?style=flat-square" alt="plataforma"/>
  <img src="https://img.shields.io/badge/licen%C3%A7a-MIT-lightgrey?style=flat-square" alt="licença"/>
  <img src="https://img.shields.io/badge/FIAP-2026-e6006e?style=flat-square" alt="FIAP"/>
</p>

---

## Visão Geral

O **EcoWatts** é um aplicativo Android nativo que ajuda o usuário a entender e reduzir seu consumo de energia elétrica residencial. A partir do cadastro de eletrodomésticos (potência e tempo de uso), o app calcula automaticamente o consumo mensal estimado em kWh e o custo correspondente na conta de luz, mostrando quais aparelhos pesam mais no bolso.

Além da calculadora de consumo, o EcoWatts entrega um dashboard financeiro completo e dicas de economia contextualizadas com o clima local, obtidas em tempo real através da API pública Open-Meteo.

### Impacto ESG

O projeto foi concebido com o ESG como parte central de sua proposta de valor:

| Pilar | Como o EcoWatts contribui |
|---|---|
| Environmental | Estimula o uso racional de energia, reduzindo desperdício e incentivando hábitos e equipamentos mais eficientes. |
| Social | Democratiza o acesso à informação sobre consumo energético, permitindo decisões mais conscientes no dia a dia. |
| Governance | Apresenta os cálculos de forma transparente, permitindo ao usuário auditar como o consumo e o custo foram estimados. |

---

## Screenshots

| Tela Inicial | Home | Dashboard |
|:---:|:---:|:---:|
| <img width="250" alt="Tela Inicial" src="https://github.com/user-attachments/assets/2f0fe152-cbc7-46c1-badf-3f1d4b2f12d9" /> | <img width="250" alt="Home" src="https://github.com/user-attachments/assets/91d8bb28-a58d-49f2-9903-d7a272924890" /> | <img width="250" alt="Dashboard" src="https://github.com/user-attachments/assets/afb10a3d-ae35-4d56-a650-d7f1fdd6ee10" /> |

---

## Principais Funcionalidades

- **Autenticação e Perfil de Usuário**
  Cadastro e login com persistência local. O usuário pode editar nome, cidade, e-mail, senha e enviar uma foto de perfil da galeria, convertida para `ByteArray` e persistida diretamente no Room.

- **Home dinâmica integrada ao clima**
  Consome a API Open-Meteo (Geocodificação + Previsão do Tempo) para obter a temperatura da cidade do usuário e gerar dicas de economia contextualizadas, como alertas sobre o uso do chuveiro elétrico em dias frios.

- **CRUD completo de eletrodomésticos**
  Cadastro, edição, visualização de detalhes e exclusão de aparelhos, informando nome, potência (W) e horas de uso diário, com cálculo automático de consumo e custo mensal.

- **Dashboard financeiro**
  Custo mensal estimado, valor configurável do kWh, gráfico de distribuição de consumo e ranking dos maiores consumidores da casa, com barras de progresso dinâmicas.

---

## Arquitetura e Tecnologias

O EcoWatts é um app Android nativo, com UI 100% declarativa em Jetpack Compose, seguindo o padrão MVVM (Model-View-ViewModel) com State Hoisting, garantindo separação de responsabilidades e uma única fonte de verdade para o estado da UI.

### Estrutura de pastas

```
app/src/main/java/br/com/fiap/EcoWatts
├── components   # Componentes de UI reutilizáveis (Compose)
├── dao          # Data Access Objects do Room
├── factory      # Factories de ViewModel
├── model        # Entidades e classes de domínio (User, Appliance...)
├── navigation   # Configuração de navegação (Navigation Compose)
├── repository   # Repositórios (dados locais e remotos)
├── screens      # Telas do app (Home, Aparelhos, Dashboard, Perfil...)
├── service      # Serviços Retrofit de consumo das APIs Open-Meteo
├── ui.theme     # Tema, cores e tipografia
├── util         # Classes utilitárias
└── MainActivity.kt
```

### Stack utilizada

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin |
| Interface | Jetpack Compose |
| Arquitetura | MVVM + State Hoisting |
| Persistência local | Room Database (SQLite) |
| Consumo de APIs | Retrofit |
| Serviços externos | Open-Meteo (Geocodificação e Previsão do Tempo) |

### Lógica de cálculo

```
Consumo mensal (kWh) = (Potência (W) × Horas de uso/dia × 30) ÷ 1000
Custo mensal (R$)    = Consumo mensal (kWh) × Preço do kWh (R$)
```

### Serviços externos consumidos

| Serviço | Endpoint | Finalidade |
|---|---|---|
| Geocodificação | `https://geocoding-api.open-meteo.com/v1/search` | Converte a cidade informada pelo usuário em coordenadas (latitude/longitude) |
| Previsão do Tempo | `https://api.open-meteo.com/v1/forecast` | Consulta temperatura, umidade e condições climáticas atuais |

---

## Como clonar e rodar o projeto localmente

### Pré-requisitos

- [Android Studio](https://developer.android.com/studio) (versão mais recente recomendada)
- JDK 17+
- Um emulador Android configurado ou dispositivo físico com depuração USB habilitada
- Conexão com a internet (para o consumo das APIs Open-Meteo)

### Passo a passo

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/ecowatts.git

# 2. Acesse a pasta do projeto
cd ecowatts
```

1. Abra o projeto no Android Studio (`File > Open` → selecione a pasta clonada).
2. Aguarde a sincronização automática do Gradle e o download das dependências.
3. Selecione um emulador ou conecte um dispositivo físico.
4. Clique em Run (ou use o atalho `Shift + F10`).

> Não é necessária nenhuma chave de API: os serviços da Open-Meteo utilizados são públicos e gratuitos.

---

## Autores e Créditos

Projeto acadêmico desenvolvido para a FIAP (Faculdade de Informática e Administração Paulista), em grupo, com base arquitetural em projetos pessoais próprios adaptados e evoluídos para este trabalho.

| Nome | GitHub |
|---|---|
| Gabriel dos Santos | [@GabrielSantos15](https://github.com/GabrielSantos15) |
| Nicolas Leonardo dos Santos | [@NicolasLeonardoo](https://github.com/NicolasLeonardoo) |
