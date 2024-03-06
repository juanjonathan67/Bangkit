package com.dicoding.proyekakhirbeginnerandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ListTourAdapter(private val listTour: ArrayList<Tour>) : RecyclerView.Adapter<ListTourAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_item_view)
        val textTitle: TextView = itemView.findViewById(R.id.text_item_title)
        val textLocation: TextView = itemView.findViewById(R.id.text_item_location)
        val textRating: TextView = itemView.findViewById(R.id.text_item_rating)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Tour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_wisata, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listTour.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, description, location, rating, image) = listTour[position]
        holder.textTitle.text = name
        holder.textLocation.text = location
        holder.textRating.text = rating

        Glide.with(holder.itemView.context)
            .load(image)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listTour[holder.adapterPosition])
        }
    }
}