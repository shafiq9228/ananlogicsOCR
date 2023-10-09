package com.analogics.pojo;

import java.util.ArrayList;

public class ReportsVO {


    String totalServiceCount="0";
    public ArrayList<String> totalCatagoryTypeList=new ArrayList<String>();
    public ArrayList<String> totalCatagoryCountList=new ArrayList<String>();

    String totalBilledServiceCount="0";
    public ArrayList<String> totalBilledCatagoryTypeList=new ArrayList<String>();
    public ArrayList<String> totalBilledCatagoryCountList=new ArrayList<String>();

    String totalUnBilledServiceCount="0";
    public ArrayList<String> totalUnBilledCatagoryTypeList=new ArrayList<String>();
    public ArrayList<String> totalUnBilledCatagoryCountList=new ArrayList<String>();


    String totalBilledAmount="0";
    public ArrayList<String> totalBilledAmountCatagoryTypeList=new ArrayList<String>();
    public ArrayList<String> totalBilledAmountCatagoryCountList=new ArrayList<String>();

    String totalBilledUnits="0";
    public ArrayList<String> totalBilledUnitsCatagoryTypeList=new ArrayList<String>();
    public ArrayList<String> totalBilledUnitsCatagoryCountList=new ArrayList<String>();

    String todaysTotalBilledCount="0";
    String todaysTotalBilledUnits="0";
    String todaysTotalBilledAmount="0";

    public ArrayList<String> demand_DistName_List=new ArrayList<String>();
    public ArrayList<String> demand_TRANSNO_List=new ArrayList<String>();
    public ArrayList<String> demand_SERVICE_NO_List=new ArrayList<String>();
    public ArrayList<String> demand_ARREARS_List=new ArrayList<String>();
    public ArrayList<String> demand_CMD_List=new ArrayList<String>();
    public ArrayList<String> demand_TOTAL_List= new ArrayList<>();

    public ArrayList<String> status_DistName_List=new ArrayList<String>();
    public ArrayList<String> status_TRANSNO_List=new ArrayList<String>();
    public ArrayList<String> status_SERVICE_NO_List=new ArrayList<String>();
    public ArrayList<String> status_pmtr_rdg_KWH_List=new ArrayList<String>();
    public ArrayList<String> status_Billed_units_List=new ArrayList<String>();

    public String getTotalServiceCount() {
        return totalServiceCount;
    }

    public void setTotalServiceCount(String totalServiceCount) {
        this.totalServiceCount = totalServiceCount;
    }

    public ArrayList<String> getTotalCatagoryTypeList() {
        return totalCatagoryTypeList;
    }

    public void setTotalCatagoryTypeList(ArrayList<String> totalCatagoryTypeList) {
        this.totalCatagoryTypeList = totalCatagoryTypeList;
    }

    public ArrayList<String> getTotalCatagoryCountList() {
        return totalCatagoryCountList;
    }

    public void setTotalCatagoryCountList(ArrayList<String> totalCatagoryCountList) {
        this.totalCatagoryCountList = totalCatagoryCountList;
    }

    public String getTotalBilledServiceCount() {
        return totalBilledServiceCount;
    }

    public void setTotalBilledServiceCount(String totalBilledServiceCount) {
        this.totalBilledServiceCount = totalBilledServiceCount;
    }

    public ArrayList<String> getTotalBilledCatagoryTypeList() {
        return totalBilledCatagoryTypeList;
    }

    public void setTotalBilledCatagoryTypeList(ArrayList<String> totalBilledCatagoryTypeList) {
        this.totalBilledCatagoryTypeList = totalBilledCatagoryTypeList;
    }

    public ArrayList<String> getTotalBilledCatagoryCountList() {
        return totalBilledCatagoryCountList;
    }

    public void setTotalBilledCatagoryCountList(ArrayList<String> totalBilledCatagoryCountList) {
        this.totalBilledCatagoryCountList = totalBilledCatagoryCountList;
    }

    public String getTotalUnBilledServiceCount() {
        return totalUnBilledServiceCount;
    }

    public void setTotalUnBilledServiceCount(String totalUnBilledServiceCount) {
        this.totalUnBilledServiceCount = totalUnBilledServiceCount;
    }

    public ArrayList<String> getTotalUnBilledCatagoryTypeList() {
        return totalUnBilledCatagoryTypeList;
    }

    public void setTotalUnBilledCatagoryTypeList(ArrayList<String> totalUnBilledCatagoryTypeList) {
        this.totalUnBilledCatagoryTypeList = totalUnBilledCatagoryTypeList;
    }

    public ArrayList<String> getTotalUnBilledCatagoryCountList() {
        return totalUnBilledCatagoryCountList;
    }

    public void setTotalUnBilledCatagoryCountList(ArrayList<String> totalUnBilledCatagoryCountList) {
        this.totalUnBilledCatagoryCountList = totalUnBilledCatagoryCountList;
    }

    public String getTotalBilledAmount() {
        return totalBilledAmount;
    }

    public void setTotalBilledAmount(String totalBilledAmount) {
        this.totalBilledAmount = totalBilledAmount;
    }

    public ArrayList<String> getTotalBilledAmountCatagoryTypeList() {
        return totalBilledAmountCatagoryTypeList;
    }

    public void setTotalBilledAmountCatagoryTypeList(ArrayList<String> totalBilledAmountCatagoryTypeList) {
        this.totalBilledAmountCatagoryTypeList = totalBilledAmountCatagoryTypeList;
    }

    public ArrayList<String> getTotalBilledAmountCatagoryCountList() {
        return totalBilledAmountCatagoryCountList;
    }

    public void setTotalBilledAmountCatagoryCountList(ArrayList<String> totalBilledAmountCatagoryCountList) {
        this.totalBilledAmountCatagoryCountList = totalBilledAmountCatagoryCountList;
    }

    public String getTotalBilledUnits() {
        return totalBilledUnits;
    }

    public void setTotalBilledUnits(String totalBilledUnits) {
        this.totalBilledUnits = totalBilledUnits;
    }

    public ArrayList<String> getTotalBilledUnitsCatagoryTypeList() {
        return totalBilledUnitsCatagoryTypeList;
    }

    public void setTotalBilledUnitsCatagoryTypeList(ArrayList<String> totalBilledUnitsCatagoryTypeList) {
        this.totalBilledUnitsCatagoryTypeList = totalBilledUnitsCatagoryTypeList;
    }

    public ArrayList<String> getTotalBilledUnitsCatagoryCountList() {
        return totalBilledUnitsCatagoryCountList;
    }

    public void setTotalBilledUnitsCatagoryCountList(ArrayList<String> totalBilledUnitsCatagoryCountList) {
        this.totalBilledUnitsCatagoryCountList = totalBilledUnitsCatagoryCountList;
    }

    public String getTodaysTotalBilledCount() {
        return todaysTotalBilledCount;
    }

    public void setTodaysTotalBilledCount(String todaysTotalBilledCount) {
        this.todaysTotalBilledCount = todaysTotalBilledCount;
    }

    public String getTodaysTotalBilledUnits() {
        return todaysTotalBilledUnits;
    }

    public void setTodaysTotalBilledUnits(String todaysTotalBilledUnits) {
        this.todaysTotalBilledUnits = todaysTotalBilledUnits;
    }

    public String getTodaysTotalBilledAmount() {
        return todaysTotalBilledAmount;
    }

    public void setTodaysTotalBilledAmount(String todaysTotalBilledAmount) {
        this.todaysTotalBilledAmount = todaysTotalBilledAmount;
    }

    public ArrayList<String> getDemand_DistName_List() {
        return demand_DistName_List;
    }

    public void setDemand_DistName_List(ArrayList<String> demand_DistName_List) {
        this.demand_DistName_List = demand_DistName_List;
    }

    public ArrayList<String> getDemand_TRANSNO_List() {
        return demand_TRANSNO_List;
    }

    public void setDemand_TRANSNO_List(ArrayList<String> demand_TRANSNO_List) {
        this.demand_TRANSNO_List = demand_TRANSNO_List;
    }

    public ArrayList<String> getDemand_SERVICE_NO_List() {
        return demand_SERVICE_NO_List;
    }

    public void setDemand_SERVICE_NO_List(ArrayList<String> demand_SERVICE_NO_List) {
        this.demand_SERVICE_NO_List = demand_SERVICE_NO_List;
    }

    public ArrayList<String> getDemand_ARREARS_List() {
        return demand_ARREARS_List;
    }

    public void setDemand_ARREARS_List(ArrayList<String> demand_ARREARS_List) {
        this.demand_ARREARS_List = demand_ARREARS_List;
    }

    public ArrayList<String> getDemand_CMD_List() {
        return demand_CMD_List;
    }

    public void setDemand_CMD_List(ArrayList<String> demand_CMD_List) {
        this.demand_CMD_List = demand_CMD_List;
    }

    public ArrayList<String> getDemand_TOTAL_List() {
        return demand_TOTAL_List;
    }

    public void setDemand_TOTAL_List(ArrayList<String> demand_TOTAL_List) {
        this.demand_TOTAL_List = demand_TOTAL_List;
    }
}
