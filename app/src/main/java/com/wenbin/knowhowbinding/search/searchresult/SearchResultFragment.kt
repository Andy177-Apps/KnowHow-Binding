package com.wenbin.knowhowbinding.search.searchresult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.databinding.FragmentSearchResultBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.user.UserProfileFragmentArgs


class SearchResultFragment: Fragment() {
    private lateinit var binding: FragmentSearchResultBinding

    private val viewModel by viewModels<SearchResultViewModel> { getVmFactory(
        SearchResultFragmentArgs.fromBundle(requireArguments()).selectedAnswers
    )}

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val searchResultAdapter = SearchResultAdapter(SearchResultAdapter.OnClickListener {
            viewModel.navigateToUserProfile(it)
        })

        binding.recyclerView.adapter = searchResultAdapter
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.setHasFixedSize(true)

        binding.recyclerView.layoutManager = layoutManager


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