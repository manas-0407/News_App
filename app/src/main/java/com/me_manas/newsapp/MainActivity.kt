package com.me_manas.newsapp

import MySingleton
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest


class MainActivity : AppCompatActivity(), NewsItemClicked {

     private lateinit var mAdapter:NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerViewref: RecyclerView = findViewById(R.id.recyclerView)
        recyclerViewref.layoutManager =LinearLayoutManager(this)
        fetchDAta()
        mAdapter= NewsListAdapter(this)
        recyclerViewref.adapter=mAdapter
    }


    private fun fetchDAta(){
        val url="https://saurav.tech/NewsAPI/top-headlines/category/science/in.json"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,
            url,
            null,
            {
                val newsjsonarray= it.getJSONArray("articles")
                val newsArray= ArrayList<News>()

                for(i in 0 until newsjsonarray.length()){
                    val newsJsonObjAti = newsjsonarray.getJSONObject(i)
                    val news = News(
                        newsJsonObjAti.getString("title"),
                        newsJsonObjAti.getString("author"),
                        newsJsonObjAti.getString("url"),
                        newsJsonObjAti.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
            {
              Toast.makeText(this,"Error in Response",Toast.LENGTH_LONG).show()
            })
        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    override fun onItemClicked(items: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(items.url))
    }
}