package com.example.newsfeed.newsDisplay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.newsfeed.R
import com.example.newsfeed.WebActivity
import com.example.newsfeed.adapter.DisplayAdapter
import com.example.newsfeed.databinding.FragmentNewsListBinding
import com.example.newsfeed.domain.News
import com.example.newsfeed.webdisplay.WebFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewsListFragment : Fragment() {

    private lateinit var binding  : FragmentNewsListBinding
    private lateinit var viewModel: NewsListViewModel
    private lateinit var newsAdapter : DisplayAdapter
    private lateinit var searchView : SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_list, container, false)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = layoutManager

        newsAdapter = DisplayAdapter(DisplayAdapter.NewsClickListener{
            url -> viewModel.onNewsClicked(url)
        })

        binding.recyclerView.adapter = newsAdapter

        val application = requireNotNull(this.activity).application
        val viewModelFactory = NewsListViewModelFactory(application, newsAdapter)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewsListViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        searchView = binding.searchBar
        searchView.clearFocus()

        viewModel.onNewsClicked.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty()){
                val imageView : View = requireView().findViewById(R.id.content)
                val bundle = bundleOf("URL" to it)
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    addSharedElement(imageView, "WebTransition")
                    //replace<WebFragment>(R.layout.fragment_web, args = bundle)
                }
                findNavController().navigate(NewsListFragmentDirections.actionNewsListFragmentToWebFragment(it))
//                Intent(application, WebActivity::class.java).also{ intent ->
//                    intent.putExtra("EXTRA_STRING", it)
//                    startActivity(intent)
//                }
                viewModel.onNewsClickedFinished()
            }
        })

        if (savedInstanceState != null) {
            val savedQuery = savedInstanceState.getString("queryString")
            if (!savedQuery.isNullOrEmpty()) {
                searchView.setQuery(savedQuery, true)
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filteredList(query)
                handleKeyboard(searchView)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredList(newText)
                return true
            }
        })

        viewModel.news.observe(viewLifecycleOwner, Observer {
            it.let {
                newsAdapter.submitList(it)
            }
            handleKeyboard(searchView)
        })


//        viewModel.dataNull.observe(viewLifecycleOwner, Observer {
//            if(it){
//                Toast.makeText(context, "No matching results found", Toast.LENGTH_SHORT).show()
//                viewModel.toastFinished()
//            }
//        })

        return binding.root
    }

    fun handleKeyboard(searchView: SearchView){
        searchView.clearFocus()
        val imm = searchView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("queryString", searchView.query.toString())
    }

    fun filteredList(newText: String?) {
        println(newText)
        val filteredNews : MutableList<News> = viewModel.news.value?.filter { new ->
            newText?.let {
                new.author.equals(it, ignoreCase = true) ||
                        new.name.equals(it, ignoreCase = true) ||
                        new.title.equals(it, ignoreCase = true) ||
                        new.content?.contains(it.toRegex()) == true ||
                        new.description?.contains(it.toRegex()) == true
            } ?: false
        }.orEmpty().toMutableList()

        if (filteredNews.isEmpty()) {
            Toast.makeText(context, "No matching results found", Toast.LENGTH_SHORT).show()
            newsAdapter.submitList(viewModel.news.value?.toMutableList())
        } else {
            newsAdapter.submitList(filteredNews)
        }
    }
}