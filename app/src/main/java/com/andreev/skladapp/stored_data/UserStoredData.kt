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
            .putString(Constants.USER, user.token)
            .apply()
    }

    fun logout() {
        user = null
        sharedPreferences.edit()
            .remove(Constants.USER)
            .apply()
    }


    private fun getUser() {
        Timber.i("getting User")
        user = User(sharedPreferences.getString(Constants.USER, null))
    }
}