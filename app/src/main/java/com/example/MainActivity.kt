package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.TransactionEntity
import com.example.data.repository.PayRepository
import com.example.model.LanguageCode
import com.example.model.OperatorType
import com.example.model.UtilityBiller
import com.example.ui.components.PinVerificationModal
import com.example.ui.components.ReceiptDialog
import com.example.ui.components.WelcomeSplash
import com.example.ui.screens.BillPaymentScreen
import com.example.ui.screens.CommissionScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DisputeTicketsScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.RechargeScreen
import com.example.ui.screens.SecurityVaultScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SupportChatScreen
import com.example.ui.theme.PayRechargeTheme
import kotlinx.coroutines.launch

data class PaymentPinRequest(
    val amount: Double,
    val recipientOrService: String,
    val onConfirmAction: suspend () -> Boolean
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PayRechargeApp()
        }
    }
}

@Composable
fun PayRechargeApp() {
    val context = LocalContext.current
    val repository = remember { PayRepository(context) }
    val scope = rememberCoroutineScope()

    val userState by repository.userState.collectAsStateWithLifecycle()
    val transactions by repository.allTransactions.collectAsStateWithLifecycle(initialValue = emptyList())
    val tickets by repository.allTickets.collectAsStateWithLifecycle(initialValue = emptyList())
    val messages by repository.allMessages.collectAsStateWithLifecycle(initialValue = emptyList())

    val isBangla = userState.language == LanguageCode.BANGLA
    val snackbarHostState = remember { SnackbarHostState() }

    var currentRoute by remember { mutableStateOf("dashboard") }
    var selectedReceiptTx by remember { mutableStateOf<TransactionEntity?>(null) }
    var pendingPinRequest by remember { mutableStateOf<PaymentPinRequest?>(null) }

    // Splash Welcome check
    var isSplashVisible by remember { mutableStateOf(!userState.isWelcomeDismissed) }

    PayRechargeTheme(darkTheme = userState.isDarkTheme) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isSplashVisible) {
                    WelcomeSplash(
                        isBangla = isBangla,
                        onGetStarted = { phone ->
                            repository.loginWithPhone(phone)
                            isSplashVisible = false
                        }
                    )
                } else {
                    when (currentRoute) {
                        "dashboard" -> {
                            DashboardScreen(
                                userState = userState,
                                recentTransactions = transactions,
                                onNavigate = { route -> currentRoute = route },
                                onToggleLanguage = {
                                    val nextLang = if (userState.language == LanguageCode.BANGLA) LanguageCode.ENGLISH else LanguageCode.BANGLA
                                    repository.setLanguage(nextLang)
                                },
                                onToggleTheme = {
                                    repository.setDarkTheme(!userState.isDarkTheme)
                                },
                                onSelectTransaction = { tx -> selectedReceiptTx = tx }
                            )
                        }

                        "recharge" -> {
                            RechargeScreen(
                                isBangla = isBangla,
                                onBack = { currentRoute = "dashboard" },
                                onRequestPin = { operator: OperatorType, number: String, amount: Double, offerTitle: String? ->
                                    pendingPinRequest = PaymentPinRequest(
                                        amount = amount,
                                        recipientOrService = "$number (${operator.code})",
                                        onConfirmAction = {
                                            val success = repository.executeRecharge(
                                                operatorName = operator.nameEn,
                                                mobileNumber = number,
                                                amount = amount,
                                                commissionPercent = operator.commissionPercent,
                                                offerTitle = offerTitle
                                            )
                                            if (success) {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        if (isBangla) "রিচার্জ সফল হয়েছে! কমিশন জমা হয়েছে।" else "Recharge Successful! Commission Credited."
                                                    )
                                                }
                                                currentRoute = "dashboard"
                                            } else {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        if (isBangla) "পর্যাপ্ত ব্যালেন্স নেই!" else "Insufficient Balance!"
                                                    )
                                                }
                                            }
                                            success
                                        }
                                    )
                                }
                            )
                        }

                        "bill_pay" -> {
                            BillPaymentScreen(
                                isBangla = isBangla,
                                onBack = { currentRoute = "dashboard" },
                                onRequestPayBill = { biller: UtilityBiller, accountNo: String, amount: Double ->
                                    pendingPinRequest = PaymentPinRequest(
                                        amount = amount,
                                        recipientOrService = "${biller.nameEn} ($accountNo)",
                                        onConfirmAction = {
                                            val success = repository.executeBillPayment(
                                                billerName = biller.nameEn,
                                                accountNo = accountNo,
                                                amount = amount
                                            )
                                            if (success) {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        if (isBangla) "বিল পেমেন্ট সফল হয়েছে!" else "Bill Payment Successful!"
                                                    )
                                                }
                                                currentRoute = "dashboard"
                                            } else {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        if (isBangla) "পর্যাপ্ত ব্যালেন্স নেই!" else "Insufficient Balance!"
                                                    )
                                                }
                                            }
                                            success
                                        }
                                    )
                                }
                            )
                        }

                        "commission" -> {
                            CommissionScreen(
                                userState = userState,
                                isBangla = isBangla,
                                onBack = { currentRoute = "dashboard" },
                                onTransferCommission = {
                                    scope.launch {
                                        val success = repository.transferCommissionToMain()
                                        if (success) {
                                            snackbarHostState.showSnackbar(
                                                if (isBangla) "কমিশন সফলভাবে মূল ওয়ালেটে যোগ হয়েছে!" else "Commission transferred to main wallet!"
                                            )
                                        }
                                    }
                                }
                            )
                        }

                        "history" -> {
                            HistoryScreen(
                                transactions = transactions,
                                isBangla = isBangla,
                                onBack = { currentRoute = "dashboard" },
                                onSelectTransaction = { tx -> selectedReceiptTx = tx }
                            )
                        }

                        "support_chat" -> {
                            SupportChatScreen(
                                messages = messages,
                                isBangla = isBangla,
                                onBack = { currentRoute = "dashboard" },
                                onSendMessage = { text ->
                                    scope.launch {
                                        repository.sendUserChatMessage(text)
                                    }
                                }
                            )
                        }

                        "dispute_tickets" -> {
                            DisputeTicketsScreen(
                                tickets = tickets,
                                isBangla = isBangla,
                                onBack = { currentRoute = "dashboard" },
                                onCreateNewTicket = { txId, category, desc ->
                                    scope.launch {
                                        val ticketNum = repository.createDisputeTicket(txId, category, desc)
                                        snackbarHostState.showSnackbar(
                                            if (isBangla) "অভিযোগ টিকিট ($ticketNum) তৈরি হয়েছে।" else "Dispute ticket ($ticketNum) created."
                                        )
                                    }
                                }
                            )
                        }

                        "security_vault" -> {
                            SecurityVaultScreen(
                                userState = userState,
                                isBangla = isBangla,
                                onBack = { currentRoute = "dashboard" },
                                onToggleBiometric = { enabled ->
                                    repository.toggleBiometric(enabled)
                                },
                                onChangePin = { newPin ->
                                    repository.updatePin(newPin)
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (isBangla) "পিন সফলভাবে পরিবর্তিত হয়েছে!" else "PIN updated successfully!"
                                        )
                                    }
                                }
                            )
                        }

                        "settings" -> {
                            SettingsScreen(
                                userState = userState,
                                isBangla = isBangla,
                                onBack = { currentRoute = "dashboard" },
                                onToggleLanguage = {
                                    val next = if (userState.language == LanguageCode.BANGLA) LanguageCode.ENGLISH else LanguageCode.BANGLA
                                    repository.setLanguage(next)
                                },
                                onToggleTheme = {
                                    repository.setDarkTheme(!userState.isDarkTheme)
                                },
                                onOpenSecurityVault = {
                                    currentRoute = "security_vault"
                                },
                                onLogout = {
                                    repository.logout()
                                    isSplashVisible = true
                                    currentRoute = "dashboard"
                                }
                            )
                        }
                    }
                }

                // PIN Modal
                pendingPinRequest?.let { req ->
                    PinVerificationModal(
                        amount = req.amount,
                        recipientOrService = req.recipientOrService,
                        storedPinHash = userState.storedPinHash,
                        isBiometricEnabled = userState.isBiometricEnabled,
                        isBangla = isBangla,
                        onDismiss = { pendingPinRequest = null },
                        onPinSuccess = {
                            val action = req.onConfirmAction
                            pendingPinRequest = null
                            scope.launch {
                                action()
                            }
                        }
                    )
                }

                // Digital Receipt Dialog
                selectedReceiptTx?.let { tx ->
                    ReceiptDialog(
                        transaction = tx,
                        isBangla = isBangla,
                        onDismiss = { selectedReceiptTx = null },
                        onReportDispute = { txId ->
                            selectedReceiptTx = null
                            currentRoute = "dispute_tickets"
                        }
                    )
                }
            }
        }
    }
}
