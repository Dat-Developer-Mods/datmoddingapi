package com.demmodders.datmoddingapi.util;

public class StringUtils {
    public static String makePlural(String string){
        if (string.charAt(string.length()-1) == 's'){
            return string + "'";
        } else {
            return string + "'s";
        }
    }
}
