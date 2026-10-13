package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.OrderEntity
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
fun OrderConfirmationScreen(
    order: OrderEntity,
    onWhatsAppShare: () -> Unit,
    onBackToHome: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(order.orderDate))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmBackground)
            .testTag("order_confirmation_screen"),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // Animated style confirmation badge
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(FarmLightGreen, CircleShape)
                    .border(2.dp, FarmBrightGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = FarmFreshGreen,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Order Placed Successfully!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = FarmDarkGreen,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Thank you for choosing REHMAT DAIRY FARM. Your pure milk and dairy products are booked.",
                fontSize = 12.sp,
                color = FarmTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )
        }

        // Order Number Badge
        item {
            Surface(
                color = FarmDarkGreenHeader,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ORDER REFERENCE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA5D6A7)
                        )
                        Text(
                            text = order.orderNumber,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    Surface(
                        color = FarmGoldGhee,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = order.status.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = FarmDarkGreen,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Order Status Flow
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Order Journey",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmDarkGreen
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val steps = listOf(
                        "1. Order Received (Confirmed)" to true,
                        "2. Fresh Milking & Chilled Packaging" to (order.status != "New"),
                        "3. Dispatched in Cold Van" to (order.status in listOf("Preparing", "Out for Delivery", "Delivered")),
                        "4. Delivered to Your Doorstep" to (order.status == "Delivered")
                    )

                    steps.forEachIndexed { idx, (stepTitle, isDone) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        if (isDone) FarmFreshGreen else Color(0xFFE0E0E0),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = stepTitle,
                                fontSize = 12.sp,
                                fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal,
                                color = if (isDone) FarmDarkGreen else FarmTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Customer & Items Details
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Delivery & Payment Summary",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmDarkGreen
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Customer Name:", fontSize = 12.sp, color = FarmTextSecondary)
                        Text(order.customerName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Phone Number:", fontSize = 12.sp, color = FarmTextSecondary)
                        Text(order.customerPhone, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Delivery Address:", fontSize = 12.sp, color = FarmTextSecondary)
                        Text(
                            order.deliveryAddress,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = FarmTextPrimary,
                            maxLines = 2,
                            modifier = Modifier.width(180.dp),
                            textAlign = TextAlign.End
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Slot:", fontSize = 12.sp, color = FarmTextSecondary)
                        Text(order.deliveryOption, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = FarmFreshGreen)
                    }

                    Divider(modifier = Modifier.padding(vertical = 10.dp), color = FarmBorder)

                    Text(
                        text = "Ordered Items:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmDarkGreen
                    )
                    Text(
                        text = order.itemsSummary,
                        fontSize = 12.sp,
                        color = FarmTextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Divider(modifier = Modifier.padding(vertical = 10.dp), color = FarmBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total Payable (Cash on Delivery):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = FarmDarkGreen)
                        Text("Rs. ${order.totalAmount.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = FarmFreshGreen)
                    }
                }
            }
        }

        // WhatsApp Confirmation & Home Buttons
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onWhatsAppShare,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FarmWhatsApp,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("confirm_whatsapp_btn")
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Confirm Order via WhatsApp", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                OutlinedButton(
                    onClick = onBackToHome,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = FarmDarkGreen),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, FarmDarkGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("back_to_home_btn")
                ) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Return to Home", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}
