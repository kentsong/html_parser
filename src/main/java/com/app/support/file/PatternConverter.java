package com.app.support.file;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Kent on 2017/1/23.
 */

public class PatternConverter {

    private static final String beginPattern = "%d{";
    private static final String endPattern = "}";

    private static boolean matchPattern(String input) {
        int beginIndex = input.indexOf(beginPattern);
        if(beginIndex != -1) {
            return input.indexOf(endPattern, beginIndex) != -1;
        } else {
            return false;
        }
    }

    public static String convert(String input) {
        if(!matchPattern(input))
            return input;

        int beginIndex = input.indexOf(beginPattern);
        int endIndex = input.indexOf(endPattern, beginIndex);

        String[] params = input.substring(beginIndex + beginPattern.length(), endIndex).split(",");
        String format = params[0].trim();
        @SuppressWarnings("HardCodedStringLiteral") String field = "dd";
        int amount = 0;

        if(params.length > 1) {
            field = params[1].trim();
            amount = Integer.parseInt(params[2].trim());
        }

        Calendar calendar = Calendar.getInstance();
        if(field.equals("dd")) {
            calendar.add(Calendar.DATE, amount);
        } else if(field.equals("MM")) {
            calendar.add(Calendar.MONTH, amount);
        } else if(field.equals("yyyy")) {
            calendar.add(Calendar.YEAR, amount);
        } else if(field.equals("HH")) {
            calendar.add(Calendar.HOUR, amount);
        } else if(field.equals("mm")) {
            calendar.add(Calendar.MINUTE, amount);
        } else if(field.equals("ss")) {
            calendar.add(Calendar.SECOND, amount);
        } else {
            throw new IllegalArgumentException("unsupported field pattern:" + field);
        }

        String date = new SimpleDateFormat(format).format(calendar.getTime());

        String output = input.replace(input.substring(beginIndex, endIndex + endPattern.length()), date);

        return convert(output);
    }
}
