package com.analogics.irda;

import android.util.Log;

import com.analogics.ui.settings.UsbService;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;

public class AnalogicsUtil {
    private static final String FINGER_PATH = "/sys/class/whty_common_dev/whty_common_dev/ty_hw_Control";

    public void setIRBaudRate1P(UsbService usbService){
        usbService.changeBaudRate(2400);
        byte[]  buffer1 = new byte[3];
        buffer1[0] = 0x1B;
        buffer1[1] = 0x52;
        buffer1[2] = 0x32;
        try{
            if (usbService != null) { // if UsbService was correctly binded, Send data
                usbService.write(buffer1);
                Thread.sleep(1000);
            }
        }catch(Exception ex){

        }
    }

    public void setIRBaudRate3P(UsbService usbService){
        usbService.changeBaudRate(2400);
        byte[]  buffer1 = new byte[3];
        buffer1[0] = 0x1B;
        buffer1[1] = 0x52;
        buffer1[2] = 0x32;
        try{
            if (usbService != null) { // if UsbService was correctly binded, Send data
                usbService.write(buffer1);
                Thread.sleep(1000);
            }
        }catch(Exception ex){

        }
    }

    public void setIRDABaudRate1P(UsbService usbService){
        usbService.changeBaudRate(2400);
        byte[]  buffer1 = new byte[3];
        buffer1[0] = 0x1B;
        buffer1[1] = 0x44;
        buffer1[2] = 0x32;
        try{
            if (usbService != null) { // if UsbService was correctly binded, Send data
                usbService.write(buffer1);
                Thread.sleep(1000);
            }
        }catch(Exception ex){

        }
    }

    public void setIRDABaudRate3P(UsbService usbService){
        usbService.changeBaudRate(9600);
        byte[]  buffer1 = new byte[3];
        buffer1[0] = 0x1B;
        buffer1[1] = 0x44;
        buffer1[2] = 0x39;
        try{
            if (usbService != null) { // if UsbService was correctly binded, Send data
                usbService.write(buffer1);
                Thread.sleep(1000);
            }
        }catch(Exception ex){

        }
    }
    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }


    //zyp add  start
    public static void fingerPrintPowerOn() {
        //device:  /sys/class/whty_common_dev/whty_common_dev/ty_hw_Control
        try (FileWriter writer = new FileWriter(FINGER_PATH)) {
            writer.write("usbidseton");
            writer.flush();
            Thread.sleep(100);
        } catch (Exception e) {
        }
    }

    public static void fingerPrintPowerOff() {
        try (FileWriter writer = new FileWriter(FINGER_PATH)) {
            writer.write("usbidsetoff");
            writer.flush();
            Thread.sleep(100);
        } catch (Exception e) {
        }
    }

    public String getPowerOnCommand(){
        String command = "";
        try{
            File powerFile = new File(FINGER_PATH);
            command = FileUtils.readFileToString(powerFile);
        }catch(Exception ex){

        }
        return command;
    }
}
