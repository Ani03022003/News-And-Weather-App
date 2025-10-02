package com.example.newsfeed.webdisplay

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import com.example.newsfeed.R
import com.example.newsfeed.databinding.FragmentWebBinding

class WebFragment : Fragment() {
    private lateinit var binding : FragmentWebBinding
    private lateinit var webView : WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.transition)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web, container, false)
        val url = WebFragmentArgs.fromBundle(requireArguments()).webUrl

        //val url = requireArguments().getString("URL") ?: "Nothing"

        webView = binding.web

        webView.loadUrl(url)

        webView.settings.javaScriptEnabled = true

        webView.settings.allowFileAccess = false
        webView.settings.allowContentAccess = false

        webView.webViewClient = object : WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.web.visibility = View.GONE
                binding.loading.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.web.visibility = View.VISIBLE
                binding.loading.visibility = View.GONE
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Check if the WebView can go back
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    // If WebView cannot go back, handle the default behavior or close the fragment
                    //requireActivity().supportFragmentManager.popBackStack() // Or popBackStack() to remove the fragment
                    parentFragmentManager.popBackStack()
                }
            }
        })

        webView.settings.setSupportZoom(true)

        return binding.root
    }
}