package com.example.shopcart.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shopcart.data.local.dao.CartDao
import com.example.shopcart.data.local.dao.ProductDao
import com.example.shopcart.data.local.entity.CartItemEntity
import com.example.shopcart.data.local.entity.ProductEntity

@Database(entities = [ProductEntity::class, CartItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
}