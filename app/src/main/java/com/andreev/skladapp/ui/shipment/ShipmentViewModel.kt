package com.andreev.skladapp.ui.shipment

import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.network.repositories.NullRepository
import com.andreev.skladapp.stored_data.UserStoredData
import com.andreev.skladapp.ui._base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShipmentViewModel: BaseViewModel() {
    @Inject
    lateinit var nullRepository: NullRepository

    @Inject
    lateinit var userStoredData: UserStoredData

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun departure(include: String, exclude: String) {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { nullRepository.ship(include, exclude, it) }
            }
        }
    }
}