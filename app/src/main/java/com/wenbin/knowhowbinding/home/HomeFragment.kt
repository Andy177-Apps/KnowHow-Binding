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
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentHomeBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager


class HomeFragment : Fragment() {
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

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("文章列表")
        }
        return binding.root
    }
}