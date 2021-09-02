package com.andreev.skladapp

import android.app.Application
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.di.DaggerApplicationComponent
import com.andreev.skladapp.di.modules.SettingsModule
import timber.log.Timber

class SkladApplication: Application() {
    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        appComponent = DaggerApplicationComponent.builder()
            .settingsModule(SettingsModule(applicationContext))
            .build()
    }
}