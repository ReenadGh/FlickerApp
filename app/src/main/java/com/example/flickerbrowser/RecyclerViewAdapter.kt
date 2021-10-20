package com.example.flickerbrowser

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.image_viewer.view.*
import kotlinx.android.synthetic.main.photo_item.view.*

class RecyclerViewAdapter (private val activity : Activity , private val photos: List<Image>): RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>(){
    class ItemViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.photo_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val link = photos[position].link
        val title = photos[position].title

        holder.itemView.apply {
            Glide.with(this)
                .load(link)
                .into(imageItem)
            Log.d("linkU",link )
            imageItem.setOnClickListener {
                displayImage(activity , link, title)

            }


        }
    }

    override fun getItemCount() = photos.size


fun displayImage(activity : Activity , link : String, title : String){
    val view = View.inflate(activity, R.layout.image_viewer, null)
    val builder = AlertDialog.Builder(activity)
    builder.setView(view)

    val dialog = builder.create()
    dialog.show()
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.setCancelable(false)

    view.titleshow.text = title
    Glide.with(activity)
        .load(link)
        .into(view.imageView1)


    view.imageButton.setOnClickListener {
        dialog.dismiss()
    }


}}
