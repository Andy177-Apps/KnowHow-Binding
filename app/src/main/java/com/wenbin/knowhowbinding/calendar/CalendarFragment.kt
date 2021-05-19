package com.wenbin.knowhowbinding.calendar

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.wenbin.knowhowbinding.KnowHowBindingApplication
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.databinding.FragmentCalendarBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.util.Logger
import com.wenbin.knowhowbinding.util.OneDayDecorator
import com.wenbin.knowhowbinding.util.SingleDateDecorator
import com.wenbin.knowhowbinding.util.TimeUtil
import org.threeten.bp.LocalDate
import java.util.*

class CalendarFragment : Fragment() {
    private lateinit var binding : FragmentCalendarBinding
    val viewModel by viewModels<CalendarViewModel> { getVmFactory() }
    private lateinit var widget : MaterialCalendarView
    private val oneDayDecorator: OneDayDecorator = OneDayDecorator()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        widget = binding.materialCalendarView
        val localDate = LocalDate.now()

        widget.setCurrentDate(localDate)




        // Add dots based on my events
        viewModel.liveEvents.observe(viewLifecycleOwner, Observer {
            Log.d("wenbin", "viewModel.allLiveEvents, it = $it")
            it?.let {
                it.forEach {event ->
                    val year = TimeUtil.stampToYear(event.endTime).toInt()
                    val month = TimeUtil.stampToMothInt(event.endTime).toInt()
                    val day = TimeUtil.stampToDay(event.endTime).toInt()
                    addDotDecoration(year, month, day)
                }
                viewModel.createdDailyEvent(TimeUtil.dateToStamp(localDate.toString(), Locale.TAIWAN))
            }
        })

        // Get the current selected date
        widget.setOnDateChangedListener { _, date, selected ->
            if (selected) {
                oneDayDecorator.setDate(date.date)

                val selectedDate = TimeUtil.dateToStamp(date.date.toString(), Locale.TAIWAN)

                // Create a sorted list of event based on the current date
                viewModel.createdDailyEvent(selectedDate)

                Logger.d("$selectedDate")
            }
        }

        viewModel.navigationToCreateEventDialogFragment.observe(viewLifecycleOwner,
        Observer { date ->
            date?.let {
                binding.imageViewCreateEvent.setOnClickListener {
                    findNavController().navigate(CalendarFragmentDirections.navigateToCreateEventDialog(date))
                }
            }
        })
        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("月曆")
        }
        return binding.root
    }

    private fun addDotDecoration(year: Int, month: Int, day: Int) {
        widget.addDecorator(
            SingleDateDecorator(
                KnowHowBindingApplication.appContext.resources.getColor(R.color.purple_500),
                CalendarDay.from(year, month, day)
            )
        )
    }
}