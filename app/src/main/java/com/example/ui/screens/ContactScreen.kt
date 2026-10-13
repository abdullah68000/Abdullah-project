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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.entity.ReviewEntity
import com.example.ui.components.RatingStars
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

@Composable
fun ContactScreen(
    configs: Map<String, String>,
    reviews: List<ReviewEntity>,
    onCallClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    onDirectionsClick: () -> Unit,
    onSocialClick: (url: String) -> Unit,
    onSubmitReview: (name: String, city: String, rating: Int, comment: String) -> Unit
) {
    val phone = configs["contact_phone"] ?: "+92 300 1234567"
    val whatsapp = configs["whatsapp_number"] ?: "+92 300 1234567"
    val email = configs["contact_email"] ?: "info@rehmatdairy.pk"
    val address = configs["farm_address"] ?: "Rehmat Dairy Farm, Raiwind Road, Farm Belt, Lahore, Pakistan"
    val hours = configs["opening_hours"] ?: "5:00 AM - 9:00 PM (Daily)"

    val fbUrl = configs["facebook_url"] ?: "https://facebook.com/rehmatdairyfarm"
    val instaUrl = configs["instagram_url"] ?: "https://instagram.com/rehmatdairyfarm"
    val tiktokUrl = configs["tiktok_url"] ?: "https://tiktok.com/@rehmatdairyfarm"
    val ytUrl = configs["youtube_url"] ?: "https://youtube.com/@rehmatdairyfarm"

    // Review Form States
    var reviewerName by remember { mutableStateOf("") }
    var reviewerCity by remember { mutableStateOf("") }
    var reviewRating by remember { mutableIntStateOf(5) }
    var reviewComment by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmBackground)
            .testTag("contact_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Contact Header Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FarmDarkGreenHeader)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_rehmat_logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, FarmGoldGhee, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "REHMAT DAIRY FARM",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Fresh Milk, Pure Trust • Lahore, Pakistan",
                                color = Color(0xFFC8E6C9),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Three Main Quick Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onCallClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FarmBrightGreen,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("contact_call_now_btn")
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Call Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onWhatsAppClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FarmWhatsApp,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("contact_whatsapp_btn")
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onDirectionsClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FarmGoldGhee,
                                contentColor = FarmDarkGreen
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1.1f)
                                .height(44.dp)
                                .testTag("contact_directions_btn")
                        ) {
                            Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Directions", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Contact Information Details Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Farm Location & Official Information",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmDarkGreen
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ContactDetailRow(
                        icon = Icons.Default.Call,
                        title = "Direct Phone Support",
                        value = phone,
                        onClick = onCallClick
                    )

                    ContactDetailRow(
                        icon = Icons.Default.Chat,
                        title = "WhatsApp Inquiries & Orders",
                        value = whatsapp,
                        onClick = onWhatsAppClick
                    )

                    ContactDetailRow(
                        icon = Icons.Default.Email,
                        title = "Official Email",
                        value = email
                    )

                    ContactDetailRow(
                        icon = Icons.Default.LocationOn,
                        title = "Farm Address",
                        value = address,
                        onClick = onDirectionsClick
                    )

                    ContactDetailRow(
                        icon = Icons.Default.Schedule,
                        title = "Operating & Milking Hours",
                        value = hours
                    )
                }
            }
        }

        // Social Media Buttons
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Follow Us on Social Media",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmDarkGreen
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SocialBadge("Facebook", Color(0xFF1877F2)) { onSocialClick(fbUrl) }
                        SocialBadge("Instagram", Color(0xFFE4405F)) { onSocialClick(instaUrl) }
                        SocialBadge("TikTok", Color(0xFF000000)) { onSocialClick(tiktokUrl) }
                        SocialBadge("YouTube", Color(0xFFFF0000)) { onSocialClick(ytUrl) }
                    }
                }
            }
        }

        // CUSTOMER REVIEWS & TESTIMONIALS SECTION
        item {
            SectionHeader(
                title = "Customer Testimonials",
                subtitle = "Read honest reviews from pure milk subscribers"
            )
        }

        // Submit Review Form Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, FarmBrightGreen)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.RateReview, contentDescription = null, tint = FarmFreshGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Leave Your Review",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = FarmDarkGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Your Rating:", fontSize = 12.sp, color = FarmTextSecondary)
                    RatingStars(
                        rating = reviewRating,
                        onRatingSelected = { reviewRating = it }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = reviewerName,
                        onValueChange = { reviewerName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("review_name_input"),
                        label = { Text("Your Name *") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = reviewerCity,
                        onValueChange = { reviewerCity = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("City / Area (e.g. Lahore DHA, Gulberg)") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = reviewComment,
                        onValueChange = { reviewComment = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("review_comment_input"),
                        label = { Text("Your Experience with REHMAT DAIRY FARM *") },
                        minLines = 3,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (reviewerName.isNotBlank() && reviewComment.isNotBlank()) {
                                onSubmitReview(reviewerName, reviewerCity, reviewRating, reviewComment)
                                reviewerName = ""
                                reviewerCity = ""
                                reviewComment = ""
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
                            .testTag("submit_review_btn")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Submit Review", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Approved Reviews List
        items(reviews) { review ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = FarmSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = review.customerName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = FarmDarkGreen
                            )
                            Text(
                                text = review.city,
                                fontSize = 11.sp,
                                color = FarmTextSecondary
                            )
                        }
                        RatingStars(rating = review.rating)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "“${review.reviewText}”",
                        fontSize = 12.sp,
                        color = FarmTextPrimary,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // SEO & Quality Stamp Panel
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FarmLightGreen)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "About Rehmat Dairy Farm Pakistan",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmDarkGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Rehmat Dairy Farm is recognized as a trusted provider of pure cow milk, fresh dairy products, organic desi ghee, and farm fresh milk subscriptions across Pakistan. We uphold zero adulteration, cold-chain hygienic milking, and eco-friendly livestock practices.",
                        fontSize = 10.sp,
                        color = FarmTextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ContactDetailRow(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(FarmLightGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = FarmFreshGreen,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 11.sp, color = FarmTextSecondary)
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (onClick != null) FarmFreshGreen else FarmDarkGreen
            )
        }
    }
}

@Composable
fun SocialBadge(name: String, color: Color, onClick: () -> Unit) {
    Surface(
        color = color,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = name,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
