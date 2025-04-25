package com.example.findest

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.findest.ui.cart.CartScreen
import com.example.findest.ui.component.BottomNavigation
import com.example.findest.ui.detail.DetailScreen
import com.example.findest.ui.home.HomeScreen
import com.example.findest.ui.navigation.Screen
import com.example.findest.ui.theme.FindestTheme
import com.example.findest.utils.shouldShowBottomBar

@Composable
fun FindestNavApp() {
    FindestTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = remember(navBackStackEntry) { navBackStackEntry?.destination?.route }
        Scaffold(
            bottomBar = {
                if (shouldShowBottomBar(currentRoute)) {
                    BottomNavigation(navController = navController, currentRoute = currentRoute)
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.HomeScreen.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.HomeScreen.route) {
                    HomeScreen(
                        onDetailClick = { productId ->
                            val route = Screen.DetailScreen.createRoute(productId)
                            navController.navigate(route)
                        }
                    )
                }
                composable(Screen.CartScreen.route) {
                    CartScreen(
                        navigateToDetail = { productId ->
                            val route = Screen.DetailScreen.createRoute(productId)
                            navController.navigate(route)
                        }
                    )

                }
                composable(
                    route = Screen.DetailScreen.ROUTE,
                    arguments = listOf(navArgument("productId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                    DetailScreen(productId = productId)
                }

            }
        }
    }
}