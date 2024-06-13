package com.example.storeslist.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeslist.data.network.NetworkUtils
import com.example.storeslist.domain.model.Store
import com.example.storeslist.domain.usecases.GetStoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getStoresUseCase: GetStoresUseCase,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _stores = MutableStateFlow<List<Store>>(emptyList())
    val stores: StateFlow<List<Store>> get() = _stores

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private var currentPage = INITIAL_PAGE
    private var wasOffline = false

    init {
        fetchStores(PER_PAGE, INITIAL_PAGE)
    }

    fun fetchStores(perPage: Int, page: Int) {
        if (networkUtils.isInternetAvailable()) {
            _isLoading.value = true
            if (wasOffline) {
                clearStores()  // Clean the list whe the internet is back
                currentPage = INITIAL_PAGE
                wasOffline = false
            }
            viewModelScope.launch {
                getStoresUseCase(perPage, page)
                    .catch { e ->
                        _error.value = e.message
                        _isLoading.value = false
                    }
                    .collect { storeList ->
                        _stores.value = if (page == INITIAL_PAGE) {
                            storeList
                        } else {
                            _stores.value + storeList
                        }
                        currentPage = page
                        _isLoading.value = false
                    }
            }
        } else {
            wasOffline = true
            if (_stores.value.isEmpty()) {
                _isLoading.value = true
                viewModelScope.launch {
                    getStoresUseCase(perPage, page)
                        .catch { e ->
                            _error.value = e.message
                            _isLoading.value = false
                        }
                        .collect { storeList ->
                            _stores.value = storeList
                            _isLoading.value = false
                        }
                }
            }
            _error.value = "No internet connection" // Set the error value for no internet
        }
    }

    private fun clearStores() {
        _stores.value = emptyList()
    }

    fun getCurrentPage(): Int {
        return currentPage
    }

    fun clearError() {
        _error.value = null
    }

    companion object {
        private const val INITIAL_PAGE = 1
        private const val PER_PAGE = 10
    }
}