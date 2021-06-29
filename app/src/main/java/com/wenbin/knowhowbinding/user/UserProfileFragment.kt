package com.wenbin.knowhowbinding.user

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
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.databinding.FragmentUserDetailBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.network.LoadApiStatus
import com.wenbin.knowhowbinding.util.Logger

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
        val imageViewGender = binding.imageViewGender
        var firstTimeEntry = true

        viewModel.getMyUserInfo(UserManager.user.email)


        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            Logger.d("Check_follow, accepted userInfo = $it")
            Log.d("Check_follow", "Enter UserProfile: $it")

            imageViewGender.isSelected = it.gender == "male"

            Logger.d("Check_follow, viewModel.userInfo.image = ${viewModel.userInfo.value!!.image}")
            if (it.email != "") {
                Logger.d("Check_follow, accepted userInfo in first if determine = $it")

                if (firstTimeEntry) {
                    Logger.d("Check_follow, accepted userInfo in second if determine = $it")
                    setupLayout(it)
                    firstTimeEntry = false
                }
            }

            // Show the count of articles of the user.
            viewModel.userArticles.observe(viewLifecycleOwner, Observer { list ->
                Logger.d("Check_userArticles, userArticles = $list")
                binding.textPosts.text = list.size.toString()
            })

            if (activity is MainActivity) {
                Logger.d("user fun is used.")
                (activity as MainActivity).coverBottomNav()
                    (activity as MainActivity).resetToolBar(it.name)
            }
        })

        // follow
        viewModel.myInfo.observe(viewLifecycleOwner, Observer { my ->

            Logger.d("Check_follow, myInfo = $my")
            if (my.followingEmail.contains(viewModel.selectedUserEmail)) {
                Log.d("Check_btn", "isContains: true line 76")

                showFollowButton(false)
//                    setupFollowButton(true, it)
            } else {
                Log.d("Check_btn", "isContains: false line 81")
                showFollowButton(true)
//                    setupFollowButton(false, it)
            }
        })

        binding.buttonUnfollow.setOnClickListener {
            Log.d("Check_btn", "buttonUnfollow is clicked line 196")

            Logger.d("Check_follow, Line136")
            Logger.d("Check_follow, Line138")

            viewModel.removeUserFromFollow(UserManager.user.email, viewModel.userInfo.value!!)
            showFollowButton(true)
        }
        binding.buttonFollow.setOnClickListener {
            Log.d("Check_btn", "buttonFollow is clicked line 210")
            Log.d("Check_follow", "Line200")

            viewModel.postUserToFollow(UserManager.user.email, viewModel.userInfo.value!!)
            showFollowButton(false)
        }

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
                findNavController().navigate(UserProfileFragmentDirections.navigateToUserArticleFragment(it))

                viewModel.onMyArticleNavigated()
            }
        })

        if (activity is MainActivity) {
            Logger.d("user fun is used.")
            (activity as MainActivity).coverBottomNav()
            viewModel.userInfo.value?.let {
                (activity as MainActivity).resetToolBar(it.name)
                (activity as MainActivity).resetToolBar("個人頁面")
            }
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
        val chipTextSize = 12F
        val chipGroupTalented = binding.chipGroupTalentedSubjects
        val genres = user.talentedSubjects

        for (genre in genres) {
            val chip = Chip(context, null, R.attr.CustomChipChoice)
            chip.text = genre
            chip.textSize = chipTextSize
            chip.setTextAppearanceResource(R.style.WhiteBoldText)
            chip.setChipBackgroundColorResource(R.color.primaryDark)
            chipGroupTalented.addView(chip)
        }

        val chipGroupInterested = binding.chipGroupInterestedSubject
        val interestedList = user.interestedSubjects

        for (genre in interestedList) {
            val chip = Chip(context, null, R.attr.CustomChipChoice)
            chip.text = genre
            chip.textSize = chipTextSize
            chip.setTextAppearanceResource(R.style.WhiteBoldText)
            chip.setChipBackgroundColorResource(R.color.primaryDark)
            chipGroupInterested.addView(chip)
        }

        binding.buttonMessage.setOnClickListener {
            viewModel.postChatRoom(viewModel.createChatRoom())
            Logger.d("Check_follow, buttonMessage is clicked")

            Logger.d("Check_follow, user.email= ${user.email}")

            if (!user.email.isNullOrEmpty()){
                    Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(NavigationDirections.navigateToMessageFragment(user.email, user.name))
                }, 3000)
                }
        }
    }

    private fun showFollowButton(showFollow: Boolean) {
        // show -> true, show buttonFollow
        // show -> false, show buttonUnFollow

        Log.d("Check_btn", "showFollow line 178= $showFollow")

        if (showFollow) {
            Logger.d("Check_follow, Line121")

            binding.buttonFollow.visibility = View.VISIBLE
            binding.buttonUnfollow.visibility = View.GONE
        } else {
            Logger.d("Check_follow, Line126")

            binding.buttonFollow.visibility = View.GONE
            binding.buttonUnfollow.visibility = View.VISIBLE
        }
    }

    private fun setupFollowButton(contains: Boolean, user: User) {
        if (contains) {
            binding.buttonUnfollow.setOnClickListener {
                Log.d("Check_btn", "buttonUnfollow is clicked line 196")

                Logger.d("Check_follow, Line136")
                Logger.d("Check_follow, Line138")

                viewModel.removeUserFromFollow(UserManager.user.email, user)
                viewModel.getUser(viewModel.selectedUserEmail)
                viewModel.getMyUserInfo(UserManager.user.email)
                showFollowButton(true)

            }
        } else {
            binding.buttonFollow.setOnClickListener {
//                Logger.d("Check_follow, Line146")
                Log.d("Check_btn", "buttonFollow is clicked line 210")

                Log.d("Check_follow", "Line200")
//                Logger.d("Check_follow, Line148")

                viewModel.postUserToFollow(UserManager.user.email, user)
                viewModel.getUser(viewModel.selectedUserEmail)
                viewModel.getMyUserInfo(UserManager.user.email)
                showFollowButton(false)

            }
        }
    }
}