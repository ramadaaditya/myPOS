package com.example.findest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.findest.data.model.Product
import com.example.findest.data.model.ProductInCart

@Database(entities = [Product::class, ProductInCart::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

}