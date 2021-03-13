package com.andreev.skladapp.ui.search

import androidx.lifecycle.MutableLiveData
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseViewModel
import timber.log.Timber

class SearchViewModel: BaseViewModel() {

    val searchedText = MutableLiveData<String>()

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun getHints() {
        Timber.i("hints")
    }

}