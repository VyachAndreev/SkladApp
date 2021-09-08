package com.andreev.skladapp.ui.search

import androidx.lifecycle.MutableLiveData
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.network.repositories.ItemsRepository
import com.andreev.skladapp.stored_data.UserStoredData
import com.andreev.skladapp.ui._base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class SearchViewModel: BaseViewModel() {
    @Inject
    lateinit var itemsRepository: ItemsRepository
    @Inject
    lateinit var userStoredData: UserStoredData

    val searchedText = MutableLiveData<String>()
    val hints = MutableLiveData<Array<String>>()
    private val _positions = MutableStateFlow(arrayOf<Position>())
    val positions: StateFlow<Array<Position>> = _positions
//    val positions = MutableLiveData<Array<Position>>()
    var searchedSize = 0

    private var lastSearched :String? = String()

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    private suspend fun loadPositions(): Array<Position>? {
        return userStoredData.user?.let { itemsRepository.getPositions(lastSearched, it) }
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
                _positions.value = response
            }
        }
    }

    fun refreshPositions() {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                loadPositions()
            }
            if (response != null) {
                _positions.value = response
            }
        }
    }
}