package com.andreev.skladapp.ui.history

import androidx.lifecycle.MutableLiveData
import com.andreev.skladapp.data.HistoryPiece
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.network.repositories.ItemsRepository
import com.andreev.skladapp.stored_data.UserStoredData
import com.andreev.skladapp.ui._base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryViewModel: BaseViewModel() {
    @Inject
    lateinit var itemsRepository: ItemsRepository
    @Inject
    lateinit var userStoredData: UserStoredData

    val historyPiecesData = MutableLiveData<Array<HistoryPiece>>()

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun getHistory() {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemsRepository.getHistory(it) }
            }
            if (response != null) {
                historyPiecesData.value = response
            }
        }
    }
}