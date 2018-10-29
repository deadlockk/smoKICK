package com.github.pocmo.sensordashboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

/*
 * @author: HJA
 */

public class Calendar extends AppCompatActivity {
    MaterialCalendarView MCV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        MCV = findViewById(R.id.calendarView);
        MCV.addDecorators(new SundayDecorator(), new SaturdayDecorator(), new OneDayDecorator());
    }
}
