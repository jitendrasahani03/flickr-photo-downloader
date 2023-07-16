package com.example.dynamicappexample

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FlickrImageViewHolder(view: View): RecyclerView.ViewHolder(view){

    var thumbnail: ImageView = view.findViewById(R.id.placeholder)
    var title: TextView = view.findViewById(R.id.title_text)
}

class FlickrRecyclerViewAdapter(private var photoList: List<Photo>): RecyclerView.Adapter<FlickrImageViewHolder>() {
    private val TAG = "FlickrRecyclerViewAdapt"
    override fun getItemCount(): Int {
        Log.d(TAG,"getItemCount called")
        return if(photoList.isNotEmpty()) photoList.size else 1
    }
    fun loadNewData(newPhotos: List<Photo>){
        photoList = newPhotos
        notifyDataSetChanged()
    }
    fun getPhoto(position: Int): Photo?{

        return if (photoList.isNotEmpty()) photoList[position] else null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        //Layout manager to create view
        Log.d(TAG,"onCreateViewHolder called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse,parent,false)
        return FlickrImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {
        //new data with exiting view called by layout manager
        Log.d(TAG,"onBindViewHolder called")
        if(photoList.isEmpty()){
            holder.thumbnail.setImageResource(R.drawable.thumbnail)
            holder.title.setText(R.string.empty_image)
        }
        else{
            val photoItem = photoList[position]
        Picasso.get().load(photoItem.image)
            .error(R.drawable.thumbnail)
            .placeholder(R.drawable.thumbnail)
            .into(holder.thumbnail)
        holder.title.text = photoItem.title

    }}
}