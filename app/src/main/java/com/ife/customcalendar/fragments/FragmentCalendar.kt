package com.ife.customcalendar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ife.customcalendar.CalendarDayViewContainer
import com.ife.customcalendar.R
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import kotlinx.android.synthetic.main.layout_calendar.*
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields
import java.util.*

class FragmentCalendar: Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)

        calendarView.dayBinder = object: DayBinder<CalendarDayViewContainer> {

            // Called only when a new container is needed
            override fun create(view: View): CalendarDayViewContainer = CalendarDayViewContainer(
                view,
                calendarView
            )

            // Called every time a container needs to be reused
            override fun bind(container: CalendarDayViewContainer, day: CalendarDay) {
                container.day = day
                container.calendarDayTextView?.text = day.date.dayOfMonth.toString()
            }
        }
    }
}