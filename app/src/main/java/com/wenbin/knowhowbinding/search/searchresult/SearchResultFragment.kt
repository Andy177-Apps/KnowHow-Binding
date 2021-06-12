package com.wenbin.knowhowbinding.search.searchresult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.databinding.FragmentSearchResultBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.following.FollowingAdapter
import androidx.lifecycle.Observer


class SearchResultFragment: Fragment() {
    private lateinit var binding: FragmentSearchResultBinding

    private val viewModel by viewModels<SearchResultViewModel> { getVmFactory()}

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(layoutInflater, container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val searchResultAdapter = SearchResultAdapter(SearchResultAdapter.OnClickListener {
            viewModel.navigateToUserProfile(it)
        })
        binding.recyclerView.adapter = searchResultAdapter

        viewModel.navigateToUserProfile.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToUserProfileFragment(it.email))
                viewModel.onUserProfileNavigated()
            }
        })

        viewModel.allUsers.observe(viewLifecycleOwner, Observer {
            Log.d("checkSearchResult", "allUsers = $it")
            searchResultAdapter.submitList(it)
        })

        return binding.root
    }
}