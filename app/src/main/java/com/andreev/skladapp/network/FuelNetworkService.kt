package com.andreev.skladapp.network

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.coroutines.awaitStringResult
import com.google.gson.Gson
import org.json.JSONObject

abstract class FuelNetworkService {
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
            return Fuel.get(path, parameters)
                .awaitStringResult()
                .fold({ jsonResponse ->
                    return@fold gson.fromJson(jsonResponse, clazz)
                }, { error ->
                    return@fold gson.fromJson(error.errorData.toString(Charsets.UTF_8), clazz)
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
                .awaitStringResult()
                .fold({ jsonResponse ->
                    return@fold gson.fromJson(jsonResponse, clazz)
                }) { error ->
                    return@fold gson.fromJson(error.errorData.toString(Charsets.UTF_8), clazz)
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
                .awaitStringResult()
                .fold({ jsonResponse ->
                    return@fold gson.fromJson(jsonResponse, clazz)
                }) { error ->
                    return@fold gson.fromJson(error.errorData.toString(Charsets.UTF_8), clazz)
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}