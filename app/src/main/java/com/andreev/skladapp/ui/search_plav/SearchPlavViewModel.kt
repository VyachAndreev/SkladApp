package com.andreev.skladapp.ui.search_plav

import androidx.lifecycle.MutableLiveData
import com.andreev.skladapp.data.MockPosition
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.network.repositories.ItemsRepository
import com.andreev.skladapp.stored_data.UserStoredData
import com.andreev.skladapp.ui._base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchPlavViewModel: BaseViewModel() {
    @Inject
    lateinit var itemsRepository: ItemsRepository
    @Inject
    lateinit var userStoredData: UserStoredData

    val searchedText = MutableLiveData<String>()
    val hints = MutableLiveData<Array<String>>()
    val positions = MutableLiveData<Array<MockPosition>>()
    var searchedSize = 0

    private var lastSearched :String? = String()

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun getPositions() {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                lastSearched = searchedText.value
                loadPositions()
            }
            if (response != null) {
                positions.value = response
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
            }
        }
    }

    suspend fun loadPositions(): Array<MockPosition>? {
        return userStoredData.user?.let { itemsRepository.getPlavPositions(lastSearched, it) }
    }

    fun getHints(tag: String?) {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemsRepository.getPlavHints(tag, it) }
            }
            if (response != null) {
                hints.value = response
            }
        }
    }
}