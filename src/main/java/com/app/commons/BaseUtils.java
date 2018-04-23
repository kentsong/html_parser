package com.app.commons;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kent on 16/7/21.
 */
public class BaseUtils {

    private static final String TAG = BaseUtils.class.getSimpleName();

    public static final String DB_DATE_FORMAT = "yyyy/MM/dd";
    public static final String DISPLAY_DATE_FORMAT = "yyyy / MM / dd";

    private static final String CASH_FORMAT_DOUBLE_WITH_DS = "$ #,##0.00";
    private static final String CASH_FORMAT_INT_WITH_DS = "$ #,##0";

    private static final String CASH_FORMAT_DOUBLE_WO_DS = "#,##0.00";
    private static final String CASH_FORMAT_INT_WO_DS = "#,##0";

    public static String now() {
        return now(DB_DATE_FORMAT);
    }

    public static String now(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    public static int dayOfWeek() {
        return dayOfWeek(new Date());
    }

    public static int dayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String dateFormat(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date stringToDate(String str, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 確認日期是否不再參考日期之後 ( 參考日當天或之前 )
     *
     * @param toCheck 要確認的日期
     * @param forRef 參考日期
     */
    public static boolean isDateNotAfter(String toCheck, String forRef) {
        toCheck = toCheck.replaceAll("[^\\d]", "");
        forRef = forRef.replaceAll("[^\\d]", "");
        return toCheck.compareTo(forRef) <= 0;
    }

    /**
     * 確認日期是否在參考日期之前 ( 不包含當天 )
     *
     * @param toCheck 要確認的日期
     * @param forRef 參考日期
     */
    public static boolean isDateBefore(String toCheck, String forRef) {
        toCheck = toCheck.replaceAll("[^\\d]", "");
        forRef = forRef.replaceAll("[^\\d]", "");
        return  toCheck.compareTo(forRef) < 0;
    }

    /**
     * 確認日期在參考日之後 ( 不包含參考日 )
     *
     * @param toCheck 要確認的日期
     * @param forRef 參考日期
     */
    public static boolean isDateAfter(String toCheck, String forRef) {
        toCheck = toCheck.replaceAll("[^\\d]", "");
        forRef = forRef.replaceAll("[^\\d]", "");
        return toCheck.compareTo(forRef) > 0;
    }

    /**
     * 確認是其是否不再參考日期之前 (參考日當天或之後）
     *
     * @param toCheck 要確認的日期
     * @param forRef 參考日期
     */
    public static boolean isDateNotBefore(String toCheck, String forRef) {
        toCheck = toCheck.replaceAll("[^\\d]", "");
        forRef = forRef.replaceAll("[^\\d]", "");
        return toCheck.compareTo(forRef) >= 0;
    }

    public static String addDate(String date, int value, String pattern) {
        return addDate(stringToDate(date, pattern), value, pattern);
    }

    public static String addDate(Date date, int value, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, value);
        return dateFormat(cal.getTime(), pattern);
    }

    public static String addMonth(String date, int value, String pattern) {
        return addMonth(stringToDate(date, pattern), value, pattern);
    }

    public static String addMonth(Date date, int value, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, value);
        return dateFormat(cal.getTime(), pattern);
    }

    public static String addHours(String date, int value, String pattern) {
        return addHours(stringToDate(date, pattern), value, pattern);
    }

    public static String addHours(Date date, int value, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, value);
        return dateFormat(cal.getTime(), pattern);
    }

    public static String addMinute(String date, int value, String pattern) {
        return addMinute(stringToDate(date, pattern), value, pattern);
    }

    public static String addMinute(Date date, int value, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, value);
        return dateFormat(cal.getTime(), pattern);
    }

    public static String filterOnlyASCII(String str) {
        //                          // only ascii                     // replace white space
        String asciiStr = str.replaceAll("[^\\x20-\\x7E]+", "").trim().replace("\n", "");
        return asciiStr;
    }

    public static String filterNumbers(String str) {
        return str.replaceAll("[^0-9\\.]", "");
    }

    public static String numberFormat(long number, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(number);
    }

    public static String numberFormat(double number, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(number);
    }

    public static String numberFormat(BigDecimal number, String discFormat) {
        return numberFormat(number.doubleValue(), discFormat);
    }

    public static String numberFormat(int number, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(number);
    }






}
