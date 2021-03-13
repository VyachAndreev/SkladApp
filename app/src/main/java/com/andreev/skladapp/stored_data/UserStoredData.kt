package com.andreev.skladapp.stored_data

import android.content.SharedPreferences
import com.andreev.skladapp.Constants
import com.andreev.skladapp.data.User

class UserStoredData(private val sharedPreferences: SharedPreferences) {
    var user: User? = null

    init {
        getUser()
    }

    fun saveUser(user: User) {
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
        user = User(sharedPreferences.getString(Constants.USER, null))
    }
}