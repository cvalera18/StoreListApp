package com.example.storeslist.data.datasources.local

import com.example.storeslist.data.models.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor() : LocalDataSource {
    override val allStores: Flow<List<Store>> = flow {
        emit(
            listOf(
                Store("ST001", "Farmatodo", "123 Main St"),
                Store("ST002", "SuperMarket", "456 Market St"),
                Store("ST003", "TechStore", "789 Tech Ave"),
                Store("ST004", "BookWorld", "101 Book St"),
                Store("ST005", "ClothingHub", "202 Fashion Ave"),
                Store("ST006", "GadgetShop", "303 Gadget Ln"),
                Store("ST007", "ToyStore", "404 Toy St"),
                Store("ST008", "HomeGoods", "505 Home Rd"),
                Store("ST009", "PetStore", "606 Pet Ave"),
                Store("ST010", "BeautyShop", "707 Beauty Ln"),
                Store("ST011", "SportsGear", "808 Sport St"),
                Store("ST012", "FurnitureStore", "909 Furniture Rd"),
                Store("ST013", "MusicStore", "1010 Music Ave"),
                Store("ST014", "GardenCenter", "1111 Garden Ln"),
                Store("ST015", "HardwareStore", "1212 Tool St")
            )
        )
    }
}