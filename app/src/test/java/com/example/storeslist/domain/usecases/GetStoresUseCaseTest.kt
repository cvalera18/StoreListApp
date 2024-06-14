//package com.example.storeslist.domain.usecases
//
//import com.example.storeslist.domain.model.Store
//import com.example.storeslist.domain.repository.StoreRepository
//import io.mockk.MockKAnnotations
//import io.mockk.coEvery
//import io.mockk.impl.annotations.MockK
//import io.mockk.junit4.MockKRule
//import junit.framework.TestCase.assertEquals
//import junit.framework.TestCase.assertTrue
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//
//@ExperimentalCoroutinesApi
//class GetStoresUseCaseTest {
//
//    @get:Rule
//    val mockkRule = MockKRule(this)
//
//    @MockK
//    private lateinit var storeRepository: StoreRepository
//
//    private lateinit var getStoresUseCase: GetStoresUseCase
//
//    @Before
//    fun setUp() {
//        MockKAnnotations.init(this)
//        getStoresUseCase = GetStoresUseCase(storeRepository)
//    }
//
//    @Test
//    fun `invoke should return a list of stores`() = runTest {
//        // given
//        val storeList = listOf(
//            Store(code = "1", name = "Store 1", address = "Address 1"),
//            Store(code = "2", name = "Store 2", address = "Address 2")
//        )
////        coEvery { storeRepository.localStore() } returns flow { emit(storeList) }
//
//        // when
//        val result = getStoresUseCase()
//
//        // then
//        result.collect { stores ->
//            assertEquals(storeList, stores)
//        }
//    }
//
//    @Test
//    fun `invoke should return an empty list when repository returns no stores`() = runTest {
//        // given
////        coEvery { storeRepository.localStore() } returns flow { emit(emptyList()) }
//
//        //when
//        val result = getStoresUseCase()
//
//        // then
//        result.collect { stores ->
//            assertTrue(stores.isEmpty())
//        }
//    }
//
//    @Test
//    fun `invoke should return stores from multiple pages`() = runTest {
//        // given
//        val storeListPage1 = listOf(
//            Store(code = "1", name = "Store 1", address = "Address 1")
//        )
//        val storeListPage2 = listOf(
//            Store(code = "2", name = "Store 2", address = "Address 2")
//        )
////        coEvery { storeRepository.localStore() } returns flow { emit(storeListPage1) }
////        coEvery { storeRepository.localStore() } returns flow { emit(storeListPage2) }
//
//        // when
//        val resultPage1 = getStoresUseCase()
//        val resultPage2 = getStoresUseCase()
//
//        // then
//        resultPage1.collect { stores ->
//            assertEquals(storeListPage1, stores)
//        }
//
//        resultPage2.collect { stores ->
//            assertEquals(storeListPage2, stores)
//        }
//    }
//}