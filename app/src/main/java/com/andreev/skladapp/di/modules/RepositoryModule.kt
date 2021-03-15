package com.andreev.skladapp.di.modules

import com.andreev.skladapp.network.repositories.ItemsRepository
import com.andreev.skladapp.network.repositories.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun providesUserRepository(): UserRepository = UserRepository()

    @Provides
    @Singleton
    fun providesItemsRepository(): ItemsRepository = ItemsRepository()
}