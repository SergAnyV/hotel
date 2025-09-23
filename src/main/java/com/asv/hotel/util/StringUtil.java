package com.asv.hotel.util;

public class StringUtil {

    public static String preparedStringForPartiallyCoincidence(String line){
        return "%"+line+"%";
    }

    public static String preparedStringForPartiallyCoincidenceStart(String line){
        return "%"+line;
    }

    public static String preparedStringForPartiallyCoincidenceEnd(String line){
        return line+"%";
    }

    public static Boolean convertStringToBoolean(String line){
        line=line.trim().toLowerCase();
        return switch (line) {
            case "true" -> Boolean.TRUE;
            case "false" -> Boolean.FALSE;
            default -> null;
        };
    }

}
