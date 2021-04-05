package com.andreev.skladapp.ui.show_all

import androidx.lifecycle.MutableLiveData
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.network.repositories.ItemsRepository
import com.andreev.skladapp.ui._base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ShowAllViewModel : BaseViewModel() {
    @Inject
    lateinit var itemRepository: ItemsRepository

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
                itemRepository.getAll()
            }
            if (response != null) {
                positions.value = response
            }
        }
    }

    fun getDiameter() {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                itemRepository.getDiameter()
            }
            if (response != null) {
                diameter.value = response
            }
        }
    }

    fun getMarks() {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                itemRepository.getMarks()
            }
            if (response != null) {
                marks.value = response
            }
        }
    }

    fun getPackings() {
        packings.value = itemRepository.getPackings()
        packings.value?.forEach {
            Timber.i(it)
        }
    }

    fun filter(arrayList: ArrayList<ArrayList<String>>) {
        filterData = arrayList
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                itemRepository.filter(arrayList)
            }
            if (response != null) {
                positions.value = response
            }
        }
    }
}