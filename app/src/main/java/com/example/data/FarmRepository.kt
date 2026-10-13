package com.example.data

import com.example.data.entity.FarmConfigEntity
import com.example.data.entity.GalleryItemEntity
import com.example.data.entity.OrderEntity
import com.example.data.entity.ProductEntity
import com.example.data.entity.ReviewEntity
import com.example.data.entity.SubscriptionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FarmRepository(private val database: AppDatabase) {

    // Products
    val allProducts: Flow<List<ProductEntity>> = database.productDao().getAllProducts()

    suspend fun getProductById(id: Int): ProductEntity? = database.productDao().getProductById(id)

    suspend fun insertProduct(product: ProductEntity): Long = database.productDao().insertProduct(product)

    suspend fun updateProduct(product: ProductEntity) = database.productDao().updateProduct(product)

    suspend fun deleteProduct(product: ProductEntity) = database.productDao().deleteProduct(product)

    // Orders
    val allOrders: Flow<List<OrderEntity>> = database.orderDao().getAllOrders()

    suspend fun getOrderByNumber(orderNumber: String): OrderEntity? = database.orderDao().getOrderByNumber(orderNumber)

    suspend fun insertOrder(order: OrderEntity) = database.orderDao().insertOrder(order)

    suspend fun updateOrderStatus(orderNumber: String, status: String) =
        database.orderDao().updateOrderStatus(orderNumber, status)

    suspend fun deleteOrder(order: OrderEntity) = database.orderDao().deleteOrder(order)

    // Subscriptions
    val allSubscriptions: Flow<List<SubscriptionEntity>> = database.subscriptionDao().getAllSubscriptions()

    suspend fun insertSubscription(subscription: SubscriptionEntity): Long =
        database.subscriptionDao().insertSubscription(subscription)

    suspend fun updateSubscriptionStatus(id: Int, status: String) =
        database.subscriptionDao().updateSubscriptionStatus(id, status)

    suspend fun deleteSubscription(subscription: SubscriptionEntity) =
        database.subscriptionDao().deleteSubscription(subscription)

    // Reviews
    val approvedReviews: Flow<List<ReviewEntity>> = database.reviewDao().getApprovedReviews()
    val allReviews: Flow<List<ReviewEntity>> = database.reviewDao().getAllReviews()

    suspend fun insertReview(review: ReviewEntity): Long = database.reviewDao().insertReview(review)

    suspend fun updateReviewApproval(id: Int, isApproved: Boolean) =
        database.reviewDao().updateReviewApproval(id, isApproved)

    suspend fun deleteReview(review: ReviewEntity) = database.reviewDao().deleteReview(review)

    // Gallery
    val allGalleryItems: Flow<List<GalleryItemEntity>> = database.galleryDao().getAllGalleryItems()

    suspend fun insertGalleryItem(item: GalleryItemEntity): Long =
        database.galleryDao().insertGalleryItem(item)

    suspend fun deleteGalleryItem(item: GalleryItemEntity) =
        database.galleryDao().deleteGalleryItem(item)

    // Farm Config
    val farmConfigs: Flow<Map<String, String>> = database.farmConfigDao().getAllConfigs().map { list ->
        list.associate { it.key to it.value }
    }

    suspend fun getConfigValue(key: String): String? = database.farmConfigDao().getConfigValue(key)

    suspend fun setConfig(key: String, value: String) =
        database.farmConfigDao().setConfig(FarmConfigEntity(key, value))

    suspend fun ensureSeeded() {
        AppDatabase.seedDatabase(database)
    }
}
