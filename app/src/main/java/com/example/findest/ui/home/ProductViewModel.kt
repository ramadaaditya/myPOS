package com.example.findest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findest.data.model.Product
import com.example.findest.data.model.ProductInCart
import com.example.findest.data.repository.ProductRepository
import com.example.findest.utils.NetworkChecker
import com.example.findest.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val networkChecker: NetworkChecker
) : ViewModel() {
    private val _detail = MutableStateFlow<UiState<Product>>(UiState.Empty)
    val detail: StateFlow<UiState<Product>> = _detail

    private val _refreshState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val refreshState: StateFlow<UiState<Unit>> = _refreshState

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _filteredProducts = MutableStateFlow<UiState<List<Product>>>(UiState.Empty)
    val filteredProducts: StateFlow<UiState<List<Product>>> = _filteredProducts

    init {
        loadLocalProductsAndFilter("All")
        refreshProductsFromRemote(showLoading = false) {
            filterProductsByCategory(_selectedCategory.value)
        }
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
        filterProductsByCategory(category)
    }

    fun refreshProductsFromRemote(
        showLoading: Boolean = true,
        onSuccess: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            if (!networkChecker.isNetworkAvailable()) {
                _refreshState.value = UiState.Error("Tidak ada koneksi internet")
                return@launch
            }

            if (showLoading) _refreshState.value = UiState.Loading

            when (val remote = repository.getAllProducts()) {
                is UiState.Success -> {
                    repository.insertAllProductsToLocal(remote.data)
                    _refreshState.value = UiState.Success(Unit)
                    onSuccess?.invoke()
                }

                is UiState.Error -> _refreshState.value = UiState.Error(remote.message)
                else -> Unit
            }
        }
    }

    private fun loadLocalProductsAndFilter(category: String) {
        _filteredProducts.value = UiState.Loading
        viewModelScope.launch {
            repository.getAllProductsFromLocal()
                .map { products ->
                    val filtered = if (category == "All") products
                    else products.filter { it.categoryName.equals(category, ignoreCase = true) }

                    if (filtered.isEmpty()) UiState.Empty else UiState.Success(filtered)
                }
                .catch { emit(UiState.Error(it.message ?: "Gagal memuat produk")) }
                .collect {
                    _filteredProducts.value = it
                }
        }
    }

    fun filterProductsByCategory(category: String) {
        loadLocalProductsAndFilter(category)
    }


    fun getDetailProduct(id: Int) {
        viewModelScope.launch {
            _detail.value = UiState.Loading
            _detail.value = repository.getDetailProduct(id)
        }
    }

    fun addToCart(product: Product, quantity: Int) {
        viewModelScope.launch {
            val productInCart = ProductInCart(product = product, quantity = quantity)
            repository.insertCartItem(productInCart)
        }
    }
}