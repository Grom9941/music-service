package com.example.musicservice.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicservice.MainActivity
import com.example.musicservice.R
import com.example.musicservice.adapters.AdapterMusic
import com.example.musicservice.data.ExampleMusics
import kotlinx.android.synthetic.main.fragment_music.view.*

/**
 * A simple [Fragment] subclass.
 */
class MusicFragment : Fragment() {

    private val apiController = MainActivity.apiController
    private val url = "https://source.unsplash.com/random"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_music, container, false)
        var pathGet = "music"
        val adapter = AdapterMusic(ArrayList(), true)
        generateDataMusic(pathGet, adapter)

        val adapterSpinner = ArrayAdapter.createFromResource(
            this.context!!,
            R.array.sort,
            android.R.layout.simple_spinner_item
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        view.spinner.adapter = adapterSpinner
        view.spinner.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int,
                id: Long
            ) {
                when (parent?.getItemAtPosition(position).toString()) {
                    "by default" -> pathGet = "music"
                    "by name" -> pathGet = "music/name"
                    "by release" -> pathGet = "music/release"
                }
                generateDataMusic(pathGet, adapter)
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val recyclerViewMusic = view.findViewById(R.id.recycler_view_music) as RecyclerView


        recyclerViewMusic.adapter = adapter
        recyclerViewMusic.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recyclerViewMusic.setHasFixedSize(true)

        view.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

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

    private fun generateDataMusic(pathGet: String, adapter: AdapterMusic) {
        val list = ArrayList<ExampleMusics>()

        /*for (i in 0 until 4) {
            val item = ExampleMusics(
                1, url,
                "zz", "zz",
                "zz"
            )
            list.add(item)
        }
        adapter.update(list)*/

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
