package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dispute_tickets")
data class DisputeTicketEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ticketNumber: String,
    val transactionId: String,
    val category: String, // RECHARGE_FAILED, WRONG_BILL, DELAYED_BALANCE, OTHER
    val description: String,
    val status: String, // OPEN, IN_PROGRESS, RESOLVED
    val createdTimestamp: Long = System.currentTimeMillis(),
    val resolvedTimestamp: Long? = null,
    val adminResponse: String? = null
)
