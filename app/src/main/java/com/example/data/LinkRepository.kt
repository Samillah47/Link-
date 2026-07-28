package com.example.data

import com.example.data.dao.FinancialDao
import com.example.data.entity.FinancialAccountEntity
import com.example.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class LinkRepository(private val dao: FinancialDao) {

    val accounts: Flow<List<FinancialAccountEntity>> = dao.getAllAccounts()
    val transactions: Flow<List<TransactionEntity>> = dao.getAllTransactions()

    suspend fun initializeSeedDataIfNeeded() {
        val defaultAccounts = listOf(
            FinancialAccountEntity(
                id = "momo",
                name = "MTN Mobile Money",
                accountType = "MOBILE_MONEY",
                accountNumber = "+250 788 123 456",
                balance = 45000.0,
                currency = "RWF",
                colorHex = "#FFCC00",
                feePercentage = 0.5,
                speedMinutes = 1
            ),
            FinancialAccountEntity(
                id = "ecash",
                name = "eCash RWF Protocol",
                accountType = "ECASH",
                accountNumber = "EC-9981-2026",
                balance = 15000.0,
                currency = "RWF",
                colorHex = "#00E676",
                feePercentage = 0.0,
                speedMinutes = 0
            ),
            FinancialAccountEntity(
                id = "bank_bk",
                name = "Bank of Kigali",
                accountType = "BANK",
                accountNumber = "00012-3456789-91",
                balance = 250000.0,
                currency = "RWF",
                colorHex = "#0288D1",
                feePercentage = 0.2,
                speedMinutes = 2
            ),
            FinancialAccountEntity(
                id = "airtel",
                name = "Airtel Money",
                accountType = "MOBILE_MONEY",
                accountNumber = "+250 733 987 654",
                balance = 28000.0,
                currency = "RWF",
                colorHex = "#E53935",
                feePercentage = 0.4,
                speedMinutes = 1
            ),
            FinancialAccountEntity(
                id = "equity",
                name = "Equity Bank Rwanda",
                accountType = "BANK",
                accountNumber = "40012-987110-02",
                balance = 180000.0,
                currency = "RWF",
                colorHex = "#8D6E63",
                feePercentage = 0.15,
                speedMinutes = 2
            )
        )

        dao.insertAccounts(defaultAccounts)

        val seedTransactions = listOf(
            TransactionEntity(
                id = "TXN-2026-8801",
                recipient = "Landlord (Mr. Kamali)",
                amount = 150000.0,
                currency = "RWF",
                category = "Rent",
                sourceAccount = "Bank of Kigali",
                timestamp = System.currentTimeMillis() - (84600000L * 22),
                status = "COMPLETED",
                purpose = "July House Rent",
                languageUsed = "English",
                riskLevel = "LOW",
                riskReason = "Verified monthly recurring lease recipient"
            ),
            TransactionEntity(
                id = "TXN-2026-8802",
                recipient = "SawaCitadel Supermarket",
                amount = 18500.0,
                currency = "RWF",
                category = "Groceries",
                sourceAccount = "eCash RWF Protocol",
                timestamp = System.currentTimeMillis() - (84600000L * 3),
                status = "COMPLETED",
                purpose = "Weekly Groceries",
                languageUsed = "French",
                riskLevel = "LOW",
                riskReason = "Merchant QR code verified at POS"
            ),
            TransactionEntity(
                id = "TXN-2026-8803",
                recipient = "Mama Alice",
                amount = 10000.0,
                currency = "RWF",
                category = "Transfer",
                sourceAccount = "MTN Mobile Money",
                timestamp = System.currentTimeMillis() - (84600000L * 1),
                status = "COMPLETED",
                purpose = "Groceries (Ibihahwa)",
                languageUsed = "Kinyarwanda",
                riskLevel = "LOW",
                riskReason = "Frequent trusted contact"
            ),
            TransactionEntity(
                id = "TXN-2026-8804",
                recipient = "Samillah (Nairobi Trade)",
                amount = 45000.0,
                currency = "RWF",
                category = "Cross-Border",
                sourceAccount = "MTN Mobile Money",
                timestamp = System.currentTimeMillis() - (84600000L * 5),
                status = "COMPLETED",
                purpose = "Artisanal Crafts Purchase (KES FX)",
                languageUsed = "Kiswahili",
                riskLevel = "LOW",
                riskReason = "Automated FX conversion & instant settlement"
            ),
            TransactionEntity(
                id = "TXN-2026-8805",
                recipient = "EUCL Rwanda Power",
                amount = 12000.0,
                currency = "RWF",
                category = "Utilities",
                sourceAccount = "eCash RWF Protocol",
                timestamp = System.currentTimeMillis() - (84600000L * 12),
                status = "COMPLETED",
                purpose = "Prepaid Electricity Token",
                languageUsed = "Kinyarwanda",
                riskLevel = "LOW",
                riskReason = "Direct utility provider gateway",
                type = "SEND"
            ),
            TransactionEntity(
                id = "TXN-2026-8806",
                recipient = "Jean Luc (Kigali Tech)",
                amount = 65000.0,
                currency = "RWF",
                category = "Incoming Transfer",
                sourceAccount = "MTN Mobile Money",
                timestamp = System.currentTimeMillis() - (84600000L * 2),
                status = "COMPLETED",
                purpose = "Consulting Fee Deposit",
                languageUsed = "Kinyarwanda",
                riskLevel = "LOW",
                riskReason = "Verified contact payment",
                type = "RECEIVE"
            ),
            TransactionEntity(
                id = "TXN-2026-8807",
                recipient = "Bank of Kigali Payroll",
                amount = 250000.0,
                currency = "RWF",
                category = "Salary",
                sourceAccount = "Bank of Kigali",
                timestamp = System.currentTimeMillis() - (84600000L * 15),
                status = "COMPLETED",
                purpose = "July Net Salary Settlement",
                languageUsed = "English",
                riskLevel = "LOW",
                riskReason = "Employer verified deposit",
                type = "RECEIVE"
            )
        )

        for (tx in seedTransactions) {
            dao.insertTransaction(tx)
        }
    }

    suspend fun executePayment(
        recipient: String,
        amount: Double,
        sourceAccountId: String,
        category: String,
        purpose: String,
        languageUsed: String,
        riskLevel: String,
        riskReason: String
    ): Boolean {
        val account = dao.getAccountById(sourceAccountId) ?: return false
        val newBalance = (account.balance - amount).coerceAtLeast(0.0)

        // Update account balance
        dao.updateAccount(account.copy(balance = newBalance))

        // Save transaction record
        val newTx = TransactionEntity(
            id = "TXN-2026-" + (1000..9999).random(),
            recipient = recipient,
            amount = amount,
            currency = "RWF",
            category = category,
            sourceAccount = account.name,
            timestamp = System.currentTimeMillis(),
            status = "COMPLETED",
            purpose = purpose,
            languageUsed = languageUsed,
            riskLevel = riskLevel,
            riskReason = riskReason,
            type = "SEND"
        )
        dao.insertTransaction(newTx)
        return true
    }

    suspend fun receiveMoney(
        senderName: String,
        amount: Double,
        targetAccountId: String = "momo",
        purpose: String = "Incoming Transfer",
        languageUsed: String = "Kinyarwanda"
    ): Boolean {
        val account = dao.getAccountById(targetAccountId) ?: return false
        val newBalance = account.balance + amount

        dao.updateAccount(account.copy(balance = newBalance))

        val newTx = TransactionEntity(
            id = "TXN-2026-" + (1000..9999).random(),
            recipient = senderName,
            amount = amount,
            currency = "RWF",
            category = "Received Funds",
            sourceAccount = account.name,
            timestamp = System.currentTimeMillis(),
            status = "COMPLETED",
            purpose = purpose,
            languageUsed = languageUsed,
            riskLevel = "LOW",
            riskReason = "Direct wallet deposit received",
            type = "RECEIVE"
        )
        dao.insertTransaction(newTx)
        return true
    }

    suspend fun resetDemoState() {
        dao.deleteAllTransactions()
        dao.deleteAllAccounts()
        initializeSeedDataIfNeeded()
    }
}
