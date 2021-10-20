package com.example.flickerbrowser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flickerbrowser.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
lateinit var ImageWindow : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lateinit var binding : ActivityMainBinding

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageSerach



        binding.imageSerach.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.imageSerach.clearFocus()
                getSearchPhotos(query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                getSearchPhotos(newText.toString())

                return false
            }


        })



        getSearchPhotos("cat")


    }

    private fun getSearchPhotos(searchText: String) {


        val api = Retrofit.Builder()
            .baseUrl("https://www.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)



        CoroutineScope(IO).launch {
            var ImagesList = arrayListOf<Image>()

            val response: Response<Photos?> =
                api.getphoto("services/rest/?method=flickr.photos.search&api_key=73ac8ab1a72c9e65947f0185a86dcd21&text=$searchText&format=json&nojsoncallback=1")!!
                    .awaitResponse()
            if (response.isSuccessful) {
                val photos = response.body()!!.photos.photo

                Log.d("thedata", photos.toString())
////        https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg

                for (photo in photos) {

                    var title = photo.title
                    var id = photo.id
                    var farm = photo.farm
                    var serverId = photo.server
                    var secretID = photo.secret
                    ImagesList.add(Image("https://farm$farm.staticflickr.com/$serverId/${id}_${secretID}.jpg",title))
                }


                withContext(Main) {



                    val myRV = findViewById<RecyclerView>(R.id.rvMain)
                    myRV.adapter = RecyclerViewAdapter( this@MainActivity , ImagesList)
                    myRV.layoutManager = GridLayoutManager(this@MainActivity , 3)
                    myRV.adapter!!.notifyDataSetChanged()


                }


            }
        }
    }
}








