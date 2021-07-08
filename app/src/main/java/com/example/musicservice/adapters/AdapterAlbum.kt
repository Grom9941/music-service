package com.example.musicservice.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicservice.MainActivity
import com.example.musicservice.data.ExampleAlbums
import com.example.musicservice.R
import com.example.musicservice.dialogs.DialogController
import com.example.musicservice.dialogs.Dialogs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_album.view.*
import org.json.JSONObject

class AdapterAlbum(myData: List<ExampleAlbums>, buttonAddToAlbums: Boolean) :
    RecyclerView.Adapter<AdapterAlbum.MyViewHolder>() {

    private val url = "https://source.unsplash.com/random"
    private val apiController = MainActivity.apiController

    private var filterListResult: List<ExampleAlbums> = myData
    val add: Boolean = buttonAddToAlbums
    private var context: Context? = null

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.main_card_album
        val imageView: ImageView = itemView.item_album_image
        val textView1: TextView = itemView.item_album_title
        val date: TextView = itemView.item_album_release
        val tracks: TextView = itemView.item_album_tracks
        val addToAlbums: ImageButton = itemView.button_add_album
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // create a new view
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_album,
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
        holder.textView1.text = currentElement.album_title
        holder.date.text = currentElement.date.take(4)
        holder.tracks.text = currentElement.tracks.toString()

        getTrackNumber("album/number", currentElement.id, holder)

        holder.cardView.setOnClickListener {
            val dialog = Dialogs()
            val dialogController = DialogController(dialog)
            dialogController.createDialogPlaylistOrAlbum(context!!, currentElement.id, true)
        }

        if (!add) {
            holder.cardView.setOnLongClickListener {
                val pathDelete = "album/delete"
                val pathGet = "album"
                deleteAndUpdate(pathDelete, pathGet, currentElement)
                return@setOnLongClickListener true
            }
        }

        if (add) {
            holder.addToAlbums.setOnClickListener {
                val pathAdd = "album/user"
                val pathGet = "new"
                deleteAndUpdate(pathAdd, pathGet, currentElement)
            }
        } else {
            holder.addToAlbums.visibility = View.GONE
        }
    }

    private fun deleteAndUpdate(
        pathDelete: String,
        pathGet: String,
        currentElement: ExampleAlbums
    ) {

        try {
            Log.v("delete before", pathDelete)
            val params = JSONObject()
            params.put("album_id", currentElement.id)
            apiController.post(pathDelete, params) { getUpdateData(pathGet) }
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
            params.put("album_id", id)
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

    private fun getUpdateData(pathGet: String) {
        val list = ArrayList<ExampleAlbums>()

        try {
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
                    update(list)
                    //adapter.update(list)
                } else {
                    update(ArrayList())
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

    fun update(data: List<ExampleAlbums>) {
        filterListResult = data
        notifyDataSetChanged()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = filterListResult.size
}