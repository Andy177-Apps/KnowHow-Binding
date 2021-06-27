package com.wenbin.knowhowbinding.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.androidbuts.multispinnerfilter.KeyPairBoolData
import com.androidbuts.multispinnerfilter.MultiSpinnerListener
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch
import com.androidbuts.multispinnerfilter.SingleSpinnerListener
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.Answer
import com.wenbin.knowhowbinding.databinding.FragmentSearchBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.util.Logger
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    val viewModel by viewModels<SearchViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //-- multipleItemSelectionSpinner_type
        // Refactor - Change String to KeyPairBoolData
        // Inject string array
        val listType = KnowHowBindingApplication.instance.resources.getStringArray(R.array.search_type)

        val listArrayType: MutableList<KeyPairBoolData> = viewModel.changeStringToKeyPairBoolData(listType)

        // Pass true If you want searchView above the list. Otherwise false. default = true.
        binding.singleItemSelectionSpinnerType.isSearchEnabled = false

        // A text that will display in search hint.
        binding.singleItemSelectionSpinnerType.setSearchHint("您想要？")
        binding.singleItemSelectionSpinnerType.setItems(
            listArrayType,
            object : SingleSpinnerListener {
                override fun onItemsSelected(selectedItem: KeyPairBoolData) {
                    Logger.d("CheckSelectedType : " + selectedItem.name)
                    viewModel.setupType(selectedItem.name)
                }

                override fun onClear() {
                    Toast.makeText(
                        KnowHowBindingApplication.appContext,
                        "Cleared Selected Item",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })


        //-- multipleItemSelectionSpinner_city Multiple
        val listCity =
            KnowHowBindingApplication.instance.resources.getStringArray(R.array.city_array)

        val listArrayCity: MutableList<KeyPairBoolData> = viewModel.changeStringToKeyPairBoolData(listCity)

        Logger.d("MultipleSpinner, Updated listArrayCity = $listArrayCity")

        viewModel.setMultipleSpinner(listArrayCity,
                true,
                "選擇城市",
                "捨棄",
                "您沒有選擇地點",
                binding.multipleItemSelectionSpinnerCity,
                "city")

        //-- multipleItemSelectionSpinner_gender
        val listGender = KnowHowBindingApplication.instance.resources.getStringArray(R.array.gender)
        val listArrayGender: MutableList<KeyPairBoolData> = viewModel.changeStringToKeyPairBoolData(listGender)

        // Pass true If you want searchView above the list. Otherwise false. default = true.
        binding.singleItemSelectionSpinnerGender.isSearchEnabled = false

        // A text that will display in search hint.
//        binding.singleItemSelectionSpinnerCategory.setSearchHint("選擇領域")
        binding.singleItemSelectionSpinnerGender.setItems(
            listArrayGender,
            object : SingleSpinnerListener {
                override fun onItemsSelected(selectedItem: KeyPairBoolData) {
                    Logger.d("CheckSelected, Gender : " + selectedItem.name)
                    viewModel.setupGender(selectedItem.name)
                }

                override fun onClear() {
                    Toast.makeText(
                        KnowHowBindingApplication.appContext,
                        "Cleared Selected Item",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        //-- singleItemSelectionSpinner_category Single
        val listCategory =
            KnowHowBindingApplication.instance.resources.getStringArray(R.array.major_category_array)
        val listArrayCategory: MutableList<KeyPairBoolData> = viewModel.changeStringToKeyPairBoolData(listCategory)

        // Pass true If you want searchView above the list. Otherwise false. default = true.
        binding.singleItemSelectionSpinnerCategory.isSearchEnabled = false

        // A text that will display in search hint.
        binding.singleItemSelectionSpinnerCategory.setSearchHint("選擇領域")
        binding.singleItemSelectionSpinnerCategory.setItems(
            listArrayCategory,
            object : SingleSpinnerListener {
                override fun onItemsSelected(selectedItem: KeyPairBoolData) {
                    Logger.d("CheckSelected, Categoryr : " + selectedItem.name)

                    viewModel.selectSubjects(selectedItem.name)
                    viewModel.setupCategory(selectedItem.name)
                }

                override fun onClear() {
                    Toast.makeText(
                        KnowHowBindingApplication.appContext,
                        "Cleared Selected Item",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        //-- multipleItemSelectionSpinner_subject
        val listArraySubject: MutableList<KeyPairBoolData> = ArrayList()

        viewModel.listSubject.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Logger.d("listSubject = $it")
            Log.d("wenbin", "listSubject = $it")

            for (i in it.indices) {
                val h = KeyPairBoolData()
                h.id = (i + 1).toLong()
                h.name = it[i]
                h.isSelected = false
                listArraySubject.add(h)
            }
        })

        Log.d("wenbin", "Updated listArraySubject in line 195 = $listArraySubject")
        Logger.d("Updated listArraySubject in line 130 = $listArraySubject")

        viewModel.setMultipleSpinner(listArraySubject,
                true,
                "選擇科目",
                "捨棄",
                "您還沒有選擇領域",
                binding.multipleItemSelectionSpinnerSubject,
                "subject")

        binding.textViewLook.setOnClickListener {
            navigateToResult()
        }
        // Observe
        viewModel.selectedType.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Logger.d("Checklivedata, selectedType = $it")
        })

        viewModel.selectedCity.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Logger.d("Checklivedata, selectedCity = $it")
        })

        viewModel.selectedGender.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Logger.d("Checklivedata, selectedGender = $it")
        })

        viewModel.selectedCategory.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Logger.d("Checklivedata, selectedCategory = $it")
        })

        viewModel.selectedSubject.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Logger.d("Checklivedata, selectedSubject = $it")
        })

        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("搜尋")
        }
        return binding.root
    }



    private fun navigateToResult() {

        val answer = Answer(
            type = viewModel.selectedType.value ?: "",
            city = viewModel.selectedCity.value ?: listOf<String>(),
            gender = viewModel.selectedGender.value ?: "",
            subject = viewModel.selectedSubject.value ?: listOf<String>()
        )
        Logger.d("Checklivedata, answer = $answer")

        findNavController().navigate(NavigationDirections.navigateToSearchResultFragment(answer))

    }
}

