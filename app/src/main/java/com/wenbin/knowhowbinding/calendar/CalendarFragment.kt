package com.wenbin.knowhowbinding.calendar

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.threeten.bp.LocalDate
import java.util.*

class CalendarFragment : Fragment() {
    private lateinit var binding : FragmentCalendarBinding
    val viewModel by viewModels<CalendarViewModel> { getVmFactory() }

    private lateinit var widget : MaterialCalendarView
    private val oneDayDecorator: OneDayDecorator = OneDayDecorator()
    @RequiresApi(Build.VERSION_CODES.O)

    private var isFABOpen: Boolean = false

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

        var adapter = CalendarAdapter()
        binding.recyclerView.adapter = adapter

        // Set Indecator of current date
        widget.setSelectedDate(localDate)

        viewModel.events.observe(viewLifecycleOwner, Observer { it ->
//            Log.d("events_in_calendar_page", "events = $it")
            it?.forEach {
                Log.d("events_in_calendar_page", "events = $it")

//                Log.d("events_in_calendar_page", "it.attendeesName.component1() = ${it.attendeesName.component1()}")
            }

        })

        // Add dots based on my events
        viewModel.liveEvents.observe(viewLifecycleOwner, Observer {
            Log.d("wenbin", "viewModel.allLiveEvents, it = $it")
            it?.let {
                it.forEach {event ->
                    val year = TimeUtil.stampToYear(event.eventTime).toInt()
                    val month = TimeUtil.stampToMothInt(event.eventTime).toInt()
                    val day = TimeUtil.stampToDay(event.eventTime).toInt()
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

                Logger.d("selectedDate = $selectedDate")
            }
        }

        viewModel.navigationToCreateEventDialogFragment.observe(viewLifecycleOwner,
        Observer { date ->
            date?.let {
//                binding.imageViewCreateEvent.setOnClickListener {
//                    findNavController().navigate(CalendarFragmentDirections.navigateToCreateEventDialog(date))
//                }
                binding.fabCreateEvent.setOnClickListener {
                    findNavController().navigate(CalendarFragmentDirections.navigateToCreateEventDialog(date))
                    binding.fabShadow.visibility = View.GONE
                    closeFABMenu()
                }

                binding.fabLayoutCreateEvent.setOnClickListener {
                    findNavController().navigate(CalendarFragmentDirections.navigateToCreateEventDialog(date))
                    binding.fabShadow.visibility = View.GONE
                    closeFABMenu()
                }
            }
        })

        // fab setOnClickListener
        binding.fab.setOnClickListener {
            if (!isFABOpen) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }
        binding.fabNotification.setOnClickListener {
            findNavController().navigate(CalendarFragmentDirections.navigateToNotifyFragment())
            Log.d("checkFab", "fabNotification is clicked")
            binding.fabShadow.visibility = View.GONE
            closeFABMenu()
        }

        binding.fabLayoutNotification.setOnClickListener {
            findNavController().navigate(CalendarFragmentDirections.navigateToNotifyFragment())
            Log.d("checkFab", "fabLayoutNotification is clicked")

            binding.fabShadow.visibility = View.GONE
            closeFABMenu()
        }



        if (activity is MainActivity) {
            (activity as MainActivity).resetToolBar("月曆")
        }
        return binding.root
    }

    private fun addDotDecoration(year: Int, month: Int, day: Int) {
        widget.addDecorator(
            SingleDateDecorator(
                KnowHowBindingApplication.appContext.applicationContext.getColor(R.color.orange),
                CalendarDay.from(year, month, day)
            )
        )
    }

    // Set fab
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showFABMenu() {
        when (fabLayout_create_event.y){
            resources.getDimension(R.dimen.standard_0) -> fabLayout_create_event.visibility = View.INVISIBLE
            else -> fabLayout_create_event.visibility = View.VISIBLE
        }
        when (fabLayout_notification.y){
            resources.getDimension(R.dimen.standard_0) -> fabLayout_notification.visibility = View.INVISIBLE
            else -> fabLayout_notification.visibility = View.VISIBLE
        }

        isFABOpen = true
        fabLayout_create_event.animate().translationY(-resources.getDimension(R.dimen.standard_55))
        fabLayout_notification.animate().translationY(-resources.getDimension(R.dimen.standard_105))
        fab.animate().rotation(45.0f)
        fab_custom_pic.animate().rotation(45.0f)
        // Show shadow background.
        binding.fabShadow.visibility = View.VISIBLE
        binding.fab.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun closeFABMenu() {

        fab_create_event.visibility = View.VISIBLE
        fab_notification.visibility = View.VISIBLE

        when (fabLayout_create_event.y){
            resources.getDimension(R.dimen.standard_0) -> fabLayout_create_event.visibility = View.INVISIBLE
            else -> fabLayout_create_event.visibility = View.VISIBLE
        }
        when (fabLayout_notification.y){
            resources.getDimension(R.dimen.standard_0) -> fabLayout_notification.visibility = View.INVISIBLE
            else -> fabLayout_notification.visibility = View.VISIBLE
        }

        isFABOpen = false
        binding.fabShadow.visibility = View.GONE
        fab.animate().rotation(90.0f)
        fab_custom_pic.animate().rotation(90.0f)
        fabLayout_create_event.animate().translationY(resources.getDimension(R.dimen.standard_0))
        fabLayout_notification.animate().translationY(resources.getDimension(R.dimen.standard_0))
    }
}