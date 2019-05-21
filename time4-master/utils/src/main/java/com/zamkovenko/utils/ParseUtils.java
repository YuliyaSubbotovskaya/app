package com.zamkovenko.utils;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 11.06.2018
 */
public class ParseUtils {

    public static String GetClearNumer(String number)
    {
        StringBuilder sb = new StringBuilder();
        for (char c :
                number.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
