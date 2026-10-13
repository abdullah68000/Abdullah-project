package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val unit: String, // "Liter", "KG", "500g", etc.
    val category: String, // "Milk", "Desi Ghee", "Yogurt / Dahi", "Butter", "Cream", "Other"
    val imageResName: String,
    val isAvailable: Boolean = true,
    val isFeatured: Boolean = false
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderNumber: String,
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String,
    val deliveryOption: String,
    val itemsSummary: String,
    val totalAmount: Double,
    val status: String, // "New", "Confirmed", "Preparing", "Out for Delivery", "Delivered", "Cancelled"
    val orderDate: Long,
    val notes: String = ""
)

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String,
    val frequency: String, // "Daily", "Weekly", "Monthly"
    val quantityLiters: Double,
    val deliveryTime: String, // "Morning (6:00 AM - 8:00 AM)" or "Evening (5:00 PM - 7:00 PM)"
    val startDate: String,
    val estimatedCost: Double,
    val status: String = "Active", // "Active", "Paused", "Cancelled"
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerName: String,
    val city: String,
    val rating: Int,
    val reviewText: String,
    val isApproved: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "gallery_items")
data class GalleryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "Cows", "Calves", "Farm", "Milking", "Dairy Products", "Daily Farm Activities"
    val description: String,
    val imageResName: String
)

@Entity(tableName = "farm_config")
data class FarmConfigEntity(
    @PrimaryKey val key: String,
    val value: String
)
