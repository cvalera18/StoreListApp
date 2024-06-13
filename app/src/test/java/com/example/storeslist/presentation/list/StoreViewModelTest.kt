package com.example.storeslist.presentation.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storeslist.MainDispatcherRule
import com.example.storeslist.data.network.NetworkUtils
import com.example.storeslist.domain.model.Store
import com.example.storeslist.domain.usecases.GetStoresUseCase
import com.example.storeslist.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StoreViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var getStoresUseCase: GetStoresUseCase

    @RelaxedMockK
    private lateinit var networkUtils: NetworkUtils

    private lateinit var viewModel: StoreViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = StoreViewModel(getStoresUseCase, networkUtils)
    }

    @Test
    fun `fetchStores should update stores and set isLoading to false when online`() = runTest {
        // given
        val storeList = listOf(
            Store(code = "1", name = "Store 1", address = "Address 1"),
            Store(code = "2", name = "Store 2", address = "Address 2")
        )
        coEvery { networkUtils.isInternetAvailable() } returns true
        coEvery { getStoresUseCase(any(), any()) } returns flow { emit(storeList) }

        // when
        viewModel.fetchStores(10, 1)
        runCurrent() // Ensure all coroutines are completed

        // then
        val stores = viewModel.stores.first()
        assertEquals(storeList, stores)

        val isLoading = viewModel.isLoading.getOrAwaitValue()
        assertFalse(isLoading)
    }

    @Test
    fun `fetchStores should set an error message when offline and stores are empty`() = runTest {
        // given
        coEvery { networkUtils.isInternetAvailable() } returns false

        // when
        viewModel.fetchStores(10, 1)

        // then
        advanceUntilIdle()  // Ensure all coroutines complete

        // Check stores
        val stores = viewModel.stores.first()
        assertTrue(stores.isEmpty())
        // Check loading state
        val isLoading = viewModel.isLoading.getOrAwaitValue()
        assertFalse(isLoading)
        // Check error message
        val error = viewModel.error.getOrAwaitValue()
        assertEquals("No internet connection", error)
    }

    @Test
    fun `fetchStores should clear the stores list and reset the current page when coming back online`() = runTest {
        // given
        val initialStoreList = listOf(
            Store(code = "1", name = "Store 1", address = "Address 1"),
            Store(code = "2", name = "Store 2", address = "Address 2")
        )
        val newStoreList = listOf(
            Store(code = "3", name = "Store 3", address = "Address 3"),
            Store(code = "4", name = "Store 4", address = "Address 4")
        )

        // Simulamos estar offline primero
        coEvery { networkUtils.isInternetAvailable() } returns false
        coEvery { getStoresUseCase(any(), any()) } returns flow { emit(initialStoreList) }

        // Cuando estamos offline, agregamos la lista inicial
        viewModel.fetchStores(10, 1)
        advanceUntilIdle()

        val storesOffline = viewModel.stores.first()
        assertEquals(initialStoreList, storesOffline)

        // Simulamos que volvemos a estar online
        coEvery { networkUtils.isInternetAvailable() } returns true
        coEvery { getStoresUseCase(any(), any()) } returns flow { emit(newStoreList) }

        // Cuando volvemos a estar online, limpiamos la lista y restablecemos la página
        viewModel.fetchStores(10, 1)
        advanceUntilIdle()

        val storesOnline = viewModel.stores.first()
        assertEquals(newStoreList, storesOnline)
        assertEquals(StoreViewModel.INITIAL_PAGE, viewModel.getCurrentPage())

        val isLoading = viewModel.isLoading.getOrAwaitValue()
        assertFalse(isLoading)
    }

    @Test
    fun `fetchStores should add stores to the current list when fetching additional pages`() = runTest {
        // given
        val initialStoreList = listOf(
            Store(code = "1", name = "Store 1", address = "Address 1"),
            Store(code = "2", name = "Store 2", address = "Address 2")
        )
        val additionalStoreList = listOf(
            Store(code = "3", name = "Store 3", address = "Address 3"),
            Store(code = "4", name = "Store 4", address = "Address 4")
        )

        coEvery { networkUtils.isInternetAvailable() } returns true
        coEvery { getStoresUseCase(10, 1) } returns flow { emit(initialStoreList) }
        coEvery { getStoresUseCase(10, 2) } returns flow { emit(additionalStoreList) }

        // when
        viewModel.fetchStores(10, 1)
        advanceUntilIdle() // Wait until all tasks are completed

        // then
        var stores = viewModel.stores.first()
        assertEquals(initialStoreList, stores)

        // when
        viewModel.fetchStores(10, 2)
        advanceUntilIdle() // Wait until all tasks are completed

        // then
        stores = viewModel.stores.first()
        val expectedStoreList = initialStoreList + additionalStoreList
        assertEquals(expectedStoreList, stores)

        val isLoading = viewModel.isLoading.getOrAwaitValue()
        assertFalse(isLoading)
    }

    @Test
    fun `initialization should fetch stores`() = runTest {
        //given
        val storeList = listOf(
            Store(code = "1", name = "Store 1", address = "Address 1"),
            Store(code = "2", name = "Store 2", address = "Address 2")
        )

        coEvery { getStoresUseCase(any(), any()) } returns flow { emit(storeList) }

        // when
        viewModel = StoreViewModel(getStoresUseCase, networkUtils)

        // then
        advanceUntilIdle()
        val stores = viewModel.stores.first()
        assertEquals(storeList, stores)

        val isLoading = viewModel.isLoading.getOrAwaitValue()
        assertFalse(isLoading)
    }
}