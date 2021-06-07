package com.wenbin.knowhowbinding.mycollect

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentMycollectBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.myarticle.MyArticleViewModel

class MyCollectFragment  : Fragment() {
    private lateinit var binding : FragmentMycollectBinding
    private val viewModel by viewModels<MyCollectViewModel> { getVmFactory()}

//    private val viewModel : MyCollectViewModel by lazy {
//        ViewModelProvider(this).get(MyCollectViewModel::class.java)
//    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMycollectBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.articles.observe(viewLifecycleOwner, Observer {
            Log.d("MyCollectFragment", "articles in Fragment = $it")
        })

        var adapter = MyCollectAdapter(viewModel)
        binding.recyclerView.adapter = adapter

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("我的收藏")
            (activity as MainActivity).coverBottomNav()
        }
        return binding.root
    }
}