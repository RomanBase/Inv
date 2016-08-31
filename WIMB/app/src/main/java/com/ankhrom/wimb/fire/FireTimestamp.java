package com.ankhrom.wimb.fire;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class FireTimestamp {

    public static Calendar get(long timestamp) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        return calendar;
    }

    public static String getReadable(long timestamp) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        return SimpleDateFormat.getDateTimeInstance().format(calendar.getTime());
    }
}
