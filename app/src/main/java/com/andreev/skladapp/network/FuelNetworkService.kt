package com.andreev.skladapp.network

import android.widget.Toast
import com.andreev.skladapp.data.User
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
    private val BASE_URL = "http://ferro-trade.ru/"
    private val gson = Gson()

    init {
        FuelManager.instance.basePath = BASE_URL
    }

    protected suspend fun <T> get(
        path: String,
        clazz: Class<T>,
        parameters: List<Pair<String, Any?>>? = null,
        user: User,
    ): T? {
        val a = path == "authTest" && clazz == String::class.java
        Timber.i("login is '${user.login}' password is '${user.password}'")
        try {
            return Fuel.get(path, parameters)
                .authentication()
                .basic(user.login, user.password)
                .awaitStringResult()
                .fold({ jsonResponse ->
                    Timber.i("get jsonResponse is $jsonResponse")
                    if (a) {
                        return@fold "" as T
                    }
                    return@fold gson.fromJson(jsonResponse, clazz)
                }, { error ->
                    Timber.i("$error")
                    return null
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }


    protected suspend fun <T> post(
        path: String,
        clazz: Class<T>,
        parameters: List<Pair<String, Any?>>? = null,
        user: User,
    ): T? {
        try {
            return Fuel.post(path, parameters)
                .authentication()
                .basic(user.login, user.password)
                .awaitStringResult()
                .fold({ jsonResponse ->
                    Timber.i("post jsonResponse is $jsonResponse")
                    return@fold gson.fromJson(jsonResponse, clazz)
                }) { error ->
                    Timber.i("$error")
                    return null
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    protected suspend fun <T> postWithJson(
        path: String,
        clazz: Class<T>,
        parameters: Any?,
        user: User,
    ): T? {
        Timber.i("parameters are $parameters")
        try {
            return Fuel.post(path)
                .jsonBody(JSONObject(gson.toJson(parameters)).toString(), Charsets.UTF_8)
                .authentication()
                .basic(user.login, user.password)
                .awaitStringResult()
                .fold({ jsonResponse ->
                    Timber.i("post jsonResponse is $jsonResponse")
                    return@fold gson.fromJson(jsonResponse, clazz)
                }) { error ->
                    Timber.i("$error")
                    return null
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}