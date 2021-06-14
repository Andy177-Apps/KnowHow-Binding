package com.wenbin.knowhowbinding.user.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentMyarticleBinding
import com.wenbin.knowhowbinding.databinding.FragmentUserArticleBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.myarticle.MyArticleAdapter
import com.wenbin.knowhowbinding.myarticle.MyArticleViewModel
import com.wenbin.knowhowbinding.user.UserProfileFragmentArgs
import com.wenbin.knowhowbinding.user.UserProfileViewModel
import androidx.lifecycle.Observer


class UserArticleFragment : Fragment() {
    private lateinit var binding : FragmentUserArticleBinding
    private val viewModel by viewModels<UserArticleViewModel> {
        getVmFactory(
                UserProfileFragmentArgs.fromBundle(requireArguments()).userEmail
        )
    }


//    private val viewModel : MyArticleViewModel by lazy {
//        ViewModelProvider(this).get(MyArticleViewModel::class.java)
//    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserArticleBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.articles.observe(viewLifecycleOwner, Observer {
            Log.d("MyArticleFragment", "articles in Fragment = $it")
        })
        var adapter = UserArticleAdapter(viewModel)
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