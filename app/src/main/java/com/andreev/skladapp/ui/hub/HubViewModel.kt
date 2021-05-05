package com.andreev.skladapp.ui.hub

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.andreev.skladapp.data.User
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.stored_data.UserStoredData
import com.andreev.skladapp.ui._base.BaseViewModel
import javax.inject.Inject

class HubViewModel : BaseViewModel() {
    @Inject
    lateinit var userStoredData: UserStoredData

    var curMenuItem = MutableLiveData<Fragment>()

    var user =  MutableLiveData<User>()

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun loadUserFromLocal() {
        user.value = userStoredData.user
    }

    fun logout() = userStoredData.logout()
}