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
    private val yearArray = arrayListOf<String>()



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

        // Always generate +20 years from the current year
        for(i in Year.now().value..Year.now().value + 20)
        {
            yearArray.add(i.toString())
        }

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(0)

        /* 20 years * 12 months */
        val lastMonth = currentMonth.plusMonths(yearArray.count() * 12.toLong())
        val firstDayOfWeek = DayOfWeek.SUNDAY

        initMonthSpinner()
        initYearSpinner()

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

        /*** MONTH SPINNER **/
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
                    val currentMonth = (YearMonth.now().month.ordinal + 1).toString()
                        .padStart(2, '0')

                    calendarView.scrollToMonth(
                        /* On first load always begin with selecting the current year and month*/
                        YearMonth.parse("${Year.now()}-${currentMonth}")
                    )
                    spinnerMonthSelector.setSelection(YearMonth.now().month.ordinal)

                    loadedOnce = false

                }
                else
                {
                    val selectedMonth = (position + 1).toString().padStart(2, '0')
                    val yearMonthToScrollTo = "${spinnerYearSelector.selectedItem}-${selectedMonth}"
                    calendarView.scrollToMonth(
                        YearMonth.parse(yearMonthToScrollTo)
                    )
                }

            }

        }
    }

    private fun initYearSpinner()
    {
        /*** YEAR SPINNER **/
        ArrayAdapter<String>(
            context!!,
            R.layout.simple_spinner_item,
            yearArray
        ).let { adapter ->
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            spinnerYearSelector.adapter = adapter
        }

        yearArray.forEach {
            println("Year: $it")
        }

        spinnerYearSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
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
                val yearMonthToScrollTo = "${spinnerYearSelector.getItemAtPosition(position)}-${(spinnerMonthSelector.selectedItemPosition + 1).toString().padStart(
                    2,
                    '0'
                )}"

                Toast.makeText(context!!, yearMonthToScrollTo, Toast.LENGTH_SHORT).show()

//                calendarView.scrollToMonth(YearMonth.parse("2020-01"))
                calendarView.scrollToMonth(YearMonth.parse(yearMonthToScrollTo))

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

