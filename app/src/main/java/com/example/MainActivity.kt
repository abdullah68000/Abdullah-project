package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.FarmViewModel
import com.example.ui.components.BrandTopBar
import com.example.ui.components.FarmBottomNavigation
import com.example.ui.components.StickyWhatsAppFab
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.CartCheckoutScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.FarmStoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderConfirmationScreen
import com.example.ui.screens.ProductsScreen
import com.example.ui.screens.SubscriptionScreen
import com.example.ui.theme.RehmatDairyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RehmatDairyTheme {
                RehmatDairyApp()
            }
        }
    }
}

@Composable
fun RehmatDairyApp(viewModel: FarmViewModel = viewModel()) {
    val context = LocalContext.current
    var currentRoute by remember { mutableStateOf("home") }
    val snackbarHostState = remember { SnackbarHostState() }

    // User feedback listener
    val userMessage by viewModel.userMessage.collectAsState()
    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearUserMessage()
        }
    }

    // State collections
    val allProducts by viewModel.allProducts.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val cart by viewModel.cart.collectAsState()
    val cartCount by viewModel.cartItemsCount.collectAsState()
    val cartSubtotal by viewModel.cartSubtotal.collectAsState()
    val deliveryFee by viewModel.deliveryFee.collectAsState()
    val cartTotal by viewModel.cartTotal.collectAsState()

    val lastPlacedOrder by viewModel.lastPlacedOrder.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    val allSubscriptions by viewModel.allSubscriptions.collectAsState()
    val approvedReviews by viewModel.approvedReviews.collectAsState()
    val allReviews by viewModel.allReviews.collectAsState()
    val galleryItems by viewModel.allGalleryItems.collectAsState()
    val configs by viewModel.farmConfigs.collectAsState()
    val isAdminUnlocked by viewModel.isAdminUnlocked.collectAsState()

    // Handle back button behavior
    BackHandler(enabled = currentRoute != "home") {
        if (currentRoute == "order_confirmation" || currentRoute == "cart" || currentRoute == "admin") {
            currentRoute = "home"
        } else {
            currentRoute = "home"
        }
    }

    val isTopLevelScreen = currentRoute in listOf("home", "products", "subscription", "farm", "contact")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentRoute != "admin") {
                BrandTopBar(
                    cartCount = cartCount,
                    onCartClick = { currentRoute = "cart" },
                    onAdminClick = { currentRoute = "admin" }
                )
            }
        },
        bottomBar = {
            if (isTopLevelScreen) {
                FarmBottomNavigation(
                    currentRoute = currentRoute,
                    onNavigate = { route -> currentRoute = route }
                )
            }
        },
        floatingActionButton = {
            if (isTopLevelScreen) {
                StickyWhatsAppFab(
                    onClick = {
                        viewModel.openGeneralWhatsApp(context)
                    }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRoute) {
                "home" -> {
                    HomeScreen(
                        products = allProducts,
                        reviews = approvedReviews,
                        cartMap = cart,
                        onAddToCart = { product, qty -> viewModel.addToCart(product, qty) },
                        onNavigateToProducts = { currentRoute = "products" },
                        onNavigateToSubscription = { currentRoute = "subscription" },
                        onNavigateToContact = { currentRoute = "contact" },
                        onNavigateToFarm = { currentRoute = "farm" },
                        onWhatsAppClick = { viewModel.openGeneralWhatsApp(context) }
                    )
                }

                "products" -> {
                    ProductsScreen(
                        products = filteredProducts,
                        selectedCategory = selectedCategory,
                        searchQuery = searchQuery,
                        cartMap = cart,
                        onSelectCategory = { viewModel.setCategory(it) },
                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                        onAddToCart = { product, delta -> viewModel.addToCart(product, delta) },
                        onViewCartClick = { currentRoute = "cart" }
                    )
                }

                "subscription" -> {
                    val milkPrice = configs["milk_price_per_liter"]?.toDoubleOrNull() ?: 240.0
                    SubscriptionScreen(
                        milkPricePerLiter = milkPrice,
                        subscriptions = allSubscriptions,
                        onSubscribe = { name, phone, address, freq, qty, slot, startDate, cost, shareWhatsApp ->
                            viewModel.createSubscription(
                                customerName = name,
                                customerPhone = phone,
                                deliveryAddress = address,
                                frequency = freq,
                                quantityLiters = qty,
                                deliveryTime = slot,
                                startDate = startDate,
                                estimatedCost = cost,
                                onSuccess = { sub ->
                                    if (shareWhatsApp) {
                                        viewModel.openWhatsAppSubscription(context, sub)
                                    }
                                }
                            )
                        }
                    )
                }

                "farm" -> {
                    FarmStoryScreen(galleryItems = galleryItems)
                }

                "contact" -> {
                    ContactScreen(
                        configs = configs,
                        reviews = approvedReviews,
                        onCallClick = {
                            val phone = configs["contact_phone"] ?: "+923001234567"
                            viewModel.dialPhone(context, phone)
                        },
                        onWhatsAppClick = {
                            viewModel.openGeneralWhatsApp(context)
                        },
                        onDirectionsClick = {
                            val addr = configs["farm_address"] ?: "Rehmat Dairy Farm, Raiwind Road, Lahore"
                            viewModel.openMaps(context, addr)
                        },
                        onSocialClick = { url ->
                            viewModel.openSocialUrl(context, url)
                        },
                        onSubmitReview = { name, city, rating, comment ->
                            viewModel.submitReview(name, city, rating, comment)
                        }
                    )
                }

                "cart" -> {
                    CartCheckoutScreen(
                        cartMap = cart,
                        subtotal = cartSubtotal,
                        deliveryFee = deliveryFee,
                        total = cartTotal,
                        onUpdateQuantity = { id, delta -> viewModel.updateCartQuantity(id, delta) },
                        onRemoveItem = { id -> viewModel.removeFromCart(id) },
                        onClearCart = { viewModel.clearCart() },
                        onPlaceOrder = { name, phone, address, option, notes ->
                            viewModel.placeOrder(
                                customerName = name,
                                customerPhone = phone,
                                deliveryAddress = address,
                                deliveryOption = option,
                                notes = notes,
                                onOrderSuccess = {
                                    currentRoute = "order_confirmation"
                                }
                            )
                        },
                        onOrderWhatsAppDirect = { name, phone, address, option ->
                            viewModel.placeOrder(
                                customerName = name,
                                customerPhone = phone,
                                deliveryAddress = address,
                                deliveryOption = option,
                                notes = "Ordered directly via WhatsApp",
                                onOrderSuccess = { order ->
                                    viewModel.openWhatsAppOrder(context, order)
                                    currentRoute = "order_confirmation"
                                }
                            )
                        },
                        onContinueShopping = { currentRoute = "products" }
                    )
                }

                "order_confirmation" -> {
                    lastPlacedOrder?.let { order ->
                        OrderConfirmationScreen(
                            order = order,
                            onWhatsAppShare = { viewModel.openWhatsAppOrder(context, order) },
                            onBackToHome = { currentRoute = "home" }
                        )
                    } ?: run {
                        currentRoute = "home"
                    }
                }

                "admin" -> {
                    AdminScreen(
                        isAdminUnlocked = isAdminUnlocked,
                        orders = allOrders,
                        subscriptions = allSubscriptions,
                        products = allProducts,
                        galleryItems = galleryItems,
                        reviews = allReviews,
                        configs = configs,
                        onUnlock = { pin -> viewModel.unlockAdmin(pin) },
                        onLock = { viewModel.lockAdmin() },
                        onUpdateOrderStatus = { orderNo, status -> viewModel.updateOrderStatus(orderNo, status) },
                        onUpdateSubStatus = { id, status -> viewModel.updateSubscriptionStatus(id, status) },
                        onSaveProduct = { id, name, desc, price, unit, cat, img, avail, feat ->
                            viewModel.saveProduct(id, name, desc, price, unit, cat, img, avail, feat)
                        },
                        onDeleteProduct = { product -> viewModel.deleteProduct(product) },
                        onAddGalleryItem = { title, cat, desc, img -> viewModel.addGalleryItem(title, cat, desc, img) },
                        onDeleteGalleryItem = { item -> viewModel.deleteGalleryItem(item) },
                        onUpdateReviewApproval = { id, isAppr -> viewModel.updateReviewApproval(id, isAppr) },
                        onDeleteReview = { review -> viewModel.deleteReview(review) },
                        onUpdateConfig = { key, value -> viewModel.updateConfig(key, value) },
                        onClose = { currentRoute = "home" }
                    )
                }
            }
        }
    }
}
