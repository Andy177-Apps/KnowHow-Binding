package com.wenbin.knowhowbinding.mycollect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentMycollectBinding

class MyCollectFragment  : Fragment() {
    private lateinit var binding : FragmentMycollectBinding
    private val viewModel : MyCollectViewModel by lazy {
        ViewModelProvider(this).get(MyCollectViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMycollectBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        var adapter = MyCollectAdapter()
        binding.recyclerView.adapter = adapter

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("我的文章")
            (activity as MainActivity).coverToolBarandBottomNav()
        }
        return binding.root
    }
}