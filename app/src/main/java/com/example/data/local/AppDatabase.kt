package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE transactionId = :txId LIMIT 1")
    suspend fun getTransactionById(txId: String): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("UPDATE transactions SET status = :newStatus WHERE transactionId = :txId")
    suspend fun updateStatus(txId: String, newStatus: String)
}

@Dao
interface DisputeTicketDao {
    @Query("SELECT * FROM dispute_tickets ORDER BY createdTimestamp DESC")
    fun getAllTickets(): Flow<List<DisputeTicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: DisputeTicketEntity)

    @Query("UPDATE dispute_tickets SET status = :status, adminResponse = :response, resolvedTimestamp = :resolvedTime WHERE ticketNumber = :ticketNum")
    suspend fun updateTicketStatus(ticketNum: String, status: String, response: String?, resolvedTime: Long?)
}

@Dao
interface SupportMessageDao {
    @Query("SELECT * FROM support_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<SupportMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(msg: SupportMessageEntity)
}

@Database(
    entities = [TransactionEntity::class, DisputeTicketEntity::class, SupportMessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun disputeTicketDao(): DisputeTicketDao
    abstract fun supportMessageDao(): SupportMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "payrecharge_database"
                )
                .fallbackToDestructiveMigration(true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
