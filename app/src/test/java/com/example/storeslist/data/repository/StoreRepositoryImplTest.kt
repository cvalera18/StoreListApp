//package com.example.storeslist.data.repository
//
//import com.example.storeslist.data.datasources.remote.FrogmiRemoteDataSource
//import com.example.storeslist.domain.model.Store
//import io.mockk.MockKAnnotations
//import io.mockk.coEvery
//import io.mockk.impl.annotations.MockK
//import junit.framework.TestCase.assertEquals
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Test
//
//@ExperimentalCoroutinesApi
//class StoreRepositoryImplTest {
//
//    @MockK
//    private lateinit var remoteDataSource: FrogmiRemoteDataSource
//
//    private lateinit var repository: StoreRepositoryImpl
//
//    @Before
//    fun setUp() {
//        MockKAnnotations.init(this)
//        repository = StoreRepositoryImpl(remoteDataSource)
//    }
//
//    @Test
//    fun `getStores should return stores from remote data source`() = runTest {
//        // Given
//        val storeList = listOf(
//            Store(code = "1", name = "Store 1", address = "Address 1"),
//            Store(code = "2", name = "Store 2", address = "Address 2")
//        )
//        coEvery { remoteDataSource.getStores(any(), any()) } returns storeList
//
//        // When
//        val result = repository.getStores(10, 1)
//
//        // Then
//        result.collect { stores ->
//            assertEquals(storeList, stores)
//        }
//    }
//}