package com.example.dynamicappexample

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.WindowCompat
import com.squareup.picasso.Picasso

class PhotoDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)
        activateToolbar(true)
        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo
        val photoTitle = findViewById<TextView>(R.id.photo_title)
        //using placeholder
        photoTitle.text = resources.getString(R.string.photo_title_text,photo.title)
        val photoTags = findViewById<TextView>(R.id.photo_tags)
        photoTags.text = photo.tags
        val photoAuthor = findViewById<TextView>(R.id.photo_author)
        photoAuthor.text = photo.author


        Picasso.get().load(photo.link)
            .error(R.drawable.thumbnail)
            .placeholder(R.drawable.thumbnail)
            .into(findViewById<ImageView>(R.id.imageView))
    }
}