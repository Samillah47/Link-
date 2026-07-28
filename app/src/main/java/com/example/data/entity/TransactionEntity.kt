package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val recipient: String,
    val amount: Double,
    val currency: String = "RWF",
    val category: String, // Transfer, Groceries, Rent, Bills, Cross-Border, Aura Sync
    val sourceAccount: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "COMPLETED", // COMPLETED, FLAGGED, PENDING
    val purpose: String = "",
    val languageUsed: String = "English",
    val riskLevel: String = "LOW",
    val riskReason: String = "",
    val type: String = "SEND" // "SEND" or "RECEIVE"
)
