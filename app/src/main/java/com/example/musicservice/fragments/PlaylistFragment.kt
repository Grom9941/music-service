package com.example.musicservice.fragments


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicservice.MainActivity
import com.example.musicservice.R
import com.example.musicservice.adapters.AdapterAlbum
import com.example.musicservice.adapters.AdapterPlaylist
import com.example.musicservice.data.ExampleAlbums
import com.example.musicservice.data.ExamplePlaylists
import kotlinx.android.synthetic.main.fragment_playlist.view.*
import kotlinx.android.synthetic.main.layout_dialog.view.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class PlaylistFragment : Fragment() {

    //private val service = ServiceVolley()
    private val apiController = MainActivity.apiController
    private val url = "https://source.unsplash.com/random"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_playlist, container, false)

        val recyclerViewPlaylist = view.findViewById(R.id.recycler_view_playlist) as RecyclerView
        val recyclerViewAlbum = view.findViewById(R.id.recycler_view_album) as RecyclerView

        val adapter1 = AdapterPlaylist(ArrayList())
        generateDataPlaylist(adapter1)

        recyclerViewPlaylist.adapter = adapter1
        recyclerViewPlaylist.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPlaylist.setHasFixedSize(true)

        val adapter2 = AdapterAlbum(ArrayList(), false)
        generateDataAlbum(adapter2)

        recyclerViewAlbum.adapter = adapter2
        recyclerViewAlbum.layoutManager = GridLayoutManager(this.context, 2)
        recyclerViewAlbum.setHasFixedSize(true)

        view.button_add_new_playlist.setOnClickListener {
            openDialog(adapter1)
        }
        return view
    }

    private fun openDialog(adapter: AdapterPlaylist) {
        val dialog = LayoutInflater.from(context).inflate(R.layout.layout_dialog, null)
        val builder = AlertDialog.Builder(context).setView(dialog).setTitle("Create playlist")
            .setNegativeButton("cancel") { _, _ -> }
            .setPositiveButton("ok") { _, _ ->
                val title = dialog.playlist_title.text
                val date = Calendar.getInstance().time.toString()

                try {
                    val pathPost = "playlist"
                    val params = JSONObject()
                    params.put("playlist_title", title)
                    params.put("playlist_release", date)
                    params.put("track_number", 0 + 1)//ИСПАРВЬ В БД
                    Log.v("post before", pathPost)
                    apiController.post(pathPost, params) { generateDataPlaylist(adapter) }

                } catch (e: Exception) {
                    Log.v("error", e.toString())
                    Toast.makeText(
                        context,
                        "Something wrong with adding playlist",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        builder.show()
    }

    private fun generateDataPlaylist(adapter: AdapterPlaylist) {
        val list = ArrayList<ExamplePlaylists>()

        try {
            val pathGet = "playlist"
            Log.v("get before", pathGet)
            apiController.get(pathGet) { response ->
                val jsonArray = response?.getJSONArray("data")
                if (jsonArray != null) {
                    for (i in 0 until jsonArray.length()) {
                        val data = jsonArray.getJSONObject(i)
                        val item = ExamplePlaylists(
                            data.getInt("id"), url,
                            data.getString("playlist_title"), data.getString("playlist_release"),
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
                "Something wrong with getting playlists data",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun generateDataAlbum(adapter: AdapterAlbum) {
        val list = ArrayList<ExampleAlbums>()

        try {
            val pathGet = "album"
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
