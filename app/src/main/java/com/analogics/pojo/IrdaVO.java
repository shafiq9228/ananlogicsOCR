package com.analogics.pojo;

import java.io.Serializable;

public class IrdaVO  implements Serializable {

    String meterNumber="";
    double voltage_R_VR=0.0;
    double voltage_Y_VY=0.0;
    double voltage_B_VB=0.0;
    double current_R_IR=0.0;
    double current_Y_IY=0.0;
    double current_B_IB=0.0;
    private static double KWH=0.0;

    private static double KVAH=0.0;
    private static double hv_rmd=0.0;
    int irdaReadingFlag=0;
    double export_KWH=0.0;
    String meterMake = "";

    public double getExport_KWH() {
        return export_KWH;
    }

    public void setExport_KWH(double export_KWH) {
        this.export_KWH = export_KWH;
    }

    public int getIrdaReadingFlag() {
        return irdaReadingFlag;
    }

    public void setIrdaReadingFlag(int irdaReadingFlag) {
        this.irdaReadingFlag = irdaReadingFlag;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public double getVoltage_R_VR() {
        return voltage_R_VR;
    }

    public void setVoltage_R_VR(double voltage_R_VR) {
        this.voltage_R_VR = voltage_R_VR;
    }

    public double getVoltage_Y_VY() {
        return voltage_Y_VY;
    }

    public void setVoltage_Y_VY(double voltage_Y_VY) {
        this.voltage_Y_VY = voltage_Y_VY;
    }

    public double getVoltage_B_VB() {
        return voltage_B_VB;
    }

    public void setVoltage_B_VB(double voltage_B_VB) {
        this.voltage_B_VB = voltage_B_VB;
    }

    public double getCurrent_R_IR() {
        return current_R_IR;
    }

    public void setCurrent_R_IR(double current_R_IR) {
        this.current_R_IR = current_R_IR;
    }

    public double getCurrent_Y_IY() {
        return current_Y_IY;
    }

    public void setCurrent_Y_IY(double current_Y_IY) {
        this.current_Y_IY = current_Y_IY;
    }

    public double getCurrent_B_IB() {
        return current_B_IB;
    }

    public void setCurrent_B_IB(double current_B_IB) {
        this.current_B_IB = current_B_IB;
    }

    public double getKWH() {
        return KWH;
    }

    public void setKWH(double KWH) {
        this.KWH = KWH;
    }

    public double getKVAH() {
        return KVAH;
    }

    public void setKVAH(double KVAH) {
        this.KVAH = KVAH;
    }

    public double getHv_rmd() {
        return hv_rmd;
    }

    public void setHv_rmd(double hv_rmd) {
        this.hv_rmd = hv_rmd;
    }

    public String getMeterMake() {
        return meterMake;
    }

    public void setMeterMake(String meterMake) {
        this.meterMake = meterMake;
    }


}
