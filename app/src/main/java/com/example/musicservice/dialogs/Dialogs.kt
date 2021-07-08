package com.example.musicservice.dialogs

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicservice.MainActivity
import com.example.musicservice.R
import com.example.musicservice.adapters.AdapterArtist
import com.example.musicservice.adapters.AdapterBottomSheetPlaylist
import com.example.musicservice.adapters.AdapterMusic
import com.example.musicservice.data.ExampleArtists
import com.example.musicservice.data.ExampleMusics
import com.example.musicservice.data.ExamplePlaylists
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import org.json.JSONObject

class Dialogs : DialogInterface {

    private val apiController = MainActivity.apiController
    private val url = "https://source.unsplash.com/random"

    override fun createDialogMusicArtist(context: Context, id: Int) {
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.bottom_sheet_dialog)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val urlPicture = "https://picsum.photos/seed/${id}/1000/300"


        Picasso.with(context).load(urlPicture).into(dialog.findViewById<ImageView>(R.id.main_image))
        val recyclerViewArtists = dialog.findViewById<RecyclerView>(R.id.recycler_view_artists)

        val adapter = AdapterArtist(ArrayList())
        generateDataMusicArtist(context, id, adapter)

        recyclerViewArtists?.adapter = adapter
        recyclerViewArtists?.layoutManager = GridLayoutManager(context, 3)
        recyclerViewArtists?.setHasFixedSize(true)
    }

    override fun createDialogPlaylistOrAlbum(context: Context, id: Int, isAlbum: Boolean) {
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.bottom_sheet_dialog_musics)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        val urlPicture = "https://picsum.photos/seed/${id}/1000/300"

        Picasso.with(context).load(urlPicture)
            .into(dialog.findViewById<ImageView>(R.id.main_image_music))
        val recyclerViewMusics = dialog.findViewById<RecyclerView>(R.id.recycler_view_musics)

        val adapter = AdapterMusic(ArrayList(), false)
        generateDataMusics(context, id, adapter, isAlbum)

        recyclerViewMusics?.adapter = adapter
        recyclerViewMusics?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewMusics?.setHasFixedSize(true)
    }

    override fun createDialogAddToPlaylist(context: Context, id: Int) {
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.bottom_sheet_dialog_playlist)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        val recyclerViewMusics =
            dialog.findViewById<RecyclerView>(R.id.bottom_sheet_recycler_view_playlist)

        val adapter = AdapterBottomSheetPlaylist(ArrayList())
        generateDataPlaylist(context, id, adapter)

        recyclerViewMusics?.adapter = adapter
        recyclerViewMusics?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewMusics?.setHasFixedSize(true)
    }

    private fun generateDataPlaylist(
        context: Context,
        id: Int,
        adapter: AdapterBottomSheetPlaylist
    ) {
        songId = id
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
                context,
                "Something wrong with getting playlists data",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun generateDataMusics(
        context: Context,
        id: Int,
        adapter: AdapterMusic,
        album: Boolean
    ) {
        val list = ArrayList<ExampleMusics>()

        try {
            val pathGet: String
            val params = JSONObject()
            if (album) {
                pathGet = "album/musics"
                params.put("album_id", id.toString())
            } else {
                pathGet = "playlist/musics"
                params.put("playlist_id", id.toString())
            }
            Log.v("get before", pathGet)
            apiController.post(pathGet, params) { response ->
                val jsonArray = response?.getJSONArray("data")
                if (jsonArray != null) {
                    for (i in 0 until jsonArray.length()) {
                        val data = jsonArray.getJSONObject(i)
                        val item = ExampleMusics(
                            data.getInt("id"), url,
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
                context, "Something wrong with getting playlists data",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun generateDataMusicArtist(context: Context, id: Int, adapter: AdapterArtist) {
        val list = ArrayList<ExampleArtists>()

        try {
            val pathGet = "music/artist"
            val params = JSONObject()
            params.put("song_id", id.toString())
            Log.v("get before", pathGet)
            apiController.post(pathGet, params) { response ->
                val jsonArray = response?.getJSONArray("data")
                if (jsonArray != null) {
                    for (i in 0 until jsonArray.length()) {
                        val data = jsonArray.getJSONObject(i)
                        val item = ExampleArtists(
                            data.getInt("id"),
                            url,
                            data.getString("first_name") + " " + data.getString("last_name"),
                            data.getString("artist_birthday"),
                            if (data.isNull("income")) 0 else data.getInt("income")
                        )
                        list.add(item)
                    }
                    adapter.update(list)
                }
            }
        } catch (e: Exception) {
            Log.v("error", e.toString())
            Toast.makeText(context, "Something wrong with getting musics data", Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        var songId = 0
    }
}