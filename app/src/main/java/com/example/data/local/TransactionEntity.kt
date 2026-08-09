package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transactionId: String,
    val type: String, // RECHARGE, BILL_PAYMENT, COMMISSION_TRANSFER, CASH_IN
    val title: String,
    val operatorOrBiller: String,
    val recipientOrAccount: String,
    val amount: Double,
    val commissionEarned: Double,
    val status: String, // SUCCESS, PENDING, FAILED, DISPUTED
    val timestamp: Long = System.currentTimeMillis(),
    val encryptedChecksum: String,
    val note: String = ""
)
