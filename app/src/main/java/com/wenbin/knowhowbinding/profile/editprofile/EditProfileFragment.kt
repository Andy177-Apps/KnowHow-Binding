package com.wenbin.knowhowbinding.profile.editprofile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.databinding.FragmentEditprofileBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.util.Logger


class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditprofileBinding
    val viewModel by viewModels<EditProfileViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditprofileBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Setup chip group for tag selection
        val chipGroup = binding.chipGroup
        chipGroup.isSingleSelection = false
        val types = KnowHowBindingApplication.instance.resources.getStringArray(R.array.all_tag_array)

        for (type in types) {
            val chip = LayoutInflater.from(requireContext()).inflate(R.layout.chip_layout, chipGroup, false) as Chip
            chip.text = type

            chip.setOnCheckedChangeListener { c, isChecked ->
                if (isChecked) {
                    // Check if the list already contains the tag, if not then add to list
                    if (viewModel.itemList.contains(c.text.toString())) {
                        Logger.d(getString(R.string.logger_already_added))
                    } else {
                        viewModel.setTags(c.text.toString(),false)
                    }

                    // Remove tag from list when uncheck
                } else {
                    viewModel.setTags(c.text.toString(),true)
                }
            }
            chipGroup.addView(chip)
        }

        // Navigating to Profile Fragment.
        viewModel.navigateToProfilePage.observe(viewLifecycleOwner, Observer{
            it?.let {
                val observeIdentity = viewModel.getUser()
                Log.d("EditPage", "observeIdentity = $observeIdentity")

                viewModel.updateUser(viewModel.getUser())
                findNavController().navigate(EditProfileFragmentDirections.navigateToProfileFragment())
                viewModel.onProfilePageNavigated()
            }
        })

        viewModel.identity.observe(viewLifecycleOwner, Observer {
            Log.d("EditPage", "identity = $it")
        })

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("編輯個人頁面")
        }

        return binding.root
    }
}