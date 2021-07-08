package com.example.musicservice.volley

import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import java.util.*

class ServiceVolley : ServiceInterface {
    val TAG = ServiceVolley::class.java.simpleName
    var authToken = ""
    val basePath = "http://192.168.0.102:3000/api/v1/"


    override fun setToken(userToken: String) {
        authToken = userToken
        Log.v("token", authToken)
    }

    override fun get(path: String, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjReq = object : JsonObjectRequest(Method.GET, basePath + path, null,
            Response.Listener<JSONObject> { response ->
                Log.v("get service_volley", "get")
                Log.d(TAG, "/post request OK! Response: $response")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                if (error.message != null) {
                    Log.v("get err service_volley", error.message!!)
                }
                VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                completionHandler(null)
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = authToken
                return headers
            }
        }
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun get(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit
    ) {
        val jsonObjReq = object : JsonObjectRequest(Method.GET, basePath + path, params,
            Response.Listener<JSONObject> { response ->
                Log.v("get service_volley", "get")
                Log.d(TAG, "/post request OK! Response: $response")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                if (error.message != null) {
                    Log.v("get err service_volley", error.message!!)
                }
                VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                completionHandler(null)
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = authToken
                return headers
            }

            override fun getParams(): MutableMap<String, String> {
                return mutableMapOf(Pair("song_id", params.getString("song_id").toString()))
            }
        }
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun post(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit
    ) {
        val jsonObjReq = object : JsonObjectRequest(Method.POST, basePath + path, params,
            Response.Listener<JSONObject> { response ->
                Log.v("post service_volley", "post")
                Log.d(TAG, "/post request OK! Response: $response")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                if (error.message != null) {
                    Log.v("post err service_volley", error.message!!)
                }
                VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                completionHandler(null)
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = authToken
                return headers
            }
        }
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun put(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit
    ) {
        val jsonObjReq = object : JsonObjectRequest(Method.PUT, basePath + path, params,
            Response.Listener<JSONObject> { response ->
                Log.v("post service_volley", "post")
                Log.d(TAG, "/post request OK! Response: $response")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                if (error.message != null) {
                    Log.v("post err service_volley", error.message!!)
                }
                VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                completionHandler(null)
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = authToken
                return headers
            }
        }
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun delete(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit
    ) {
        val jsonObjReq = object : JsonObjectRequest(Method.DELETE, basePath + path, params,
            Response.Listener<JSONObject> { response ->
                Log.v("post service_volley", "post")
                Log.d(TAG, "/post request OK! Response: $response")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                if (error.message != null) {
                    Log.v("post err service_volley", error.message!!)
                }
                VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                completionHandler(null)
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = authToken
                return headers
            }
        }
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }
}
