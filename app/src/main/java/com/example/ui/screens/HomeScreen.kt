package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Sanitizer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.entity.ProductEntity
import com.example.data.entity.ReviewEntity
import com.example.ui.components.ProductCard
import com.example.ui.components.RatingStars
import com.example.ui.components.SectionHeader
import com.example.ui.theme.FarmBackground
import com.example.ui.theme.FarmBorder
import com.example.ui.theme.FarmBrightGreen
import com.example.ui.theme.FarmBrownLight
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

@Composable
fun HomeScreen(
    products: List<ProductEntity>,
    reviews: List<ReviewEntity>,
    cartMap: Map<Int, com.example.ui.model.CartItem>,
    onAddToCart: (ProductEntity, Int) -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToContact: () -> Unit,
    onNavigateToFarm: () -> Unit,
    onWhatsAppClick: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmBackground)
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. HERO SECTION
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
            ) {
                // High-quality cow / dairy farm background
                Image(
                    painter = painterResource(id = R.drawable.img_farm_hero),
                    contentDescription = "Rehmat Dairy Farm Pasture",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp),
                    contentScale = ContentScale.Crop
                )

                // Dark Green Gradient Overlay for readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x770D2A1D),
                                    Color(0xCC0D2A1D),
                                    Color(0xF50D2A1D)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Logo Badge
                    Image(
                        painter = painterResource(id = R.drawable.img_rehmat_logo),
                        contentDescription = "REHMAT DAIRY FARM Logo",
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .border(2.dp, FarmGoldGhee, CircleShape)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "REHMAT DAIRY FARM",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    )

                    Surface(
                        color = FarmBrightGreen.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    ) {
                        Text(
                            text = "“Fresh Milk, Pure Trust”",
                            color = Color(0xFFA5D6A7),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "100% Raw, Unpasteurized Pure Cow Milk & Traditional Dairy Products Delivered Fresh Every Morning in Lahore & Surroundings.",
                        color = Color(0xFFE8F5E9),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp,
                        maxLines = 2,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // CTA Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = onNavigateToProducts,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FarmGoldGhee,
                                contentColor = FarmDarkGreen
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("hero_order_milk_btn")
                        ) {
                            Text("Order Fresh Milk", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        OutlinedButton(
                            onClick = onNavigateToContact,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(0.9f)
                                .height(46.dp)
                                .testTag("hero_contact_btn")
                        ) {
                            Text("Contact Us", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // 2. FOUR TRUST PILLARS
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "THE REHMAT PROMISE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = FarmFreshGreen,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Pure Pakistani Dairy Standards",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = FarmDarkGreen
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PillarCard(
                        icon = Icons.Default.WaterDrop,
                        title = "Fresh & Pure Milk",
                        subtitle = "No water or powder mixing",
                        modifier = Modifier.weight(1f)
                    )
                    PillarCard(
                        icon = Icons.Default.Pets,
                        title = "Healthy Cows",
                        subtitle = "Grass-fed & vet inspected",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PillarCard(
                        icon = Icons.Default.Sanitizer,
                        title = "Hygienic Milking",
                        subtitle = "Stainless steel pipeline",
                        modifier = Modifier.weight(1f)
                    )
                    PillarCard(
                        icon = Icons.Default.LocalShipping,
                        title = "Farm Fresh Delivery",
                        subtitle = "Direct to doorstep at 6 AM",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 3. DAILY MILK SUBSCRIPTION SPOTLIGHT BANNER
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { onNavigateToSubscription() }
                    .testTag("subscription_banner_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FarmDarkGreenHeader),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(FarmBrightGreen.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = "Subscription",
                            tint = FarmGoldGhee,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            color = FarmGoldGhee,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "DAILY DOORSTEP DELIVERY",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = FarmDarkGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Daily Milk Subscription",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Choose daily, weekly, or monthly delivery. Morning 6 AM slot guaranteed.",
                            color = Color(0xFFC8E6C9),
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Subscribe",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // 4. FEATURED PRODUCTS SECTION
        item {
            SectionHeader(
                title = "Farm Fresh Dairy",
                subtitle = "Pure Cow Milk, Desi Ghee & Fresh Yogurt",
                actionText = "View All",
                onActionClick = onNavigateToProducts
            )
        }

        item {
            val featuredProducts = products.take(4)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                featuredProducts.forEach { product ->
                    val inCart = cartMap[product.id]?.quantity ?: 0
                    ProductCard(
                        product = product,
                        currentQuantityInCart = inCart,
                        onAddToCart = { qty -> onAddToCart(product, qty) }
                    )
                }
            }
        }

        // 5. OUR FARM STORY TEASER
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .clickable { onNavigateToFarm() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FarmBrownSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_milking_hygiene),
                            contentDescription = "Milking hygiene",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "OUR FARM HERITAGE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = FarmNaturalBrown
                            )
                            Text(
                                text = "Behind Every Drop of Milk",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = FarmDarkGreen
                            )
                            Text(
                                text = "Explore our cows, feeding, touchless milking, and hygiene standards.",
                                fontSize = 11.sp,
                                color = FarmTextSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }

        // 6. CUSTOMER TESTIMONIALS
        item {
            SectionHeader(
                title = "Customer Trust",
                subtitle = "Loved by Pakistani households for pure unadulterated taste",
                actionText = "Reviews",
                onActionClick = onNavigateToContact
            )
        }

        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reviews.take(5)) { review ->
                    TestimonialCard(review = review)
                }
            }
        }

        // 7. ORDER ON WHATSAPP PROMINENT BANNER
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onWhatsAppClick() },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FarmWhatsApp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Direct WhatsApp Orders",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Prefer chatting? Send your order list on WhatsApp directly.",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp
                        )
                    }
                    Button(
                        onClick = onWhatsAppClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = FarmDarkGreen
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Chat Now", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun PillarCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(FarmLightGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = FarmFreshGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = FarmDarkGreen
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = FarmTextSecondary,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun TestimonialCard(review: ReviewEntity) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .height(150.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RatingStars(rating = review.rating)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = FarmBrightGreen,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "Verified",
                            fontSize = 9.sp,
                            color = FarmFreshGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "“${review.reviewText}”",
                    fontSize = 11.sp,
                    color = FarmTextPrimary,
                    maxLines = 3,
                    lineHeight = 15.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = review.customerName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = FarmDarkGreen
                )
                Text(
                    text = " • ${review.city}",
                    fontSize = 11.sp,
                    color = FarmTextSecondary
                )
            }
        }
    }
}
