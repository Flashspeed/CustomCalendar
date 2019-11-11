package com.ife.customcalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ife.customcalendar.fragments.FragmentCalendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragContainer, FragmentCalendar()).commit()
    }
}
