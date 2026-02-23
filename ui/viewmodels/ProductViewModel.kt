package com.example.shopcart.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopcart.data.local.entity.ProductEntity
import com.example.shopcart.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories = _categories.asStateFlow()

    val products: StateFlow<List<ProductEntity>> = combine(
        _searchQuery,
        _selectedCategory
    ) { query, category -> Pair(query, category) }
        .flatMapLatest { (query, category) ->
            when {
                query.isNotEmpty() -> repository.searchProducts(query)
                category != null -> repository.getProductsByCategory(category)
                else -> repository.getProducts()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.refreshProducts()
            _categories.value = repository.getCategories()
            _isLoading.value = false
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelected(category: String?) {
        _selectedCategory.value = category
        _searchQuery.value = ""
    }

    suspend fun getProductById(id: Int): ProductEntity? {
        return repository.getProductById(id)
    }
}
