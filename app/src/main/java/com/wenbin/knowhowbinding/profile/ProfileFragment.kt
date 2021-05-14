package com.wenbin.knowhowbinding.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentProfileBinding


class ProfileFragment  : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private val viewModel : ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        var adapter = ProfileCommentAdapter()
        binding.recyclerView.adapter = adapter

        // Navigating to My Article Fragment.
        viewModel.navigateToMyArticle.observe(viewLifecycleOwner, Observer{
            it?.let {
                findNavController().navigate(ProfileFragmentDirections.navigateToMyArticleFragment())
                viewModel.onMyArticleNavigated()
            }
        })

        // Navigating to My Collect Fragment.
        viewModel.navigateToMyCollect.observe(viewLifecycleOwner, Observer{
            it?.let {
                findNavController().navigate(ProfileFragmentDirections.navigateToMyCollectFragment())
                viewModel.onMyCollectNavigated()
            }
        })


        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("個人頁面")
        }
        return binding.root
    }
}