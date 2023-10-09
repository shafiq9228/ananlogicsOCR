package com.analogics.utils;

public class TextFormaterUtils {

    public static double aroundUp(double number, int canDecimal) {
        int cifras = (int) Math.pow(10, canDecimal);
        return (double) (Math.ceil(number * cifras) / cifras);

    }



}
