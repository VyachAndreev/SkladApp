package com.andreev.skladapp.network

import com.andreev.skladapp.data.User
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.coroutines.awaitStringResult
import com.google.gson.Gson
import org.json.JSONObject
import timber.log.Timber

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
        Timber.i("""GET call:
            |path: $path
        """.trimMargin())
        Timber.i("login is '${user.login}' password is '${user.password}'")
        try {
            return Fuel.get(path, parameters)
                .authentication()
                .basic(user.login, user.password)
                .awaitStringResult()
                .fold({ jsonResponse ->
                    Timber.i(
                        """GET $path
                        |jsonResponse is $jsonResponse"""
                            .trimMargin()
                    )
                    if (path == "authTest" && clazz == String::class.java) {
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
        Timber.i("""POST call:
            |path: $path
            |parameters are $parameters
        """.trimMargin())
        Timber.i("login is '${user.login}' password is '${user.password}'")
        try {
            return Fuel.post(path, parameters)
                .authentication()
                .basic(user.login, user.password)
                .awaitStringResult()
                .fold({ jsonResponse ->
                    Timber.i(
                        """POST $path
                        |jsonResponse is $jsonResponse"""
                            .trimMargin()
                    )
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
        Timber.i("""POST call:
            |path: $path
            |parameters are $parameters
        """.trimMargin())
        Timber.i("login is '${user.login}' password is '${user.password}'")
        try {
            return Fuel.post(path)
                .jsonBody(JSONObject(gson.toJson(parameters)).toString(), Charsets.UTF_8)
                .authentication()
                .basic(user.login, user.password)
                .awaitStringResult()
                .fold({ jsonResponse ->
                    Timber.i(
                        """POST $path
                        |jsonResponse is $jsonResponse"""
                            .trimMargin()
                    )
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