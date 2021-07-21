package com.wenbin.knowhowbinding.mycollect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentMycollectBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.util.Logger

class MyCollectFragment  : Fragment() {
    private lateinit var binding : FragmentMycollectBinding
    private val viewModel by viewModels<MyCollectViewModel> { getVmFactory()}

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMycollectBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.articles.observe(viewLifecycleOwner, Observer {
            Logger.d("MyCollectFragment, articles in Fragment = $it")
        })

        val adapter = MyCollectAdapter(viewModel)
        binding.recyclerView.adapter = adapter

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("我的收藏")
        }
        return binding.root
    }
}