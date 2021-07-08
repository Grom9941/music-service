package com.example.musicservice.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicservice.MainActivity
import com.example.musicservice.R
import com.example.musicservice.adapters.AdapterAlbum
import com.example.musicservice.data.ExampleAlbums

/**
 * A simple [Fragment] subclass.
 */
class AllAlbumFragment : Fragment() {

    //private val service = ServiceVolley()
    private val apiController = MainActivity.apiController
    private val url = "https://source.unsplash.com/random"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_album, container, false)

        Toast.makeText(this.context, "album", Toast.LENGTH_SHORT).show()
        val recyclerViewAlbum = view.findViewById(R.id.recycler_view_all_album) as RecyclerView

        val adapter = AdapterAlbum(ArrayList(), true)
        generateDataAlbum(adapter)

        recyclerViewAlbum.adapter = adapter
        recyclerViewAlbum.layoutManager = GridLayoutManager(this.context, 2)
        recyclerViewAlbum.setHasFixedSize(true)
        return view
    }

    private fun generateDataAlbum(adapter: AdapterAlbum) {
        val list = ArrayList<ExampleAlbums>()

        try {
            val pathGet = "album/all"
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
                "Something wrong with getting albums data",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
}
