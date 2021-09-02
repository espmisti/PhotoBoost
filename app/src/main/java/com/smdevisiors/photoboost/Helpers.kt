package com.smdevisiors.photoboost

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL


object Helpers {

    private const val TAG = "eblan"
    var link = "https://data.av100.ru/socialapi.ashx?key=e9b9368f-0f87-4bfd-baab-e40ba235ec85&method=byphoto"

    enum class RequestMethod {
        GET, POST
    }

    fun makeRequest(url: String, method: RequestMethod, parameters: Map<String, String>): String {
        try {
            var link = url
            val constructor = StringBuilder()
// val keys: Array<Any> = parameters.keys.toTypedArray() as Any
            for (i in parameters) {
                constructor.append(parameters.keys.first())
                constructor.append("=")
                constructor.append(parameters.getValue(parameters.keys.first()))
                constructor.append("&")
            }
// Log.i(TAG, "makeRequest: $constructor")
            if (method == RequestMethod.GET) link += "?$constructor"
            val connection = URL(link).openConnection() as HttpURLConnection
            connection.requestMethod = method.toString()
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            connection.doOutput = true
            connection.doInput = true
            if (method != RequestMethod.GET) {
                connection.outputStream.write(constructor.toString().toByteArray())
            }
            Log.i(TAG, "responseCode: ${connection.responseCode}")
            Log.i(TAG, "responseMessage: ${connection.responseMessage}")
            val stream = connection.getInputStream()
            val responseBuilder = StringBuilder()
            var temp = stream.read()
            while (temp > 0) {
                responseBuilder.append(temp.toChar())
                temp = stream.read()
            }
            return responseBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(TAG, "ОШИБКА: $e")
            return "пошел нахуй"
        }
    }

    fun getUserById(url: String, method: RequestMethod, parameters: Map<String, String>): String {
        try {
            var link = url
            val constructor = StringBuilder()
// val keys: Array<Any> = parameters.keys.toTypedArray() as Any
            for (i in parameters) {
                constructor.append(parameters.keys.first())
                constructor.append("=")
                constructor.append(parameters.getValue(parameters.keys.first()))
                constructor.append("&")
            }
// Log.i(TAG, "makeRequest: $constructor")
            if (method == RequestMethod.GET) link += "?$constructor"
            val connection = URL(link).openConnection() as HttpURLConnection
            connection.requestMethod = method.toString()
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            connection.doOutput = true
            connection.doInput = true
            if (method != RequestMethod.GET) {
                connection.outputStream.write(constructor.toString().toByteArray())
            }
            Log.i(TAG, "responseCode: ${connection.responseCode}")
            Log.i(TAG, "responseMessage: ${connection.responseMessage}")
            val stream = connection.getInputStream()
            val responseBuilder = StringBuilder()
            var temp = stream.read()
            while (temp > 0) {
                responseBuilder.append(temp.toChar())
                temp = stream.read()
            }
            return responseBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(TAG, "ОШИБКА: $e")
            return "пошел нахуй"
        }
    }
}