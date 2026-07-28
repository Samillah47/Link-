package com.example.ai

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun parseFinancialIntent(
        userInput: String,
        availableAccountsSummary: String,
        recentTransactionHistorySummary: String
    ): PaymentIntentResult = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String ?: ""
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Fallback mock engine if API key not injected
            return@withContext fallbackIntentParser(userInput)
        }

        val prompt = """
You are the AI engine for LINK, an African financial operating system.
User Input: "$userInput"

Available Financial Accounts:
$availableAccountsSummary

Recent Transaction Context:
$recentTransactionHistorySummary

Instructions:
1. Detect input language (Kinyarwanda, Kiswahili, French, English).
2. Determine financial intent:
   - "SEND_MONEY": Direct transfer to person/contact
   - "SPLIT_BILL": Splitting dinner or expense among friends
   - "PAY_BILL": Electricity, water, internet bill
   - "CROSS_BORDER": Remittance to China, Kenya, DRC, Uganda, UK, etc.
   - "QUERY_MEMORY": Question about past transactions, spending habits, landlord payments
3. Extract amount, recipient, purpose, and preferred_language.
4. Perform AI Financial Guardian Risk Assessment:
   - If amount > 100,000 RWF or recipient looks unfamiliar/unverified, set risk_level to "MEDIUM" or "HIGH" and provide a clear warning in risk_reason. Otherwise "LOW".
5. Perform AI Smart Routing:
   - Recommend the best account source ("eCash", "MTN MoMo", "Bank of Kigali") based on balance, lowest fee, and speed. State reason.
6. If CROSS_BORDER: calculate estimated foreign currency (e.g. CNY, USD, KES) and exchange rate info.
7. If QUERY_MEMORY: compose a concise, helpful answer summarizing past spending.

Return ONLY a valid JSON object matching this structure (no markdown formatting, no code blocks):
{
  "intent": "SEND_MONEY",
  "recipient": "Mama Alice",
  "amount": 10000.0,
  "currency": "RWF",
  "purpose": "Groceries",
  "preferred_language": "Kinyarwanda",
  "risk_level": "LOW",
  "risk_reason": "Normal low-risk payment to saved contact",
  "recommended_source": "eCash",
  "routing_reason": "Lowest transaction fee and instant zero-latency processing",
  "target_currency": "CNY",
  "estimated_foreign_amount": 0.0,
  "exchange_rate_text": "",
  "memory_answer": ""
}
""".trimIndent()

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        val jsonPayload = JSONObject().apply {
            put("contents", org.json.JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            })
        }

        val requestBody = jsonPayload.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                Log.e("GeminiService", "Error ${response.code}: $responseBody")
                return@withContext fallbackIntentParser(userInput)
            }

            val jsonResponse = JSONObject(responseBody)
            val text = jsonResponse.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")

            parseJsonToIntentResult(text, userInput)
        } catch (e: Exception) {
            Log.e("GeminiService", "Exception calling Gemini", e)
            fallbackIntentParser(userInput)
        }
    }

    private fun parseJsonToIntentResult(rawText: String, userInput: String): PaymentIntentResult {
        return try {
            val cleanJson = rawText.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val json = JSONObject(cleanJson)
            PaymentIntentResult(
                intent = json.optString("intent", "SEND_MONEY"),
                recipient = json.optString("recipient", "Contact"),
                amount = json.optDouble("amount", 0.0),
                currency = json.optString("currency", "RWF"),
                purpose = json.optString("purpose", "Payment"),
                preferredLanguage = json.optString("preferred_language", "English"),
                riskLevel = json.optString("risk_level", "LOW"),
                riskReason = json.optString("risk_reason", "Verified transaction pattern"),
                recommendedSource = json.optString("recommended_source", "eCash"),
                routingReason = json.optString("routing_reason", "Lowest transaction cost"),
                targetCurrency = json.optString("target_currency", null),
                estimatedForeignAmount = if (json.has("estimated_foreign_amount")) json.optDouble("estimated_foreign_amount") else null,
                exchangeRateText = json.optString("exchange_rate_text", null),
                memoryAnswer = json.optString("memory_answer", null)
            )
        } catch (e: Exception) {
            fallbackIntentParser(userInput)
        }
    }

    private fun fallbackIntentParser(input: String): PaymentIntentResult {
        val lower = input.lowercase()
        return when {
            lower.contains("ohereza") || lower.contains("mama") -> {
                PaymentIntentResult(
                    intent = "SEND_MONEY",
                    recipient = "Mama Alice",
                    amount = extractAmount(input, 10000.0),
                    currency = "RWF",
                    purpose = "Groceries (Ibihahwa)",
                    preferredLanguage = "Kinyarwanda",
                    riskLevel = "LOW",
                    riskReason = "Verified primary contact in Rwanda",
                    recommendedSource = "eCash",
                    routingReason = "eCash zero transfer fee & zero latency"
                )
            }
            lower.contains("china") || lower.contains("yen") || lower.contains("cny") -> {
                val amt = extractAmount(input, 100000.0)
                PaymentIntentResult(
                    intent = "CROSS_BORDER",
                    recipient = "Friend in China (Li Wei)",
                    amount = amt,
                    currency = "RWF",
                    purpose = "Cross-Border Remittance",
                    preferredLanguage = "English",
                    riskLevel = "MEDIUM",
                    riskReason = "International cross-border FX routing required",
                    recommendedSource = "Bank of Kigali",
                    routingReason = "Bank of Kigali direct SWIFT / UnionPay bridge has best FX margin",
                    targetCurrency = "CNY",
                    estimatedForeignAmount = (amt / 182.4 * 100).toInt() / 100.0,
                    exchangeRateText = "1 CNY = 182.40 RWF (Est. Fee: 1,200 RWF)"
                )
            }
            lower.contains("dîner") || lower.contains("diner") || lower.contains("split") || lower.contains("jean") -> {
                PaymentIntentResult(
                    intent = "SPLIT_BILL",
                    recipient = "Jean & Friends (4 People)",
                    amount = extractAmount(input, 15000.0),
                    currency = "RWF",
                    purpose = "Dinner Split",
                    preferredLanguage = if (lower.contains("francs")) "French" else "English",
                    riskLevel = "LOW",
                    riskReason = "Standard small bill split",
                    recommendedSource = "eCash",
                    routingReason = "eCash instant split distribution"
                )
            }
            lower.contains("landlord") || lower.contains("food") || lower.contains("spent") || lower.contains("history") || lower.contains("when") -> {
                PaymentIntentResult(
                    intent = "QUERY_MEMORY",
                    recipient = "",
                    amount = 0.0,
                    currency = "RWF",
                    purpose = "Financial Query",
                    preferredLanguage = "English",
                    riskLevel = "LOW",
                    riskReason = "Query only - no money movement",
                    recommendedSource = "eCash",
                    routingReason = "N/A",
                    memoryAnswer = "You paid 150,000 RWF for Rent to Landlord (Mr. Kamali) on July 5, 2026 via Bank of Kigali. Your total food expenses this month amount to 42,500 RWF across 6 eCash/MoMo transactions."
                )
            }
            lower.contains("500000") || lower.contains("500,000") || lower.contains("large") -> {
                PaymentIntentResult(
                    intent = "SEND_MONEY",
                    recipient = "Unknown Recipient (Merchant #98221)",
                    amount = 500000.0,
                    currency = "RWF",
                    purpose = "Unverified High Value Transfer",
                    preferredLanguage = "English",
                    riskLevel = "HIGH",
                    riskReason = "Your usual payments are below 20,000 RWF. This transaction is 500,000 RWF to a new recipient. Please verify before continuing.",
                    recommendedSource = "Bank of Kigali",
                    routingReason = "Bank of Kigali high-security escrow route"
                )
            }
            else -> {
                PaymentIntentResult(
                    intent = "SEND_MONEY",
                    recipient = "John Nshuti",
                    amount = extractAmount(input, 15000.0),
                    currency = "RWF",
                    purpose = "Direct Payment",
                    preferredLanguage = "English",
                    riskLevel = "LOW",
                    riskReason = "Normal peer-to-peer transaction",
                    recommendedSource = "eCash",
                    routingReason = "eCash lowest transaction fee"
                )
            }
        }
    }

    private fun extractAmount(input: String, defaultVal: Double): Double {
        val regex = Regex("(\\d+([.,]\\d+)?)")
        val match = regex.find(input)
        return match?.value?.replace(",", "")?.toDoubleOrNull() ?: defaultVal
    }
}
