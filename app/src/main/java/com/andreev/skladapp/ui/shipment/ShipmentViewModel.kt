package com.andreev.skladapp.ui.shipment

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.network.repositories.ItemsRepository
import com.andreev.skladapp.network.repositories.NullRepository
import com.andreev.skladapp.stored_data.UserStoredData
import com.andreev.skladapp.ui._base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShipmentViewModel: BaseViewModel() {
    @Inject
    lateinit var itemsRepository: ItemsRepository

    @Inject
    lateinit var userStoredData: UserStoredData

    val positions = MutableLiveData<Array<Position>>()

    val toastText = MutableLiveData<String>()

    val confirmResponse = MutableLiveData<ItemsRepository.ConfirmResponse>()

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun departure(include: String, exclude: String) {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemsRepository.ship(include, exclude, it) }
            }
            if (response != null) {
                positions.value = response
            } else {
                toastText.value = "Произошла ошибка"
            }
        }
    }

    fun confirm(data: Array<String>, contrAgent: String, account: String) {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemsRepository.confirm(data, contrAgent, account, it) }
            }
            if (response != null)
                confirmResponse.value = response
            else {
                toastText.value = "Произошла ошибка"
            }
        }
    }
}