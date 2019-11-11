package com.ife.customcalendar

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.layout_calendar_day.view.*
import org.threeten.bp.LocalDate

class CalendarDayViewContainer(
    view: View,
    calendarView: CalendarView,
    selectedDates: MutableSet<LocalDate>
) : ViewContainer(view)
{
    val calendarDayTextView: TextView? = view.txtCalendarDayText

    // Will be set when this container is bound. See the dayBinder.
    lateinit var day: CalendarDay

    init
    {
        view.setOnClickListener {
            if(day.owner == DayOwner.THIS_MONTH)
            {
                /* Check if a date is already selected, remove it from the array if it is
                 * or add it to the array if it isn't already selected.*/
                if(selectedDates.contains(day.date))
                {
                    selectedDates.remove(day.date)
                }
                else
                {
                    selectedDates.add(day.date)
                }
                calendarView.notifyDayChanged(day)
            }
        }
    }
}