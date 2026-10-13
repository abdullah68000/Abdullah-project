package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.FarmConfigEntity
import com.example.data.entity.GalleryItemEntity
import com.example.data.entity.OrderEntity
import com.example.data.entity.ProductEntity
import com.example.data.entity.ReviewEntity
import com.example.data.entity.SubscriptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY isFeatured DESC, id ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: Int): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE orderNumber = :orderNumber LIMIT 1")
    suspend fun getOrderByNumber(orderNumber: String): OrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Query("UPDATE orders SET status = :status WHERE orderNumber = :orderNumber")
    suspend fun updateOrderStatus(orderNumber: String, status: String)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)
}

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM subscriptions ORDER BY createdAt DESC")
    fun getAllSubscriptions(): Flow<List<SubscriptionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscription(subscription: SubscriptionEntity): Long

    @Query("UPDATE subscriptions SET status = :status WHERE id = :id")
    suspend fun updateSubscriptionStatus(id: Int, status: String)

    @Delete
    suspend fun deleteSubscription(subscription: SubscriptionEntity)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE isApproved = 1 ORDER BY createdAt DESC")
    fun getApprovedReviews(): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews ORDER BY createdAt DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reviews: List<ReviewEntity>)

    @Query("UPDATE reviews SET isApproved = :isApproved WHERE id = :id")
    suspend fun updateReviewApproval(id: Int, isApproved: Boolean)

    @Delete
    suspend fun deleteReview(review: ReviewEntity)

    @Query("SELECT COUNT(*) FROM reviews")
    suspend fun getReviewCount(): Int
}

@Dao
interface GalleryDao {
    @Query("SELECT * FROM gallery_items ORDER BY id DESC")
    fun getAllGalleryItems(): Flow<List<GalleryItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGalleryItem(item: GalleryItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GalleryItemEntity>)

    @Delete
    suspend fun deleteGalleryItem(item: GalleryItemEntity)

    @Query("SELECT COUNT(*) FROM gallery_items")
    suspend fun getGalleryCount(): Int
}

@Dao
interface FarmConfigDao {
    @Query("SELECT * FROM farm_config")
    fun getAllConfigs(): Flow<List<FarmConfigEntity>>

    @Query("SELECT value FROM farm_config WHERE `key` = :key LIMIT 1")
    suspend fun getConfigValue(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setConfig(config: FarmConfigEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setAllConfigs(configs: List<FarmConfigEntity>)

    @Query("SELECT COUNT(*) FROM farm_config")
    suspend fun getConfigCount(): Int
}
