package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.OrderEntity
import com.example.ui.components.getDrawableForName
import com.example.ui.model.CartItem
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

@Composable
fun CartCheckoutScreen(
    cartMap: Map<Int, CartItem>,
    subtotal: Double,
    deliveryFee: Double,
    total: Double,
    onUpdateQuantity: (productId: Int, delta: Int) -> Unit,
    onRemoveItem: (productId: Int) -> Unit,
    onClearCart: () -> Unit,
    onPlaceOrder: (
        name: String,
        phone: String,
        address: String,
        deliveryOption: String,
        notes: String
    ) -> Unit,
    onOrderWhatsAppDirect: (
        name: String,
        phone: String,
        address: String,
        deliveryOption: String
    ) -> Unit,
    onContinueShopping: () -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var deliveryAddress by remember { mutableStateOf("") }
    var deliveryOption by remember { mutableStateOf("Morning Express (6:00 AM - 8:30 AM)") }
    var orderNotes by remember { mutableStateOf("") }

    val itemsList = cartMap.values.toList()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmBackground)
            .testTag("cart_checkout_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Back & Title Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onContinueShopping) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = FarmDarkGreen
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Shopping Cart & Checkout",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmDarkGreen
                    )
                }

                if (itemsList.isNotEmpty()) {
                    Text(
                        text = "Clear",
                        fontSize = 12.sp,
                        color = Color(0xFFEF5350),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { onClearCart() }
                            .padding(8.dp)
                    )
                }
            }
        }

        if (itemsList.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(FarmLightGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = FarmFreshGreen,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Your Cart is Empty",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = FarmDarkGreen
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Add farm fresh cow milk, golden desi ghee, and thick dahi to your cart.",
                            fontSize = 12.sp,
                            color = FarmTextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = onContinueShopping,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FarmDarkGreen,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Browse Fresh Products", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // Cart Items List
            item {
                Text(
                    text = "Items in Your Cart (${itemsList.sumOf { it.quantity }})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = FarmDarkGreen
                )
            }

            items(itemsList, key = { it.product.id }) { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = getDrawableForName(item.product.imageResName)),
                            contentDescription = item.product.name,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.product.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = FarmDarkGreen
                            )
                            Text(
                                text = "Rs. ${item.product.price.toInt()} / ${item.product.unit}",
                                fontSize = 11.sp,
                                color = FarmTextSecondary
                            )
                            Text(
                                text = "Subtotal: Rs. ${item.totalPrice.toInt()}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = FarmFreshGreen
                            )
                        }

                        // Quantity selector
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = FarmLightGreen,
                            border = androidx.compose.foundation.BorderStroke(1.dp, FarmBrightGreen)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { onUpdateQuantity(item.product.id, -1) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Minus",
                                        tint = FarmDarkGreen,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Text(
                                    text = "${item.quantity}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = FarmDarkGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                )
                                IconButton(
                                    onClick = { onUpdateQuantity(item.product.id, 1) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Plus",
                                        tint = FarmDarkGreen,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = { onRemoveItem(item.product.id) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove",
                                tint = Color(0xFFEF5350),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Delivery Details Form
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Customer & Delivery Address",
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
                                .testTag("checkout_name_input"),
                            label = { Text("Customer Name *") },
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
                                .testTag("checkout_phone_input"),
                            label = { Text("Phone / WhatsApp (e.g. 0300-1234567) *") },
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
                                .testTag("checkout_address_input"),
                            label = { Text("Delivery Address (House #, Street, Area) *") },
                            leadingIcon = {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = FarmFreshGreen)
                            },
                            shape = RoundedCornerShape(10.dp),
                            minLines = 2
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = orderNotes,
                            onValueChange = { orderNotes = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Special Instructions (Optional)") },
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Select Delivery Option",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = FarmDarkGreen
                        )

                        val options = listOf(
                            "Morning Express (6:00 AM - 8:30 AM)",
                            "Standard Daytime Delivery (10:00 AM - 3:00 PM)",
                            "Evening Delivery (5:00 PM - 8:00 PM)"
                        )

                        options.forEach { opt ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { deliveryOption = opt }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = deliveryOption == opt,
                                    onClick = { deliveryOption = opt },
                                    colors = RadioButtonDefaults.colors(selectedColor = FarmFreshGreen)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = opt,
                                    fontSize = 12.sp,
                                    color = FarmDarkGreen,
                                    fontWeight = if (deliveryOption == opt) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            // Order Price Breakdown Card
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Order Summary",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = FarmDarkGreen
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Products Subtotal", fontSize = 13.sp, color = FarmTextSecondary)
                            Text("Rs. ${subtotal.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Doorstep Delivery Fee", fontSize = 13.sp, color = FarmTextSecondary)
                            if (deliveryFee == 0.0) {
                                Text("FREE", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = FarmFreshGreen)
                            } else {
                                Text("Rs. ${deliveryFee.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                            }
                        }

                        if (deliveryFee > 0.0) {
                            Text(
                                text = "💡 Tip: Orders above Rs. 1,500 qualify for FREE delivery!",
                                fontSize = 11.sp,
                                color = FarmFreshGreen,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Divider(modifier = Modifier.padding(vertical = 10.dp), color = FarmBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Payable", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = FarmDarkGreen)
                            Text("Rs. ${total.toInt()}", fontSize = 20.sp, fontWeight = FontWeight.Black, color = FarmFreshGreen)
                        }
                    }
                }
            }

            // Place Order & WhatsApp Buttons
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            onPlaceOrder(
                                customerName,
                                customerPhone,
                                deliveryAddress,
                                deliveryOption,
                                orderNotes
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FarmDarkGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("place_in_app_order_btn")
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Place Order (Rs. ${total.toInt()})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    // Prominent Order on WhatsApp button
                    Button(
                        onClick = {
                            onOrderWhatsAppDirect(
                                customerName,
                                customerPhone,
                                deliveryAddress,
                                deliveryOption
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FarmWhatsApp,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("order_on_whatsapp_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Chat, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Order on WhatsApp",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
