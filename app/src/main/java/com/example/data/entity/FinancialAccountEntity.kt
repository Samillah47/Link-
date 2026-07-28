package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "financial_accounts")
data class FinancialAccountEntity(
    @PrimaryKey val id: String,
    val name: String,
    val accountType: String, // MOBILE_MONEY, ECASH, BANK
    val accountNumber: String,
    val balance: Double,
    val currency: String = "RWF",
    val colorHex: String,
    val feePercentage: Double,
    val speedMinutes: Int = 1
)
