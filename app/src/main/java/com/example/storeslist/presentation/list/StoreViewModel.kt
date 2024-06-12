package com.example.storeslist.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeslist.domain.usecases.GetStoresUseCaseFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    getAllStoresUseCase: GetStoresUseCaseFlow
) : ViewModel() {
    val allStores = getAllStoresUseCase.invoke()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )
}