package com.example.musicservice.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicservice.MainActivity
import com.example.musicservice.data.ExampleMusics

import com.example.musicservice.R
import com.example.musicservice.adapters.AdapterAllMusic
import kotlinx.android.synthetic.main.fragment_all_music.view.*

/**
 * A simple [Fragment] subclass.
 */
class AllMusicFragment : Fragment() {

    private val apiController = MainActivity.apiController
    private val url = "https://source.unsplash.com/random"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_music, container, false)

        Toast.makeText(this.context, "music", Toast.LENGTH_SHORT).show()

        val pathGet = "music/all"
        val adapter = AdapterAllMusic(ArrayList())
        generateDataMusic(pathGet, adapter)

        val recyclerViewMusic = view.findViewById(R.id.recycler_view_all_music) as RecyclerView

        recyclerViewMusic.adapter = adapter
        recyclerViewMusic.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recyclerViewMusic.setHasFixedSize(true)

        view.all_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter.filter(query)
                return false
            }

        })
        return view
    }

    private fun generateDataMusic(pathGet: String, adapter: AdapterAllMusic) {
        val list = ArrayList<ExampleMusics>()

        try {
            Log.v("get before", pathGet)
            apiController.get(pathGet) { response ->

                val jsonArray = response?.getJSONArray("data")
                if (jsonArray != null) {
                    for (i in 0 until jsonArray.length()) {

                        val data = jsonArray.getJSONObject(i)
                        val item = ExampleMusics(
                            data.getInt("id"), url,
                            data.getString("song_name"), data.getString("song_duration"),
                            data.getString("song_release")
                        )
                        Log.v("item_loop", item.toString())
                        Log.v("music_list_loop", list.toString())
                        list.add(item)

                    }
                    adapter.update(list)
                }
            }
        } catch (e: Exception) {
            Log.v("error", e.toString())
            Toast.makeText(
                this.context,
                "Something wrong with getting musics data",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        Log.v("music_list", list.toString())
    }


}
