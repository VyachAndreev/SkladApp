package com.andreev.skladapp.network

import com.andreev.skladapp.di.modules.SettingsModule
import com.andreev.skladapp.stored_data.UserStoredData
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.coroutines.awaitStringResult
import com.google.gson.Gson
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

abstract class FuelNetworkService {
    @Inject
    lateinit var userStoredData: UserStoredData

    private val BASE_URL = "http://ferro-trade.ru/"
    private val gson = Gson()

    init {
        FuelManager.instance.basePath = BASE_URL
    }

    protected suspend fun <T> get(
        path: String,
        clazz: Class<T>,
        parameters: List<Pair<String, Any?>>? = null
    ): T? {
        try {
            userStoredData.user?.let {
                return Fuel.get(path, parameters)
                    .authentication()
                    .basic(it.login, it.password)
                    .awaitStringResult()
                    .fold({ jsonResponse ->
                        Timber.i("get jsonResponse is $jsonResponse")
                        return@fold gson.fromJson(jsonResponse, clazz)
                    }, { error ->
                        Timber.i("$error")
                        return null
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    protected suspend fun loginUser(): Boolean {
        try {
            userStoredData.user?.let {
                return Fuel.get("authTest", null)
                    .authentication()
                    .basic(it.login, it.password)
                    .awaitStringResult()
                    .fold({ jsonResponse ->
                        Timber.i("login success")
                        return true
                    }, { error ->
                        Timber.i("login failure")
                        return false
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


    protected suspend fun <T> post(
        path: String,
        clazz: Class<T>,
        parameters: List<Pair<String, Any?>>? = null
    ): T? {
        try {
            userStoredData.user?.let {
                return Fuel.post(path, parameters)
                    .authentication()
                    .basic(it.login, it.password)
                    .awaitStringResult()
                    .fold({ jsonResponse ->
                        Timber.i("post jsonResponse is $jsonResponse")
                        return@fold gson.fromJson(jsonResponse, clazz)
                    }) { error ->
                        Timber.i("$error")
                        return null
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    protected suspend fun <T> postWithJson(
        path: String,
        clazz: Class<T>,
        parameters: Any?
    ): T? {
        try {
            userStoredData.user?.let {
                return Fuel.post(path)
                    .jsonBody(JSONObject(gson.toJson(parameters)).toString(), Charsets.UTF_8)
                    .authentication()
                    .basic(it.login, it.password)
                    .awaitStringResult()
                    .fold({ jsonResponse ->
                        return@fold gson.fromJson(jsonResponse, clazz)
                    }) { error ->
                        Timber.i("$error")
                        return null
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}