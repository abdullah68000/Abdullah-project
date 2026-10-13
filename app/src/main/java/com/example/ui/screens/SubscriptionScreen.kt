package com.example.ui.screens

import android.content.Context
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.SubscriptionEntity
import com.example.ui.components.SectionHeader
import com.example.ui.theme.FarmBackground
import com.example.ui.theme.FarmBorder
import com.example.ui.theme.FarmBrightGreen
import com.example.ui.theme.FarmDarkGreen
import com.example.ui.theme.FarmDarkGreenHeader
import com.example.ui.theme.FarmFreshGreen
import com.example.ui.theme.FarmGoldGhee
import com.example.ui.theme.FarmLightGreen
import com.example.ui.theme.FarmSurfaceCard
import com.example.ui.theme.FarmTextPrimary
import com.example.ui.theme.FarmTextSecondary
import com.example.ui.theme.FarmWhatsApp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SubscriptionScreen(
    milkPricePerLiter: Double = 240.0,
    subscriptions: List<SubscriptionEntity>,
    onSubscribe: (
        name: String,
        phone: String,
        address: String,
        frequency: String,
        quantity: Double,
        deliveryTime: String,
        startDate: String,
        cost: Double,
        shareWhatsApp: Boolean
    ) -> Unit
) {
    val context = LocalContext.current

    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var deliveryAddress by remember { mutableStateOf("") }
    var selectedFrequency by remember { mutableStateOf("Daily") }
    var selectedQuantity by remember { mutableDoubleStateOf(2.0) }
    var selectedTimeSlot by remember { mutableStateOf("Morning (6:00 AM - 8:00 AM)") }

    val defaultStartDate = remember {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(System.currentTimeMillis() + 86400000L))
    }
    var startDate by remember { mutableStateOf(defaultStartDate) }

    // Cost estimation
    // Daily: 30 days * qty * price
    // Weekly: 4 weeks (7 days each = 28 days) * qty * price
    // Monthly: 30 days * qty * price
    val daysCount = when (selectedFrequency) {
        "Weekly" -> 7
        "Monthly" -> 30
        else -> 30 // Daily billed monthly
    }
    val estimatedCost = daysCount * selectedQuantity * milkPricePerLiter

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmBackground)
            .testTag("subscription_screen_list"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FarmDarkGreenHeader)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(FarmBrightGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Daily Milk Subscription",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Pure Cow Milk at your doorstep every morning",
                                color = Color(0xFFC8E6C9),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SubscriptionPill(text = "✓ Zero Adulteration")
                        SubscriptionPill(text = "✓ Pause Anytime")
                        SubscriptionPill(text = "✓ Morning 6 AM")
                    }
                }
            }
        }

        // STEP 1: Frequency Selection
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Select Delivery Frequency",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmDarkGreen
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Daily", "Weekly", "Monthly").forEach { freq ->
                            val isSelected = selectedFrequency == freq
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedFrequency = freq }
                                    .testTag("freq_$freq"),
                                color = if (isSelected) FarmDarkGreen else FarmBackground,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) FarmDarkGreen else FarmBorder
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = freq,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else FarmTextPrimary,
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // STEP 2: Milk Quantity Selection
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "2. Select Milk Quantity",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = FarmDarkGreen
                        )
                        Text(
                            text = "${selectedQuantity.toInt()} Liters / delivery",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = FarmFreshGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(1.0, 2.0, 3.0, 4.0, 5.0).forEach { qty ->
                            val isSelected = selectedQuantity == qty
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedQuantity = qty }
                                    .testTag("qty_${qty.toInt()}L"),
                                color = if (isSelected) FarmFreshGreen else FarmBackground,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) FarmFreshGreen else FarmBorder
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${qty.toInt()} L",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else FarmTextPrimary
                                    )
                                    Text(
                                        text = "Rs. ${(qty * milkPricePerLiter).toInt()}",
                                        fontSize = 10.sp,
                                        color = if (isSelected) Color(0xFFE8F5E9) else FarmTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // STEP 3: Delivery Time & Start Date
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "3. Preferred Delivery Slot & Start Date",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmDarkGreen
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val timeSlots = listOf(
                        "Morning (6:00 AM - 8:00 AM)",
                        "Evening (5:00 PM - 7:00 PM)"
                    )
                    timeSlots.forEach { slot ->
                        val isSelected = selectedTimeSlot == slot
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedTimeSlot = slot },
                            color = if (isSelected) FarmLightGreen else FarmBackground,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) FarmBrightGreen else FarmBorder
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = if (isSelected) FarmFreshGreen else FarmTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = slot,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) FarmDarkGreen else FarmTextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("subscription_start_date_input"),
                        label = { Text("Starting Date") },
                        leadingIcon = {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = FarmFreshGreen)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FarmFreshGreen,
                            unfocusedBorderColor = FarmBorder
                        )
                    )
                }
            }
        }

        // STEP 4: Customer Details
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "4. Delivery Address & Contact Details",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmDarkGreen
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("sub_name_input"),
                        label = { Text("Full Name") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = FarmFreshGreen)
                        },
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("sub_phone_input"),
                        label = { Text("Mobile / WhatsApp Number (e.g. 0300-1234567)") },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = FarmFreshGreen)
                        },
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = deliveryAddress,
                        onValueChange = { deliveryAddress = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("sub_address_input"),
                        label = { Text("Complete Delivery Address (House/Street/Sector/City)") },
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = FarmFreshGreen)
                        },
                        shape = RoundedCornerShape(10.dp),
                        minLines = 2
                    )
                }
            }
        }

        // ESTIMATED COST BREAKDOWN
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, FarmBrightGreen)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Estimated Subscription Plan",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmDarkGreen
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$selectedFrequency (${selectedQuantity.toInt()}L x $daysCount deliveries @ Rs.${milkPricePerLiter.toInt()}/L)",
                            fontSize = 12.sp,
                            color = FarmTextSecondary
                        )
                        Text(
                            text = "Rs. ${estimatedCost.toInt()}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = FarmDarkGreen
                        )
                    }

                    Text(
                        text = "* Free insulated doorstep delivery included. Pay weekly or monthly after delivery confirmation.",
                        fontSize = 11.sp,
                        color = FarmTextSecondary,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        }

        // CONFIRMATION ACTION BUTTONS
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        onSubscribe(
                            customerName,
                            customerPhone,
                            deliveryAddress,
                            selectedFrequency,
                            selectedQuantity,
                            selectedTimeSlot,
                            startDate,
                            estimatedCost,
                            false
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FarmDarkGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("confirm_subscription_btn")
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Confirm Subscription (Rs. ${estimatedCost.toInt()})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Button(
                    onClick = {
                        onSubscribe(
                            customerName,
                            customerPhone,
                            deliveryAddress,
                            selectedFrequency,
                            selectedQuantity,
                            selectedTimeSlot,
                            startDate,
                            estimatedCost,
                            true
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FarmWhatsApp,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("whatsapp_subscription_btn")
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Subscribe via WhatsApp",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Active Subscriptions on this device if any
        if (subscriptions.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Your Active Subscriptions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = FarmDarkGreen
                )
            }

            items(subscriptions.size) { index ->
                val sub = subscriptions[index]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${sub.quantityLiters.toInt()}L ${sub.frequency} Milk",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FarmDarkGreen
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = FarmLightGreen,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = sub.status,
                                        fontSize = 10.sp,
                                        color = FarmFreshGreen,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Slot: ${sub.deliveryTime}",
                                fontSize = 11.sp,
                                color = FarmTextSecondary
                            )
                            Text(
                                text = "To: ${sub.deliveryAddress}",
                                fontSize = 11.sp,
                                color = FarmTextSecondary,
                                maxLines = 1
                            )
                        }

                        Text(
                            text = "Rs. ${sub.estimatedCost.toInt()}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = FarmFreshGreen
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionPill(text: String) {
    Surface(
        color = Color(0x33A5D6A7),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = text,
            color = Color(0xFFC8E6C9),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
