package com.wenbin.knowhowbinding.myarticle

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
import com.wenbin.knowhowbinding.databinding.FragmentMyarticleBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.user.UserProfileFragmentArgs
import com.wenbin.knowhowbinding.user.UserProfileViewModel

class MyArticleFragment : Fragment() {
    private lateinit var binding : FragmentMyarticleBinding

    private val viewModel by viewModels<MyArticleViewModel> { getVmFactory()}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyarticleBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.articles.observe(viewLifecycleOwner, Observer {
            Log.d("MyArticleFragment", "articles in Fragment = $it")
        })
        var adapter = MyArticleAdapter(viewModel)
        binding.recyclerView.adapter = adapter

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("我的文章")
            (activity as MainActivity).coverBottomNav()
        }
        return binding.root
    }
}