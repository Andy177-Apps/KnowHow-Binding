package com.wenbin.knowhowbinding.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.calendar.createevent.CreateEventViewModel
import com.wenbin.knowhowbinding.databinding.FragmentCalendarBinding
import com.wenbin.knowhowbinding.ext.getVmFactory

class CalendarFragment : Fragment() {
    private lateinit var binding : FragmentCalendarBinding
    val viewModel by viewModels<CalendarViewModel> { getVmFactory() }

//    private val viewModel : CalendarViewModel by lazy {
//        ViewModelProvider(this).get(CalendarViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.imageViewCreateEvent.setOnClickListener {
            findNavController().navigate(CalendarFragmentDirections.navigateToCreateEventDialog())
        }
        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("月曆")
        }
        return binding.root
    }
}