package com.example.storeslist.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class StoreRealm : RealmObject {
    @PrimaryKey
    var code: String = ""
    var name: String = ""
    var address: String = ""
}