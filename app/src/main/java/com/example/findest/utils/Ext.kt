package com.example.findest.utils

import com.example.findest.model.Product
import com.example.findest.model.remote.response.ProductsItem
import com.example.findest.ui.navigation.Screen

fun ProductsItem.toProductEntity(): Product {
    return Product(
        id = this.id ?: 0,
        name = this.title.orEmpty(),
        imageUrl = this.image.orEmpty(),
        categoryName = this.category.orEmpty(),
        price = this.price ?: 0,
        discount = this.discount ?: 0,
        description = this.description.orEmpty(),
        color = this.color.orEmpty(),
        brand = this.brand.orEmpty()

    )
}

fun shouldShowBottomBar(route: String?): Boolean {
    return route in listOf(
        Screen.HomeScreen.route,
        Screen.CartScreen.route,
    )
}

fun Product.getDiscountedPrice(): Int {
    return price - (price * discount / 100)
}