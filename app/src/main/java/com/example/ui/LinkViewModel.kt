package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiService
import com.example.ai.PaymentIntentResult
import com.example.data.AppDatabase
import com.example.data.LinkRepository
import com.example.data.entity.FinancialAccountEntity
import com.example.data.entity.TransactionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.example.util.AppLanguage
import java.util.UUID

enum class AuraSyncStep {
    IDLE, SCANNING, DEVICE_FOUND, SECURE_HANDSHAKE, CONFIRM_PAYMENT, SUCCESS
}

data class LinkNotification(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: String = "INFO" // "RECEIVE", "SEND", "SECURITY", "INFO"
)

class LinkViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LinkRepository
    private val geminiService = GeminiService()

    val accounts: StateFlow<List<FinancialAccountEntity>>
    val transactions: StateFlow<List<TransactionEntity>>
    val totalBalance: StateFlow<Double>

    private val _appLanguage = MutableStateFlow(AppLanguage.EN)
    val appLanguage = _appLanguage.asStateFlow()

    private val _notifications = MutableStateFlow<List<LinkNotification>>(
        listOf(
            LinkNotification(
                title = "🎉 Money Received!",
                message = "You received 65,000 RWF from Jean Luc (Kigali Tech) via MTN Mobile Money.",
                type = "RECEIVE",
                timestamp = System.currentTimeMillis() - 7200000L
            ),
            LinkNotification(
                title = "🛡️ AI Guardian Shield Active",
                message = "256-bit ECDH Proximity Encryption verified for Kigali RW-01 node.",
                type = "SECURITY",
                timestamp = System.currentTimeMillis() - 86400000L
            )
        )
    )
    val notifications = _notifications.asStateFlow()

    fun setLanguage(language: AppLanguage) {
        _appLanguage.value = language
    }

    fun pushNotification(title: String, message: String, type: String = "INFO") {
        val newNotif = LinkNotification(title = title, message = message, type = type)
        _notifications.value = listOf(newNotif) + _notifications.value
    }

    fun markNotificationsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    fun simulateReceiveMoney(senderName: String = "Jean Luc", amount: Double = 25000.0) {
        viewModelScope.launch {
            val success = repository.receiveMoney(
                senderName = senderName,
                amount = amount,
                targetAccountId = "momo",
                purpose = "Incoming Quick Transfer",
                languageUsed = _appLanguage.value.displayName
            )
            if (success) {
                pushNotification(
                    title = "🎉 Received Money!",
                    message = "You received ${amount.toInt()} RWF from $senderName into your MTN Mobile Money account.",
                    type = "RECEIVE"
                )
                _paymentSuccessMessage.value = "Received $amount RWF from $senderName!"
            }
        }
    }

    private val _naturalLanguageInput = MutableStateFlow("")
    val naturalLanguageInput = _naturalLanguageInput.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing = _isAnalyzing.asStateFlow()

    private val _parsedResult = MutableStateFlow<PaymentIntentResult?>(null)
    val parsedResult = _parsedResult.asStateFlow()

    private val _selectedAccountForPayment = MutableStateFlow<String>("ecash")
    val selectedAccountForPayment = _selectedAccountForPayment.asStateFlow()

    private val _auraStep = MutableStateFlow(AuraSyncStep.IDLE)
    val auraStep = _auraStep.asStateFlow()

    private val _auraTargetDevice = MutableStateFlow("Keza's Pixel 9 Pro")
    val auraTargetDevice = _auraTargetDevice.asStateFlow()

    private val _auraAmount = MutableStateFlow(12000.0)
    val auraAmount = _auraAmount.asStateFlow()

    private val _showPaymentConfirmation = MutableStateFlow(false)
    val showPaymentConfirmation = _showPaymentConfirmation.asStateFlow()

    private val _paymentSuccessMessage = MutableStateFlow<String?>(null)
    val paymentSuccessMessage = _paymentSuccessMessage.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = LinkRepository(db.financialDao())

        viewModelScope.launch {
            repository.initializeSeedDataIfNeeded()
        }

        accounts = repository.accounts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        transactions = repository.transactions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        totalBalance = accounts.combine(accounts) { list, _ ->
            list.sumOf { it.balance }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 310000.0
        )
    }

    fun updateInput(text: String) {
        _naturalLanguageInput.value = text
    }

    fun processNaturalLanguageInput(overridePrompt: String? = null) {
        val prompt = overridePrompt ?: _naturalLanguageInput.value
        if (prompt.isBlank()) return

        viewModelScope.launch {
            _isAnalyzing.value = true
            _paymentSuccessMessage.value = null

            val accountsSummary = accounts.value.joinToString("\n") {
                "- ${it.name} (${it.accountType}): Balance = ${it.balance} RWF, Fee = ${it.feePercentage}%"
            }

            val historySummary = transactions.value.take(5).joinToString("\n") {
                "- ${it.recipient}: ${it.amount} RWF for ${it.purpose} via ${it.sourceAccount}"
            }

            val result = geminiService.parseFinancialIntent(
                userInput = prompt,
                availableAccountsSummary = accountsSummary,
                recentTransactionHistorySummary = historySummary
            )

            _parsedResult.value = result
            _isAnalyzing.value = false

            if (result.intent != "QUERY_MEMORY") {
                _showPaymentConfirmation.value = true
            }
        }
    }

    fun confirmAndExecutePayment() {
        val result = _parsedResult.value ?: return
        viewModelScope.launch {
            val accountId = when {
                result.recommendedSource.lowercase().contains("momo") || result.recommendedSource.lowercase().contains("mobile") -> "momo"
                result.recommendedSource.lowercase().contains("bank") || result.recommendedSource.lowercase().contains("kigali") -> "bank_bk"
                else -> "ecash"
            }

            val success = repository.executePayment(
                recipient = result.recipient.ifBlank { "Contact" },
                amount = if (result.amount > 0) result.amount else 5000.0,
                sourceAccountId = accountId,
                category = if (result.intent == "CROSS_BORDER") "Cross-Border" else "AI Intent Pay",
                purpose = result.purpose,
                languageUsed = result.preferredLanguage,
                riskLevel = result.riskLevel,
                riskReason = result.riskReason
            )

            if (success) {
                _showPaymentConfirmation.value = false
                val msg = "Successfully paid ${result.amount} RWF to ${result.recipient} using ${result.recommendedSource}!"
                _paymentSuccessMessage.value = msg
                pushNotification(
                    title = "💸 Payment Sent",
                    message = msg,
                    type = "SEND"
                )
                _naturalLanguageInput.value = ""
            }
        }
    }

    fun dismissConfirmation() {
        _showPaymentConfirmation.value = false
    }

    fun dismissSuccessMessage() {
        _paymentSuccessMessage.value = null
    }

    private val _auraMode = MutableStateFlow("SEND") // "SEND" or "RECEIVE"
    val auraMode = _auraMode.asStateFlow()

    private val _auraRecipientInput = MutableStateFlow("@alice")
    val auraRecipientInput = _auraRecipientInput.asStateFlow()

    fun setAuraMode(mode: String) {
        _auraMode.value = mode
        _auraStep.value = AuraSyncStep.IDLE
    }

    fun setAuraRecipient(recipient: String) {
        _auraRecipientInput.value = recipient
    }

    // Aura Sync Proximity Actions
    fun startAuraSyncScan() {
        viewModelScope.launch {
            _auraStep.value = AuraSyncStep.SCANNING
            kotlinx.coroutines.delay(1600) // Simulate encrypted BLE/UWB proximity handshake scan
            _auraStep.value = AuraSyncStep.DEVICE_FOUND
        }
    }

    fun connectAuraDevice() {
        viewModelScope.launch {
            _auraStep.value = AuraSyncStep.SECURE_HANDSHAKE
            kotlinx.coroutines.delay(1200)
            _auraStep.value = AuraSyncStep.CONFIRM_PAYMENT
        }
    }

    fun authorizeAuraPayment() {
        val target = if (_auraRecipientInput.value.isNotBlank()) _auraRecipientInput.value else _auraTargetDevice.value
        val sendAmount = _auraAmount.value

        viewModelScope.launch {
            if (_auraMode.value == "SEND") {
                val success = repository.executePayment(
                    recipient = target,
                    amount = sendAmount,
                    sourceAccountId = "ecash",
                    category = "Aura Sync Proximity",
                    purpose = "Encrypted Proximity Payment",
                    languageUsed = _appLanguage.value.displayName,
                    riskLevel = "LOW",
                    riskReason = "Dual-authenticated encrypted ephemeral proximity session"
                )

                if (success) {
                    _auraStep.value = AuraSyncStep.SUCCESS
                    pushNotification(
                        title = "📶 Sent via Aura Sync",
                        message = "You paid ${sendAmount.toInt()} RWF to $target via encrypted eCash Proximity link.",
                        type = "SEND"
                    )
                }
            } else {
                // RECEIVE MODE
                val success = repository.receiveMoney(
                    senderName = target,
                    amount = sendAmount,
                    targetAccountId = "ecash",
                    purpose = "Incoming Aura Proximity Sync",
                    languageUsed = _appLanguage.value.displayName
                )

                if (success) {
                    _auraStep.value = AuraSyncStep.SUCCESS
                    pushNotification(
                        title = "🎉 Received via Aura Sync!",
                        message = "You received ${sendAmount.toInt()} RWF from $target via encrypted eCash Proximity beacon.",
                        type = "RECEIVE"
                    )
                }
            }
        }
    }

    fun resetAuraSync() {
        _auraStep.value = AuraSyncStep.IDLE
    }

    fun updateAuraAmount(amount: Double) {
        _auraAmount.value = amount
    }

    fun resetDemoData() {
        viewModelScope.launch {
            repository.resetDemoState()
            _paymentSuccessMessage.value = "Demo balances & transactions restored to default!"
        }
    }
}
