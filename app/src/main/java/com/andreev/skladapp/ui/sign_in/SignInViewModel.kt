package com.andreev.skladapp.ui.sign_in

import androidx.lifecycle.MutableLiveData
import com.andreev.skladapp.data.User
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.network.FuelNetworkService
import com.andreev.skladapp.network.repositories.UserRepository
import com.andreev.skladapp.stored_data.UserStoredData
import com.andreev.skladapp.ui._base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SignInViewModel: BaseViewModel() {
    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userStoredData: UserStoredData

    val isLoginSuccessful = MutableLiveData<Boolean>()

    val login = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val isSignInAvailable = MutableLiveData<Boolean>()

    init {
        isSignInAvailable.value = false
    }

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun checkSingInAbility() {
        val login = login.value ?: ""
        val password = password.value ?: ""
        isSignInAvailable.value =
            login.isNotEmpty() && password.isNotEmpty()
        Timber.i("${isSignInAvailable.value}")
    }

    fun signUser(){
        val login = login.value ?: return
        val password = password.value ?: return
        userStoredData.saveUser(User(login, password))
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { userRepository.login(it) }
            }
            isLoginSuccessful.value = response != null
        }
    }

}
