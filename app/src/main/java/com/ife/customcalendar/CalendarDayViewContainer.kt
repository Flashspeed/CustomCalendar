package com.ife.customcalendar

import android.view.View
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.layout_calendar_day.view.*

class CalendarDayViewContainer(view: View): ViewContainer(view) {
    val calendarDayTextView = view.txtCalendarDayText
}