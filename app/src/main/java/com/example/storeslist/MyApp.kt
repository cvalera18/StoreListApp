package com.example.storeslist

import android.app.Application
import com.example.storeslist.data.model.StoreRealm
import dagger.hilt.android.HiltAndroidApp
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import java.io.File

@HiltAndroidApp
class MyApp : Application() {

//    companion object {
//        lateinit var realm: Realm
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        val config = RealmConfiguration.Builder(schema = setOf(StoreRealm::class)).build()
//
//        realm = Realm.open(config)
//    }

}