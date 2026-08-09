package com.example.model

import androidx.compose.ui.graphics.Color

data class RechargeOffer(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val detailBn: String,
    val detailEn: String,
    val validityBn: String,
    val validityEn: String,
    val amount: Double,
    val cashbackCommission: Double
)

enum class OperatorType(
    val code: String,
    val nameEn: String,
    val nameBn: String,
    val prefixes: List<String>,
    val brandColor: Color,
    val commissionPercent: Double
) {
    JIO(
        code = "JIO",
        nameEn = "Reliance Jio",
        nameBn = "জিও (Jio)",
        prefixes = listOf("600", "700", "790", "888", "898", "999", "933", "701", "987"),
        brandColor = Color(0xFF0F52BA),
        commissionPercent = 3.2
    ),
    AIRTEL(
        code = "AIRTEL",
        nameEn = "Airtel India",
        nameBn = "এয়ারটেল (Airtel)",
        prefixes = listOf("981", "989", "991", "880", "704", "971", "983"),
        brandColor = Color(0xFFE40000),
        commissionPercent = 3.0
    ),
    VI(
        code = "VI",
        nameEn = "Vi (Vodafone Idea)",
        nameBn = "ভি (Vodafone Idea)",
        prefixes = listOf("982", "984", "985", "988", "900", "992", "972"),
        brandColor = Color(0xFFC70C0C),
        commissionPercent = 3.5
    ),
    BSNL(
        code = "BSNL",
        nameEn = "BSNL Mobile",
        nameBn = "বিএসএনএল (BSNL)",
        prefixes = listOf("941", "942", "943", "944", "945", "940", "946"),
        brandColor = Color(0xFF003399),
        commissionPercent = 4.0
    );

    companion object {
        fun detectFromNumber(number: String): OperatorType {
            val clean = number.trim().replace("-", "").replace(" ", "")
            val prefix = if (clean.startsWith("+91")) clean.substring(3, 6)
            else if (clean.length >= 3) clean.substring(0, 3)
            else ""

            return entries.firstOrNull { it.prefixes.contains(prefix) } ?: JIO
        }

        fun getSampleOffers(operator: OperatorType): List<RechargeOffer> {
            return listOf(
                RechargeOffer(
                    id = "off_1",
                    titleBn = "১.৫ জিবি/দিন + আনলিমিটেড কল",
                    titleEn = "1.5GB/Day + Unlimited Calls",
                    detailBn = "মেয়াদ ২৮ দিন | প্রতিদিন ১০০ এসএমএস",
                    detailEn = "28 Days Validity | 100 SMS/day",
                    validityBn = "২৮ দিন",
                    validityEn = "28 Days",
                    amount = 299.0,
                    cashbackCommission = 10.0
                ),
                RechargeOffer(
                    id = "off_2",
                    titleBn = "২ জিবি/দিন মেগা প্যাক (৮৪ দিন)",
                    titleEn = "2GB/Day Mega Value Pack",
                    detailBn = "মেয়াদ ৮৪ দিন | ৫জি হাই স্পিড ডাটা",
                    detailEn = "84 Days Validity | 5G High Speed Data",
                    validityBn = "৮৪ দিন",
                    validityEn = "84 Days",
                    amount = 719.0,
                    cashbackCommission = 25.0
                ),
                RechargeOffer(
                    id = "off_3",
                    titleBn = "১ জিবি/দিন বাজেট প্যাক",
                    titleEn = "1GB/Day Smart Plan",
                    detailBn = "মেয়াদ ২৪ দিন | অল ইন্ডিয়া ফ্রি রোমিং",
                    detailEn = "24 Days Validity | All India Free Roaming",
                    validityBn = "২৪ দিন",
                    validityEn = "24 Days",
                    amount = 209.0,
                    cashbackCommission = 8.0
                ),
                RechargeOffer(
                    id = "off_4",
                    titleBn = "১.৫ জিবি/দিন সুপার কম্বো (৫৬ দিন)",
                    titleEn = "1.5GB/Day Super Combo",
                    detailBn = "৫৬ দিন | আনলিমিটেড ভয়েস ও এসএমএস",
                    detailEn = "56 Days | Unlimited Voice & SMS",
                    validityBn = "৫৬ দিন",
                    validityEn = "56 Days",
                    amount = 479.0,
                    cashbackCommission = 18.0
                )
            )
        }
    }
}
