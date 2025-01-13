package com.example.pamfirebase

import android.app.Application
import com.example.pamfirebase.dependenciesinjection.ContainerApp

class MahasiswaApp : Application() {
    lateinit var containerApp: ContainerApp
    override fun onCreate() {
        super.onCreate()
        containerApp = ContainerApp(this)
    }
}

