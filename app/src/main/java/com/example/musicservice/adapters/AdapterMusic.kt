package com.example.musicservice.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.musicservice.MainActivity
import com.example.musicservice.R
import com.example.musicservice.data.ExampleMusics
import com.example.musicservice.dialogs.DialogController
import com.example.musicservice.dialogs.Dialogs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_music.view.*
import org.json.JSONObject

class AdapterMusic(myData: List<ExampleMusics>, private val deletable: Boolean) :
    RecyclerView.Adapter<AdapterMusic.MyViewHolder>(), Filterable {

    private val url = "https://source.unsplash.com/random"
    private val apiController = MainActivity.apiController

    var filterListResult: List<ExampleMusics> = myData
    var filterListResult1: List<ExampleMusics> = filterListResult
    private var context: Context? = null

    override fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(charString: CharSequence?): FilterResults {
                val charSearch = charString.toString()
                filterListResult = if (charSearch.isEmpty())
                    filterListResult1
                else {
                    val resultList = ArrayList<ExampleMusics>()
                    for (row in filterListResult1) {
                        if (row.text_music.toLowerCase().contains(charSearch.toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResult = FilterResults()
                filterResult.values = filterListResult
                return filterResult
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults?
            ) {
                filterListResult = filterResults!!.values as List<ExampleMusics>
                notifyDataSetChanged()
            }

        }
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: ConstraintLayout = itemView.main_layout
        val imageView: ImageView = itemView.item_image
        val textView1: TextView = itemView.item_text_music
        val textView2: TextView = itemView.item_text_release
        val duration: TextView = itemView.item_duration
        val toArtist: ImageButton = itemView.button_to_artist
        val addToPlaylist: ImageButton = itemView.button_add_to_playlist
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // create a new view
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_music,
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

        val urlPicture = "https://picsum.photos/seed/${currentElement.id}/300/300?portrait"

        //val imageTask = ImagesTask()
        //imageTask.setImageTask(holder.imageView)
        //imageTask.execute(url)
        //holder.imageView.setImage(url)
        //holder.imageView.setImageBitmap(ImagesTask().execute(url))
        Picasso.with(context).load(urlPicture).into(holder.imageView)
        //holder.imageView.setImageBitmap(MusicService.imageList[currentElement.id % (MusicService.imageList.size - 1)])
        //holder.imageView.setImageResource( RigPalette.fromColor(Color.GREEN))
        holder.textView1.text = currentElement.text_music
        holder.textView2.text = currentElement.text_release.take(10)
        holder.duration.text = currentElement.duration.take(5)
        holder.toArtist.setOnClickListener {
            //           when (it.id) {
            //               R.id.button_to_artist -> {
            val dialog = Dialogs()
            val dialogController = DialogController(dialog)
            dialogController.createDialogMusicArtist(context!!, currentElement.id)
//                }
            //           }
        }

        holder.addToPlaylist.setOnClickListener {
            //val pathDelete = "playlist/music"
            //val pathGet = "music"
            //deleteAndUpdate(pathDelete, pathGet, currentElement)
            val dialog = Dialogs()
            val dialogController = DialogController(dialog)
            dialogController.createDialogAddToPlaylist(context!!, currentElement.id)
        }

        if (deletable) {
            holder.layout.setOnLongClickListener {
                val pathDelete = "music/delete"
                val pathGet = "music"
                deleteAndUpdate(pathDelete, pathGet, currentElement)
                return@setOnLongClickListener true
            }
        }
    }

    private fun deleteAndUpdate(
        pathDelete: String,
        pathGet: String,
        currentElement: ExampleMusics
    ) {

        try {
            Log.v("get before", pathDelete)
            val params = JSONObject()
            params.put("song_id", currentElement.id)
            apiController.post(pathDelete, params) {
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
                            //adapter.update(list)
                            update(list)
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

    fun update(data: List<ExampleMusics>) {
        filterListResult = data
        filterListResult1 = data
        notifyDataSetChanged()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = filterListResult.size
}