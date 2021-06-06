package com.wenbin.knowhowbinding.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.databinding.FragmentHomeBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.network.LoadApiStatus


class HomeFragment(val type: String) : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.articles.observe(viewLifecycleOwner, Observer {
            Log.d("wenbin", "articles = $it")
        })

        Log.d("userEmail", UserManager.user.email)
//         Navigating to Post Article Fragment.
        viewModel.navigateToPostArticle.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            it?.let {
                findNavController().navigate(HomeFragmentDirections.navigateToPostArticleFragment())
                viewModel.onPostArticleNavigated()
            }
        })

        var adapter = HomeAdapter()
        binding.recyclerView.adapter = adapter

        //決定哪個分頁
        viewModel.articles.observe(viewLifecycleOwner, Observer {
            if (type == KnowHowBindingApplication.appContext.getString(R.string.pager_title_all)) {
                adapter.submitList(it)
            } else {
                it?.let {
                    adapter.submitList(it.filter { Article ->
                        Article.type == type
                    })
                }
            }
        })

        // Lottie
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadApiStatus.LOADING -> {
//                    binding.animationView.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                }
                LoadApiStatus.DONE, LoadApiStatus.ERROR -> {
//                    binding.animationView.visibility = View.GONE
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        })
//        if (activity is MainActivity) {
//            (activity as MainActivity).hideToolBar()
//        }
        return binding.root
    }

//    override fun onDestroyView() {
//        if (activity is MainActivity) {
//            (activity as MainActivity).recoverToolBarandBottomNav()
//        }
//        super.onDestroyView()
//    }
}