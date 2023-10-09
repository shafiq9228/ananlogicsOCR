package com.analogics.utils;

import java.io.UnsupportedEncodingException;

public class HexStringConverter {
    static String hexRep = "0123456789ABCDEF";
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
    private static HexStringConverter hexStringConverter = null;

    public HexStringConverter() {
    }

    public static HexStringConverter getHexStringConverterInstance() {
        if (hexStringConverter == null) hexStringConverter = new HexStringConverter();
        return hexStringConverter;
    }

    public String stringToHex(String input) throws UnsupportedEncodingException {
        if (input == null) throw new NullPointerException();
        return asHex(input.getBytes());
    }

    public String hexToString(String txtInHex) {
        byte[] txtInByte = new byte[txtInHex.length() / 2];
        int j = 0;
        for (int i = 0; i < txtInHex.length(); i += 2) {
            txtInByte[j++] = Byte.parseByte(txtInHex.substring(i, i + 2), 16);
        }
        return new String(txtInByte);
    }

    public String asHex(byte[] buf) {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }


    public int hexToDecimal(String hex) {

        int counter = hex.length() - 1;
        int sum = 0;

        for (char c : hex.toCharArray()) {
            int i = hexRep.indexOf(c);
            sum = (int) (sum + (Math.pow(16, counter)) * i);
            counter--;
        }

        return sum;
    }

    public String hex2Binary(String hexdata)
    {
        hexdata=hexdata.trim();

        if(hexdata.length()<8){
            int len=hexdata.length();
            for(int j=0;j<8-len;j++){
                hexdata="0"+hexdata;
            }
        }
    //Converting Hexa decimal number to Decimal in Java
    Long decimal = Long.parseLong(hexdata, 16);

        System.out.println("Converted Decimal number is : "+decimal);

    //Converting hexa decimal number to binary in Java
    String binary = Long.toBinaryString(decimal);
        //System.out.printf("Hexadecimal to Binary conversion of %s is %s %n",hexdata,binary );

        return binary;

}

public String AWM_dateTimeConverter(String hexdata){



   String binaryDateTime =hex2Binary(hexdata);

        if(binaryDateTime.length()!=32){
            int length=binaryDateTime.length();
            for(int i=0;i<32-length;i++){
                binaryDateTime="0"+binaryDateTime;
            }
        }
    // Min   Hours    Year    Month  Date
    //  6      5        12       4     5
    //100110 01000 000000010011 0001 00001
    //012345 67890 123456789012 3456 789012
    //0-6     6-11   11-23      23-27 27
    //0,6     6-11   11-23      23-27 27
        String mins= Integer.parseInt(binaryDateTime.substring(0,6), 2)+"";
        String hours= Integer.parseInt(binaryDateTime.substring(6,11), 2)+"";
        String year= Integer.parseInt(binaryDateTime.substring(11,23), 2)+"";
        String month= Integer.parseInt(binaryDateTime.substring(23,27), 2)+"";
        String day= Integer.parseInt(binaryDateTime.substring(27), 2)+"";




   if(mins.length()==1){
    mins="0"+mins;
   }

    if(hours.length()==1){
        hours="0"+hours;
    }
    if(year.length()==1){
        year="0"+year;
    }
    if(month.length()==1){
        month="0"+month;
    }
    if(day.length()==1){
        day="0"+day;
    }




    return day+"/"+month+"/"+year+" "+hours+":"+mins;
}



    private static String binaryToHex(String binary) {
        int decimalValue = 0;
        int length = binary.length() - 1;
        for (int i = 0; i < binary.length(); i++) {
            decimalValue += Integer.parseInt(binary.charAt(i) + "") * Math.pow(2, length);
            length--;
        }
        return decimalToHex(decimalValue);
    }
    private static String decimalToHex(int decimal){
        String hex = "";
        while (decimal != 0){
            int hexValue = decimal % 16;
            hex = toHexChar(hexValue) + hex;
            decimal = decimal / 16;
        }
        return hex;
    }

    private static char toHexChar(int hexValue) {
        if (hexValue <= 9 && hexValue >= 0)
            return (char)(hexValue + '0');
        else
            return (char)(hexValue - 10 + 'A');
    }
}