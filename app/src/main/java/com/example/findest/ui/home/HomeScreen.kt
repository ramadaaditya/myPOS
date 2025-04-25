package com.example.findest.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.findest.ui.component.CategoryRow
import com.example.findest.ui.component.EmptyState
import com.example.findest.ui.component.ErrorState
import com.example.findest.ui.component.LoadingState
import com.example.findest.ui.component.ProductCard
import com.example.findest.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    onDetailClick: (Int) -> Unit
) {
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()

    val refreshState by viewModel.refreshState.collectAsStateWithLifecycle()
    val isRefreshing by remember {
        derivedStateOf { refreshState is UiState.Loading }
    }
    val categories = listOf("All", "Audio", "TV", "Mobile", "Laptop", "Gaming", "Appliances")

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        CategoryRow(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { selected ->
                viewModel.setCategory(selected)
            },
            modifier = Modifier.padding(16.dp)
        )
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                viewModel.refreshProductsFromRemote {
                    viewModel.filterProductsByCategory(selectedCategory)
                }
            }
        ) {
            when (val state = filteredProducts) {
                is UiState.Loading -> LoadingState()
                is UiState.Empty -> EmptyState()
                is UiState.Error -> ErrorState(message = state.message)
                is UiState.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        items(state.data) { product ->
                            ProductCard(product = product, onClick = { onDetailClick(product.id) })
                        }
                    }
                }
            }
        }
    }
}