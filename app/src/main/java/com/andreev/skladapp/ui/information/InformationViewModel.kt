package com.andreev.skladapp.ui.information

import androidx.lifecycle.MutableLiveData
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.network.repositories.ItemsRepository
import com.andreev.skladapp.stored_data.UserStoredData
import com.andreev.skladapp.ui._base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class InformationViewModel : BaseViewModel(){
    @Inject
    lateinit var itemsRepository: ItemsRepository

    @Inject
    lateinit var userStoredData: UserStoredData

    var item = MutableLiveData<Position>()

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun getPosition(id: Long?) {
        Timber.i("position ID is $id")
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemsRepository.getPosition(id.toString(), it) }
            }
            if (response != null) {
                item.value = response
                Timber.i("${item.value}")
            }
        }
    }

    fun getPackage(id: Long?) {
        Timber.i("package ID is $id")
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemsRepository.getPackage(id.toString(), it) }
            }
            if (response != null) {
                item.value = response
                Timber.i("${item.value}")
            }
        }
    }
}