package com.example.newsfeed

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.newsfeed.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {
    private lateinit var webView : WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding : ActivityWebBinding = DataBindingUtil.setContentView(this, R.layout.activity_web)
        val url = intent.getStringExtra("EXTRA_STRING")

        webView = binding.webMain

        if (url != null) {
            webView.loadUrl(url)
        }

        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.webMain.visibility = View.GONE
                binding.webLoading.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.webMain.visibility = View.VISIBLE
                binding.webLoading.visibility = View.GONE
            }

        }

        webView.settings.setSupportZoom(true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(webView.canGoBack()) {
            webView.goBack()
        }
        else{
            finish()
        }
    }
}