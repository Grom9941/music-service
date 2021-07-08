package com.example.musicservice.volley

import android.util.Log
import org.json.JSONObject

class APIController constructor(serviceInjection: ServiceInterface) : ServiceInterface {
    private val service: ServiceInterface = serviceInjection

    override fun setToken(userToken: String) {
        service.setToken(userToken)
    }

    override fun get(path: String, completionHandler: (response: JSONObject?) -> Unit) {
        Log.v("get api_controller", "get")
        service.get(path, completionHandler)
    }

    override fun get(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit
    ) {
        Log.v("get api_controller", "get")
        service.get(path, params, completionHandler)
    }

    override fun post(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit
    ) {
        Log.v("post api_controller", "post")
        service.post(path, params, completionHandler)
    }

    override fun put(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit
    ) {
        Log.v("post api_controller", "post")
        service.post(path, params, completionHandler)
    }

    override fun delete(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit
    ) {
        Log.v("delete api_controller", "delete")
        service.post(path, params, completionHandler)
    }
}