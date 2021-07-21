package com.wenbin.knowhowbinding.user.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentUserArticleBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.user.UserProfileFragmentArgs
import androidx.lifecycle.Observer
import com.wenbin.knowhowbinding.util.Logger


class UserArticleFragment : Fragment() {
    private lateinit var binding : FragmentUserArticleBinding
    private val viewModel by viewModels<UserArticleViewModel> {
        getVmFactory(
                UserProfileFragmentArgs.fromBundle(requireArguments()).userEmail
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserArticleBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.articles.observe(viewLifecycleOwner, Observer {
            Logger.d("MyArticleFragment, articles in Fragment = $it")
        })
        val adapter = UserArticleAdapter(viewModel)
        binding.recyclerView.adapter = adapter

        if (activity is MainActivity) {
            viewModel.articles.value?.let {
                it[0].author?.let { it1 -> (activity as MainActivity).resetToolBar(it1.name) }
            }
            (activity as MainActivity).coverBottomNav()
        }
        return binding.root
    }
}