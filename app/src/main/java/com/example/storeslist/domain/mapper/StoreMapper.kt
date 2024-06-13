package com.example.storeslist.domain.mapper

import com.example.storeslist.domain.model.Store
import com.example.storeslist.data.model.StoreData

fun StoreData.toStore(): Store {
    return Store(
        code = this.attributes.code,
        name = this.attributes.name,
        address = this.attributes.full_address
    )
}