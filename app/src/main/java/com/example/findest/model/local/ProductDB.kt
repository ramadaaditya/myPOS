package com.example.findest.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.findest.model.Product
import com.example.findest.model.ProductInCart

@Database(entities = [Product::class, ProductInCart::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

}