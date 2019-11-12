package com.ife.customcalendar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ife.customcalendar.CalendarDayViewContainer
import com.ife.customcalendar.R
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import kotlinx.android.synthetic.main.layout_calendar.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.Year
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter

class FragmentCalendar : Fragment()
{

    private val selectedDates = mutableSetOf<LocalDate>()
    private val today = LocalDate.now()
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.layout_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = DayOfWeek.SUNDAY

        initMonthSpinner()

        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
//        calendarView.hasBoundaries = true
//        calendarView.inDateStyle = InDateStyle.NONE
//        calendarView.outDateStyle = OutDateStyle.NONE

        calendarView.dayBinder = object : DayBinder<CalendarDayViewContainer>
        {

            // Called only when a new container is needed
            override fun create(view: View): CalendarDayViewContainer = CalendarDayViewContainer(
                view,
                calendarView,
                selectedDates
            )

            // Called every time a container needs to be reused
            override fun bind(container: CalendarDayViewContainer, day: CalendarDay)
            {
                container.day = day
                container.calendarDayTextView?.text = day.date.dayOfMonth.toString()

                if(day.owner == DayOwner.THIS_MONTH)
                {
                    when
                    {
                        selectedDates.contains(day.date) ->
                        {
                            container.calendarDayTextView?.setTextColor(context!!.getColor(R.color.colorWhite))
                            container.calendarDayTextView?.setBackgroundResource(R.drawable.shape_selected_day_background)
                        }
                        today == day.date ->
                        {
                            container.calendarDayTextView?.setTextColor(context!!.getColor(R.color.colorWhite))
                            container.calendarDayTextView?.setBackgroundResource(R.drawable.shape_curent_day_background)
                        }
                        else ->
                        {
                            container.calendarDayTextView?.setTextColor(context!!.getColor(R.color.colorNormalUnselectedCalendarDay))
                            container.calendarDayTextView?.background = null

                        }
                    }

                }
            }
        }

    }

    private fun initMonthSpinner()
    {
        ArrayAdapter.createFromResource(
            context!!,
            R.array.days_of_month,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerMonthSelector.adapter = adapter
        }


        spinnerMonthSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {

            var loadedOnce = true

            override fun onNothingSelected(parent: AdapterView<*>?)
            {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            )
            {
                if(loadedOnce)
                {
                    val selectedMonthAndYear = (YearMonth.now().month.ordinal + 1).toString()
                        .padStart(2, '0')

                    calendarView.scrollToMonth(
                        /* Always begin with selecting the current year and month*/
                        YearMonth.parse(
                            "${Year.now()}-${(YearMonth.now().month.ordinal + 1).toString().padStart(
                                2,
                                '0'
                            )}"
                        )
                    )
                    spinnerMonthSelector.setSelection(YearMonth.now().month.ordinal)
                    Toast.makeText(
                        context!!,
                        "Selected position non index $selectedMonthAndYear",
                        Toast.LENGTH_SHORT
                    ).show()

                    loadedOnce = false

                }
                else
                {
                    val selectedMonthAndYear = (position + 1).toString().padStart(2, '0')

                    calendarView.scrollToMonth(
                        YearMonth.parse(
                            //TODO make year dynamic instead of always using current year
                            "${Year.now()}-${(position + 1).toString().padStart(
                                2,
                                '0'
                            )}"
                        )
                    )
                    Toast.makeText(
                        context!!,
                        "Selected position non index $selectedMonthAndYear",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }
    }

}

object Constants
{
    const val JANUARY = 0
    const val FEBRUARY = 1
    const val MARCH = 2
    const val APRIL = 3
    const val MAY = 4
    const val JUNE = 5
    const val JULY = 6
    const val AUGUST = 7
    const val SEPTEMBER = 8
    const val OCTOBER = 9
    const val NOVEMBER = 10
    const val DECEMBER = 11
}

