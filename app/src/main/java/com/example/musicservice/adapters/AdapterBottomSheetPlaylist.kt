package com.example.musicservice.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicservice.MainActivity
import com.example.musicservice.R
import com.example.musicservice.data.ExamplePlaylists
import com.example.musicservice.dialogs.Dialogs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_playlist.view.*
import org.json.JSONObject

class AdapterBottomSheetPlaylist(myData: List<ExamplePlaylists>) :
    RecyclerView.Adapter<AdapterBottomSheetPlaylist.MyViewHolder>() {

    private val apiController = MainActivity.apiController

    private var filterListResult: List<ExamplePlaylists> = myData
    private var context: Context? = null

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.main_card_playlist
        val imageView: ImageView = itemView.item_playlist_image
        val textView1: TextView = itemView.item_playlist_title
        val date: TextView = itemView.item_playlist_release
        val tracks: TextView = itemView.item_playlist_tracks
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // create a new view
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_playlist,
            parent, false
        )
        context = parent.context
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val currentElement = filterListResult[position]

        val urlPicture = "https://picsum.photos/seed/${currentElement.id}/300/300"

        Picasso.with(context).load(urlPicture).into(holder.imageView)
        holder.textView1.text = currentElement.playlist_title
        holder.date.text = currentElement.date.take(4)
        holder.tracks.text = currentElement.tracks.toString()

        getTrackNumber("playlist/number", currentElement.id, holder)

        holder.cardView.setOnClickListener {
            addToPlaylist(currentElement.id, Dialogs.songId)
        }
    }

    private fun addToPlaylist(playlistId: Int, songId: Int) {

        try {
            val pathAdd = "playlist/music"
            Log.v("get before", pathAdd)
            val params = JSONObject()
            params.put("playlist_id", playlistId)
            params.put("song_id", songId)
            apiController.post(pathAdd, params) { }
        } catch (e: Exception) {
            Log.v("error", e.toString())
            Toast.makeText(
                this.context,
                "Something wrong with getting musics data",
                Toast.LENGTH_SHORT
            )
                .show()
        }

    }

    private fun getTrackNumber(pathGet: String, id: Int, holder: MyViewHolder) {
        var number: Int
        try {
            Log.v("get before", pathGet)
            val params = JSONObject()
            params.put("playlist_id", id)
            apiController.post(pathGet, params) { response ->
                val jsonObject = response?.getJSONArray("data")?.getJSONObject(0)
                if (jsonObject != null) {
                    number = jsonObject.getInt("count")
                    holder.tracks.text = number.toString()
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
    }

    fun update(data: List<ExamplePlaylists>) {
        filterListResult = data
        notifyDataSetChanged()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = filterListResult.size
}