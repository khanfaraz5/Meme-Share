package com.example.memeshare

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeshare.databinding.ActivityMainBinding
import java.util.Random

class MainActivity : AppCompatActivity() {
    var currentImgUrl: String? = null
    private var isDarkTheme = true
    private  lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadMeme()

        binding.changeThemeBtn.setOnClickListener {
            toggleTheme()
        }

    }

    private fun toggleTheme() {

        isDarkTheme = !isDarkTheme

        val newBackgroundColor:Int = if(isDarkTheme){
            getRandomColor()
        }
        else{
            getRandomColor()
        }

        binding.root.setBackgroundColor(newBackgroundColor)
    }

    private fun getRandomColor(): Int {
        val random = Random()
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }

    private fun loadMeme(){

        binding.progressBar.visibility = View.VISIBLE
        // just copied code from volley github sending request

        // Instantiate the RequestQueue.

        val url = "https://meme-api.com/gimme"  // pasting meme api url from github

// Request a string response o .
// oorom the provided URL.
        val jsonObjectRequest = JsonObjectRequest(

            Request.Method.GET, url, null,
            { response ->
                currentImgUrl = response.getString("url")
                Glide.with(this)
                    .load(currentImgUrl)
                    .listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .into(binding.memeImageView)
            },
            {
                Toast.makeText(this,"Something went wrong !!!", Toast.LENGTH_SHORT).show()
            })

// Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest) // calling getInstance function of MySingleton class for request queue
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout this cool meme I found on reddit: $currentImgUrl")
        val chooser = Intent.createChooser(intent, "Share this meme using...")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}