package com.wenbin.knowhowbinding.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.MainViewModel
import com.wenbin.knowhowbinding.databinding.FragmentArticleBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import kotlinx.android.synthetic.*

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding

        val viewModel by viewModels<ArticleViewModel> { getVmFactory() }
//    private val homeViewModel: HomeViewModel by lazy {
//        ViewModelProvider(this).get(HomeViewModel::class.java)
//    }

//    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//        val homeViewModel = ViewModelProvider(this.requireActivity()).get(HomeViewModel::class.java)
//
//        binding.lifecycleOwner = this
//
//        binding = FragmentCreateBinding.inflate(layoutInflater, container, false)

        binding = FragmentArticleBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.imageViewSearch.setOnClickListener {
            Log.d("checkSearch", "binding.imageViewSearch is clicked")

            Log.d("checkSearch", "binding.editTextSearch.text.toString() = ${binding.editTextSearch.text}")

            viewModel.searchEditText.value = binding.editTextSearch.text.toString()
        }

        viewModel.searchEditText.observe(viewLifecycleOwner, Observer {
            Log.d("checkSearch", "searchEditText in ArticleFragment = $it")
        })

//        FragmentArticleBinding.inflate(inflater, container, false).apply {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewpagerArticles.let {
                tabsArticles.setupWithViewPager(it)
                it.adapter = ArticleAdapter(childFragmentManager)
                it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabsArticles))
            }
            if (activity is MainActivity) {
                (activity as MainActivity).hideToolBar()
                (activity as MainActivity).resetToolBar("")
            }
            return@onCreateView root
        }
    }

    override fun onDestroyView() {
        if (activity is MainActivity) {
            (activity as MainActivity).recoverToolBarandBottomNav()
        }
        super.onDestroyView()
    }
}