package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.DisputeTicketEntity
import com.example.data.local.SupportMessageEntity
import com.example.data.local.TransactionEntity
import com.example.model.LanguageCode
import com.example.ui.security.SecurityManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random

data class UserState(
    val userName: String = "মনোদীপ রায় (Monidip Roy)",
    val userPhone: String = "9876543210",
    val userTierBn: String = "প্রো রিসেলার ওয়ালেট (3.5% কমিশন)",
    val userTierEn: String = "Pro Reseller Account (3.5% Commission)",
    val mainBalance: Double = 2450.00,
    val commissionBalance: Double = 185.50,
    val language: LanguageCode = LanguageCode.BANGLA,
    val isDarkTheme: Boolean = false,
    val isBiometricEnabled: Boolean = true,
    val is2faEnabled: Boolean = true,
    val storedPinHash: String = SecurityManager.hashPin("1234"),
    val isWelcomeDismissed: Boolean = false
)

class PayRepository(private val context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    val allTransactions: Flow<List<TransactionEntity>> = db.transactionDao().getAllTransactions()
    val allTickets: Flow<List<DisputeTicketEntity>> = db.disputeTicketDao().getAllTickets()
    val allMessages: Flow<List<SupportMessageEntity>> = db.supportMessageDao().getAllMessages()

    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    init {
        scope.launch {
            seedSampleDataIfEmpty()
        }
    }

    private suspend fun seedSampleDataIfEmpty() {
        val currentTx = allTransactions.first()
        if (currentTx.isEmpty()) {
            val now = System.currentTimeMillis()
            val sampleTxs = listOf(
                TransactionEntity(
                    transactionId = "TXN-8829104",
                    type = "RECHARGE",
                    title = "জিও মোবাইল রিচার্জ (9876543210)",
                    operatorOrBiller = "Reliance Jio",
                    recipientOrAccount = "9876543210",
                    amount = 299.0,
                    commissionEarned = 9.56,
                    status = "SUCCESS",
                    timestamp = now - 3600000,
                    encryptedChecksum = SecurityManager.generateEncryptedChecksum("9876543210:299")
                ),
                TransactionEntity(
                    transactionId = "TXN-8829103",
                    type = "BILL_PAYMENT",
                    title = "ডাব্লিউবিএসইডিসিএল বিদ্যুৎ বিল পেমেন্ট",
                    operatorOrBiller = "WBSEDCL",
                    recipientOrAccount = "Consumer #102938475",
                    amount = 1480.0,
                    commissionEarned = 0.0,
                    status = "SUCCESS",
                    timestamp = now - 86400000,
                    encryptedChecksum = SecurityManager.generateEncryptedChecksum("WBSEDCL:1480")
                ),
                TransactionEntity(
                    transactionId = "TXN-8829102",
                    type = "RECHARGE",
                    title = "এয়ারটেল ক্যাশব্যাক রিচার্জ (9812345678)",
                    operatorOrBiller = "Airtel India",
                    recipientOrAccount = "9812345678",
                    amount = 479.0,
                    commissionEarned = 14.37,
                    status = "SUCCESS",
                    timestamp = now - 172800000,
                    encryptedChecksum = SecurityManager.generateEncryptedChecksum("9812345678:479")
                )
            )
            sampleTxs.forEach { db.transactionDao().insertTransaction(it) }
        }

        val currentTickets = allTickets.first()
        if (currentTickets.isEmpty()) {
            val ticket = DisputeTicketEntity(
                ticketNumber = "TKT-9901",
                transactionId = "TXN-8829103",
                category = "BILL_PAYMENT_INQUIRY",
                description = "বিল পেমেন্টের আপডেট ওয়ালেটে সঠিক দেখাচ্ছে কি না নিশ্চিত করুন।",
                status = "RESOLVED",
                createdTimestamp = System.currentTimeMillis() - 86400000,
                resolvedTimestamp = System.currentTimeMillis() - 43200000,
                adminResponse = "আপনার পেমেন্ট বিবিবিপিএস (BBPS) বিদ্যুৎ সার্ভারে সফলভাবে জমা হয়েছে। রসিদ সংগ্রহ করতে পারেন।"
            )
            db.disputeTicketDao().insertTicket(ticket)
        }

        val currentMsgs = allMessages.first()
        if (currentMsgs.isEmpty()) {
            db.supportMessageDao().insertMessage(
                SupportMessageEntity(
                    sender = "BOT",
                    text = "স্বাগতম! পে-রিচার্জ ইন্ডিয়া হেল্প চ্যাটে আপনাকে সাহায্য করতে পেরে আমরা আনন্দিত। মোবাইল রিচার্জ বা বিদ্যুৎ বিল পেমেন্ট সম্পর্কিত যেকোনো প্রয়োজনে লিখুন।",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun setLanguage(languageCode: LanguageCode) {
        _userState.value = _userState.value.copy(language = languageCode)
    }

    fun setDarkTheme(isDark: Boolean) {
        _userState.value = _userState.value.copy(isDarkTheme = isDark)
    }

    fun toggleBiometric(enabled: Boolean) {
        _userState.value = _userState.value.copy(isBiometricEnabled = enabled)
    }

    fun updatePin(newPin: String) {
        _userState.value = _userState.value.copy(storedPinHash = SecurityManager.hashPin(newPin))
    }

    fun dismissWelcome() {
        _userState.value = _userState.value.copy(isWelcomeDismissed = true)
    }

    fun loginWithPhone(phone: String, name: String? = null) {
        val cleanPhone = phone.trim().replace("+91", "").replace(" ", "").replace("-", "")
        val finalPhone = if (cleanPhone.length >= 10) cleanPhone else "9876543210"
        val finalName = if (!name.isNullOrBlank()) name else if (_userState.value.userPhone == finalPhone) _userState.value.userName else "ইউজার ($finalPhone)"
        _userState.value = _userState.value.copy(
            userPhone = finalPhone,
            userName = finalName,
            isWelcomeDismissed = true
        )
    }

    fun logout() {
        _userState.value = _userState.value.copy(isWelcomeDismissed = false)
    }

    suspend fun executeRecharge(
        operatorName: String,
        mobileNumber: String,
        amount: Double,
        commissionPercent: Double,
        offerTitle: String? = null
    ): Boolean {
        if (_userState.value.mainBalance < amount) return false

        val commissionAmount = amount * (commissionPercent / 100.0)
        val newMain = _userState.value.mainBalance - amount
        val newCommission = _userState.value.commissionBalance + commissionAmount

        _userState.value = _userState.value.copy(
            mainBalance = newMain,
            commissionBalance = newCommission
        )

        val txId = "TXN-" + Random.nextInt(1000000, 9999999)
        val title = if (offerTitle != null) "$operatorName - $offerTitle ($mobileNumber)"
                    else "$operatorName রিচার্জ ($mobileNumber)"

        val tx = TransactionEntity(
            transactionId = txId,
            type = "RECHARGE",
            title = title,
            operatorOrBiller = operatorName,
            recipientOrAccount = mobileNumber,
            amount = amount,
            commissionEarned = commissionAmount,
            status = "SUCCESS",
            timestamp = System.currentTimeMillis(),
            encryptedChecksum = SecurityManager.generateEncryptedChecksum("$mobileNumber:$amount")
        )

        db.transactionDao().insertTransaction(tx)
        return true
    }

    suspend fun executeBillPayment(
        billerName: String,
        accountNo: String,
        amount: Double
    ): Boolean {
        if (_userState.value.mainBalance < amount) return false

        val newMain = _userState.value.mainBalance - amount
        _userState.value = _userState.value.copy(mainBalance = newMain)

        val txId = "TXN-" + Random.nextInt(1000000, 9999999)
        val tx = TransactionEntity(
            transactionId = txId,
            type = "BILL_PAYMENT",
            title = "$billerName পেমেন্ট",
            operatorOrBiller = billerName,
            recipientOrAccount = accountNo,
            amount = amount,
            commissionEarned = 0.0,
            status = "SUCCESS",
            timestamp = System.currentTimeMillis(),
            encryptedChecksum = SecurityManager.generateEncryptedChecksum("$billerName:$accountNo:$amount")
        )

        db.transactionDao().insertTransaction(tx)
        return true
    }

    suspend fun transferCommissionToMain(): Boolean {
        val comm = _userState.value.commissionBalance
        if (comm <= 0.0) return false

        val newMain = _userState.value.mainBalance + comm
        _userState.value = _userState.value.copy(
            mainBalance = newMain,
            commissionBalance = 0.0
        )

        val txId = "TXN-" + Random.nextInt(1000000, 9999999)
        val tx = TransactionEntity(
            transactionId = txId,
            type = "COMMISSION_TRANSFER",
            title = "কমিশন ওয়ালেট থেকে স্থানান্তর",
            operatorOrBiller = "Commission Wallet",
            recipientOrAccount = _userState.value.userPhone,
            amount = comm,
            commissionEarned = 0.0,
            status = "SUCCESS",
            timestamp = System.currentTimeMillis(),
            encryptedChecksum = SecurityManager.generateEncryptedChecksum("COMMISSION_TRANSFER:$comm")
        )

        db.transactionDao().insertTransaction(tx)
        return true
    }

    suspend fun createDisputeTicket(
        transactionId: String,
        category: String,
        description: String
    ): String {
        val ticketNum = "TKT-" + Random.nextInt(1000, 9999)
        val ticket = DisputeTicketEntity(
            ticketNumber = ticketNum,
            transactionId = transactionId,
            category = category,
            description = description,
            status = "OPEN",
            createdTimestamp = System.currentTimeMillis()
        )

        db.disputeTicketDao().insertTicket(ticket)
        db.transactionDao().updateStatus(transactionId, "DISPUTED")

        // Auto Bot Message in Support Chat
        db.supportMessageDao().insertMessage(
            SupportMessageEntity(
                sender = "BOT",
                text = "আপনার অভিযোগ টিকিট ($ticketNum) সফলভাবে জমা নেওয়া হয়েছে। ট্রানজ্যাকশন আইডি: $transactionId। কাস্টমার প্রতিনিধি ২ মিনিটের মধ্যে রিভিউ করবেন।",
                ticketId = ticketNum
            )
        )

        return ticketNum
    }

    suspend fun sendUserChatMessage(text: String) {
        val userMsg = SupportMessageEntity(
            sender = "USER",
            text = text,
            timestamp = System.currentTimeMillis()
        )
        db.supportMessageDao().insertMessage(userMsg)

        // Smart Bot Logic Response in Bangla/English
        val isBn = _userState.value.language == LanguageCode.BANGLA
        val responseText = when {
            text.contains("রিচার্জ") || text.contains("recharge") -> {
                if (isBn) "রিচার্জ পেন্ডিং থাকলে সাধারণত ৩০ সেকেন্ড থেকে ১ মিনিটের মধ্যে সফল হয়। কোনো সমস্যা হলে অভিযোগ টিকিট ওপেন করতে পারেন।"
                else "Mobile recharge usually processes within 30 seconds. If stuck, please submit a Dispute Ticket from History."
            }
            text.contains("কমিশন") || text.contains("commission") -> {
                if (isBn) "প্রতিটি সফল ফ্লেক্সিলোড ও রিচার্জের সাথে সাথে অটোমেটিক কমিশন আপনার 'কমিশন ওয়ালেটে' জমা হয়।"
                else "Commission is credited instantly to your Commission Wallet on every successful recharge transaction!"
            }
            text.contains("বিল") || text.contains("bill") -> {
                if (isBn) "বিদ্যুৎ, গ্যাস, পানি ও ইন্টারনেটের অফিশিয়াল রসিদ পেমেন্ট শেষে ডিজিটাল কপি ডাউনলোড করতে পারবেন।"
                else "You can view and download official digital payment receipts immediately after bill payment."
            }
            else -> {
                if (isBn) "ধন্যবাদ! আমরা আপনার মেসেজটি পেয়েছি। আমাদের হেল্প ডেস্ক টিম আপনার সাথে অতি দ্রুত যোগাযোগ করছে।"
                else "Thank you! Our support desk has received your inquiry and will get back to you shortly."
            }
        }

        db.supportMessageDao().insertMessage(
            SupportMessageEntity(
                sender = "BOT",
                text = responseText,
                timestamp = System.currentTimeMillis() + 800
            )
        )
    }
}
