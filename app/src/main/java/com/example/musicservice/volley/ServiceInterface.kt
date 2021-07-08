package com.example.musicservice.volley

import org.json.JSONObject

interface ServiceInterface {
    fun setToken(userToken: String)

    fun get(path: String, completionHandler: (response: JSONObject?) -> Unit)

    fun get(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)

    fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)

    fun put(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)

    fun delete(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
}