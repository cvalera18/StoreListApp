package com.example.storeslist.data.datasources.local

import com.example.storeslist.data.model.StoreRealm
import com.example.storeslist.domain.model.Store
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalDataSourceImpl(private val realm: Realm) : LocalDataSource {

    override fun getStores(): Flow<List<Store>> = flow {
        val results: RealmResults<StoreRealm> = realm.query<StoreRealm>().find()
        emit(results.map { storeRealm ->
            Store(
                code = storeRealm.code,
                name = storeRealm.name,
                address = storeRealm.address
            )
        })
    }

    override suspend fun saveStores(stores: List<Store>) {
        realm.write {
            stores.forEach { store ->
                val storeRealm: StoreRealm? = this.query<StoreRealm>("code == $0", store.code).first().find()
                if (storeRealm != null) {
                    storeRealm.name = store.name
                    storeRealm.address = store.address
                } else {
                    val newStoreRealm = StoreRealm().apply {
                        code = store.code
                        name = store.name
                        address = store.address
                    }
                    copyToRealm(newStoreRealm)
                }
            }
        }
    }
}