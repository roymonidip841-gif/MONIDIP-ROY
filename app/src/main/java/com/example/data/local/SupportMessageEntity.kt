package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "support_messages")
data class SupportMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // USER, BOT, AGENT
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val ticketId: String? = null
)
