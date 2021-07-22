package com.wenbin.knowhowbinding.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.*
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.databinding.FragmentArticleBinding
import com.wenbin.knowhowbinding.databinding.FragmentHomeBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.network.LoadApiStatus
import com.wenbin.knowhowbinding.util.Logger
import java.util.*


class HomeFragment (val type: String) : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    private val articleViewModel by viewModels<ArticleViewModel>({ requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val mainViewModel = ViewModelProvider(this@HomeFragment.requireActivity()).get(MainViewModel::class.java)

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            Logger.d("userInfo = $it")
            if (viewModel.checkIfInfoComplete()) {
                if (mainViewModel.noticed.value == false) {
                    findNavController().navigate(NavigationDirections.navigateToFreshUserDialog())
                    mainViewModel.noticed.value = true
                } else {
                    Toast.makeText(this@HomeFragment.requireActivity(), getString(R.string.reminder_user_info), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.articles.observe(viewLifecycleOwner, Observer {
            Logger.d("articles = $it")
        })

        // Navigating to Post Article Fragment.
        viewModel.navigateToPostArticle.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            it?.let {
                findNavController().navigate(HomeFragmentDirections.navigateToPostArticleFragment())
                viewModel.onPostArticleNavigated()
            }
        })

        val adapter = HomeAdapter(viewModel)
        binding.recyclerView.adapter = adapter

        // Determine fragment.
        viewModel.articles.observe(viewLifecycleOwner, Observer {

            articleViewModel.searchEditText.observe(viewLifecycleOwner, Observer { searchEditText->
                if (type == KnowHowBindingApplication.appContext.getString(R.string.pager_title_all)) {
                    val resultList = mutableListOf<Article>()
                    for (item in it) {
                        item.author?.let {
                            if (
                                    item.author.name.contains(searchEditText) ||
                                    item.author.identity.contains(searchEditText) ||
                                    item.city.contains(searchEditText) ||
                                    item.find.contains(searchEditText) ||
                                    item.give.contains(searchEditText) ||
                                    item.content.contains(searchEditText)
                            ) {
                                resultList.add(item)
                            }
                        }
                    }
                    adapter.submitList(resultList)

                } else {
                    it?.let {
                        val resultList = mutableListOf<Article>()
                        for (item in it) {
                            item.author?.let {
                                if (
                                        item.author.name.contains(searchEditText) ||
                                        item.author.identity.contains(searchEditText) ||
                                        item.city.contains(searchEditText) ||
                                        item.find.contains(searchEditText) ||
                                        item.give.contains(searchEditText) ||
                                        item.content.contains(searchEditText)
                                ) {
                                    resultList.add(item)
                                }
                            }
                        }
                        adapter.submitList(resultList.filter { Article ->
                            Article.type == type
                        })
                    }
                }
            })

            // Original
            if (articleViewModel.searchEditText.value == null) {
                if (type == KnowHowBindingApplication.appContext.getString(R.string.pager_title_all)) {
                    adapter.submitList(it)
                } else {
                    it?.let {
                        adapter.submitList(it.filter { Article ->
                            Article.type == type
                        })
                    }
                }
            }

        })

        // ProgressBar
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadApiStatus.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                LoadApiStatus.DONE, LoadApiStatus.ERROR -> {
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        })

        return binding.root
    }
}