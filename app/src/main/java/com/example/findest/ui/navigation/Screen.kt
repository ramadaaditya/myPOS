package com.example.findest.ui.navigation

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home")
    data object CartScreen : Screen("cart")
    data class DetailScreen(val productId: Int) :
        Screen("detail/{productId}") {
        companion object {
            const val ROUTE = "detail/{productId}"
            fun createRoute(productId: Int) = "detail/$productId"
        }
    }
}