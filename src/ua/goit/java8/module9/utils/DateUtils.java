package ua.goit.java8.module9.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Taras on 21.09.2017.
 */
public class DateUtils {
    public String convertDateToString(Date indate)
    {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        try{
            dateString = sdfr.format( indate );
        }catch (Exception ex ){
            System.out.println(ex);
        }
        return dateString;
    }
}
