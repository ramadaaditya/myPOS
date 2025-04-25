package com.example.findest.ui.home

import com.example.findest.MainDispatcherRule
import com.example.findest.data.model.ProductInCart
import com.example.findest.data.repository.ProductRepository
import com.example.findest.utils.NetworkChecker
import com.example.findest.utils.UiState
import com.example.findest.utils.dummyProducts
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`


@ExperimentalCoroutinesApi
class ProductViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var repository: ProductRepository
    private lateinit var networkChecker: NetworkChecker
    private lateinit var viewModel: ProductViewModel

    @Before
    fun setup() {
        repository = mock(ProductRepository::class.java)
        networkChecker = mock(NetworkChecker::class.java)
        `when`(networkChecker.isNetworkAvailable()).thenReturn(true)
        viewModel = ProductViewModel(repository, networkChecker)
        viewModel.autoInit = false
    }

    @Test
    fun `init loads local products and filters based on default category`() = runTest {
        val allProducts = dummyProducts
        `when`(repository.getAllProductsFromLocal()).thenReturn(flowOf(allProducts))

        viewModel = ProductViewModel(repository, networkChecker)
        viewModel.autoInit = true
        advanceUntilIdle()

        assertEquals(UiState.Success(allProducts), viewModel.filteredProducts.value)
    }

    @Test
    fun `refreshProductsFromRemote emits error when no internet`() = runTest {
        `when`(networkChecker.isNetworkAvailable()).thenReturn(false)

        viewModel.refreshProductsFromRemote()
        advanceUntilIdle()

        assertEquals(UiState.Error("Tidak ada koneksi internet"), viewModel.refreshState.value)
    }

    @Test
    fun `refreshProductsFromRemote emits success when data fetched`() = runTest {
        val expected = dummyProducts
        `when`(repository.getAllProducts()).thenReturn(UiState.Success(expected))

        viewModel.refreshProductsFromRemote()
        advanceUntilIdle()

        assertEquals(UiState.Success(Unit), viewModel.refreshState.value)
        verify(repository).insertAllProductsToLocal(expected)
    }

    @Test
    fun `getDetailProduct updates detail state with success`() = runTest {
        val expected = dummyProducts[0]
        `when`(repository.getDetailProduct(expected.id)).thenReturn(UiState.Success(expected))

        viewModel.getDetailProduct(expected.id)
        advanceUntilIdle()

        assertEquals(UiState.Success(expected), viewModel.detail.value)
    }

    @Test
    fun `getDetailProduct updates detail state with error`() = runTest {
        val productId = 1
        val errorMessage = "Gagal mengambil detail produk"
        `when`(repository.getDetailProduct(productId)).thenReturn(UiState.Error(errorMessage))

        viewModel.getDetailProduct(productId)
        advanceUntilIdle()

        assertEquals(UiState.Error(errorMessage), viewModel.detail.value)
    }

    @Test
    fun `addToCart calls repository with correct data`() = runTest {
        val product = dummyProducts[0]
        val quantity = 2

        viewModel.addToCart(product, quantity)
        verify(repository).insertCartItem(ProductInCart(product = product, quantity = quantity))
    }

    @Test
    fun `setCategory filters products correctly`() = runTest {
        val allProducts = dummyProducts
        `when`(repository.getAllProductsFromLocal()).thenReturn(flowOf(allProducts))

        viewModel.setCategory("Audio")
        advanceUntilIdle()
        assertEquals(UiState.Empty, viewModel.filteredProducts.value)
        assertEquals("Audio", viewModel.selectedCategory.value)
    }

    @Test
    fun `setCategory to All shows all products`() = runTest {
        val allProducts = dummyProducts
        `when`(repository.getAllProductsFromLocal()).thenReturn(flowOf(allProducts))

        viewModel.setCategory("All")
        advanceUntilIdle()

        assertEquals(UiState.Success(allProducts), viewModel.filteredProducts.value)
        assertEquals("All", viewModel.selectedCategory.value)
    }

    @Test
    fun `filterProductsByCategory loads and filters local products`() = runTest {
        val allProducts = dummyProducts
        `when`(repository.getAllProductsFromLocal()).thenReturn(flowOf(allProducts))

        viewModel.filterProductsByCategory("Audio")
        advanceUntilIdle()

        assertEquals(UiState.Empty, viewModel.filteredProducts.value)
    }

    @Test
    fun `filterProductsByCategory emits Empty state when no products in category`() = runTest {
        val allProducts = dummyProducts
        `when`(repository.getAllProductsFromLocal()).thenReturn(flowOf(allProducts))

        viewModel.filterProductsByCategory("Unknown")
        advanceUntilIdle()

        assertEquals(UiState.Empty, viewModel.filteredProducts.value)
    }
}