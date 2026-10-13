package com.example.ui.model

import com.example.data.entity.ProductEntity

data class CartItem(
    val product: ProductEntity,
    val quantity: Int
) {
    val totalPrice: Double
        get() = product.price * quantity
}
