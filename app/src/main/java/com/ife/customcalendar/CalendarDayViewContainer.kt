package com.ife.customcalendar

import android.util.Log
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
    dateViewer: TextView,
    selectedDates: MutableSet<LocalDate>
) : ViewContainer(view)
{
    val calendarDayTextView: TextView? = view.txtCalendarDayText

    // Will be set when this container is bound. See the dayBinder.
    lateinit var day: CalendarDay
    val today = LocalDate.now()
    private var userSelectedDate: LocalDate? = null
    private var selectedDate: LocalDate? = null
    init
    {
        view.setOnClickListener {
            Log.d("pxx", "SelectedDates: ${selectedDates}")
            /* Don't allow dates older than the current day to be selectable */
            if(day.owner == DayOwner.THIS_MONTH && (day.date == today || day.date.isAfter(today)))
            {

                /* Check if a date is already selected, remove it from the array if it is
                 * or add it to the array if it isn't already selected.*/
                if(selectedDates.contains(day.date))
                {
                    selectedDates.remove(day.date)
                    updateDateViewerTextView(dateViewer, "")
                    Log.d("pxx", "Result of remove date: ${selectedDates}")
                }
                else
                {
                    if(selectedDates.count() < 1)
                    {
                        selectedDate = day.date

                        selectedDates.add(day.date)
                        updateDateViewerTextView(dateViewer, selectedDate.toString())
                    }
                }
                Log.d("pxx", "Chosen date: ${day.date}")

                calendarView.notifyDayChanged(day)
            }
        }
    }

    private fun updateDateViewerTextView(dateViewer: TextView, value: String?)
    {
        dateViewer.text = value
    }
}