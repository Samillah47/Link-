# LINK — Universal eCash & AI Proximity Payment Protocol

> **"Pay people, not phone numbers."**  
> *A next-generation financial protocol for Africa and emerging markets powered by Jetpack Compose, Room, and Gemini AI.*

---

## 🌟 Overview

**LINK** is a unified financial experience that removes friction from mobile payments. Instead of typing long, error-prone phone numbers or juggling disparate banking and mobile money apps, LINK uses natural language AI parsing, encrypted proximity linking (**Aura Sync**), and zero-fee eCash routing to connect users directly.

Whether paying a merchant in Kinyarwanda, sending funds to `@alice` via close proximity, or calculating East African cross-border FX rates, LINK bridges mobile money (MTN MoMo, Airtel), traditional banks (Bank of Kigali), and digital eCash wallets into a single, seamless interface.

---

## 🚀 Key Features

### 1. 🤖 AI Intent Pay (Multilingual Gemini NLU)
- Express payment requests in natural spoken or written language.
- Full support for **Kinyarwanda**, **English**, **Kiswahili**, and **French**.
- Example prompts:
  - *"Yishyura Jean Luc 25,000 RWF kuri MoMo kuri consulting fee"*
  - *"Send 12,000 RWF for electricity token to EUCL"*
  - *"Lipa Alice 5,000 RWF kupitia eCash"*
- **Smart Security Shield**: Detects suspicious or high-risk transfers and flags them before execution.

### 2. 📶 Aura Sync Proximity Linking (UWB / BLE Encrypted)
- **Send & Receive**: Send money directly to handles like `@alice` or receive funds via encrypted proximity channels.
- **256-bit Ephemeral ECDH Encryption**: Dual-authenticated proximity session that burns the cryptographic channel immediately upon transaction completion.
- **Zero Protocol Fees**: Uses eCash protocol layer to eliminate traditional telco transfer tariffs.

### 3. 💳 Unified Liquidity Hub
- Real-time balance aggregation across **MTN Mobile Money**, **Bank of Kigali**, **eCash Protocol**, and **Airtel Money**.
- **AI Smart Routing**: Automatically selects the optimal payment gateway based on fee, speed, and liquidity constraints.

### 4. 📲 Live QR & Receive Badge
- Display a personalized **LINK Badge** (`@keza.rw` / `RW-2026-99201`) or copyable paylink.
- Built-in simulation tool to demonstrate real-time incoming transfer notifications.

### 5. 📊 Smart Financial Analytics & Health Score
- **Financial Health Score (94/100)**: Analyzes zero-fee eCash utilization and monthly tariff savings.
- **Income vs Outflow Breakdown**: Real-time category tracking for utilities, rent, groceries, and cross-border transfers.
- **Live East Africa FX Calculator**: Instant conversion across **KES**, **UGX**, and **USD**.
- **One-Click Statement Export**: Export official financial statements in PDF/CSV formats.

### 6. 🌐 Multilingual Locale Engine
- Instant system language switching between **Kinyarwanda (🇷🇼)**, **English (🇬🇧)**, **Kiswahili (🇹🇿)**, and **Français (🇫🇷)**.

---

## 🏗️ Architecture & Tech Stack

- **Language**: 100% Modern Kotlin
- **UI Framework**: Jetpack Compose (Material Design 3 with custom color tokens)
- **Local Database**: Room 2.6+ with `fallbackToDestructiveMigration` enabled for schema resilience
- **AI Engine**: Google Gemini API via server-side key / injected `BuildConfig`
- **State Management**: Android ViewModel & Kotlin `StateFlow` / `collectAsStateWithLifecycle`
- **Asynchronous Execution**: Kotlin Coroutines & Flow
- **Icons**: Material Symbols & Icons Extended

---

## 🛠️ Project Structure

```
app/src/main/java/com/example/
├── ai/                      # Gemini NLU service & intent parsing models
│   ├── GeminiService.kt
│   └── PaymentIntentResult.kt
├── data/                    # Room Database, DAOs & Repository
│   ├── AppDatabase.kt
│   ├── LinkRepository.kt
│   ├── dao/
│   │   └── FinancialDao.kt
│   └── entity/
│       ├── FinancialAccountEntity.kt
│       └── TransactionEntity.kt
├── ui/                      # Jetpack Compose Screens & ViewModels
│   ├── LinkViewModel.kt
│   ├── MainScreen.kt
│   ├── components/          # Reusable Dialogs, Sheets & Modals
│   │   ├── AuthModal.kt
│   │   ├── EditCredentialsDialog.kt
│   │   ├── IntentConfirmationSheet.kt
│   │   ├── NotificationCenterDialog.kt
│   │   ├── ReceiveMoneyModal.kt
│   │   ├── ReportsDialog.kt
│   │   └── TransactionDetailDialog.kt
│   ├── screens/             # Primary Navigation Views
│   │   ├── AccountScreen.kt
│   │   ├── AiIntentScreen.kt
│   │   ├── AuraSyncScreen.kt
│   │   ├── DashboardScreen.kt
│   │   └── FinancialMemoryScreen.kt
│   └── theme/               # Material 3 Color Tokens & Typography
└── util/                    # Localization strings & multi-language mapping
    └── Localization.kt
```

---

## 🏃 Getting Started & How to Run

### Prerequisites
- **Android Studio**: Jellyfish / Ladybug or newer
- **JDK**: JDK 17 or higher
- **Android SDK**: `compileSdk 35`, `minSdk 26`

### Steps to Run

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Samillah47/Latent-Space-Cadets.git
   cd Latent-Space-Cadets
   ```

2. **Configure Environment Variables (API Keys)**:
   - Create a `.env` file in the root directory (refer to `.env.example`):
     ```env
     GEMINI_API_KEY=your_gemini_api_key_here
     ```
   - *Note*: If no key is specified, the app gracefully falls back to local NLU heuristic parsing.

3. **Build & Install**:
   - Open the project in Android Studio.
   - Sync Gradle project dependencies.
   - Run on an Android Device or Emulator (Android 8.0 / API 26+).

---

## 🔒 Security & Privacy

- **Zero Hardcoded Credentials**: Sensitive keystores and API tokens are excluded via `.gitignore`.
- **Biometric Guard**: Optional fingerprint / face verification step before authorizing high-value transfers.
- **Anomalous Payment Guardian**: Real-time AI risk evaluation flags suspicious accounts before fund deduction.

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
