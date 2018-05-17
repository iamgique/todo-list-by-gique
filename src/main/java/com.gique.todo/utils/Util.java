package com.gique.todo.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class Util {
    private static final Logger log = LogManager.getLogger(Util.class);

    public static boolean checkCreateTodoFormat(String msg) {
        log.info("checkCreateTodoFormat: {}", msg);
        String regex = "(.*)\\s:\\s(\\d{1,2}/\\d{1,2}/\\d{1,2}|today|tomorrow)(\\s:\\s\\d{1,2}:\\d{1,2}|$)";
        //String regex = "(\\w.*\\s:\\s)[^:]\\w.*";
        //String regex = "/.*\\s:\\s.*\\s/g";
        return Pattern.matches(regex, msg);
    }

    public static String getDueDate(String reqDate, String reqTime) throws ParseException {
        log.info("getDueDate: reqDate: {} reqTime: {}", reqDate, reqTime);
        String createdAt = "";
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yy");
        Date d = sdf.parse(convertTodayTomorrowDate(reqDate));

        if(reqTime == null || reqTime.equals("")){
            sdf.applyPattern("yyyy-MM-dd 12:00");
            createdAt = sdf.format(d);
        } else {
            sdf.applyPattern("yyyy-MM-dd " + reqTime);
            createdAt = sdf.format(d);
        }
        return createdAt;
    }

    public static String convertTodayTomorrowDate(String param) {
        log.info("convertTodayTomorrowDate: reqDate: {}", param);
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yy", new Locale("en", "EN"));
        Date d = new Date();
        LocalDateTime now;

        if(param.equals("today")){
            now = LocalDateTime.now();
        } else if(param.equals("tomorrow")){
            now = LocalDateTime.from(d.toInstant().atZone(ZoneId.of("UTC+07"))).plusDays(1);
        } else {
            return param;
        }

        Instant instant = now.atZone(ZoneId.of("UTC+07")).toInstant();
        Date dateFromOld = Date.from(instant);
        return dateFormat.format(dateFromOld);
    }

    public static void main(String args[]) throws ParseException {

        System.err.println(getDueDate("today", ""));
        System.err.println(getDueDate("tomorrow", ""));

        /*saveTodoTask();
        *//**//*String a = "aaaa : 20/12/18";
        String b = "aaaa : 20/12/18 : dssadkl";
        String c = "aaaa : 20/12/18 : 22:22";
        String d = "aaaaaa";
        String e = "aaa@aaa.com";
        String f = "aaaa : : 20/12/18 : 22:22";
        String g = "aaaa vvvv : 20/12/18 : 22:22";

        System.err.println(checkCreateTodoFormat(a));
        System.err.println(checkCreateTodoFormat(b));
        System.err.println(checkCreateTodoFormat(c));
        System.err.println(checkCreateTodoFormat(d));
        System.err.println(checkCreateTodoFormat(e));
        System.err.println(checkCreateTodoFormat(f));
        System.err.println(checkCreateTodoFormat(g));*/
    }

}
