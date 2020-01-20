package com.work_order.restapi;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
public class DateFormatter  {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    protected Date stringToDate(String date)  {

        try {
            Date  time = simpleDateFormat.parse(date);
            return time;
        }
        catch(Exception e){

            System.out.print(e);
        }

       return null;
    }


    protected Long getSecondsSinceCreation(Date date) {

        Long now = new Date().getTime();
        Long orderTime = date.getTime();

        return (now - orderTime) / 1000;
    }

    protected Date generateRandomDate(){

        int day = createRandomIntBetween(1, 28);
        int month = createRandomIntBetween(1, 12);
        int year = createRandomIntBetween(1970, 2020);

        int hour = createRandomIntBetween(0,12);
        int min_sec = createRandomIntBetween(0, 60);

        String randomDate = day + "-" + month + "-" + year + " " + hour + ":" + min_sec + ":" +min_sec;
        return stringToDate(randomDate);

    }

    public static int createRandomIntBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }


}
