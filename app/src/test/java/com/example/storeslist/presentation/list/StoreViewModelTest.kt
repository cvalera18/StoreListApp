package com.example.storeslist.presentation.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storeslist.MainDispatcherRule
import com.example.storeslist.domain.model.Store
import com.example.storeslist.domain.usecases.GetStoresUseCase
import com.example.storeslist.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StoreViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getStoresUseCase: GetStoresUseCase

    private lateinit var viewModel: StoreViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = StoreViewModel(getStoresUseCase)
    }

    @Test
    fun `fetchStores should update stores and set isLoading to false`() = runTest {
        // given
        val storeList = listOf(
            Store(code = "1", name = "Store 1", address = "Address 1"),
            Store(code = "2", name = "Store 2", address = "Address 2")
        )
        coEvery { getStoresUseCase(any(), any()) } returns flow { emit(storeList) }

        // when
        viewModel.fetchStores(10, 1)
        // Advance until idle to ensure all coroutines have completed
        advanceUntilIdle()

        // then
        val stores = viewModel.stores.value
        assertEquals(storeList, stores)

        val isLoading = viewModel.isLoading.getOrAwaitValue()
        assertFalse(isLoading)
    }

    @Test
    fun `fetchStores should handle errors and set isLoading to false`() = runTest {
        // given
        val exception = RuntimeException("Error fetching stores")
        coEvery { getStoresUseCase(any(), any()) } returns flow { throw exception }

        // when
        viewModel.fetchStores(10, 1)
        advanceUntilIdle()

        // then
        val stores = viewModel.stores.first()
        assertTrue(stores.isEmpty())

        val isLoading = viewModel.isLoading.getOrAwaitValue()
        assertFalse(isLoading)

        val error = viewModel.error.getOrAwaitValue()
        assertEquals("Error fetching stores", error)
    }

    @Test
    fun `fetchStores should update currentPage`() = runTest {
        // given
        val storeList = listOf(
            Store(code = "1", name = "Store 1", address = "Address 1"),
            Store(code = "2", name = "Store 2", address = "Address 2")
        )
        coEvery { getStoresUseCase(any(), any()) } returns flow { emit(storeList) }

        // when
        val pageToFetch = 2
        viewModel.fetchStores(10, pageToFetch)

        // Advance until idle to ensure all coroutines have completed
        advanceUntilIdle()

        // then
        val currentPage = viewModel.getCurrentPage()
        assertEquals(pageToFetch, currentPage)

        val stores = viewModel.stores.first()
        assertEquals(storeList, stores)
    }

    @Test
    fun `clearError should set error to null`() = runTest {
        // Given
        val exception = RuntimeException("Error fetching stores")
        coEvery { getStoresUseCase(any(), any()) } returns flow { throw exception }

        // Simulate an error
        viewModel.fetchStores(10, 1)
        advanceUntilIdle()
        val errorField = viewModel::class.java.getDeclaredField("_error")
        errorField.isAccessible = true
        errorField.set(viewModel, MutableLiveData<String?>().apply { value = "An error" })
        assertNotNull(viewModel.error.getOrAwaitValue())

        // When
        viewModel.clearError()

        // Then
        val error = viewModel.error.getOrAwaitValue()
        assertNull(error)
    }
}