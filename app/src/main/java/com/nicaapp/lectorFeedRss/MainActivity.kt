package com.nicaapp.lectorFeedRss

import android.app.ProgressDialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.nicaapp.lectorFeedRss.Adapter.FeedAdapter
import com.nicaapp.lectorFeedRss.Commom.HTTPDataHandler
import com.nicaapp.lectorFeedRss.Model.RSSObject


import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val RSS_link = "https://radionotimat.com/feed/feed/?paged=2/&api_key=ye2bkytlmkamngmqtuhxcpuryox0oqi1ii5glvko"
    private val RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "Noticias Matagalpa- Notimat"
        setSupportActionBar(toolbar)

        val linearLayoutManager = LinearLayoutManager(
            baseContext, LinearLayoutManager.VERTICAL, false
        )

        recyclerView.layoutManager = linearLayoutManager


        //loadRSS(RSS_link2,RSS_to_Json_API2)
        loadRSS(RSS_link,RSS_to_Json_API)


    }

    private fun  loadRSS( UriXml:String, UriJson:String) {
        val loadRSSAsync = object : AsyncTask<String, String, String>() {
            internal var mDialog = ProgressDialog(this@MainActivity)

            override fun onPreExecute() {
                mDialog.setMessage("Espere...")
                mDialog.show()
            }

            override fun onPostExecute(result: String?) {
                mDialog.dismiss()
                var rssObject: RSSObject

                rssObject = Gson().fromJson<RSSObject>(result, RSSObject::class.java!!)
                val adapter = FeedAdapter(rssObject, baseContext)

                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun doInBackground(vararg params: String?): String {
                val result: String
                val http= HTTPDataHandler()
                result = http.GetHTTPDataHandler(params[0])
                return result
            }
        }

        val url_get_data = StringBuffer(UriJson)
        url_get_data.append(UriXml)

        loadRSSAsync.execute(url_get_data.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh)
            loadRSS(RSS_link,RSS_to_Json_API)

        return true
    }


}
