package com.gique.todo.utils;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class Util {

    public String getDueDate(String reqDate, String reqTime) throws ParseException {
        String createdAt = "";
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yy");
        Date d = sdf.parse(this.convertTodayTomorrowDate(reqDate));

        if(reqTime == null || reqTime.equals("")){
            sdf.applyPattern("yyyy-MM-dd 12:00");
            createdAt = sdf.format(d);
        } else {
            sdf.applyPattern("yyyy-MM-dd " + reqTime);
            createdAt = sdf.format(d);
        }
        return createdAt;
    }

    public String convertTodayTomorrowDate(String param) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date d = new Date();
        if(param.equals("today")){
            return dateFormat.format(d);
        } else if(param.equals("tomorrow")){
            DateTime dtOrg = new DateTime(d);
            DateTime dtPlusOne = dtOrg.plusDays(1);
            return dateFormat.format(dtPlusOne);
        } else {
            return param;
        }
    }

}
