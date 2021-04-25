package com.andreev.skladapp.ui.history

import androidx.lifecycle.MutableLiveData
import com.andreev.skladapp.data.HistoryPiece
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.network.repositories.ItemsRepository
import com.andreev.skladapp.ui._base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class HistoryViewModel: BaseViewModel() {
    @Inject
    lateinit var itemsRepository: ItemsRepository

    val historyPiecesData = MutableLiveData<Array<HistoryPiece>>()

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun getHistory() {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                itemsRepository.getHistory()
            }
            if (response != null) {
                historyPiecesData.value = response
            }
        }
    }

}