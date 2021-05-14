package com.wenbin.knowhowbinding.myarticle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentMyarticleBinding

class MyArticleFragment : Fragment() {
    private lateinit var binding : FragmentMyarticleBinding
    private val viewModel : MyArticleViewModel by lazy {
        ViewModelProvider(this).get(MyArticleViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyarticleBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        var adapter = MyArticleAdapter()
        binding.recyclerView.adapter = adapter

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("我的收藏")
            (activity as MainActivity).coverToolBarandBottomNav()
        }
        return binding.root
    }
}