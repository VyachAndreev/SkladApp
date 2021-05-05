package com.andreev.skladapp.ui.search

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

open class SearchViewModel: BaseViewModel() {
    @Inject
    lateinit var itemsRepository: ItemsRepository

    @Inject
    lateinit var userStoredData: UserStoredData

    val searchedText = MutableLiveData<String>()
    val hints = MutableLiveData<Array<String>>()
    val positions = MutableLiveData<Array<Position>>()

    protected var lastSearched :String? = String()

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    open fun getHints(tag: String?) {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemsRepository.getHints(tag, it) }
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
                loadPositions()
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
                loadPositions()
            }
            if (response != null) {
                positions.value = response
                Timber.i("${positions.value}")
            }
        }
    }

    protected open suspend fun loadPositions(): Array<Position>? {
        return userStoredData.user?.let { itemsRepository.getPositions(lastSearched, it) }
    }

}