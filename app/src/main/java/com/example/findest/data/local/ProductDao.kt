package com.example.findest.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.findest.data.model.Product
import com.example.findest.data.model.ProductInCart
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM product WHERE id = :id")
    fun getProductById(id: Int): Flow<Product?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProduct(products: List<Product>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductItem(products: Product)

    @Query("SELECT * FROM product")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM product_in_cart")
    fun getAllCartItems(): Flow<List<ProductInCart>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: ProductInCart)

    @Update
    suspend fun updateCartItem(item: ProductInCart)

    @Delete
    suspend fun deleteCartItem(item: ProductInCart)

    @Query("DELETE FROM product_in_cart")
    suspend fun deleteAllCartItem()

    @Query("DELETE FROM product_in_cart WHERE productId = :productId")
    suspend fun deleteCartItemById(productId: Int)
}