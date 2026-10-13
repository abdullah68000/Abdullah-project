package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.FarmRepository
import com.example.data.entity.GalleryItemEntity
import com.example.data.entity.OrderEntity
import com.example.data.entity.ProductEntity
import com.example.data.entity.ReviewEntity
import com.example.data.entity.SubscriptionEntity
import com.example.ui.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FarmViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FarmRepository

    init {
        val db = AppDatabase.getDatabase(application, viewModelScope)
        repository = FarmRepository(db)
        viewModelScope.launch {
            repository.ensureSeeded()
        }
    }

    // --- Products & Filtering ---
    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredProducts: StateFlow<List<ProductEntity>> = combine(
        allProducts,
        _selectedCategory,
        _searchQuery
    ) { products, category, query ->
        products.filter { product ->
            val matchesCategory = (category == "All" || product.category.equals(category, ignoreCase = true))
            val matchesQuery = query.isBlank() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // --- Cart Management ---
    private val _cart = MutableStateFlow<Map<Int, CartItem>>(emptyMap())
    val cart: StateFlow<Map<Int, CartItem>> = _cart.asStateFlow()

    val cartItemsCount: StateFlow<Int> = _cart.combine(_cart) { map, _ ->
        map.values.sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val cartSubtotal: StateFlow<Double> = _cart.combine(_cart) { map, _ ->
        map.values.sumOf { it.totalPrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Farm Configs
    val farmConfigs: StateFlow<Map<String, String>> = repository.farmConfigs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val deliveryFee: StateFlow<Double> = combine(cartSubtotal, farmConfigs) { subtotal, configs ->
        if (subtotal == 0.0) return@combine 0.0
        val threshold = configs["free_delivery_threshold"]?.toDoubleOrNull() ?: 1500.0
        val standardFee = configs["delivery_charge"]?.toDoubleOrNull() ?: 100.0
        if (subtotal >= threshold) 0.0 else standardFee
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartTotal: StateFlow<Double> = combine(cartSubtotal, deliveryFee) { subtotal, fee ->
        if (subtotal == 0.0) 0.0 else subtotal + fee
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun addToCart(product: ProductEntity, quantity: Int = 1) {
        val current = _cart.value.toMutableMap()
        val existing = current[product.id]
        if (existing != null) {
            current[product.id] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            current[product.id] = CartItem(product, quantity)
        }
        _cart.value = current
        showToast("${product.name} added to cart!")
    }

    fun updateCartQuantity(productId: Int, delta: Int) {
        val current = _cart.value.toMutableMap()
        val existing = current[productId] ?: return
        val newQty = existing.quantity + delta
        if (newQty <= 0) {
            current.remove(productId)
        } else {
            current[productId] = existing.copy(quantity = newQty)
        }
        _cart.value = current
    }

    fun removeFromCart(productId: Int) {
        val current = _cart.value.toMutableMap()
        current.remove(productId)
        _cart.value = current
    }

    fun clearCart() {
        _cart.value = emptyMap()
    }

    // --- Order Checkout ---
    private val _lastPlacedOrder = MutableStateFlow<OrderEntity?>(null)
    val lastPlacedOrder: StateFlow<OrderEntity?> = _lastPlacedOrder.asStateFlow()

    fun placeOrder(
        customerName: String,
        customerPhone: String,
        deliveryAddress: String,
        deliveryOption: String,
        notes: String = "",
        onOrderSuccess: (OrderEntity) -> Unit
    ) {
        if (_cart.value.isEmpty()) {
            showToast("Your cart is empty")
            return
        }
        if (customerName.isBlank() || customerPhone.isBlank() || deliveryAddress.isBlank()) {
            showToast("Please fill in all contact and delivery details")
            return
        }

        viewModelScope.launch {
            val randomDigits = (10000..99999).random()
            val orderNo = "RDF-$randomDigits"
            val itemsSummary = _cart.value.values.joinToString(", ") {
                "${it.product.name} (${it.quantity}x)"
            }
            val total = cartTotal.value

            val newOrder = OrderEntity(
                orderNumber = orderNo,
                customerName = customerName.trim(),
                customerPhone = customerPhone.trim(),
                deliveryAddress = deliveryAddress.trim(),
                deliveryOption = deliveryOption,
                itemsSummary = itemsSummary,
                totalAmount = total,
                status = "New",
                orderDate = System.currentTimeMillis(),
                notes = notes.trim()
            )

            repository.insertOrder(newOrder)
            _lastPlacedOrder.value = newOrder
            clearCart()
            showToast("Order $orderNo placed successfully!")
            onOrderSuccess(newOrder)
        }
    }

    // Orders list for Order History & Admin
    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateOrderStatus(orderNumber: String, status: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderNumber, status)
            showToast("Order $orderNumber status updated to $status")
        }
    }

    // --- Daily Milk Subscription ---
    val allSubscriptions: StateFlow<List<SubscriptionEntity>> = repository.allSubscriptions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createSubscription(
        customerName: String,
        customerPhone: String,
        deliveryAddress: String,
        frequency: String,
        quantityLiters: Double,
        deliveryTime: String,
        startDate: String,
        estimatedCost: Double,
        onSuccess: (SubscriptionEntity) -> Unit
    ) {
        if (customerName.isBlank() || customerPhone.isBlank() || deliveryAddress.isBlank()) {
            showToast("Please complete all subscription fields")
            return
        }

        viewModelScope.launch {
            val subscription = SubscriptionEntity(
                customerName = customerName.trim(),
                customerPhone = customerPhone.trim(),
                deliveryAddress = deliveryAddress.trim(),
                frequency = frequency,
                quantityLiters = quantityLiters,
                deliveryTime = deliveryTime,
                startDate = startDate,
                estimatedCost = estimatedCost,
                status = "Active"
            )
            repository.insertSubscription(subscription)
            showToast("Milk Subscription started successfully!")
            onSuccess(subscription)
        }
    }

    fun updateSubscriptionStatus(id: Int, status: String) {
        viewModelScope.launch {
            repository.updateSubscriptionStatus(id, status)
            showToast("Subscription status updated to $status")
        }
    }

    // --- Reviews ---
    val approvedReviews: StateFlow<List<ReviewEntity>> = repository.approvedReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReviews: StateFlow<List<ReviewEntity>> = repository.allReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun submitReview(name: String, city: String, rating: Int, comment: String) {
        if (name.isBlank() || comment.isBlank()) {
            showToast("Please enter your name and review")
            return
        }
        viewModelScope.launch {
            val review = ReviewEntity(
                customerName = name.trim(),
                city = city.ifBlank { "Pakistan" }.trim(),
                rating = rating.coerceIn(1, 5),
                reviewText = comment.trim(),
                isApproved = true // Auto-approved for customer delight
            )
            repository.insertReview(review)
            showToast("Thank you! Your review has been submitted.")
        }
    }

    fun updateReviewApproval(id: Int, isApproved: Boolean) {
        viewModelScope.launch {
            repository.updateReviewApproval(id, isApproved)
            showToast("Review approval updated")
        }
    }

    fun deleteReview(review: ReviewEntity) {
        viewModelScope.launch {
            repository.deleteReview(review)
            showToast("Review deleted")
        }
    }

    // --- Gallery ---
    val allGalleryItems: StateFlow<List<GalleryItemEntity>> = repository.allGalleryItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addGalleryItem(title: String, category: String, description: String, imageResName: String) {
        viewModelScope.launch {
            val item = GalleryItemEntity(
                title = title.trim(),
                category = category,
                description = description.trim(),
                imageResName = imageResName
            )
            repository.insertGalleryItem(item)
            showToast("Gallery item added")
        }
    }

    fun deleteGalleryItem(item: GalleryItemEntity) {
        viewModelScope.launch {
            repository.deleteGalleryItem(item)
            showToast("Gallery item removed")
        }
    }

    // --- Product Admin ---
    fun saveProduct(
        id: Int = 0,
        name: String,
        description: String,
        price: Double,
        unit: String,
        category: String,
        imageResName: String,
        isAvailable: Boolean,
        isFeatured: Boolean
    ) {
        if (name.isBlank() || price <= 0) {
            showToast("Please enter a valid product name and price")
            return
        }
        viewModelScope.launch {
            val product = ProductEntity(
                id = id,
                name = name.trim(),
                description = description.trim(),
                price = price,
                unit = unit.trim(),
                category = category,
                imageResName = imageResName,
                isAvailable = isAvailable,
                isFeatured = isFeatured
            )
            if (id == 0) {
                repository.insertProduct(product)
                showToast("Product added successfully")
            } else {
                repository.updateProduct(product)
                showToast("Product updated successfully")
            }
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.deleteProduct(product)
            showToast("Product deleted")
        }
    }

    // --- Farm Config & Admin Settings ---
    fun updateConfig(key: String, value: String) {
        viewModelScope.launch {
            repository.setConfig(key, value)
            showToast("Setting saved")
        }
    }

    // Admin Auth
    private val _isAdminUnlocked = MutableStateFlow(false)
    val isAdminUnlocked: StateFlow<Boolean> = _isAdminUnlocked.asStateFlow()

    fun unlockAdmin(pin: String): Boolean {
        val configuredPin = farmConfigs.value["admin_pin"] ?: "1234"
        return if (pin == configuredPin) {
            _isAdminUnlocked.value = true
            showToast("Admin access granted")
            true
        } else {
            showToast("Incorrect Admin PIN")
            false
        }
    }

    fun lockAdmin() {
        _isAdminUnlocked.value = false
        showToast("Admin session locked")
    }

    // --- User Feedback Messages ---
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    fun showToast(message: String) {
        _userMessage.value = message
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    // --- WhatsApp & Dial Intents Helper ---
    fun openWhatsAppOrder(context: Context, order: OrderEntity) {
        val whatsAppNumber = farmConfigs.value["whatsapp_number"] ?: "+923001234567"
        val cleanNumber = whatsAppNumber.replace("+", "").replace("-", "").replace(" ", "")

        val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(order.orderDate))

        val message = """
*🥛 REHMAT DAIRY FARM - ONLINE ORDER*
*Fresh Milk, Pure Trust*
----------------------------------------
*Order Number:* ${order.orderNumber}
*Date:* $dateStr

*Customer Details:*
• *Name:* ${order.customerName}
• *Phone:* ${order.customerPhone}
• *Address:* ${order.deliveryAddress}
• *Delivery Slot:* ${order.deliveryOption}

*Selected Products:*
${order.itemsSummary}

*Total Payable:* Rs. ${order.totalAmount.toInt()}
----------------------------------------
Please confirm my order. Thank you!
        """.trimIndent()

        launchWhatsAppIntent(context, cleanNumber, message)
    }

    fun openWhatsAppSubscription(context: Context, subscription: SubscriptionEntity) {
        val whatsAppNumber = farmConfigs.value["whatsapp_number"] ?: "+923001234567"
        val cleanNumber = whatsAppNumber.replace("+", "").replace("-", "").replace(" ", "")

        val message = """
*🥛 REHMAT DAIRY FARM - MILK SUBSCRIPTION*
*Fresh Milk, Pure Trust*
----------------------------------------
*Customer Name:* ${subscription.customerName}
*Phone:* ${subscription.customerPhone}
*Address:* ${subscription.deliveryAddress}

*Subscription Details:*
• *Frequency:* ${subscription.frequency}
• *Quantity:* ${subscription.quantityLiters} Liter(s)
• *Delivery Time:* ${subscription.deliveryTime}
• *Start Date:* ${subscription.startDate}
• *Estimated Cost:* Rs. ${subscription.estimatedCost.toInt()}

Please activate my fresh milk subscription. Thank you!
        """.trimIndent()

        launchWhatsAppIntent(context, cleanNumber, message)
    }

    fun openGeneralWhatsApp(context: Context, query: String = "Hello REHMAT DAIRY FARM, I want to inquire about pure fresh milk and dairy products.") {
        val whatsAppNumber = farmConfigs.value["whatsapp_number"] ?: "+923001234567"
        val cleanNumber = whatsAppNumber.replace("+", "").replace("-", "").replace(" ", "")
        launchWhatsAppIntent(context, cleanNumber, query)
    }

    private fun launchWhatsAppIntent(context: Context, cleanNumber: String, message: String) {
        try {
            val encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanNumber&text=$encodedMessage")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            showToast("Could not open WhatsApp: ${e.localizedMessage}")
        }
    }

    fun dialPhone(context: Context, phone: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            showToast("Could not open dialer")
        }
    }

    fun openMaps(context: Context, address: String) {
        try {
            val encoded = URLEncoder.encode(address, StandardCharsets.UTF_8.toString())
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$encoded")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to web maps
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=${URLEncoder.encode(address, "UTF-8")}")).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            } catch (_: Exception) {
                showToast("Could not open maps")
            }
        }
    }

    fun openSocialUrl(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            showToast("Could not open link")
        }
    }
}
