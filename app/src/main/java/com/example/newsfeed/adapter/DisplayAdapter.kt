package com.example.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsfeed.databinding.ListContentBinding
import com.example.newsfeed.domain.News

class DisplayAdapter(private val newsClickListener: NewsClickListener) : ListAdapter<News, DisplayAdapter.NewsViewHolder>(NewsDiffUtil()) {

//    var data : List<News> = emptyList()
//        set(value){
//            field = value
//            println("Inside set")
//            notifyDataSetChanged()
//        }

    class NewsDiffUtil() : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.url ==  newItem.url
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }

    }

    class NewsViewHolder(private val binding : ListContentBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(news: News, newsClickListener: NewsClickListener, position: Int) {

            if(position%4==0 || position%2==0){
                binding.content.layoutParams.height = 600
            }
            else{
                binding.content.layoutParams.height = 400
            }
            binding.news = news
            binding.executePendingBindings()
            binding.clickListener = newsClickListener
        }

        companion object{
            fun from(parent: ViewGroup) : NewsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ListContentBinding.inflate(layoutInflater,parent,false)
                return NewsViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder.from(parent)
    }

//    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position), newsClickListener, position)
    }

//    fun setFilteredData(filteredNews: MutableList<News>) {
//        data = filteredNews
//        //println(data)
//        //notifyDataSetChanged()
//    }

    class NewsClickListener(val clickListener: (webUrl : String?) -> Unit){
        fun onClick(news : News) = clickListener(news.url)
    }
}