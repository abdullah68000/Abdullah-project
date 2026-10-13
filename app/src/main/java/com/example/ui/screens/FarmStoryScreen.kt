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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Sanitizer
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.entity.GalleryItemEntity
import com.example.ui.components.SectionHeader
import com.example.ui.components.getDrawableForName
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

@Composable
fun FarmStoryScreen(
    galleryItems: List<GalleryItemEntity>
) {
    var selectedGalleryCategory by remember { mutableStateOf("All") }
    val galleryCategories = listOf("All", "Cows", "Calves", "Farm", "Milking", "Dairy Products", "Daily Farm Activities")

    val filteredGallery = remember(galleryItems, selectedGalleryCategory) {
        if (selectedGalleryCategory == "All") {
            galleryItems
        } else {
            galleryItems.filter { it.category.equals(selectedGalleryCategory, ignoreCase = true) }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmBackground)
            .testTag("farm_story_screen"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Hero Image & Story Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_farm_hero),
                    contentDescription = "Farm Pasture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x990D2A1D))
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Surface(
                        color = FarmGoldGhee,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "OUR DAIRY HERITAGE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = FarmDarkGreen,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "REHMAT DAIRY FARM",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Cultivating pure nutrition, humane livestock welfare, and unadulterated freshness in Pakistan.",
                        fontSize = 12.sp,
                        color = Color(0xFFC8E6C9),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // 1. Farm Story Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "The Story Behind 'Pure Trust'",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmDarkGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Founded with a single moral mission: to restore real, thick, grass-fed Pakistani cow milk to urban homes without chemicals, hormone injections, or dilution. Located on fertile agricultural belts outside Lahore, our farm combines traditional pastoral care with modern automated hygiene technology.",
                        fontSize = 13.sp,
                        color = FarmTextSecondary,
                        lineHeight = 19.sp
                    )
                }
            }
        }

        // 2. The 6 Pillars of Farm Operation
        item {
            SectionHeader(
                title = "Farm Operations & Standards",
                subtitle = "Transparency from pasture to your morning glass"
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FarmProcessCard(
                    icon = Icons.Default.Pets,
                    title = "Healthy Cows & Breeds",
                    description = "We breed resilient indigenous Sahiwal and Cholistani cows cross-bred with top Holstein-Friesian stock for superior A2 protein balance and high natural butterfat."
                )
                FarmProcessCard(
                    icon = Icons.Default.Grass,
                    title = "Nutritious Feeding Process",
                    description = "Zero industrial chemical feeds. Our cows consume organically cultivated green lucerne (barseem), whole corn silage, mineral salts, and fresh bore-well water daily."
                )
                FarmProcessCard(
                    icon = Icons.Default.Sanitizer,
                    title = "Touchless Milking Process",
                    description = "Automated Swedish-grade stainless steel pipeline milking. Hands never touch the milk, eliminating bacteria, dust, and atmospheric pathogens."
                )
                FarmProcessCard(
                    icon = Icons.Default.Shield,
                    title = "Strict Hygiene Standards",
                    description = "Every udder is sanitized before milking. Milking clusters and tanks undergo automated CIP (Clean-in-Place) heat sterilization cycles twice every single day."
                )
                FarmProcessCard(
                    icon = Icons.Default.WaterDrop,
                    title = "Fresh Milk Cold-Chain Collection",
                    description = "Fresh milk is immediately piped into chilled bulk coolers and dropped to 4°C within 15 minutes to lock in freshness, enzymes, and natural sweet cream."
                )
                FarmProcessCard(
                    icon = Icons.Default.LocalShipping,
                    title = "Guaranteed Morning Delivery",
                    description = "Chilled insulated transport vans dispatch before sunrise, guaranteeing that your family receives chilled raw milk at your door by 6:00 AM - 8:00 AM."
                )
            }
        }

        // 3. Cows & Farm Gallery Section
        item {
            Spacer(modifier = Modifier.height(14.dp))
            SectionHeader(
                title = "Cows & Farm Gallery",
                subtitle = "Browse our livestock, barns, and daily activities"
            )
        }

        // Gallery Filter Chips
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(galleryCategories) { category ->
                    val isSelected = selectedGalleryCategory == category
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (isSelected) FarmDarkGreen else FarmSurfaceCard,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) FarmDarkGreen else FarmBorder
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { selectedGalleryCategory = category }
                    ) {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else FarmTextPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                filteredGallery.forEach { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
                    ) {
                        Column {
                            Image(
                                painter = painterResource(id = getDrawableForName(item.imageResName)),
                                contentDescription = item.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                contentScale = ContentScale.Crop
                            )
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = FarmDarkGreen
                                    )
                                    Surface(
                                        color = FarmLightGreen,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = item.category,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = FarmFreshGreen,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.description,
                                    fontSize = 12.sp,
                                    color = FarmTextSecondary,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FarmProcessCard(
    icon: ImageVector,
    title: String,
    description: String
) {
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
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(FarmLightGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = FarmFreshGreen,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = FarmDarkGreen
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = FarmTextSecondary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
