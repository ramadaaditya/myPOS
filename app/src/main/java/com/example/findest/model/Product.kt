package com.example.findest.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val categoryName: String,
    val price: Int,
    val description: String,
    val color: String,
    val brand: String,
    val discount: Int
)

@Entity(tableName = "product_in_cart")
data class ProductInCart(
    @PrimaryKey(autoGenerate = true) val productId: Int = 0,
    @Embedded val product: Product,
    val quantity: Int
)