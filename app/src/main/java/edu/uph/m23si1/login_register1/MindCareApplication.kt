package edu.uph.m23si1.login_register1

import android.app.Application
// Hapus import Realm dan RealmConfiguration

class MindCareApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Hapus semua kode inisialisasi Realm di sini
        // Realm.init(this)
        // val config = RealmConfiguration.Builder()...
        // Realm.setDefaultConfiguration(config)
    }
}