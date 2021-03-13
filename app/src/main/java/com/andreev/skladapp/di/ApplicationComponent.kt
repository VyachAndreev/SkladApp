package com.andreev.skladapp.di

import com.andreev.skladapp.ui.MainActivity
import com.andreev.skladapp.di.modules.SettingsModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [SettingsModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
}