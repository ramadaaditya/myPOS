package com.example.findest.ui.cart

import com.example.findest.MainDispatcherRule
import com.example.findest.data.repository.ProductRepository
import com.example.findest.utils.UiState
import com.example.findest.utils.dummyProductsInCart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class CartViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var repository: ProductRepository
    private lateinit var viewModel: CartViewModel

    @Before
    fun setup() {
        repository = mock(ProductRepository::class.java)
        `when`(repository.getAllCartItems()).thenReturn(flowOf(dummyProductsInCart))
        viewModel = CartViewModel(repository)
    }

    @Test
    fun `getCartItems returns Success state when items exist`() = runTest {
        advanceUntilIdle()
        assertEquals(UiState.Success(dummyProductsInCart), viewModel.cartItem.value)
    }

    @Test
    fun `getCartItems returns Empty state when no items exist`() = runTest {
        `when`(repository.getAllCartItems()).thenReturn(flowOf(emptyList()))
        viewModel = CartViewModel(repository)
        advanceUntilIdle()
        assertEquals(UiState.Empty, viewModel.cartItem.value)
    }

    @Test
    fun `increaseQuantity calls insertCartItem with increased quantity`() = runTest {
        val item = dummyProductsInCart[0]
        val expected = item.copy(quantity = item.quantity + 1)

        viewModel.increaseQuantity(item)
        advanceUntilIdle()

        verify(repository).insertCartItem(expected)
    }

    @Test
    fun `decreaseQuantity with quantity greater than 1 updates item`() = runTest {
        val item = dummyProductsInCart[0].copy(quantity = 2)
        val expected = item.copy(quantity = 1)

        viewModel.decreaseQuantity(item)
        advanceUntilIdle()

        verify(repository).insertCartItem(expected)
    }

    @Test
    fun `decreaseQuantity with quantity 1 deletes item`() = runTest {
        val item = dummyProductsInCart[0].copy(quantity = 1)

        viewModel.decreaseQuantity(item)
        advanceUntilIdle()

        verify(repository).deleteCartItem(item)
    }

    @Test
    fun `checkout deletes all cart items`() = runTest {
        viewModel.checkout()
        advanceUntilIdle()

        verify(repository).deleteAllCartItem()
    }

    @Test
    fun `getCartItems sets Error state when repository throws`() = runTest {
        `when`(repository.getAllCartItems()).thenReturn(
            flow { throw RuntimeException("Database error") }
        )

        viewModel = CartViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.cartItem.value
        assert(state is UiState.Error && state.message == "Database error")
    }
}