package de.overcooked_industries.util;

import java.util.Arrays;

public abstract class StringUtils {
    public static String findAfterLast(String string, String substring)
    {
        return Arrays.stream(string.split(substring)).toList().getLast();
    }

    public static String toHexString(int num)
    {
        return "0x" + Integer.toHexString(num);
    }
}
