package com.example.ai

data class PaymentIntentResult(
    val intent: String = "SEND_MONEY", // SEND_MONEY, SPLIT_BILL, PAY_BILL, CROSS_BORDER, QUERY_MEMORY, UNKNOWN
    val recipient: String = "",
    val amount: Double = 0.0,
    val currency: String = "RWF",
    val purpose: String = "General Transfer",
    val preferredLanguage: String = "English",
    val riskLevel: String = "LOW", // LOW, MEDIUM, HIGH
    val riskReason: String = "Normal transaction pattern",
    val recommendedSource: String = "eCash",
    val routingReason: String = "Lowest fee and zero instant latency",
    val targetCurrency: String? = null,
    val estimatedForeignAmount: Double? = null,
    val exchangeRateText: String? = null,
    val memoryAnswer: String? = null
)
