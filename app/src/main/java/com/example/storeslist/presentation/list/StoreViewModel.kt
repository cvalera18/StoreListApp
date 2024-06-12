package com.example.storeslist.presentation.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeslist.domain.model.Store
import com.example.storeslist.domain.usecases.GetStoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getStoresUseCase: GetStoresUseCase
) : ViewModel() {

    private val _stores = MutableStateFlow<List<Store>>(emptyList())
    val stores: StateFlow<List<Store>> get() = _stores

    /* I can do this with Flow too, but I used LiveData to show how we can use it.
    Additionally, LiveData is more effective due to its direct integration with the lifecycle of the views */
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private var currentPage = INITIAL_PAGE

    fun fetchStores(perPage: Int, page: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            getStoresUseCase(perPage, page)
                .catch { e ->
                    _error.value = e.message
                    _isLoading.value = false
                    Log.e("StoreViewModel", "Error fetching stores", e)
                }
                .collect { storeList ->
                    _stores.value += storeList
                    currentPage = page
                    _isLoading.value = false
                }
        }
    }

    fun getCurrentPage(): Int {
        return currentPage
    }

    fun clearError() {
        _error.value = null
    }

    companion object {
        private const val INITIAL_PAGE = 1
    }
}

