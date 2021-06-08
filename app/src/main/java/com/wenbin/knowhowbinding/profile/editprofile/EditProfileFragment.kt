package com.wenbin.knowhowbinding.profile.editprofile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.databinding.FragmentEditprofileBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.chip.Chip
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wenbin.knowhowbinding.*
import com.wenbin.knowhowbinding.ext.checkPermission
import com.wenbin.knowhowbinding.ext.getLocalImg
import com.wenbin.knowhowbinding.util.Logger
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
            Log.d("checkElvis", "userInfo = $it")
            Log.d("checkElvis", "userInfo.introduction = ${it.introduction}")
        })
        viewModel.introduction.observe(viewLifecycleOwner, Observer {
            Log.d("checkElvis", "viewModel.introduction = $it")
        })

        Log.d("checkElvis", "viewModel.introduction = ${viewModel.introduction.value}")

        binding.imageViewUpdateAvatar.setOnClickListener {
            Log.d("checkUpdateImage", "imageViewUpdateAvatar is clicked")
            checkPermission()
        }
        // Firebase Storage //

        // 設置雲存儲
        // 訪問 Cloud Storage 存儲FirebaseStorage第一步是創建FirebaseStorage的實例：
        val storage = Firebase.storage

        // Points to the root reference
        // 取得它的參考
        val storageRef = storage.reference

        // Points to "images"
        // 指向資料夾 images
        var imagesRef = storageRef.child("images")

        // Points to "images/space.jpg"
        // Note that you can use variables to create child values
        val fileName = "space.jpg"
        // 指向 imagesRef 的資料夾：images 裡面的檔案：fileName i.e "space.jpg"
        val spaceRef = imagesRef.child(fileName)

        // File path is "images/space.jpg"
        // 該檔案後面加 .path 可以叫出它的檔案路徑
        val path = spaceRef.path

        // File name is "space.jpg"
        // 該檔案後面加 .path 可以叫出它的檔案名稱
        val name = spaceRef.name

        // Points to "images"
        // 該檔案後面加 .path 可以叫出儲存它的資料夾的名稱，像這邊就是 image
        imagesRef = spaceRef.parent!!

        //  End  //

        // Image //


        // Image //

        // Call mainViewModel to observe if the save button is pressed
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

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

        // Setup Spinner for city
        binding.spinnerCity.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                if (parent != null) {
                    val selectedType = parent.selectedItem.toString()
                    Log.d("Spinner_type","position = $position")
                    Log.d("Spinner_type","id = $id")

                    viewModel.setCity(selectedType)
                }
            }
        }

        // Observer for save button, when pressed send update user info (With empty handel)
        // 進入頁面之後這個 fun 就會被啟動，有點不太穩定，我先暫時註解掉，先用下面的
//        mainViewModel.saveIsPressed.observe(viewLifecycleOwner, Observer {
//            Log.d("checkBtn", "mainViewModel.saveIsPressed is used")
//            if (it) {
//                if (viewModel.checkIfComplete()) {
//                    val user = viewModel.getUser()
//                    Log.d("check_user", "user = $user")
//                    viewModel.updateUser(user)
//                    findNavController().navigate(NavigationDirections.navigateToProfileFragment())
//                    mainViewModel.saveIsPressed.value = false
//                } else {
//                    Toast.makeText(KnowHowBindingApplication.appContext, getString(R.string.reminder_finish_user_info), Toast.LENGTH_SHORT).show()
//                }
//            }
//        })



        // Navigating to Profile Fragment.
        viewModel.navigateToProfilePage.observe(viewLifecycleOwner, Observer{
            Log.d("checkBtn", "viewModel.navigateToProfilePage is used")
            it?.let {
                val observeIdentity = viewModel.getUser()
                Log.d("EditPage", "observeIdentity = $observeIdentity")

                viewModel.updateUser(observeIdentity)

//                findNavController().navigate(EditProfileFragmentDirections.navigateToProfileFragment())
                Toast.makeText(this.context, "資料儲存成功！", Toast.LENGTH_SHORT).show()

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocalImg(this)
                } else {
                    Toast.makeText(this.context, R.string.do_nothing, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE -> {
                    val filePath = ImagePicker.getFilePath(data) ?: ""

                    if (filePath.isNotEmpty()) {

                        Log.d("checkImage", "filePath = $filePath")
                        Toast.makeText(this.requireContext(), filePath, Toast.LENGTH_SHORT).show()
                        Glide.with(this.requireContext()).load(filePath).into(binding.imageUserAvatar)

                        // Update file to Firebase Storage.
                        viewModel.getImageUri(filePath)
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