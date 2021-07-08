package com.example.musicservice

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import java.net.URL


internal class ImagesTask : AsyncTask<String, Void, List<Bitmap>>() {

    private var exception: Exception? = null

    override fun doInBackground(vararg url: String): List<Bitmap>? {
        return try {
            val listImage: MutableList<Bitmap> = ArrayList()
            for (i in 0 until 10) {
                val image =
                    BitmapFactory.decodeStream(URL(url[0]).openConnection().getInputStream())
                Log.v("image", image.toString())
                listImage.add(image)
            }
            listImage
        } catch (e: Exception) {
            this.exception = e
            null
        }
    }

    override fun onPostExecute(listBitmap: List<Bitmap>?) {
        MusicService.imageList = listBitmap ?: ArrayList()
    }
}