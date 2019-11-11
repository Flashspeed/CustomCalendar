package com.ife.customcalendar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ife.customcalendar.CalendarDayViewContainer
import com.ife.customcalendar.R
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import kotlinx.android.synthetic.main.layout_calendar.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter

class FragmentCalendar: Fragment() {


    private val selectedDates = mutableSetOf<LocalDate>()
    private val today = LocalDate.now()
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = DayOfWeek.SUNDAY

        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)

        calendarView.dayBinder = object: DayBinder<CalendarDayViewContainer> {

            // Called only when a new container is needed
            override fun create(view: View): CalendarDayViewContainer = CalendarDayViewContainer(
                view,
                calendarView,
                selectedDates
            )

            // Called every time a container needs to be reused
            override fun bind(container: CalendarDayViewContainer, day: CalendarDay) {
                container.day = day
                container.calendarDayTextView?.text = day.date.dayOfMonth.toString()

                if(day.owner == DayOwner.THIS_MONTH)
                {
                    if(selectedDates.contains(day.date))
                    {
                        container.calendarDayTextView?.setTextColor(context!!.getColor(R.color.colorWhite))
                        container.calendarDayTextView?.setBackgroundResource(R.drawable.shape_selected_day_background)
                    }
                    else if(today == day.date)
                    {
                        container.calendarDayTextView?.setTextColor(context!!.getColor(R.color.colorWhite))
                        container.calendarDayTextView?.setBackgroundResource(R.drawable.shape_curent_day_background)
                    }
                    else
                    {
                        container.calendarDayTextView?.setTextColor(context!!.getColor(R.color.colorNormalUnselectedCalendarDay))
                        container.calendarDayTextView?.background = null

                    }

                }
            }
        }
    }
}