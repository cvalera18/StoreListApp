package com.example.storeslist.domain.mapper

import com.example.storeslist.domain.model.StoreAttributes
import com.example.storeslist.domain.model.StoreData
import junit.framework.TestCase.assertEquals
import org.junit.Test

class StoreMapperTest {

    @Test
    fun `toStore should convert StoreData to Store correctly`() {
        // Given
        val storeData = StoreData(
            id = "1",
            type = "store",
            attributes = StoreAttributes(
                name = "Store 1",
                code = "1",
                full_address = "Address 1"
            )
        )
        // When
        val store = storeData.toStore()
        // Then
        assertEquals("1", store.code)
        assertEquals("Store 1", store.name)
        assertEquals("Address 1", store.address)
    }
}