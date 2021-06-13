package com.wenbin.knowhowbinding.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.androidbuts.multispinnerfilter.KeyPairBoolData
import com.androidbuts.multispinnerfilter.MultiSpinnerListener
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.databinding.FragmentSearchBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import java.util.*

class SearchFragment  : Fragment() {
    private lateinit var binding : FragmentSearchBinding
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

        // multipleItemSelectionSpinner_city
        val listCity = KnowHowBindingApplication.instance.resources.getStringArray(R.array.city_array)
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
        binding.multipleItemSelectionSpinnerCity.setItems(listArrayCity, MultiSpinnerListener { items ->
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

            Log.d("MultipleSpinner", "Final city list in line 67 =$list")
        })



        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("搜尋文章")
        }
        return binding.root
    }
}