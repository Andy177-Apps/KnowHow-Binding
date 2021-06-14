package com.wenbin.knowhowbinding.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.androidbuts.multispinnerfilter.KeyPairBoolData
import com.androidbuts.multispinnerfilter.MultiSpinnerListener
import com.androidbuts.multispinnerfilter.SingleSpinnerListener
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.NavigationDirections
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.Answer
import com.wenbin.knowhowbinding.databinding.FragmentSearchBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    val viewModel by viewModels<SearchViewModel> { getVmFactory() }
//
//    private val viewModel : SearchViewModel by lazy {
//        ViewModelProvider(this).get(SearchViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //-- multipleItemSelectionSpinner_type
        val listType = KnowHowBindingApplication.instance.resources.getStringArray(R.array.search_type)
        val listArrayType: MutableList<KeyPairBoolData> = ArrayList()
        for (i in listType.indices) {
            val h = KeyPairBoolData()
            h.id = (i + 1).toLong()
            h.name = listType[i]
            h.isSelected = false
            listArrayType.add(h)
        }

        // Pass true If you want searchView above the list. Otherwise false. default = true.
        binding.singleItemSelectionSpinnerType.isSearchEnabled = false

        // A text that will display in search hint.
        binding.singleItemSelectionSpinnerType.setSearchHint("您想要？")
        binding.singleItemSelectionSpinnerType.setItems(
            listArrayType,
            object : SingleSpinnerListener {
                override fun onItemsSelected(selectedItem: KeyPairBoolData) {
                    Log.d("CheckSelected", "Type : " + selectedItem.name)
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
        val listArrayCity: MutableList<KeyPairBoolData> = ArrayList()

        for (i in listCity.indices) {
            val h = KeyPairBoolData()
            h.id = (i + 1).toLong()
            h.name = listCity[i]
            h.isSelected = false
            listArrayCity.add(h)
        }

        Log.d("MultipleSpinner", "Updated listArrayCity = $listArrayCity")

        binding.multipleItemSelectionSpinnerCity.isSearchEnabled = true
        binding.multipleItemSelectionSpinnerCity.setSearchHint("選擇城市")
        binding.multipleItemSelectionSpinnerCity.setClearText("捨棄")
        binding.multipleItemSelectionSpinnerCity.setEmptyTitle("您沒有選擇地點")
        binding.multipleItemSelectionSpinnerCity.setItems(
            listArrayCity,
            MultiSpinnerListener { items ->
                val list = mutableListOf<String>()

                for (i in items.indices) {
                    if (items[i].isSelected) {
                        Log.d(
                            "MultipleSpinner",
                            i.toString() + " : " + items[i].name + " : " + items[i].isSelected
                        )
                        list.add(items[i].name)
                    }
                }

                Log.d("CheckSelected", "Final city list in line 108 =$list")
                viewModel.setupCity(list)
            })

        //-- multipleItemSelectionSpinner_gender
        val listGender = listOf<String>("male", "female", "unlimited")

        val listArrayGender: MutableList<KeyPairBoolData> = ArrayList()
        for (i in listGender.indices) {
            val h = KeyPairBoolData()
            h.id = (i + 1).toLong()
            h.name = listGender[i]
            h.isSelected = false
            listArrayGender.add(h)
        }
        // Pass true If you want searchView above the list. Otherwise false. default = true.
        binding.singleItemSelectionSpinnerGender.isSearchEnabled = false

        // A text that will display in search hint.
//        binding.singleItemSelectionSpinnerCategory.setSearchHint("選擇領域")
        binding.singleItemSelectionSpinnerGender.setItems(
            listArrayGender,
            object : SingleSpinnerListener {
                override fun onItemsSelected(selectedItem: KeyPairBoolData) {
                    Log.d("CheckSelected", "Gender : " + selectedItem.name)
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
        val listArrayCategory: MutableList<KeyPairBoolData> = ArrayList()
        for (i in listCategory.indices) {
            val h = KeyPairBoolData()
            h.id = (i + 1).toLong()
            h.name = listCategory[i]
            h.isSelected = false
            listArrayCategory.add(h)
        }
        // Pass true If you want searchView above the list. Otherwise false. default = true.
        binding.singleItemSelectionSpinnerCategory.isSearchEnabled = false

        // A text that will display in search hint.
        binding.singleItemSelectionSpinnerCategory.setSearchHint("選擇領域")
        binding.singleItemSelectionSpinnerCategory.setItems(
            listArrayCategory,
            object : SingleSpinnerListener {
                override fun onItemsSelected(selectedItem: KeyPairBoolData) {
                    Log.d("CheckSelected", "Categoryr : " + selectedItem.name)

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
            Log.d("MultipleSpinner", "listSubject = $it")

            for (i in it.indices) {
                val h = KeyPairBoolData()
                h.id = (i + 1).toLong()
                h.name = it[i]
                h.isSelected = false
                listArraySubject.add(h)
            }
        })

        Log.d("MultipleSpinner", "Updated listArraySubject in line 130 = $listArraySubject")

        binding.multipleItemSelectionSpinnerSubject.isSearchEnabled = true
        binding.multipleItemSelectionSpinnerSubject.setSearchHint("選擇科目")
        binding.multipleItemSelectionSpinnerSubject.setClearText("捨棄")
        binding.multipleItemSelectionSpinnerSubject.setEmptyTitle("您還沒有選擇領域")
        binding.multipleItemSelectionSpinnerSubject.setItems(
            listArraySubject,
            MultiSpinnerListener { items ->
                val list = mutableListOf<String>()

                for (i in items.indices) {
                    if (items[i].isSelected) {
                        Log.d(
                            "MultipleSpinner",
                            i.toString() + " : " + items[i].name + " : " + items[i].isSelected
                        )
                        list.add(items[i].name)
                    }
                }
                Log.d("CheckSelected", "Final Subject list in line 216 =$list")
                viewModel.setupSubject(list)
            })

        binding.textViewLook.setOnClickListener {
            navigateToResult()
        }
        // Observe
        viewModel.selectedType.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("Checklivedata", "selectedType = $it")
        })

        viewModel.selectedCity.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("Checklivedata", "selectedCity = $it")
        })

        viewModel.selectedGender.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("Checklivedata", "selectedGender = $it")
        })

        viewModel.selectedCategory.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("Checklivedata", "selectedCategory = $it")
        })

        viewModel.selectedSubject.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("Checklivedata", "selectedSubject = $it")
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
        Log.d("Checklivedata", "answer = $answer")

        findNavController().navigate(NavigationDirections.navigateToSearchResultFragment(answer))

    }
}

