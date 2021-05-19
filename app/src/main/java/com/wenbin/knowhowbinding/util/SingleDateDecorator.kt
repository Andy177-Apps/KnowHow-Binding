package com.wenbin.knowhowbinding.util

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class SingleDateDecorator(private val color: Int, date: CalendarDay) :
DayViewDecorator {
    var theDay = date
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day == theDay
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(10F, color))
    }
}