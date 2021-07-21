package com.wenbin.knowhowbinding.profile.editprofile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wenbin.knowhowbinding.databinding.FragmentEditprofileBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import androidx.lifecycle.Observer
import com.androidbuts.multispinnerfilter.KeyPairBoolData
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.chip.Chip
import com.wenbin.knowhowbinding.*
import com.wenbin.knowhowbinding.ext.checkPermission
import com.wenbin.knowhowbinding.ext.getLocalImg
import com.wenbin.knowhowbinding.util.Logger
import com.wenbin.knowhowbinding.util.PICK_BACKGROUND_IMAGE
import com.wenbin.knowhowbinding.util.PICK_IMAGE
import com.wenbin.knowhowbinding.util.REQUEST_EXTERNAL_STORAGE


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

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            Logger.d("checkElvis, userInfo = $it")
            Logger.d("checkElvis, userInfo.introduction = ${it.introduction}")
        })
        viewModel.introduction.observe(viewLifecycleOwner, Observer {
            Logger.d("checkElvis, viewModel.introduction = $it")
        })

        binding.imageViewUpdateAvatar.setOnClickListener {
            checkPermission(PICK_IMAGE)
        }

        binding.imageViewUpdateBg.setOnClickListener {
            updateBackground()
        }

        // Setup chip group for TalentedSubjects chip selection
        val chipGroupTalented = binding.chipGroupTalentedSubjects
        chipGroupTalented.isSingleSelection = false
        val typesTalented = KnowHowBindingApplication.instance.resources.getStringArray(R.array.all_tag_array)

        for (type in typesTalented) {
            val chip = LayoutInflater.from(requireContext()).inflate(R.layout.chip_layout, chipGroupTalented, false) as Chip
            chip.text = type

            chip.setOnCheckedChangeListener { c, isChecked ->
                if (isChecked) {
                    // Check if the list already contains the tag, if not then add to list
                    if (viewModel.talentedList.contains(c.text.toString())) {
                        Logger.d(getString(R.string.logger_already_added))
                    } else {
                        viewModel.setTalented(c.text.toString(),false)
                    }
                    // Remove tag from list when uncheck
                } else {
                    viewModel.setTalented(c.text.toString(),true)
                }
            }
            chipGroupTalented.addView(chip)
        }

        // Setup chip group for interestedSubjects chip selection
        val chipGroupInterested = binding.chipGroupInterestedSubject
        chipGroupInterested.isSingleSelection = false
        val typesInterested = KnowHowBindingApplication.instance.resources.getStringArray(R.array.all_tag_array)

        for (type in typesInterested) {
            val chip = LayoutInflater.from(requireContext()).inflate(R.layout.chip_layout, chipGroupInterested, false) as Chip
            chip.text = type

            chip.setOnCheckedChangeListener { c, isChecked ->
                if (isChecked) {
                    // Check if the list already contains the tag, if not then add to list
                    if (viewModel.talentedList.contains(c.text.toString())) {
                        Logger.d(getString(R.string.logger_already_added))
                    } else {
                        viewModel.setInterested(c.text.toString(),false)
                    }

                    // Remove tag from list when uncheck
                } else {
                    viewModel.setInterested(c.text.toString(),true)
                }
            }
            chipGroupInterested.addView(chip)
        }

        // Setup Radio button---Gender
        binding.radioGender.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_male -> viewModel.setGender("Male")
                R.id.radio_female -> viewModel.setGender("Female")
            }
        }

        //-- multipleItemSelectionSpinner_type
        val listType = KnowHowBindingApplication.instance.resources.getStringArray(R.array.city_array)
        val listArrayType: MutableList<KeyPairBoolData> = viewModel.changeStringToKeyPairBoolData(listType)

        viewModel.setSingleSpinner(
                binding.singleItemSelectionSpinnerCity,
                true,
                "搜尋居住的縣市",
                listArrayType
        )

        //-- multipleItemSelectionSpinner_subject_talentedSubjects
        val listTalentedSubject =
                KnowHowBindingApplication.instance.resources.getStringArray(R.array.all_tag_array)
        val listArrayTalentedSubject: MutableList<KeyPairBoolData> = viewModel.changeStringToKeyPairBoolData(listTalentedSubject)

        Logger.d("Updated listArraySubject in line 130 = $listArrayTalentedSubject")

        viewModel.setMultipleSpinner(
                binding.multipleItemSelectionSpinnerSubjectTalentedSubjects,
                true,
                "選擇擅長的項目",
                "捨棄",
                "您還沒有選擇",
                listArrayTalentedSubject,
                "talented"
        )

        //-- multipleItemSelectionSpinner_subject_interestedSubjects
        val listInterestedSubject =
                KnowHowBindingApplication.instance.resources.getStringArray(R.array.all_tag_array)
        val listArrayInterestedSubject: MutableList<KeyPairBoolData> = viewModel.changeStringToKeyPairBoolData(listInterestedSubject)

        Logger.d("Updated listArraySubject in line 130 = $listArrayTalentedSubject")

        viewModel.setMultipleSpinner(
                binding.multipleItemSelectionSpinnerSubjectInterestedSubjects,
                true,
                "選擇有興趣的項目",
                "捨棄",
                "您還沒有選擇",
                listArrayInterestedSubject,
                "interested"
        )

        // Navigating to Profile Fragment.
        viewModel.navigateToProfilePage.observe(viewLifecycleOwner, Observer{
            Logger.d("checkBtn, viewModel.navigateToProfilePage is used")
            it?.let {
                val observeIdentity = viewModel.getUser()
                viewModel.updateUser(observeIdentity)

                Toast.makeText(this.context, "資料儲存成功！", Toast.LENGTH_SHORT).show()

                viewModel.onProfilePageNavigated()
            }
        })

        // Observe
        viewModel.identity.observe(viewLifecycleOwner, Observer {
            Logger.d("EditPage, identity = $it")
        })

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("編輯個人頁面")
        }

        return binding.root
    }

    private fun updateBackground() {
        checkPermission(PICK_BACKGROUND_IMAGE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocalImg(this, requestCode)
                } else {
                    Toast.makeText(this.context, R.string.do_nothing, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Logger.d("resultCode = $resultCode , requestCode = $requestCode")
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE -> {
                    val filePath = ImagePicker.getFilePath(data) ?: ""

                    if (filePath.isNotEmpty()) {

                        Toast.makeText(this.requireContext(), filePath, Toast.LENGTH_SHORT).show()
                        Glide.with(this.requireContext()).load(filePath).into(binding.imageUserAvatar)

                        // Update file to Firebase Storage.
                        viewModel.getImageUri(filePath)
                    } else {
                        Toast.makeText(this.requireContext(), R.string.load_img_fail, Toast.LENGTH_SHORT).show()
                    }
                }

                PICK_BACKGROUND_IMAGE -> {

                    val filePath = ImagePicker.getFilePath(data) ?: ""

                    if (filePath.isNotEmpty()) {

                        Toast.makeText(this.requireContext(), filePath, Toast.LENGTH_SHORT).show()
                        Glide.with(this.requireContext()).load(filePath).into(binding.imageViewBackground)

                        // Update file to Firebase Storage.
                        viewModel.getBgImageUri(filePath)
                    } else {
                        Toast.makeText(this.requireContext(), R.string.load_img_fail, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this.requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this.requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}