package com.andreev.skladapp.stored_data

import android.content.SharedPreferences
import com.andreev.skladapp.Constants
import timber.log.Timber

class UserStoredData(private val sharedPreferences: SharedPreferences) {
    var user: String? = null
        private set

    init {
        getUser()
        Timber.i("initialization")
    }

    fun saveUser(string: String) {
        Timber.i("saving user")
        this.user = user
        sharedPreferences.edit()
            .putString(Constants.USER, string)
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
        user = sharedPreferences.getString(Constants.USER, null)
    }
}