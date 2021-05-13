package com.wenbin.knowhowbinding.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private val viewModel : HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Navigating to Post Article Fragment.
        viewModel.navigateToPostArticle.observe(viewLifecycleOwner, Observer{
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