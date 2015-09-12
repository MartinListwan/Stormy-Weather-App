package com.martinciesielski_listwan.stomez.UI;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.martinciesielski_listwan.stomez.R;

public class DailyForecastActivity extends ListActivity {
    // A list view requires you to have an id that is android:id/list
    // A list view requires you to have an id that is android:id/empty in case there is nothing loaded

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        String[] daysOfTheWeek = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
        // Adapter is what you use to move content to the UI
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                daysOfTheWeek);
        setListAdapter(adapter);
    }
}