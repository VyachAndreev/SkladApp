package com.andreev.skladapp.ui.unite

import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.network.repositories.NullRepository
import com.andreev.skladapp.stored_data.UserStoredData
import com.andreev.skladapp.ui._base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class UniteViewModel : BaseViewModel() {
    @Inject
    lateinit var nullRepository: NullRepository
    @Inject
    lateinit var userStoredData: UserStoredData

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun unite(text: String) {
        scopeIO.launch {
            userStoredData.user?.let { nullRepository.unite(text, it) }
        }
    }
}