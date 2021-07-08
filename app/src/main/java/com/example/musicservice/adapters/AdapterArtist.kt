package com.example.musicservice.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicservice.R
import com.example.musicservice.data.ExampleArtists
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_artist.view.*

class AdapterArtist(myData: List<ExampleArtists>) :
    RecyclerView.Adapter<AdapterArtist.MyViewHolder>() {

    private var filterListResult: List<ExampleArtists> = myData
    private var context: Context? = null

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.item_artist_image
        val textView1: TextView = itemView.item_artist_name_surname
        val date: TextView = itemView.item_artist_birthday
        val income: TextView = itemView.item_artist_income
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // create a new view
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_artist,
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

        //val urlPicture = "http://lorempixel.com/output/people-q-c-300-300-${currentElement.id}.jpg"
        val urlPicture = "https://api.adorable.io/avatars/285/${currentElement.id}.png"
        Picasso.with(context).load(urlPicture).into(holder.imageView)
        holder.textView1.text = currentElement.name
        holder.date.text = currentElement.artist_birthday.take(4)
        holder.income.text = currentElement.income.toString()
    }

    fun update(data: List<ExampleArtists>) {
        filterListResult = data
        notifyDataSetChanged()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = filterListResult.size
}