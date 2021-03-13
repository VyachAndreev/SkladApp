package com.andreev.skladapp.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.andreev.skladapp.Constants
import com.andreev.skladapp.stored_data.UserStoredData
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SettingsModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
        context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideStoredUser(sharedPreferences: SharedPreferences): UserStoredData =
        UserStoredData(sharedPreferences)
}