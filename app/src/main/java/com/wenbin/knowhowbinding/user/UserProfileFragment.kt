package com.wenbin.knowhowbinding.user

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.databinding.FragmentUserDetailBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.network.LoadApiStatus
import kotlinx.android.synthetic.main.fragment_home.view.*

class UserProfileFragment: Fragment() {
    private lateinit var binding: FragmentUserDetailBinding

    private val viewModel by viewModels<UserProfileViewModel> {
        getVmFactory(
                UserProfileFragmentArgs.fromBundle(requireArguments()).userEmail
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        var firstTimeEntry = true



        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            Log.d("check_follow", "accepted userInfo = $it")

            Log.d("check_follow", "viewModel.userInfo.image = ${viewModel.userInfo.value!!.image}")
            if (it.email != "") {
                Log.d("check_follow", "accepted userInfo in first if determine = $it")

                if (firstTimeEntry) {
                    Log.d("check_follow", "accepted userInfo in second if determine = $it")
                    setupLayout(it)
                    firstTimeEntry = false
                }
            }

            // Show the count of articles of the user.
            viewModel.userArticles.observe(viewLifecycleOwner, Observer { list ->
                Log.d("check_userArticles", "userArticles = $list")
                binding.textPosts.text = list.size.toString()
            })


            // follow
            viewModel.myInfo.observe(viewLifecycleOwner, Observer { my ->

                Log.d("check_follow", "myInfo = $my")
                if (my.followingEmail.contains(it.email)) {
                    showFollowButton(false)
                    setupFollowButton(true, it)
                } else {
                    showFollowButton(true)
                    setupFollowButton(false, it)
                }

            })
        })

        // Progress Bar
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }
                LoadApiStatus.DONE, LoadApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                }
            }
        })

        // Navigating to My Article Fragment.
        viewModel.navigateToMyArticle.observe(viewLifecycleOwner, Observer{
            it?.let {
                findNavController().navigate(UserProfileFragmentDirections.navigateToMyArticleFragment())
                viewModel.onMyArticleNavigated()
            }
        })

        if (activity is MainActivity) {
            (activity as MainActivity).coverBottomNav()
            (activity as MainActivity).hideToolBar()
        }
        return binding.root
    }

    override fun onDestroyView() {
        if (activity is MainActivity) {
            (activity as MainActivity).recoverToolBarandBottomNav()
        }
        super.onDestroyView()
    }

    private fun setupLayout(user: User) {
        val chipTextSize = 20F
        val chipGroupTalented = binding.chipGroupTalentedSubjects
        val genres = user.talentedSubjects

        for (genre in genres) {
            val chip = Chip(context, null, R.attr.CustomChipChoice)
            chip.text = genre
            chip.textSize = chipTextSize
            chipGroupTalented.addView(chip)
        }

        val chipGroupInterested = binding.chipGroupInterestedSubject
        val interestedList = user.interestedSubjects

        for (genre in interestedList) {
            val chip = Chip(context, null, R.attr.CustomChipChoice)
            chip.text = genre
            chip.textSize = chipTextSize
            chipGroupInterested.addView(chip)
        }

        binding.buttonMessage.setOnClickListener {
            viewModel.postChatRoom(viewModel.createChatRoom())
            Log.d("check_follow", "buttonMessage is clicked")

            Log.d("check_follow", "user.email= ${user.email}")

            if (!user.email.isNullOrEmpty()){
                    Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(NavigationDirections.navigateToMessageFragment(user.email, user.name))
                }, 3000)
                }
        }
    }

    private fun showFollowButton(showFollow: Boolean) {
        // show -> true, show buttonFollow
        // show -> false, show buttonUnfollow

        if (showFollow) {
            Log.d("check_follow", "Line121")

            binding.buttonFollow.visibility = View.VISIBLE
            binding.buttonUnfollow.visibility = View.GONE
        } else {
            Log.d("check_follow", "Line126")

            binding.buttonFollow.visibility = View.GONE
            binding.buttonUnfollow.visibility = View.VISIBLE
        }
    }

    private fun setupFollowButton(contains: Boolean, user: User) {
        if (contains) {
            binding.buttonUnfollow.setOnClickListener {
                Log.d("check_follow", "Line136")
                showFollowButton(true)
                Log.d("check_follow", "Line138")

                viewModel.removeUserFromFollow(UserManager.user.email, user)
                viewModel.getUser(viewModel.selectedUserEmail)
                viewModel.getMyUserInfo(UserManager.user.email)
            }
        } else {
            binding.buttonFollow.setOnClickListener {
                Log.d("check_follow", "Line146")
                showFollowButton(false)
                Log.d("check_follow", "Line148")

                viewModel.postUserToFollow(UserManager.user.email, user)
                viewModel.getUser(viewModel.selectedUserEmail)
                viewModel.getMyUserInfo(UserManager.user.email)
            }
        }
    }
}