package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.ProductEntity
import com.example.ui.components.ProductCard
import com.example.ui.components.SectionHeader
import com.example.ui.model.CartItem
import com.example.ui.theme.FarmBackground
import com.example.ui.theme.FarmBorder
import com.example.ui.theme.FarmBrightGreen
import com.example.ui.theme.FarmDarkGreen
import com.example.ui.theme.FarmFreshGreen
import com.example.ui.theme.FarmGoldGhee
import com.example.ui.theme.FarmLightGreen
import com.example.ui.theme.FarmSurfaceCard
import com.example.ui.theme.FarmTextPrimary
import com.example.ui.theme.FarmTextSecondary

@Composable
fun ProductsScreen(
    products: List<ProductEntity>,
    selectedCategory: String,
    searchQuery: String,
    cartMap: Map<Int, CartItem>,
    onSelectCategory: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onAddToCart: (ProductEntity, Int) -> Unit,
    onViewCartClick: () -> Unit
) {
    val categories = listOf("All", "Milk", "Desi Ghee", "Yogurt / Dahi", "Butter", "Cream", "Other")

    val cartItemCount = cartMap.values.sumOf { it.quantity }
    val cartSubtotal = cartMap.values.sumOf { it.totalPrice }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("product_search_input"),
                    placeholder = {
                        Text(
                            "Search pure milk, desi ghee, dahi...",
                            fontSize = 13.sp,
                            color = FarmTextSecondary
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = FarmFreshGreen
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Search",
                                    tint = FarmTextSecondary
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = FarmSurfaceCard,
                        unfocusedContainerColor = FarmSurfaceCard,
                        focusedBorderColor = FarmFreshGreen,
                        unfocusedBorderColor = FarmBorder
                    ),
                    singleLine = true
                )
            }

            // Category Chips Row
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) FarmDarkGreen else FarmSurfaceCard,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) FarmDarkGreen else FarmBorder
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onSelectCategory(category) }
                            .testTag("category_chip_$category")
                    ) {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else FarmTextPrimary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Products Count Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${products.size} Products Available",
                    fontSize = 12.sp,
                    color = FarmTextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "100% Unadulterated Guarantee",
                    fontSize = 11.sp,
                    color = FarmFreshGreen,
                    fontWeight = FontWeight.Bold
                )
            }

            if (products.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFFB0BEC5),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No dairy products found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarmDarkGreen
                        )
                        Text(
                            text = "Try searching for milk, ghee, or clearing the filter.",
                            style = MaterialTheme.typography.bodySmall,
                            color = FarmTextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 6.dp,
                        bottom = if (cartItemCount > 0) 140.dp else 90.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(products, key = { it.id }) { product ->
                        val currentQty = cartMap[product.id]?.quantity ?: 0
                        ProductCard(
                            product = product,
                            currentQuantityInCart = currentQty,
                            onAddToCart = { qty -> onAddToCart(product, qty) }
                        )
                    }
                }
            }
        }

        // Floating Sticky Cart summary bar if items in cart
        if (cartItemCount > 0) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 16.dp, end = 16.dp, bottom = 80.dp)
                    .fillMaxWidth()
                    .clickable { onViewCartClick() }
                    .testTag("floating_cart_summary_bar"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FarmDarkGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(FarmGoldGhee, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$cartItemCount",
                                fontWeight = FontWeight.ExtraBold,
                                color = FarmDarkGreen,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Cart Total: Rs. ${cartSubtotal.toInt()}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Tap to checkout or order via WhatsApp",
                                color = Color(0xFFA5D6A7),
                                fontSize = 10.sp
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "View Cart",
                            color = FarmGoldGhee,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Go to Cart",
                            tint = FarmGoldGhee,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
