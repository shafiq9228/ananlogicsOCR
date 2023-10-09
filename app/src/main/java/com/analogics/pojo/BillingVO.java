package com.analogics.pojo;

public class BillingVO {

    double currentUnits=0;
    double currentKwhUnits=0;
    double solarCloseKwh=0;
    double solarCurrentKwhUnits=0;
    double currentKvahUnits=0;
    double energyCharges=0;
    double fixedCharges=0;
    double customerCharges=0;
    double energyDuty=0;
    double capacitorCharges=0;
    String catagoryClass="";
    String presentMeterStatus="";
    double subsidyAmount=0;
    double PF=0;

    double netBillAmount=0;
    double totalBillAmount= 0;

    public double getSolarCloseKwh() {
        return solarCloseKwh;
    }
    public void setSolarCloseKwh(double solarCloseKwh) {
        this.solarCloseKwh = solarCloseKwh;
    }

    public double getSolarCurrentKwhUnits() {
        return solarCurrentKwhUnits;
    }
    public void setSolarCurrentKwhUnits(double solarCurrentKwhUnits) {
        this.solarCurrentKwhUnits = solarCurrentKwhUnits;
    }

    public double getNetBillAmount() {
        return netBillAmount;
    }
    public void setNetBillAmount(double netBillAmount) {
        this.netBillAmount = netBillAmount;
    }

    public double getTotalBillAmount() {
        return totalBillAmount;
    }
    public void setTotalBillAmount(double totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    public double getSubsidyAmount() {
        return subsidyAmount;
    }
    public void setSubsidyAmount(double subsidyAmount) {
        this.subsidyAmount = subsidyAmount;
    }

    public String getPresentMeterStatus() {
        return presentMeterStatus;
    }
    public void setPresentMeterStatus(String presentMeterStatus) {
        this.presentMeterStatus = presentMeterStatus;
    }

    public double getPF() {
        return PF;
    }
    public void setPF(double PF) {
        this.PF = PF;
    }

    public double getCurrentUnits() {
        return currentUnits;
    }
    public void setCurrentUnits(double currentUnits) {
        this.currentUnits = currentUnits;
    }

    public double getCurrentKwhUnits() {
        return currentKwhUnits;
    }
    public void setCurrentKwhUnits(double currentKwhUnits) {
        this.currentKwhUnits = currentKwhUnits;
    }

    public double getCurrentKvahUnits() {
        return currentKvahUnits;
    }

    public void setCurrentKvahUnits(double currentKvahUnits) {
        this.currentKvahUnits = currentKvahUnits;
    }

    public double getEnergyCharges() {
        return energyCharges;
    }

    public void setEnergyCharges(double energyCharges) {
        this.energyCharges = energyCharges;
    }

    public double getFixedCharges() {
        return fixedCharges;
    }

    public void setFixedCharges(double fixedCharges) {
        this.fixedCharges = fixedCharges;
    }

    public double getCustomerCharges() {
        return customerCharges;
    }
    public void setCustomerCharges(double customerCharges) {
        this.customerCharges = customerCharges;
    }

    public double getEnergyDuty() {
        return energyDuty;
    }
    public void setEnergyDuty(double energyDuty) {
        this.energyDuty = energyDuty;
    }

    public double getCapacitorCharges() {
        return capacitorCharges;
    }
    public void setCapacitorCharges(double capacitorCharges) {
        this.capacitorCharges = capacitorCharges;
    }

    public String getCatagoryClass() {
        return catagoryClass;
    }
    public void setCatagoryClass(String catagoryClass) {
        this.catagoryClass = catagoryClass;
    }

}
