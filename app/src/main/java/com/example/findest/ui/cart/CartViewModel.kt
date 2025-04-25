package com.example.findest.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findest.model.ProductInCart
import com.example.findest.model.repository.ProductRepository
import com.example.findest.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    private val _cartItems = MutableStateFlow<UiState<List<ProductInCart>>>(UiState.Empty)
    val cartItem: StateFlow<UiState<List<ProductInCart>>> = _cartItems

    init {
        getCartItems()
    }

    fun increaseQuantity(item: ProductInCart) {
        val newQuantity = item.quantity + 1
        updateCart(item, newQuantity)
    }

    fun decreaseQuantity(item: ProductInCart) {
        val newQuantity = item.quantity - 1
        if (newQuantity <= 0) {
            viewModelScope.launch {
                repository.deleteCartItem(item)
                getCartItems()
            }
        } else {
            updateCart(item, newQuantity)
        }
    }

    private fun updateCart(item: ProductInCart, newQuantity: Int) {
        viewModelScope.launch {
            val updatedItem = item.copy(quantity = newQuantity)
            repository.insertCartItem(updatedItem)
            getCartItems()
        }
    }

    fun checkout() {
        viewModelScope.launch {
            repository.deleteAllCartItem()
            getCartItems()
        }
    }

    private fun getCartItems() {
        viewModelScope.launch {
            repository.getAllCartItems()
                .catch {
                    _cartItems.value = UiState.Error(it.message ?: "Error")
                }
                .collect {
                    _cartItems.value = UiState.Success(it)
                }
        }
    }
}