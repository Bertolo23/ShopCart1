package com.example.shopcart.data.repository

import com.example.shopcart.data.local.dao.CartDao
import com.example.shopcart.data.local.dao.ProductDao
import com.example.shopcart.data.local.entity.CartItemEntity
import com.example.shopcart.data.local.entity.ProductEntity
import com.example.shopcart.data.remote.FakeStoreApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val api: FakeStoreApi,
    private val productDao: ProductDao,
    private val cartDao: CartDao
) {
    // API & Sync
    suspend fun refreshProducts() {
        try {
            val remoteProducts = api.getProducts()
            val entities = remoteProducts.map {
                ProductEntity(it.id, it.title, it.price, it.description, it.category, it.image)
            }
            productDao.insertProducts(entities)
        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun getCategories(): List<String> {
        return try {
            api.getCategories()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Products (Room)
    fun getProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()

    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> =
        productDao.getProductsByCategory(category)

    suspend fun getProductById(id: Int): ProductEntity? = productDao.getProductById(id)

    fun searchProducts(query: String): Flow<List<ProductEntity>> = productDao.searchProducts(query)

    // Cart (Room)
    fun getCartItems(): Flow<List<CartItemEntity>> = cartDao.getCartItems()

    suspend fun addToCart(product: ProductEntity) {
        cartDao.addToCart(CartItemEntity(product.id, product.title, product.price, product.image, 1))
    }

    suspend fun removeFromCart(item: CartItemEntity) {
        cartDao.removeFromCart(item)
    }

    suspend fun clearCart() = cartDao.clearCart()
}
