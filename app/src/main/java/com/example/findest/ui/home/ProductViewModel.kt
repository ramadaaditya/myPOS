package com.example.findest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findest.model.Product
import com.example.findest.model.ProductInCart
import com.example.findest.model.repository.ProductRepository
import com.example.findest.utils.NetworkChecker
import com.example.findest.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    val allProducts: StateFlow<UiState<List<Product>>> = repository.getAllProductsFromLocal()
        .map { products ->
            if (products.isEmpty()) UiState.Empty else UiState.Success(products)
        }
        .catch { emit(UiState.Error(it.message ?: "Terjadi kesalahan saat memuat produk")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    fun refreshProductsFromRemote() {
        viewModelScope.launch {
            if (!networkChecker.isNetworkAvailable()) {
                _refreshState.value = UiState.Error("Tidak ada koneksi internet")
                return@launch
            }
            _refreshState.value = UiState.Loading
            when (val remote = repository.getAllProducts()) {
                is UiState.Success -> {
                    repository.insertAllProductsToLocal(remote.data)
                    _refreshState.value = UiState.Success(Unit)
                }

                is UiState.Error -> {
                    _refreshState.value = UiState.Error(remote.message)
                }

                else -> Unit
            }
        }
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