package com.example.storeslist.presentation.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.storeslist.MainDispatcherRule
import com.example.storeslist.domain.model.Store
import com.example.storeslist.domain.usecases.GetLocalStoresUseCase
import com.example.storeslist.domain.usecases.GetStoresUseCase
import com.example.storeslist.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
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
    private lateinit var getAllStoresUseCase: GetLocalStoresUseCase

    private lateinit var viewModel: StoreViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = StoreViewModel(getStoresUseCase, getAllStoresUseCase)
    }

    @Test
    fun `fetchStores should call getStoresUseCase and loading state is updated`() = runTest {
        // given
        coEvery { getStoresUseCase.invoke() } just Runs
        val loadingList = mutableListOf<Boolean>()
        val observer = Observer<Boolean> {
            loadingList.add(it)
        }
        viewModel.isLoading.observeForever(observer)

        // when
        viewModel.fetchStores()

        // then
        coVerify { getStoresUseCase.invoke() }
        assertEquals(2, loadingList.size)
        assertTrue(loadingList[0])
        assertFalse(loadingList[1])
        viewModel.isLoading.removeObserver(observer)
    }

    @Test
    fun `fetchStores should set an error message when it fails`() = runTest {
        // given
        val errorMessage = "Error message"
        coEvery { getStoresUseCase.invoke() } throws Exception(errorMessage)

        // when
        viewModel.fetchStores()

        // then
        val error = viewModel.error.getOrAwaitValue()
        assertEquals(errorMessage, error)
    }

    @Test
    fun `when stores is collected, then call getAllStoresUseCase`() = runTest {
        // given

        every { getAllStoresUseCase.invoke() } returns flow { emit(emptyList()) }

        // when
        val job = launch {
            viewModel.stores.collect {
            }
        }
        verify { getAllStoresUseCase.invoke() }
        job.cancel()
    }

    @Test
    fun `when getAllStoresUseCase emits value, then stores is updated`() = runTest {
        // given
        val storeList = listOf(
            Store(code = "1", name = "Store 1", address = "Address 1"),
            Store(code = "2", name = "Store 2", address = "Address 2")
        )
        every { getAllStoresUseCase.invoke() } returns flow { emit(storeList) }
        viewModel = StoreViewModel(getStoresUseCase, getAllStoresUseCase)

        // when
        val emittedStores = mutableListOf<List<Store>>()
        val job = launch {
            viewModel.stores.collect {
                emittedStores.add(it)
            }
        }

        advanceUntilIdle()

        // then
        assertEquals(1, emittedStores.size)
        assertEquals(storeList, emittedStores[0])
        job.cancel()
    }
}