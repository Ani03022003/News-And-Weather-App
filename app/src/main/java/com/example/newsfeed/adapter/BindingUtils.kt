package com.example.newsfeed.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newsfeed.R
import com.example.newsfeed.network.WeatherData
import java.util.Locale

@BindingAdapter("bindImage")
fun bindImage(itemView : ImageView, url: String?){
    if(url.isNullOrEmpty()){
        itemView.setImageResource(R.drawable.ic_broken_image)
    }
    else {
        url.let {
            val uri = url.toUri().buildUpon().scheme("https").build()
            Glide.with(itemView.context)
                .load(uri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                )
                .into(itemView)
        }
    }
}

@BindingAdapter("tempAndArea")
fun bindTempAndArea(itemView : TextView, data: WeatherData?){
    val temp = String.format(Locale.getDefault(), "%.1f", (data?.main?.temp ?: 273.15) - 273.15)
    if (data != null) {
        itemView.text = "$temp°C in ${data.location}"
    }
    else {
        itemView.text = "$temp°C in Chennai"
    }
}

@BindingAdapter("weatherBindImage")
fun weatherBindImage(itemView : ImageView, data : WeatherData?){
    val code = data?.weather?.get(0)?.icon ?: "01d"
    val url = "https://openweathermap.org/img/wn/${code}@2x.png"
    url.let {
        val uri = url.toUri().buildUpon().scheme("https").build()
        Glide.with(itemView.context)
            .load(uri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(itemView)
    }
}

@BindingAdapter("description")
fun bindDescription(itemView: TextView, data: WeatherData?){
    itemView.text = data?.weather?.get(0)?.description ?: "Sunny"
}