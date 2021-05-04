package com.andreev.skladapp.stored_data

import android.content.SharedPreferences
import com.andreev.skladapp.Constants
import com.andreev.skladapp.data.User
import timber.log.Timber

class UserStoredData(private val sharedPreferences: SharedPreferences) {
    var user: User? = null
        private set

    init {
        getUser()
        Timber.i("initialization")
    }

    fun saveUser(user: User) {
        Timber.i("saving user")
        this.user = user
        sharedPreferences.edit()
            .putString(Constants.LOGIN, user.login)
            .putString(Constants.PASSWORD, user.password)
            .apply()
    }

    fun logout() {
        user = null
        sharedPreferences.edit()
            .remove(Constants.LOGIN)
            .remove(Constants.PASSWORD)
            .apply()
    }


    private fun getUser() {
        Timber.i("getting User")
        val login = sharedPreferences.getString(Constants.LOGIN, null)
        val password = sharedPreferences.getString(Constants.PASSWORD, null)
        user = login?.let { password?.let { User(it, password)} }
    }
}