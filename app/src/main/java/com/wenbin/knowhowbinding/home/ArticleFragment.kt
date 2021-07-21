package com.wenbin.knowhowbinding.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentArticleBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.util.Logger
import kotlinx.android.synthetic.*

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding

        val viewModel by viewModels<ArticleViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentArticleBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.imageViewSearch.setOnClickListener {
            viewModel.searchEditText.value = binding.editTextSearch.text.toString()
        }

        viewModel.searchEditText.observe(viewLifecycleOwner, Observer {
        })

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