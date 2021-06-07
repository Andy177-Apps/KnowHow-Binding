package com.wenbin.knowhowbinding.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentSearchBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.home.HomeAdapter
import com.wenbin.knowhowbinding.home.HomeViewModel

class SearchFragment  : Fragment() {
    private lateinit var binding : FragmentSearchBinding
    val viewModel by viewModels<SearchViewModel> { getVmFactory() }
//
//    private val viewModel : SearchViewModel by lazy {
//        ViewModelProvider(this).get(SearchViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

//        var adapter = HomeAdapter(viewModel)
//        binding.recyclerView.adapter = adapter

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("搜尋文章")
        }
        return binding.root
    }
}