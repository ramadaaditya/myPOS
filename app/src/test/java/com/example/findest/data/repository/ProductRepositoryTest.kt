package com.example.findest.data.repository

import com.example.findest.MainDispatcherRule
import com.example.findest.data.local.ProductDao
import com.example.findest.data.remote.api.ApiService
import com.example.findest.data.remote.response.DetailResponse
import com.example.findest.data.remote.response.ProductResponse
import com.example.findest.utils.UiState
import com.example.findest.utils.dummyProducts
import com.example.findest.utils.dummyProductsItem
import com.example.findest.utils.toProductEntity
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class ProductRepositoryTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var apiService: ApiService
    private lateinit var dao: ProductDao
    private lateinit var repository: ProductRepository

    @Before
    fun setUp() {
        apiService = mock(ApiService::class.java)
        dao = mock(ProductDao::class.java)
        repository = ProductRepository(apiService, dao)
    }

    @Test
    fun `getAllProducts returns Success when API call is successful`() = runTest {
        val response = ProductResponse(products = dummyProductsItem)
        `when`(apiService.getAllProducts()).thenReturn(response)
        val expected = dummyProductsItem.map { it.toProductEntity() }
        val result = repository.getAllProducts()
        assertTrue(result is UiState.Success)
        assertEquals(expected, (result as UiState.Success).data)
        verify(dao).insertAllProduct(expected)
    }

    @Test
    fun `getAllProducts returns Error when API call fails`() = runTest {
        `when`(apiService.getAllProducts()).thenThrow(RuntimeException("Network error"))
        val result = repository.getAllProducts()
        assertTrue(result is UiState.Error)
        assertEquals("Network error", (result as UiState.Error).message)
    }

    @Test
    fun `getDetailProduct returns from local when available`() = runTest {
        val expectedProduct = dummyProducts[0]
        `when`(dao.getProductById(expectedProduct.id)).thenReturn(flowOf(expectedProduct))
        val result = repository.getDetailProduct(expectedProduct.id)
        assertTrue(result is UiState.Success)
        assertEquals(expectedProduct, (result as UiState.Success).data)
    }

    @Test
    fun `getDetailProduct fetches from API if not found locally`() = runTest {
        val expectedProduct = dummyProducts[1]
        `when`(dao.getProductById(expectedProduct.id)).thenReturn(flowOf(null))
        val response = DetailResponse(product = dummyProductsItem[1])
        `when`(apiService.getDetailProduct(expectedProduct.id)).thenReturn(response)
        val result = repository.getDetailProduct(expectedProduct.id)
        assertTrue(result is UiState.Success)
        assertEquals(expectedProduct, (result as UiState.Success).data)
        verify(dao).insertProductItem(expectedProduct)
    }

    @Test
    fun `getDetailProduct returns Error when API fails and not in local`() = runTest {
        val id = 999
        `when`(dao.getProductById(id)).thenReturn(flowOf(null))
        `when`(apiService.getDetailProduct(id)).thenThrow(RuntimeException("API down"))
        val result = repository.getDetailProduct(id)
        assertTrue(result is UiState.Error)
        assertEquals("API down", (result as UiState.Error).message)
    }
}