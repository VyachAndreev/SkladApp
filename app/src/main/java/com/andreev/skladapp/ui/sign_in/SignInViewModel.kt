package com.andreev.skladapp.ui.sign_in

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.andreev.skladapp.data.User
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.network.repositories.UserRepository
import com.andreev.skladapp.stored_data.UserStoredData
import com.andreev.skladapp.ui._base.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SignInViewModel: BaseViewModel() {
    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userData: UserStoredData

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

    suspend fun signUser() : Boolean {
        val login = login.value ?: return false
        val password = password.value ?: return false
        val response = userRepository.signInUser(
            login, password
        )
        response?.let {
            if (it != null) {
                Timber.i("save")
                userData.saveUser(it)
                return true
            }
        }
        return false
    }

}