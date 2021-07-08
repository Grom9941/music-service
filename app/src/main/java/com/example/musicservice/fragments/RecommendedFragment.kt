package com.example.musicservice.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicservice.*
import com.example.musicservice.adapters.AdapterAlbum
import com.example.musicservice.adapters.AdapterMusic
import com.example.musicservice.data.ExampleAlbums
import com.example.musicservice.data.ExampleMusics

/**
 * A simple [Fragment] subclass.
 */
class RecommendedFragment : Fragment() {

    //private val service = ServiceVolley()
    private val apiController = MainActivity.apiController
    private val url = "https://source.unsplash.com/random"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recommended, container, false)

        val recyclerViewChart = view.findViewById(R.id.recycler_chart) as RecyclerView

        val adapter1 = AdapterMusic(ArrayList(), false)
        generateDataMusic(adapter1)

        recyclerViewChart.adapter = adapter1
        recyclerViewChart.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recyclerViewChart.setHasFixedSize(true)

        val recyclerViewNewAlbum = view.findViewById(R.id.recycler_new_albums) as RecyclerView

        val adapter2 = AdapterAlbum(ArrayList(), true)
        generateDataAlbum(adapter2)

        recyclerViewNewAlbum.adapter = adapter2
        recyclerViewNewAlbum.layoutManager = GridLayoutManager(this.context, 3)
        recyclerViewNewAlbum.setHasFixedSize(true)

        return view
    }


    private fun generateDataMusic(adapter: AdapterMusic): List<ExampleMusics> {
        val list = ArrayList<ExampleMusics>()

        try {
            val pathGet = "top"
            Log.v("get before", pathGet)
            apiController.get(pathGet) { response ->
                val jsonArray = response?.getJSONArray("data")
                if (jsonArray != null) {
                    for (i in 0 until jsonArray.length()) {
                        val data = jsonArray.getJSONObject(i)
                        val item = ExampleMusics(
                            data.getInt("song_id"), url,
                            data.getString("song_name"), data.getString("song_duration"),
                            data.getString("song_release")
                        )
                        list.add(item)
                    }
                    adapter.update(list)
                }
            }
        } catch (e: Exception) {
            Log.v("error", e.toString())
            Toast.makeText(
                this.context,
                "Something wrong with getting top musics data",
                Toast.LENGTH_SHORT
            ).show()
        }
        return list
    }

    private fun generateDataAlbum(adapter: AdapterAlbum): List<ExampleAlbums> {
        val list = ArrayList<ExampleAlbums>()

        try {
            val pathGet = "new"
            Log.v("get before", pathGet)
            apiController.get(pathGet) { response ->
                val jsonArray = response?.getJSONArray("data")
                if (jsonArray != null) {
                    for (i in 0 until jsonArray.length()) {
                        val data = jsonArray.getJSONObject(i)
                        val item = ExampleAlbums(
                            data.getInt("id"), url,
                            data.getString("album_title"), data.getString("album_release"),
                            data.getInt("track_number")
                        )
                        list.add(item)
                    }
                    adapter.update(list)
                }
            }
        } catch (e: Exception) {
            Log.v("error", e.toString())
            Toast.makeText(
                this.context,
                "Something wrong with getting new albums data",
                Toast.LENGTH_SHORT
            ).show()
        }
        return list
    }
}
