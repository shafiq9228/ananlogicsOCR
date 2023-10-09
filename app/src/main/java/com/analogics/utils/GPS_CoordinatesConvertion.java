package com.analogics.utils;

/**
 * Created by atilhydpun-05 on 6/26/2017.
 */

public class GPS_CoordinatesConvertion {

    public String convertLatLong(String value, boolean isLatitude){
        String convertedValue = "";
        try{
            String integerValue = "";
            double output = 0D;

            if(isLatitude){
                integerValue = value.substring(0, 2);
            }else{
                integerValue = value.substring(0, 3);
            }

            double doubleValue = Float.valueOf(value.trim());
            doubleValue = doubleValue * 10000;

            doubleValue = doubleValue % 1000000;
            doubleValue = doubleValue / 600000;

            output = Double.valueOf(integerValue) + doubleValue;
            output = (double) Math.round(output * 100000000) / 100000000;

            convertedValue = String.valueOf(output);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return convertedValue;
    }
}
