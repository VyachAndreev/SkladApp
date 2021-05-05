package com.andreev.skladapp.ui.show_all

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

class ShowAllViewModel : BaseViewModel() {
    @Inject
    lateinit var itemRepository: ItemsRepository

    @Inject
    lateinit var userStoredData: UserStoredData

    val positions = MutableLiveData<Array<Position>>()
    val diameter = MutableLiveData<Array<String>>()
    val marks = MutableLiveData<Array<String>>()
    val packings = MutableLiveData<Array<String>>()
    var filterData: ArrayList<ArrayList<String>>? = null

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun getPositions() {
        filterData = null
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemRepository.getAll(it) }
            }
            if (response != null) {
                positions.value = response
            }
        }
    }

    fun getTable() {
        filterData = null
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemRepository.getTable(it) }
            }
            if (response != null) {
                positions.value = response
            }
        }
    }

    fun getDiameter() {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemRepository.getDiameter(it) }
            }
            if (response != null) {
                diameter.value = response
            }
        }
    }

    fun getMarks() {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemRepository.getMarks(it) }
            }
            if (response != null) {
                marks.value = response
            }
        }
    }

    fun getPackings() {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemRepository.getPackings(it) }
            }
            if (response != null) {
                packings.value = response
            }
        }
    }

    fun filter(arrayList: ArrayList<ArrayList<String>>) {
        filterData = arrayList
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemRepository.filter(arrayList, it) }
            }
            if (response != null) {
                positions.value = response
            }
        }
    }



    fun filterTable(arrayList: ArrayList<ArrayList<String>>) {
        filterData = arrayList
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemRepository.filterTable(arrayList, it) }
            }
            if (response != null) {
                positions.value = response
            }
        }
    }

}