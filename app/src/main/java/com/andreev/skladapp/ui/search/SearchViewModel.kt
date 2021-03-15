package com.andreev.skladapp.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
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
    val hints = MutableLiveData<ArrayList<String>>()

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

}