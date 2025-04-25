package com.example.findest.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val icon: ImageVector,
    val screen: String,
    val contentDescription: String,
    val title: String
)

internal val navigationItem = listOf(
    NavigationItem(
        title = "Home",
        icon = Icons.Default.Home,
        screen = Screen.HomeScreen.route,
        contentDescription = Screen.HomeScreen.route
    ),
    NavigationItem(
        title = "Cart",
        icon = Icons.Default.ShoppingCart,
        screen = Screen.CartScreen.route,
        contentDescription = Screen.CartScreen.route
    )
)