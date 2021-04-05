package com.andreev.skladapp.ui.unite

import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.network.repositories.NullRepository
import com.andreev.skladapp.ui._base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UniteViewModel: BaseViewModel() {
    @Inject
    lateinit var nullRepository: NullRepository

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun unite(text: String) {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                nullRepository.unite(text)
            }
        }
    }
}