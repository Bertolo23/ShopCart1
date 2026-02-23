package com.example.shopcart.data.local.dao

import androidx.room.*
import com.example.shopcart.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(item: CartItemEntity)

    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    @Delete
    suspend fun removeFromCart(item: CartItemEntity)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}