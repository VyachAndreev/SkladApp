package com.andreev.skladapp.network

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
    private val login = "sergey"
    private val password = "vAlAvin2002"

    init {
        FuelManager.instance.basePath = BASE_URL
    }

    protected suspend fun <T> get(
        path: String,
        clazz: Class<T>,
        parameters: List<Pair<String, Any?>>? = null
    ): T? {
        try {
            return Fuel.get(path, parameters)
                .authentication()
                .basic(login, password)
                .awaitStringResult()
                .fold({ jsonResponse ->
                    Timber.i("get jsonResponse is $jsonResponse")
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
        parameters: List<Pair<String, Any?>>? = null
    ): T? {
        try {
            return Fuel.post(path, parameters)
                .authentication()
                .basic(login, password)
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
        parameters: Any?
    ): T? {
        try {
            return Fuel.post(path)
                .jsonBody(JSONObject(gson.toJson(parameters)).toString(), Charsets.UTF_8)
                .authentication()
                .basic(login, password)
                .awaitStringResult()
                .fold({ jsonResponse ->
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