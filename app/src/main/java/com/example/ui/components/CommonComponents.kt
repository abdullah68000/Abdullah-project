package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.entity.ProductEntity
import com.example.ui.theme.FarmBackground
import com.example.ui.theme.FarmBorder
import com.example.ui.theme.FarmBrightGreen
import com.example.ui.theme.FarmBrownSurface
import com.example.ui.theme.FarmDarkGreen
import com.example.ui.theme.FarmDarkGreenHeader
import com.example.ui.theme.FarmFreshGreen
import com.example.ui.theme.FarmGoldGhee
import com.example.ui.theme.FarmLightGreen
import com.example.ui.theme.FarmNaturalBrown
import com.example.ui.theme.FarmSurfaceCard
import com.example.ui.theme.FarmTextPrimary
import com.example.ui.theme.FarmTextSecondary
import com.example.ui.theme.FarmWhatsApp

fun getDrawableForName(name: String): Int {
    return when (name) {
        "img_rehmat_logo" -> R.drawable.img_rehmat_logo
        "img_farm_hero" -> R.drawable.img_farm_hero
        "img_milking_hygiene" -> R.drawable.img_milking_hygiene
        "img_dairy_products" -> R.drawable.img_dairy_products
        else -> R.drawable.img_dairy_products
    }
}

@Composable
fun BrandTopBar(
    cartCount: Int,
    onCartClick: () -> Unit,
    onAdminClick: () -> Unit
) {
    Surface(
        color = FarmDarkGreenHeader,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_rehmat_logo),
                    contentDescription = "REHMAT DAIRY FARM Logo",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, FarmBrightGreen, CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "REHMAT DAIRY FARM",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Fresh Milk, Pure Trust",
                        color = Color(0xFFC8E6C9),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onAdminClick,
                    modifier = Modifier.testTag("admin_header_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Admin Dashboard",
                        tint = Color(0xFFB0BEC5),
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(
                    onClick = onCartClick,
                    modifier = Modifier.testTag("cart_header_btn")
                ) {
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge(
                                    containerColor = FarmGoldGhee,
                                    contentColor = FarmDarkGreen
                                ) {
                                    Text(
                                        text = cartCount.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Shopping Cart",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp, 20.dp)
                        .background(FarmBrightGreen, RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = FarmDarkGreen
                )
            }
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = FarmTextSecondary,
                    modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                )
            }
        }

        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelLarge,
                color = FarmFreshGreen,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onActionClick() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun ProductCard(
    product: ProductEntity,
    currentQuantityInCart: Int,
    onAddToCart: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Image(
                    painter = painterResource(id = getDrawableForName(product.imageResName)),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentScale = ContentScale.Crop
                )

                // Category Tag
                Surface(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart),
                    color = FarmDarkGreen.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = product.category,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                if (product.isFeatured) {
                    Surface(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.TopEnd),
                        color = FarmGoldGhee,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = FarmDarkGreen,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Pure Choice",
                                color = FarmDarkGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarmDarkGreen,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = FarmTextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Rs. ${product.price.toInt()}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = FarmFreshGreen
                        )
                        Text(
                            text = "per ${product.unit}",
                            style = MaterialTheme.typography.labelSmall,
                            color = FarmTextSecondary
                        )
                    }

                    if (currentQuantityInCart > 0) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = FarmLightGreen,
                            border = androidx.compose.foundation.BorderStroke(1.dp, FarmBrightGreen)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                IconButton(
                                    onClick = { onAddToCart(-1) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Decrease",
                                        tint = FarmDarkGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "$currentQuantityInCart",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = FarmDarkGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                IconButton(
                                    onClick = { onAddToCart(1) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Increase",
                                        tint = FarmDarkGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Button(
                            onClick = { onAddToCart(1) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FarmDarkGreen,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                horizontal = 14.dp,
                                vertical = 8.dp
                            ),
                            modifier = Modifier.testTag("add_to_cart_btn_${product.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RatingStars(
    rating: Int,
    maxRating: Int = 5,
    onRatingSelected: ((Int) -> Unit)? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..maxRating) {
            val isFilled = i <= rating
            val icon = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder
            val tint = if (isFilled) FarmGoldGhee else Color(0xFFB0BEC5)

            if (onRatingSelected != null) {
                IconButton(
                    onClick = { onRatingSelected(i) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(imageVector = icon, contentDescription = "$i Stars", tint = tint)
                }
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = "$i Stars",
                    tint = tint,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun StickyWhatsAppFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = FarmWhatsApp,
        contentColor = Color.White,
        shape = CircleShape,
        elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(6.dp),
        modifier = modifier
            .testTag("sticky_whatsapp_fab")
            .size(56.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Chat,
            contentDescription = "Order on WhatsApp",
            modifier = Modifier.size(28.dp)
        )
    }
}

sealed class Screen(val route: String, val title: String, val selectedIcon: @Composable () -> Unit, val unselectedIcon: @Composable () -> Unit) {
    object Home : Screen(
        route = "home",
        title = "Home",
        selectedIcon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
        unselectedIcon = { Icon(Icons.Outlined.Home, contentDescription = "Home") }
    )
    object Products : Screen(
        route = "products",
        title = "Products",
        selectedIcon = { Icon(Icons.Filled.Storefront, contentDescription = "Products") },
        unselectedIcon = { Icon(Icons.Outlined.Storefront, contentDescription = "Products") }
    )
    object Subscription : Screen(
        route = "subscription",
        title = "Daily Milk",
        selectedIcon = { Icon(Icons.Filled.Eco, contentDescription = "Subscription") },
        unselectedIcon = { Icon(Icons.Filled.Eco, contentDescription = "Subscription") }
    )
    object Farm : Screen(
        route = "farm",
        title = "Our Farm",
        selectedIcon = { Icon(Icons.Filled.Info, contentDescription = "Our Farm") },
        unselectedIcon = { Icon(Icons.Outlined.Info, contentDescription = "Our Farm") }
    )
    object Contact : Screen(
        route = "contact",
        title = "Contact",
        selectedIcon = { Icon(Icons.Filled.Phone, contentDescription = "Contact") },
        unselectedIcon = { Icon(Icons.Outlined.Phone, contentDescription = "Contact") }
    )
}

@Composable
fun FarmBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        Screen.Home,
        Screen.Products,
        Screen.Subscription,
        Screen.Farm,
        Screen.Contact
    )

    NavigationBar(
        containerColor = FarmDarkGreenHeader,
        contentColor = Color.White,
        tonalElevation = 6.dp,
        modifier = Modifier.navigationBarsPadding()
    ) {
        items.forEach { screen ->
            val isSelected = currentRoute == screen.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen.route) },
                icon = { if (isSelected) screen.selectedIcon() else screen.unselectedIcon() },
                label = {
                    Text(
                        text = screen.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = FarmDarkGreen,
                    selectedTextColor = Color.White,
                    indicatorColor = Color(0xFFA5D6A7),
                    unselectedIconColor = Color(0xFFB0BEC5),
                    unselectedTextColor = Color(0xFF90A4AE)
                )
            )
        }
    }
}
