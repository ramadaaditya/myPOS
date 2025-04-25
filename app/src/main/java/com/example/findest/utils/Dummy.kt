package com.example.findest.utils

import com.example.findest.data.model.Product
import com.example.findest.data.model.ProductInCart
import com.example.findest.data.remote.response.ProductsItem


val dummyProducts = listOf(
    Product(
        id = 1,
        name = "Gaming Laptop X",
        imageUrl = "https://example.com/laptop.jpg",
        categoryName = "Electronics",
        price = 1000,
        description = "High-performance gaming laptop with superior cooling system.",
        color = "Black",
        brand = "Asus",
        discount = 10
    ),
    Product(
        id = 2,
        name = "Wireless Headphones",
        imageUrl = "https://example.com/headphone.jpg",
        categoryName = "Accessories",
        price = 80,
        description = "Wireless headphones with clear sound and powerful bass.",
        color = "White",
        brand = "Sony",
        discount = 5
    ),
    Product(
        id = 3,
        name = "Smartphone Z",
        imageUrl = "https://example.com/smartphone.jpg",
        categoryName = "Electronics",
        price = 567,
        description = "Smartphone with top-tier camera and high performance.",
        color = "Blue",
        brand = "Samsung",
        discount = 15
    ),
    Product(
        id = 4,
        name = "RGB Gaming Mouse",
        imageUrl = "https://example.com/mouse.jpg",
        categoryName = "Accessories",
        price = 23,
        description = "Gaming mouse with RGB lighting and high DPI sensitivity.",
        color = "Black",
        brand = "Razer",
        discount = 20
    ),
    Product(
        id = 5,
        name = "27-inch Monitor",
        imageUrl = "https://example.com/monitor.jpg",
        categoryName = "Electronics",
        price = 213,
        description = "27-inch monitor with high resolution for professional work.",
        color = "Silver",
        brand = "Dell",
        discount = 7
    )
)

val dummyProductsInCart = listOf(
    ProductInCart(product = dummyProducts[0], quantity = 1),
    ProductInCart(product = dummyProducts[1], quantity = 2),
    ProductInCart(product = dummyProducts[3], quantity = 1)
)

val dummyProductsItem = listOf(
    ProductsItem(
        id = 1,
        title = "Gaming Laptop X",
        image = "https://example.com/laptop.jpg",
        category = "Electronics",
        price = 1000,
        description = "High-performance gaming laptop with superior cooling system.",
        color = "Black",
        brand = "Asus",
        discount = 10
    ),
    ProductsItem(
        id = 2,
        title = "Wireless Headphones",
        image = "https://example.com/headphone.jpg",
        category = "Accessories",
        price = 80,
        description = "Wireless headphones with clear sound and powerful bass.",
        color = "White",
        brand = "Sony",
        discount = 5
    ),
    ProductsItem(
        id = 3,
        title = "Smartphone Z",
        image = "https://example.com/smartphone.jpg",
        category = "Electronics",
        price = 567,
        description = "Smartphone with top-tier camera and high performance.",
        color = "Blue",
        brand = "Samsung",
        discount = 15
    ),
    ProductsItem(
        id = 4,
        title = "RGB Gaming Mouse",
        image = "https://example.com/mouse.jpg",
        category = "Accessories",
        price = 23,
        description = "Gaming mouse with RGB lighting and high DPI sensitivity.",
        color = "Black",
        brand = "Razer",
        discount = 20
    ),
    ProductsItem(
        id = 5,
        title = "27-inch Monitor",
        image = "https://example.com/monitor.jpg",
        category = "Electronics",
        price = 213,
        description = "27-inch monitor with high resolution for professional work.",
        color = "Silver",
        brand = "Dell",
        discount = 7
    )
)