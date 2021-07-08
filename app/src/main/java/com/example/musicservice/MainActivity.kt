package com.example.musicservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.musicservice.volley.APIController
import com.example.musicservice.volley.ServiceVolley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://source.unsplash.com/random"

        val imagesTask = ImagesTask()
        imagesTask.execute(url)

        login.setOnClickListener(this)
        create_account.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login -> {
                try {
                    val pathPost = "auth/signin"
                    val params = JSONObject()
                    params.put("email", email.text)
                    params.put("password", password.text)
                    params.put("nickname", nickname.text)
                    Log.v("post before", params.toString())
                    apiController.post(pathPost, params) { response ->
                        val jsonObject = response?.getJSONObject("data")
                        if (jsonObject != null) {
                            Log.v("token", jsonObject.getString("token"))
                            apiController.setToken(jsonObject.getString("token"))
                            Log.v("post_data", jsonObject.getString("email"))
                            Log.v("post_data", jsonObject.getString("user_nickname"))
                        }
                    }

                    val loginSucceed = Intent(this, MusicService::class.java)
                    startActivity(loginSucceed)

                } catch (e: Exception) {
                    Log.v("error", e.toString())
                    Toast.makeText(this, "Data are wrong", Toast.LENGTH_SHORT).show()
                }
                val loginSucceed = Intent(this, MusicService::class.java)
                startActivity(loginSucceed)
            }
            R.id.create_account -> {
                try {
                    val pathPost = "auth/signup"
                    val params = JSONObject()
                    params.put("email", email.text)
                    params.put("password", password.text)
                    params.put("nickname", nickname.text)
                    Log.v("post before", pathPost)
                    apiController.post(pathPost, params) { response ->

                        Toast.makeText(this, "Account was created", Toast.LENGTH_SHORT).show()
                        val jsonObject: JSONObject = response!!.getJSONObject("data")
                        Log.v("post_data", jsonObject.getString("email"))
                        Log.v("post_data", jsonObject.getString("user_nickname"))
                        Log.v("post_data", jsonObject.getString("token"))
                    }
                } catch (e: Exception) {
                    Log.v("error", e.toString())
                    Toast.makeText(this, "This email exists", Toast.LENGTH_SHORT).show()
                }
                //add data into base

            }
        }
    }

    companion object {
        private val service = ServiceVolley()
        val apiController = APIController(service)
    }
}
