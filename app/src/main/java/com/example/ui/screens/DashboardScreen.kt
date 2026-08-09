package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TransactionEntity
import com.example.data.repository.UserState
import com.example.model.AppStrings
import com.example.model.LanguageCode

data class QuickActionItem(
    val route: String,
    val titleBn: String,
    val titleEn: String,
    val icon: ImageVector,
    val containerColor: Color,
    val iconColor: Color,
    val badgeTextBn: String? = null,
    val badgeTextEn: String? = null
)

@Composable
fun DashboardScreen(
    userState: UserState,
    recentTransactions: List<TransactionEntity>,
    onNavigate: (String) -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleTheme: () -> Unit,
    onSelectTransaction: (TransactionEntity) -> Unit
) {
    val isBangla = userState.language == LanguageCode.BANGLA
    var isBalanceVisible by remember { mutableStateOf(false) }

    val quickActions = listOf(
        QuickActionItem(
            route = "recharge",
            titleBn = "মোবাইল রিচার্জ",
            titleEn = "Mobile Recharge",
            icon = Icons.Default.PhoneAndroid,
            containerColor = Color(0xFFE0F2F1),
            iconColor = Color(0xFF00796B),
            badgeTextBn = "৩.৫% ব্যাক",
            badgeTextEn = "3.5% Cash"
        ),
        QuickActionItem(
            route = "bill_pay",
            titleBn = "বিল পে",
            titleEn = "Bill Payment",
            icon = Icons.Default.Receipt,
            containerColor = Color(0xFFE1F5FE),
            iconColor = Color(0xFF0288D1),
            badgeTextBn = "অফিশিয়াল",
            badgeTextEn = "Official"
        ),
        QuickActionItem(
            route = "commission",
            titleBn = "কমিশন ওয়ালেট",
            titleEn = "Commission Wallet",
            icon = Icons.Default.MonetizationOn,
            containerColor = Color(0xFFFFF8E1),
            iconColor = Color(0xFFFF8F00),
            badgeTextBn = "₹ ${String.format("%.1f", userState.commissionBalance)}",
            badgeTextEn = "₹ ${String.format("%.1f", userState.commissionBalance)}"
        ),
        QuickActionItem(
            route = "history",
            titleBn = "লেনদেনের ইতিহাস",
            titleEn = "Transaction History",
            icon = Icons.Default.History,
            containerColor = Color(0xFFEDE7F6),
            iconColor = Color(0xFF512DA8)
        ),
        QuickActionItem(
            route = "support_chat",
            titleBn = "লাইভ চ্যাট সাপোর্ট",
            titleEn = "Live Chat Support",
            icon = Icons.Default.Chat,
            containerColor = Color(0xFFE8F5E9),
            iconColor = Color(0xFF2E7D32),
            badgeTextBn = "২৪/৭ একটিভ",
            badgeTextEn = "24/7 Active"
        ),
        QuickActionItem(
            route = "dispute_tickets",
            titleBn = "অভিযোগ ট্র্যাকিং",
            titleEn = "Issue Dispute",
            icon = Icons.Default.ReportProblem,
            containerColor = Color(0xFFFFEBEE),
            iconColor = Color(0xFFC62828)
        ),
        QuickActionItem(
            route = "security_vault",
            titleBn = "সিকিউরিটি ও ভল্ট",
            titleEn = "Security & Vault",
            icon = Icons.Default.Security,
            containerColor = Color(0xFFECEFF1),
            iconColor = Color(0xFF37474F),
            badgeTextBn = "AES-256",
            badgeTextEn = "AES-256"
        ),
        QuickActionItem(
            route = "settings",
            titleBn = "সেটিংস",
            titleEn = "Settings",
            icon = Icons.Default.Settings,
            containerColor = Color(0xFFF3E5F5),
            iconColor = Color(0xFF7B1FA2)
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Top Header
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "MR",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = userState.userName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = if (isBangla) userState.userTierBn else userState.userTierEn,
                                    fontSize = 11.sp,
                                    color = Color(0xFFFFD54F),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Row {
                            IconButton(onClick = onToggleLanguage) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = "Language",
                                    tint = Color.White
                                )
                            }
                            IconButton(onClick = onToggleTheme) {
                                Icon(
                                    imageVector = if (userState.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = "Theme",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Balance Card Banner
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isBalanceVisible = !isBalanceVisible },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AccountBalanceWallet,
                                        contentDescription = "Wallet",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = AppStrings.get("main_balance", isBangla),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF546E7A)
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle Balance",
                                        tint = Color(0xFF00796B),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isBalanceVisible) (if (isBangla) "লুকান" else "Hide")
                                        else (if (isBangla) "দেখুন" else "Tap"),
                                        fontSize = 11.sp,
                                        color = Color(0xFF00796B),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (isBalanceVisible) "₹ ${String.format("%.2f", userState.mainBalance)}"
                                else "₹ • • • • • •",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF004D40)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Commission wallet highlight row inside balance card
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFFF8E1))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.MonetizationOn,
                                        contentDescription = "Commission",
                                        tint = Color(0xFFFF8F00),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isBangla) "অর্জিত কমিশন ব্যালেন্স:" else "Commission Cashback:",
                                        fontSize = 12.sp,
                                        color = Color(0xFF5D4037),
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Text(
                                    text = "₹ ${String.format("%.2f", userState.commissionBalance)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE65100)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Services Header
        item {
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 12.dp)) {
                Text(
                    text = if (isBangla) "সকল সেবাসমূহ" else "Main Services",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isBangla) "সহজে রিচার্জ, বিল পরিশোধ ও কমিশন ওয়ালেট" else "Quick mobile recharge, bill payment & security",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Quick Grid Services
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                val chunks = quickActions.chunked(2)
                for (chunk in chunks) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (item in chunk) {
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onNavigate(item.route) },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(item.containerColor),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = item.titleEn,
                                                tint = item.iconColor,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }

                                        val badge = if (isBangla) item.badgeTextBn else item.badgeTextEn
                                        if (badge != null) {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = item.containerColor
                                            ) {
                                                Text(
                                                    text = badge,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = item.iconColor,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = if (isBangla) item.titleBn else item.titleEn,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Security Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .clickable { onNavigate("security_vault") },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF101921)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00E676).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "AES-256",
                            tint = Color(0xFF00E676),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBangla) "AES-256 এনক্রিপ্টেড ওয়ালেট" else "AES-256 Encrypted Security",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isBangla) "মাল্টি-লেভেল সিকিউরিটি ও পিন প্রটেকশন সক্রিয়" else "Multi-level security & SHA-256 PIN active",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open Vault",
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Recent Transactions Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBangla) "সাম্প্রতিক লেনদেন" else "Recent Transactions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = if (isBangla) "সব দেখুন" else "View All",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onNavigate("history") }
                )
            }
        }

        // Recent Transactions List
        items(recentTransactions.take(5)) { tx ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
                    .clickable { onSelectTransaction(tx) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    if (tx.type == "RECHARGE") Color(0xFFE0F2F1)
                                    else if (tx.type == "BILL_PAYMENT") Color(0xFFE1F5FE)
                                    else Color(0xFFFFF8E1)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (tx.type == "RECHARGE") Icons.Default.PhoneAndroid
                                else if (tx.type == "BILL_PAYMENT") Icons.Default.Receipt
                                else Icons.Default.MonetizationOn,
                                contentDescription = tx.type,
                                tint = if (tx.type == "RECHARGE") Color(0xFF00796B)
                                else if (tx.type == "BILL_PAYMENT") Color(0xFF0288D1)
                                else Color(0xFFFF8F00),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = tx.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = tx.recipientOrAccount,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "₹ ${String.format("%.2f", tx.amount)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = tx.status,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (tx.status == "SUCCESS") Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }
            }
        }
    }
}
