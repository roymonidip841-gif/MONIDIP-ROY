package com.example.model

import androidx.compose.ui.graphics.Color

enum class BillCategory(val code: String, val nameBn: String, val nameEn: String) {
    ELECTRICITY("ELEC", "বিদ্যুৎ বিল", "Electricity"),
    GAS("GAS", "গ্যাস বিল", "Gas Bill"),
    WATER("WATER", "পানি বিল", "Water Bill"),
    INTERNET("INT", "ইন্টারনেট বিল", "Internet"),
    DTH("DTH", "ডিটিএইচ টিভি", "DTH TV")
}

data class UtilityBiller(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val category: BillCategory,
    val color: Color,
    val accountNoLabelBn: String,
    val accountNoLabelEn: String,
    val sampleBillAmount: Double,
    val serviceFee: Double = 0.0
) {
    companion object {
        fun getAllBillers(): List<UtilityBiller> {
            return listOf(
                UtilityBiller(
                    id = "wbsedcl",
                    nameEn = "WBSEDCL Electricity (West Bengal)",
                    nameBn = "ডাব্লিউবিএসইডিসিএল (WBSEDCL)",
                    category = BillCategory.ELECTRICITY,
                    color = Color(0xFF00897B),
                    accountNoLabelBn = "কনজিউমার আইডি (৯ সংখ্যা)",
                    accountNoLabelEn = "Consumer ID (9 Digits)",
                    sampleBillAmount = 1480.0
                ),
                UtilityBiller(
                    id = "msedcl",
                    nameEn = "MSEDCL / Mahavitaran (Maharashtra)",
                    nameBn = "এমএসইডিসিএল (MSEDCL)",
                    category = BillCategory.ELECTRICITY,
                    color = Color(0xFF1976D2),
                    accountNoLabelBn = "কনজিউমার নম্বর (১২ সংখ্যা)",
                    accountNoLabelEn = "Consumer Number (12 Digits)",
                    sampleBillAmount = 2150.0
                ),
                UtilityBiller(
                    id = "bescom",
                    nameEn = "BESCOM Electricity (Bengaluru)",
                    nameBn = "বেসকমে বিদ্যুৎ (BESCOM)",
                    category = BillCategory.ELECTRICITY,
                    color = Color(0xFFD32F2F),
                    accountNoLabelBn = "একাউন্ট আইডি / কাস্টমার আইডি",
                    accountNoLabelEn = "Account ID / Consumer ID",
                    sampleBillAmount = 1850.0
                ),
                UtilityBiller(
                    id = "tatapower",
                    nameEn = "Tata Power (Mumbai & Delhi)",
                    nameBn = "টাটা পাওয়ার (Tata Power)",
                    category = BillCategory.ELECTRICITY,
                    color = Color(0xFF388E3C),
                    accountNoLabelBn = "কনজিউমার নম্বর",
                    accountNoLabelEn = "Consumer Number",
                    sampleBillAmount = 1240.0
                ),
                UtilityBiller(
                    id = "igl",
                    nameEn = "IGL Gas (Indraprastha Gas)",
                    nameBn = "আইজিএল গ্যাস (IGL Gas)",
                    category = BillCategory.GAS,
                    color = Color(0xFFF57C00),
                    accountNoLabelBn = "বিপি নম্বর (১০ সংখ্যা)",
                    accountNoLabelEn = "BP Number (10 Digits)",
                    sampleBillAmount = 1080.0
                ),
                UtilityBiller(
                    id = "mgl",
                    nameEn = "MGL Gas (Mahanagar Gas)",
                    nameBn = "এমজিএল গ্যাস (MGL Gas)",
                    category = BillCategory.GAS,
                    color = Color(0xFF7B1FA2),
                    accountNoLabelBn = "কাস্টমার একাউন্ট নম্বর",
                    accountNoLabelEn = "Customer Account Number",
                    sampleBillAmount = 975.0
                ),
                UtilityBiller(
                    id = "djb",
                    nameEn = "Delhi Jal Board Water",
                    nameBn = "দিল্লি জল বোর্ড (DJB)",
                    category = BillCategory.WATER,
                    color = Color(0xFF0288D1),
                    accountNoLabelBn = "কে নম্বর (K-Number)",
                    accountNoLabelEn = "K-Number",
                    sampleBillAmount = 650.0
                ),
                UtilityBiller(
                    id = "kmcwater",
                    nameEn = "Kolkata Municipal Corp Water",
                    nameBn = "কলকাতা মিউনিসিপ্যাল ওয়াটার (KMC)",
                    category = BillCategory.WATER,
                    color = Color(0xFF0097A7),
                    accountNoLabelBn = "অ্যাসেসমেন্ট নম্বর",
                    accountNoLabelEn = "Assessment Number",
                    sampleBillAmount = 450.0
                ),
                UtilityBiller(
                    id = "jiofiber",
                    nameEn = "JioFiber Broadband",
                    nameBn = "জিওফাইবার ব্রডব্যান্ড",
                    category = BillCategory.INTERNET,
                    color = Color(0xFFC2185B),
                    accountNoLabelBn = "জিওফাইবার সার্ভিস আইডি",
                    accountNoLabelEn = "JioFiber Service ID",
                    sampleBillAmount = 999.0
                ),
                UtilityBiller(
                    id = "airtelstream",
                    nameEn = "Airtel Xstream Fiber",
                    nameBn = "এয়ারটেল এক্সস্ট্রিম ফাইবার",
                    category = BillCategory.INTERNET,
                    color = Color(0xFF303F9F),
                    accountNoLabelBn = "ল্যান্ডলাইন / একাউন্ট নম্বর",
                    accountNoLabelEn = "Landline / Account Number",
                    sampleBillAmount = 1199.0
                ),
                UtilityBiller(
                    id = "tataplay",
                    nameEn = "Tata Play DTH (Tata Sky)",
                    nameBn = "টাটা প্লে ডিটিএইচ (Tata Play)",
                    category = BillCategory.DTH,
                    color = Color(0xFFE64A19),
                    accountNoLabelBn = "সাবস্ক্রাইবার আইডি (১০ সংখ্যা)",
                    accountNoLabelEn = "Subscriber ID (10 Digits)",
                    sampleBillAmount = 450.0
                ),
                UtilityBiller(
                    id = "airteldth",
                    nameEn = "Airtel Digital TV DTH",
                    nameBn = "এয়ারটেল ডিজিটাল টিভি (DTH)",
                    category = BillCategory.DTH,
                    color = Color(0xFFD32F2F),
                    accountNoLabelBn = "কাস্টমার আইডি (১০ সংখ্যা)",
                    accountNoLabelEn = "Customer ID (10 Digits)",
                    sampleBillAmount = 380.0
                )
            )
        }
    }
}
