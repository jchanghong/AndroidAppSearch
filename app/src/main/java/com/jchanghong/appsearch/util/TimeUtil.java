package com.jchanghong.appsearch.util;

import java.util.Calendar;

class TimeUtil {
    public static final int HOUR_PER_DAY = 24;              //1day=24h
    public static final int MINUTE_PER_HOUR = 60;           //1h=60min
    public static final int SECOND_PER_MINUTE = 60;           //1min=60s
    public static final int MILLISECOND_PER_SECOND = 1000;    //1s=1000ms

    /**
     * {@link Calendar#YEAR}
     *
     * @return
     */
    public static int getYear() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.YEAR);
    }


    /**
     * {@link Calendar#MONTH}
     *
     * @return
     */
    public static int getMonth() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.MONTH);
    }

    /**
     * get days since 1970.1.1 to timeInMillis when timeInMillis>=0;
     * get days since 1970.1.1 to now when timeInMillis<0.
     *
     * @return
     */
    public static int getDaySince1970January1(long timeInMillis) {
        long milliSecond = timeInMillis;
        if (timeInMillis < 0) {
            Calendar calendar = Calendar.getInstance();
            milliSecond = calendar.getTimeInMillis();
        }

        return (int) (milliSecond / TimeUtil.MILLISECOND_PER_SECOND / TimeUtil.SECOND_PER_MINUTE / TimeUtil.MINUTE_PER_HOUR / TimeUtil.HOUR_PER_DAY);
    }

    /**
     * {@link Calendar#DAY_OF_MONTH}
     *
     * @return
     */
    public static int getDayOfMonth() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * {@link Calendar#DAY_OF_YEAR}
     *
     * @return
     */
    public static int getDayOfYear() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * {@link Calendar#HOUR_OF_DAY}
     *
     * @return
     */
    private static int getHourOfDay() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * {@link Calendar#MINUTE}
     *
     * @return
     */
    private static int getMinute() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.MINUTE);
    }

    /**
     * Indicating the minute of the day.
     * E.g., at 10:04:15.250 PM the {@code TimeUtil#getMinuteOfDay()} is (22*60+04).
     *
     * @return
     */
    private static int getMinuteOfDay() {

        return (TimeUtil.getHourOfDay() * TimeUtil.MINUTE_PER_HOUR) + TimeUtil.getMinute();
    }

    /**
     * {@link Calendar#SECOND}
     *
     * @return
     */
    private static int getSecond() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.SECOND);
    }

    /**
     * Indicating the second of the day.
     * E.g., at 10:04:15.250 PM the {@code TimeUtil#getSecondOfDay()} is (22*60+04)*60+15.
     *
     * @return
     */
    private static int getSecondOfDay() {

        return (TimeUtil.getMinuteOfDay() * TimeUtil.SECOND_PER_MINUTE) + TimeUtil.getSecond();
    }

    /**
     * {@link Calendar#MILLISECOND}
     *
     * @return
     */
    private static int getMilliSecond() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.MILLISECOND);
    }

    /**
     * Indicating the millisecond of the day.
     * E.g., at 10:04:15.250 PM the {@code TimeUtil#getMilliSecondOfDay()} is ((22*60+04)*60+15)*1000+250.
     *
     * @return
     */
    public static int getMilliSecondOfDay() {

        return (TimeUtil.getSecondOfDay() * TimeUtil.MILLISECOND_PER_SECOND) + TimeUtil.getMilliSecond();
    }
}
