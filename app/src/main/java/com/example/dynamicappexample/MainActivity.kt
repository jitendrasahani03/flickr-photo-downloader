package com.example.dynamicappexample


import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : BaseActivity() ,GetRawData.OnDownloadComplete,
    GetFlickrJsonData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener{
    private val TAG = "MainActivity"
    private val flickrRecyclerAdapter = FlickrRecyclerViewAdapter(ArrayList())
    private lateinit var getResult: String
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        activateToolbar(false)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this,recyclerView,this))
        recyclerView.adapter = flickrRecyclerAdapter

        toolbar.setTitleTextColor(ContextCompat.getColor(applicationContext,R.color.white))
        Log.d(TAG, "OnCreate: Called")

        val url = createUri("https://api.flickr.com/services/feeds/photos_public.gne","android,oreo","en-us",true)
        val getRawData = GetRawData(this)
        //getRawData.setOnDownloadCompleteListener(this)
        getRawData.execute<String>(url)
        //downloadURL(url)
    }

    override fun onItemClick(view: View, position: Int) {
        //Toast.makeText(this,"Normal tap",Toast.LENGTH_SHORT).show()
        val photo = flickrRecyclerAdapter.getPhoto(position)
        if (photo != null){
            val intent = Intent(this,PhotoDetailsActivity::class.java)
            //pass the photo object as extra
            intent.putExtra(PHOTO_TRANSFER,photo)
            startActivity(intent)
        }
    }

    override fun onItemLongClick(view: View, position: Int) {
        //Toast.makeText(this,"Long tap",Toast.LENGTH_SHORT).show()
        val photo = flickrRecyclerAdapter.getPhoto(position)
        if (photo != null){
            val intent = Intent(this,PhotoDetailsActivity::class.java)
            //pass the photo object as extra
           intent.putExtra(PHOTO_TRANSFER,photo)
            startActivity(intent)
        }
    }

    private fun createUri(baseURL: String, searchCriteria: String, lang: String, matchAll: Boolean): String{
    Log.d(TAG,"createUri starts")
    val uri = Uri.parse(baseURL).
            buildUpon().
            appendQueryParameter("tags",searchCriteria).
            appendQueryParameter("tagmode",if(matchAll) "ALL" else "ANY").
            appendQueryParameter("lang",lang).
            appendQueryParameter("format","json").
            appendQueryParameter("nojsoncallback","1").
            build()
    Log.d(TAG,"created uri is $uri")
    return uri.toString()
}
    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete is completed") //$data
            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute<String>(data)
        } else {
            Log.d(TAG, "onDownloadComplete failed with $status and Error message $data")
        }

    }
    override fun onDataAvailable(data: ArrayList<Photo>) {
        Log.d(TAG, "onDataAvailable starts")
        flickrRecyclerAdapter.loadNewData(data)
        Log.d(TAG, "onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.d(TAG, "onError called ${exception.message}")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_search -> {
                startActivity(Intent(this,SearchActivity::class.java))
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        sharedPref = getSharedPreferences("tags", MODE_PRIVATE)
        getResult = sharedPref.getString(FLICKR_QUERY,"").toString()
    Log.d(TAG, "onResume: $getResult")
        if (getResult != null) {
            if (getResult.isNotEmpty()){
                val url = createUri("https://api.flickr.com/services/feeds/photos_public.gne",getResult,"en-us",true)
                val getRawData = GetRawData(this)
                //getRawData.setOnDownloadCompleteListener(this)
                getRawData.execute<String>(url)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPref.edit().remove("tags").apply()
        getResult = ""
    }
}