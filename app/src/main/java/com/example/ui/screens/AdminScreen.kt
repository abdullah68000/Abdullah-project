package com.example.ui.screens

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.GalleryItemEntity
import com.example.data.entity.OrderEntity
import com.example.data.entity.ProductEntity
import com.example.data.entity.ReviewEntity
import com.example.data.entity.SubscriptionEntity
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
fun AdminScreen(
    isAdminUnlocked: Boolean,
    orders: List<OrderEntity>,
    subscriptions: List<SubscriptionEntity>,
    products: List<ProductEntity>,
    galleryItems: List<GalleryItemEntity>,
    reviews: List<ReviewEntity>,
    configs: Map<String, String>,
    onUnlock: (String) -> Boolean,
    onLock: () -> Unit,
    onUpdateOrderStatus: (orderNumber: String, status: String) -> Unit,
    onUpdateSubStatus: (id: Int, status: String) -> Unit,
    onSaveProduct: (id: Int, name: String, desc: String, price: Double, unit: String, category: String, image: String, isAvailable: Boolean, isFeatured: Boolean) -> Unit,
    onDeleteProduct: (ProductEntity) -> Unit,
    onAddGalleryItem: (title: String, category: String, desc: String, image: String) -> Unit,
    onDeleteGalleryItem: (GalleryItemEntity) -> Unit,
    onUpdateReviewApproval: (id: Int, isApproved: Boolean) -> Unit,
    onDeleteReview: (ReviewEntity) -> Unit,
    onUpdateConfig: (key: String, value: String) -> Unit,
    onClose: () -> Unit
) {
    var enteredPin by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Orders") }
    val tabs = listOf("Orders", "Subscriptions", "Products", "Farm Settings", "Gallery", "Reviews")

    // Product Dialog state
    var showProductDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<ProductEntity?>(null) }

    // Gallery Dialog state
    var showGalleryDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmBackground)
            .testTag("admin_screen")
    ) {
        // Top Header
        Surface(color = FarmDarkGreenHeader, shadowElevation = 4.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Close Admin", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Admin & Farm Operations",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isAdminUnlocked) {
                    IconButton(onClick = onLock) {
                        Icon(Icons.Default.Lock, contentDescription = "Lock Admin", tint = FarmGoldGhee)
                    }
                }
            }
        }

        if (!isAdminUnlocked) {
            // PIN ENTRY SCREEN
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(FarmLightGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = FarmFreshGreen, modifier = Modifier.size(28.dp))
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Admin Authentication",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = FarmDarkGreen
                        )
                        Text(
                            text = "Enter Farm Manager PIN to manage orders, subscriptions, prices, and farm configuration.",
                            fontSize = 12.sp,
                            color = FarmTextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                        Text(
                            text = "(Default PIN: 1234)",
                            fontSize = 11.sp,
                            color = FarmFreshGreen,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = enteredPin,
                            onValueChange = { enteredPin = it },
                            label = { Text("Enter 4-digit PIN") },
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_pin_input")
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                if (onUnlock(enteredPin)) {
                                    enteredPin = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FarmDarkGreen,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("admin_login_btn")
                        ) {
                            Text("Unlock Dashboard", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // ADMIN DASHBOARD CONTENT
            // Tabs Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tabs) { tab ->
                    val isSelected = selectedTab == tab
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (isSelected) FarmDarkGreen else Color(0xFFF1F8E9),
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { selectedTab = tab }
                            .testTag("admin_tab_$tab")
                    ) {
                        Text(
                            text = tab,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else FarmDarkGreen,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Divider(color = FarmBorder)

            when (selectedTab) {
                "Orders" -> AdminOrdersTab(orders, onUpdateOrderStatus)
                "Subscriptions" -> AdminSubscriptionsTab(subscriptions, onUpdateSubStatus)
                "Products" -> AdminProductsTab(
                    products = products,
                    onAddClick = {
                        editingProduct = null
                        showProductDialog = true
                    },
                    onEditClick = { product ->
                        editingProduct = product
                        showProductDialog = true
                    },
                    onDelete = onDeleteProduct
                )
                "Farm Settings" -> AdminSettingsTab(configs, onUpdateConfig)
                "Gallery" -> AdminGalleryTab(
                    galleryItems = galleryItems,
                    onAddClick = { showGalleryDialog = true },
                    onDelete = onDeleteGalleryItem
                )
                "Reviews" -> AdminReviewsTab(reviews, onUpdateReviewApproval, onDeleteReview)
            }
        }
    }

    // Product Add/Edit Dialog
    if (showProductDialog) {
        ProductEditDialog(
            product = editingProduct,
            onDismiss = { showProductDialog = false },
            onSave = { id, name, desc, price, unit, cat, img, avail, feat ->
                onSaveProduct(id, name, desc, price, unit, cat, img, avail, feat)
                showProductDialog = false
            }
        )
    }

    // Gallery Add Dialog
    if (showGalleryDialog) {
        GalleryAddDialog(
            onDismiss = { showGalleryDialog = false },
            onAdd = { title, cat, desc, img ->
                onAddGalleryItem(title, cat, desc, img)
                showGalleryDialog = false
            }
        )
    }
}

@Composable
fun AdminOrdersTab(
    orders: List<OrderEntity>,
    onUpdateStatus: (String, String) -> Unit
) {
    if (orders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No customer orders yet", color = FarmTextSecondary, fontSize = 14.sp)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders) { order ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = order.orderNumber,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = FarmDarkGreen
                            )
                            Surface(
                                color = when (order.status) {
                                    "New" -> Color(0xFFFFF3E0)
                                    "Confirmed" -> Color(0xFFE8F5E9)
                                    "Preparing" -> Color(0xFFE1F5FE)
                                    "Out for Delivery" -> Color(0xFFFFF9C4)
                                    "Delivered" -> Color(0xFFC8E6C9)
                                    else -> Color(0xFFFFEBEE)
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = order.status,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FarmDarkGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Customer: ${order.customerName} (${order.customerPhone})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = FarmTextPrimary)
                        Text(text = "Address: ${order.deliveryAddress}", fontSize = 11.sp, color = FarmTextSecondary)
                        Text(text = "Slot: ${order.deliveryOption}", fontSize = 11.sp, color = FarmTextSecondary)
                        Text(text = "Items: ${order.itemsSummary}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = FarmDarkGreen, modifier = Modifier.padding(top = 4.dp))

                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = FarmBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Total: Rs. ${order.totalAmount.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FarmFreshGreen)

                            // Status Changer dropdown/chips
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                listOf("Confirmed", "Preparing", "Out for Delivery", "Delivered").forEach { st ->
                                    Surface(
                                        color = if (order.status == st) FarmDarkGreen else FarmBackground,
                                        shape = RoundedCornerShape(6.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .clickable { onUpdateStatus(order.orderNumber, st) }
                                    ) {
                                        Text(
                                            text = st.take(4),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (order.status == st) Color.White else FarmTextPrimary,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminSubscriptionsTab(
    subscriptions: List<SubscriptionEntity>,
    onUpdateStatus: (Int, String) -> Unit
) {
    if (subscriptions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No milk subscriptions registered yet", color = FarmTextSecondary, fontSize = 14.sp)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(subscriptions) { sub ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${sub.customerName} (${sub.quantityLiters.toInt()}L ${sub.frequency})",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = FarmDarkGreen
                            )
                            Surface(
                                color = if (sub.status == "Active") Color(0xFFC8E6C9) else Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = sub.status,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FarmDarkGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Phone: ${sub.customerPhone}", fontSize = 11.sp, color = FarmTextSecondary)
                        Text(text = "Address: ${sub.deliveryAddress}", fontSize = 11.sp, color = FarmTextSecondary)
                        Text(text = "Delivery Slot: ${sub.deliveryTime}", fontSize = 11.sp, color = FarmFreshGreen)
                        Text(text = "Monthly Plan: Rs. ${sub.estimatedCost.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FarmDarkGreen)

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { onUpdateStatus(sub.id, if (sub.status == "Active") "Paused" else "Active") },
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(if (sub.status == "Active") "Pause Subscription" else "Resume Subscription", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminProductsTab(
    products: List<ProductEntity>,
    onAddClick: () -> Unit,
    onEditClick: (ProductEntity) -> Unit,
    onDelete: (ProductEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${products.size} Products in Catalog",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = FarmDarkGreen
            )
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = FarmFreshGreen, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("admin_add_product_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Product", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(products) { product ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = product.name,
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
                                        text = product.category,
                                        fontSize = 9.sp,
                                        color = FarmFreshGreen,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Rs. ${product.price.toInt()} / ${product.unit}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = FarmFreshGreen
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { onEditClick(product) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = FarmDarkGreen, modifier = Modifier.size(20.dp))
                            }
                            IconButton(onClick = { onDelete(product) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE53935), modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminSettingsTab(
    configs: Map<String, String>,
    onUpdateConfig: (String, String) -> Unit
) {
    var phone by remember(configs) { mutableStateOf(configs["contact_phone"] ?: "+92 300 1234567") }
    var whatsapp by remember(configs) { mutableStateOf(configs["whatsapp_number"] ?: "+92 300 1234567") }
    var address by remember(configs) { mutableStateOf(configs["farm_address"] ?: "Rehmat Dairy Farm, Raiwind Road, Lahore") }
    var deliveryFee by remember(configs) { mutableStateOf(configs["delivery_charge"] ?: "100") }
    var freeThreshold by remember(configs) { mutableStateOf(configs["free_delivery_threshold"] ?: "1500") }
    var pin by remember(configs) { mutableStateOf(configs["admin_pin"] ?: "1234") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Farm Contact & WhatsApp Settings", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FarmDarkGreen)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Call Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = whatsapp,
                        onValueChange = { whatsapp = it },
                        label = { Text("WhatsApp Ordering Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Farm Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onUpdateConfig("contact_phone", phone)
                            onUpdateConfig("whatsapp_number", whatsapp)
                            onUpdateConfig("farm_address", address)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FarmDarkGreen, contentColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save Contact Settings")
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Delivery & Security", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FarmDarkGreen)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = deliveryFee,
                        onValueChange = { deliveryFee = it },
                        label = { Text("Delivery Charge (PKR)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = freeThreshold,
                        onValueChange = { freeThreshold = it },
                        label = { Text("Free Delivery Threshold (PKR)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = pin,
                        onValueChange = { pin = it },
                        label = { Text("Manager Admin PIN") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onUpdateConfig("delivery_charge", deliveryFee)
                            onUpdateConfig("free_delivery_threshold", freeThreshold)
                            onUpdateConfig("admin_pin", pin)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FarmDarkGreen, contentColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save Rates & PIN")
                    }
                }
            }
        }
    }
}

@Composable
fun AdminGalleryTab(
    galleryItems: List<GalleryItemEntity>,
    onAddClick: () -> Unit,
    onDelete: (GalleryItemEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Gallery Media Items (${galleryItems.size})", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FarmDarkGreen)
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = FarmFreshGreen, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Photo")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(galleryItems) { item ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FarmDarkGreen)
                            Text(text = "Category: ${item.category}", fontSize = 11.sp, color = FarmFreshGreen)
                            Text(text = item.description, fontSize = 11.sp, color = FarmTextSecondary, maxLines = 1)
                        }
                        IconButton(onClick = { onDelete(item) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE53935))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminReviewsTab(
    reviews: List<ReviewEntity>,
    onUpdateApproval: (Int, Boolean) -> Unit,
    onDelete: (ReviewEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(reviews) { review ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "${review.customerName} (${review.rating} ★)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = FarmDarkGreen)
                            Text(text = review.city, fontSize = 10.sp, color = FarmTextSecondary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = if (review.isApproved) "Approved" else "Hidden", fontSize = 11.sp, color = if (review.isApproved) FarmFreshGreen else Color.Gray)
                            Switch(
                                checked = review.isApproved,
                                onCheckedChange = { onUpdateApproval(review.id, it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = FarmDarkGreen, checkedTrackColor = FarmLightGreen)
                            )
                            IconButton(onClick = { onDelete(review) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE53935), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "“${review.reviewText}”", fontSize = 11.sp, color = FarmTextPrimary)
                }
            }
        }
    }
}

@Composable
fun ProductEditDialog(
    product: ProductEntity?,
    onDismiss: () -> Unit,
    onSave: (id: Int, name: String, desc: String, price: Double, unit: String, cat: String, img: String, avail: Boolean, feat: Boolean) -> Unit
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var priceStr by remember { mutableStateOf(product?.price?.toInt()?.toString() ?: "200") }
    var unit by remember { mutableStateOf(product?.unit ?: "Liter") }
    var category by remember { mutableStateOf(product?.category ?: "Milk") }
    var isAvailable by remember { mutableStateOf(product?.isAvailable ?: true) }
    var isFeatured by remember { mutableStateOf(product?.isFeatured ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (product == null) "Add Dairy Product" else "Edit Product", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Product Name") }, singleLine = true)
                OutlinedTextField(value = priceStr, onValueChange = { priceStr = it }, label = { Text("Price (PKR)") }, singleLine = true)
                OutlinedTextField(value = unit, onValueChange = { unit = it }, label = { Text("Unit (Liter, 1kg, 500g)") }, singleLine = true)
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category (Milk, Desi Ghee, Yogurt / Dahi, Butter, Cream, Other)") }, singleLine = true)
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Short Description") }, minLines = 2)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = isFeatured, onCheckedChange = { isFeatured = it })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Feature on Home", fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = priceStr.toDoubleOrNull() ?: 200.0
                    val img = product?.imageResName ?: if (category.contains("Ghee", true)) "img_dairy_products" else "img_farm_hero"
                    onSave(product?.id ?: 0, name, description, price, unit, category, img, isAvailable, isFeatured)
                },
                colors = ButtonDefaults.buttonColors(containerColor = FarmDarkGreen)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun GalleryAddDialog(
    onDismiss: () -> Unit,
    onAdd: (title: String, category: String, description: String, image: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Farm") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Gallery Photo", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, singleLine = true)
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category (Cows, Calves, Farm, Milking, Dairy Products)") }, singleLine = true)
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, minLines = 2)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val img = when {
                            category.contains("Milk", true) -> "img_milking_hygiene"
                            category.contains("Product", true) -> "img_dairy_products"
                            else -> "img_farm_hero"
                        }
                        onAdd(title, category, description, img)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = FarmDarkGreen)
            ) {
                Text("Add Photo")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
