package com.martinciesielski_listwan.stomez.weather;

/**
 * Created by marti_000 on 2015-08-28.
 */
public class Forecast {
    public Day[] getDailyForecast() {
        return mDailyForecast;
    }

    public void setDailyForecast(Day[] dailyForecast) {
        mDailyForecast = dailyForecast;
    }

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Hour[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public void setHourlyForecast(Hour[] hourlyForecast) {
        mHourlyForecast = hourlyForecast;
    }

    private Current mCurrent;
    private Hour[] mHourlyForecast;
    private Day[] mDailyForecast;
}
