package com.andreev.skladapp.ui.search

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

class SearchViewModel: BaseViewModel() {
    @Inject
    lateinit var itemsRepository: ItemsRepository

    val searchedText = MutableLiveData<String>()
    val hints = MutableLiveData<Array<String>>()
    val positions = MutableLiveData<Array<Position>>()

    private var lastSearched :String? = String()

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun getHints(tag: String?) {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                itemsRepository.getHints(tag)
            }
            if (response != null) {
                hints.value = response
            }
        }
    }

    fun getPositions() {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                lastSearched = searchedText.value
                itemsRepository.getPositions(lastSearched)
            }
            if (response != null) {
                positions.value = response
                Timber.i("${positions.value}")
            }
        }
    }

    fun refreshPositions() {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                itemsRepository.getPositions(lastSearched)
            }
            if (response != null) {
                positions.value = response
                Timber.i("${positions.value}")
            }
        }
    }

}