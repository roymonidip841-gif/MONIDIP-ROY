package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppStrings
import com.example.model.OperatorType
import com.example.model.RechargeOffer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RechargeScreen(
    isBangla: Boolean,
    onBack: () -> Unit,
    onRequestPin: (OperatorType, String, Double, String?) -> Unit
) {
    var selectedOperator by remember { mutableStateOf(OperatorType.JIO) }
    var mobileNumber by remember { mutableStateOf("9876543210") }
    var amountText by remember { mutableStateOf("299") }
    var connectionType by remember { mutableStateOf("PREPAID") } // PREPAID, POSTPAID
    var selectedOffer by remember { mutableStateOf<RechargeOffer?>(null) }
    var activeTab by remember { mutableIntStateOf(0) }

    val quickContacts = listOf(
        Pair("নিজের নম্বর (Self)", "9876543210"),
        Pair("আম্মু (Mom)", "9811998877"),
        Pair("আব্বু (Dad)", "9822334455"),
        Pair("অফিস (Office)", "98555667788")
    )

    val currentAmount = amountText.toDoubleOrNull() ?: 0.0
    val commissionEarned = currentAmount * (selectedOperator.commissionPercent / 100.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isBangla) "মোবাইল রিচার্জ" else "Mobile Recharge",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Operator Selection Row
            item {
                Column {
                    Text(
                        text = if (isBangla) "অপারেটর নির্বাচন করুন" else "Select Mobile Operator",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OperatorType.entries.forEach { op ->
                            val isSelected = selectedOperator == op
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedOperator = op },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) op.brandColor else MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = op.code,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${op.commissionPercent}%",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.Yellow else Color(0xFFFF8F00)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Connection Type Pills (Prepaid / Postpaid)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("PREPAID" to "প্রিপেইড", "POSTPAID" to "পোস্টপেইড").forEach { pair ->
                        val isSel = connectionType == pair.first
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { connectionType = pair.first },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isBangla) pair.second else pair.first,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Phone Number Entry Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = mobileNumber,
                            onValueChange = {
                                mobileNumber = it
                                if (it.length >= 3) {
                                    selectedOperator = OperatorType.detectFromNumber(it)
                                }
                            },
                            label = { Text(text = if (isBangla) "মোবাইল নম্বর লিখুন" else "Enter Mobile Number") },
                            leadingIcon = { Icon(imageVector = Icons.Default.PhoneAndroid, contentDescription = "Phone") },
                            trailingIcon = {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = selectedOperator.brandColor
                                ) {
                                    Text(
                                        text = selectedOperator.code,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick contacts row
                        Text(
                            text = if (isBangla) "দ্রুত নম্বর সিলেক্ট করুন:" else "Quick Select Contacts:",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(quickContacts) { contact ->
                                Surface(
                                    modifier = Modifier.clickable {
                                        mobileNumber = contact.second
                                        selectedOperator = OperatorType.detectFromNumber(contact.second)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Contact",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = contact.first,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Amount Input / Commission Banner Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = amountText,
                            onValueChange = {
                                if (it.isEmpty() || it.toDoubleOrNull() != null) {
                                    amountText = it
                                    selectedOffer = null
                                }
                            },
                            label = { Text(text = if (isBangla) "রিচার্জের পরিমাণ (₹)" else "Recharge Amount (₹)") },
                            leadingIcon = { Icon(imageVector = Icons.Default.FlashOn, contentDescription = "Amount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Quick Amounts row
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(listOf(19, 49, 199, 299, 479, 719)) { amt ->
                                Surface(
                                    modifier = Modifier.clickable {
                                        amountText = amt.toString()
                                        selectedOffer = null
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (amountText == amt.toString()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = "₹$amt",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (amountText == amt.toString()) Color.White else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        // Commission Cashback preview highlight box
                        if (currentAmount > 0) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.MonetizationOn,
                                            contentDescription = "Commission",
                                            tint = Color(0xFFFF8F00),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isBangla) "কমিশন ক্যাশব্যাক পাবেন:" else "Instant Cashback Earned:",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF5D4037)
                                        )
                                    }

                                    Text(
                                        text = "+ ₹ ${String.format("%.2f", commissionEarned)}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFE65100)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Offers catalog tab
            item {
                Column {
                    Text(
                        text = if (isBangla) "${selectedOperator.nameBn} স্পেশাল প্যাকেজ ও অফার" else "${selectedOperator.nameEn} Special Offers",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val offers = OperatorType.getSampleOffers(selectedOperator)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        offers.forEach { offer ->
                            val isOfferSelected = selectedOffer == offer
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedOffer = offer
                                        amountText = offer.amount.toInt().toString()
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isOfferSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                ),
                                border = if (isOfferSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (isBangla) offer.titleBn else offer.titleEn,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = if (isBangla) offer.detailBn else offer.detailEn,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "₹ ${offer.amount.toInt()}",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "+ ₹${offer.cashbackCommission.toInt()} ক্যাশব্যাক",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFD84315)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Submit Button
            item {
                Button(
                    onClick = {
                        val amt = amountText.toDoubleOrNull() ?: 0.0
                        if (mobileNumber.length >= 10 && amt > 0) {
                            onRequestPin(
                                selectedOperator,
                                mobileNumber,
                                amt,
                                selectedOffer?.let { if (isBangla) it.titleBn else it.titleEn }
                            )
                        }
                    },
                    enabled = mobileNumber.length >= 10 && (amountText.toDoubleOrNull() ?: 0.0) > 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Text(
                        text = if (isBangla) "রিচার্জ নিশ্চিত করুন (₹ $amountText)" else "Proceed Recharge (₹ $amountText)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
