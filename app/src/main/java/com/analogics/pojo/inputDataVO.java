package com.analogics.pojo;

import java.io.Serializable;

public class inputDataVO implements Serializable {
    private String datasd;
    private String ip;

    public float getOldReadingKwhTemp() {
        return oldReadingKwhTemp;
    }

    public void setOldReadingKwhTemp(float oldReadingKwhTemp) {
        this.oldReadingKwhTemp = oldReadingKwhTemp;
    }

    float oldReadingKwhTemp;
    public float getNewmfTemp() {
        return newmfTemp;
    }

    public void setNewmfTemp(float newmfTemp) {
        this.newmfTemp = newmfTemp;
    }

    float newmfTemp;

    public float getOldmetermdTemp() {
        return oldmetermdTemp;
    }

    public void setOldmetermdTemp(float oldmetermdTemp) {
        this.oldmetermdTemp = oldmetermdTemp;
    }

    float oldmetermdTemp;

    public float getNewmetermdTemp() {
        return newmetermdTemp;
    }

    public void setNewmetermdTemp(float newmetermdTemp) {
        this.newmetermdTemp = newmetermdTemp;
    }

    float newmetermdTemp;

    public int getAunits() {
        return aunits;
    }

    public void setAunits(int aunits) {
        this.aunits = aunits;
    }

    int aunits=0;

    public String getDuplicatePrintDT() {
        return DuplicatePrintDT;
    }

    public void setDuplicatePrintDT(String duplicatePrintDT) {
        DuplicatePrintDT = duplicatePrintDT;
    }

    public String getQTPrintData() {
        return QTPrintData;
    }

    public void setQTPrintData(String QTPrintData) {
        this.QTPrintData = QTPrintData;
    }

    String DuplicatePrintDT;
    String QTPrintData;
    String ver_ser;
    String latitude,longitude;
    String sim_type,sim_no;
    int Bill_count;

    public String getVer_ser() {
        return ver_ser;
    }

    public void setVer_ser(String ver_ser) {
        this.ver_ser = ver_ser;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSim_type() {
        return sim_type;
    }

    public void setSim_type(String sim_type) {
        this.sim_type = sim_type;
    }

    public String getSim_no() {
        return sim_no;
    }

    public void setSim_no(String sim_no) {
        this.sim_no = sim_no;
    }

    public int getBill_count() {
        return Bill_count;
    }

    public void setBill_count(int bill_count) {
        Bill_count = bill_count;
    }

    public int getCurrent_record_value() {
        return current_record_value;
    }

    public void setCurrent_record_value(int current_record_value) {
        this.current_record_value = current_record_value;
    }

    public int getTotal_seq_count() {
        return total_seq_count;
    }

    public void setTotal_seq_count(int total_seq_count) {
        this.total_seq_count = total_seq_count;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getLoc1() {
        return loc1;
    }

    public void setLoc1(String loc1) {
        this.loc1 = loc1;
    }

    public String getSerno() {
        return serno;
    }

    public void setSerno(String serno) {
        this.serno = serno;
    }

    public String getService_number() {
        return service_number;
    }

    public void setService_number(String service_number) {
        this.service_number = service_number;
    }

    public String getMtrno() {
        return mtrno;
    }

    public void setMtrno(String mtrno) {
        this.mtrno = mtrno;
    }

    public String getUscNo() {
        return uscNo;
    }

    public void setUscNo(String uscNo) {
        this.uscNo = uscNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdd1() {
        return add1;
    }

    public void setAdd1(String add1) {
        this.add1 = add1;
    }

    public String getAdd2() {
        return add2;
    }

    public void setAdd2(String add2) {
        this.add2 = add2;
    }

    public String getAdd3() {
        return add3;
    }

    public void setAdd3(String add3) {
        this.add3 = add3;
    }

    public String getAcode() {
        return acode;
    }

    public void setAcode(String acode) {
        this.acode = acode;
    }

    public String getStructurecode() {
        return structurecode;
    }

    public void setStructurecode(String structurecode) {
        this.structurecode = structurecode;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getCat() {
        return cat;
    }

    public void setCat(int cat) {
        this.cat = cat;
    }

    public String getSubcat() {
        return subcat;
    }

    public void setSubcat(String subcat) {
        this.subcat = subcat;
    }

    public int getSub_cat() {
        return sub_cat;
    }

    public void setSub_cat(int sub_cat) {
        this.sub_cat = sub_cat;
    }

    public int getPh() {
        return ph;
    }

    public void setPh(int ph) {
        this.ph = ph;
    }

    public float getMf() {
        return mf;
    }

    public void setMf(float mf) {
        this.mf = mf;
    }

    public long getOmtrred() {
        return omtrred;
    }

    public void setOmtrred(long omtrred) {
        this.omtrred = omtrred;
    }

    public long getPreviousExportReading() {
        return previousExportReading;
    }

    public void setPreviousExportReading(long previousExportReading) {
        this.previousExportReading = previousExportReading;
    }

    public int getOmtrsts() {
        return omtrsts;
    }

    public void setOmtrsts(int omtrsts) {
        this.omtrsts = omtrsts;
    }

    public int getOmtrdd() {
        return omtrdd;
    }

    public void setOmtrdd(int omtrdd) {
        this.omtrdd = omtrdd;
    }

    public int getOmtrmm() {
        return omtrmm;
    }

    public void setOmtrmm(int omtrmm) {
        this.omtrmm = omtrmm;
    }

    public int getOmtryy() {
        return omtryy;
    }

    public void setOmtryy(int omtryy) {
        this.omtryy = omtryy;
    }

    public long getFrezdred() {
        return frezdred;
    }

    public void setFrezdred(long frezdred) {
        this.frezdred = frezdred;
    }

    public String getLpdt() {
        return lpdt;
    }

    public void setLpdt(String lpdt) {
        this.lpdt = lpdt;
    }

    public float getArrearsBefore() {
        return arrearsBefore;
    }

    public void setArrearsBefore(float arrearsBefore) {
        this.arrearsBefore = arrearsBefore;
    }

    public float getArrearsAfter() {
        return arrearsAfter;
    }

    public void setArrearsAfter(float arrearsAfter) {
        this.arrearsAfter = arrearsAfter;
    }

    public float getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(float additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public float getIntrestOnACD() {
        return intrestOnACD;
    }

    public void setIntrestOnACD(float intrestOnACD) {
        this.intrestOnACD = intrestOnACD;
    }

    public float getPedchg() {
        return pedchg;
    }

    public void setPedchg(float pedchg) {
        this.pedchg = pedchg;
    }

    public float getIntrestOnED() {
        return intrestOnED;
    }

    public void setIntrestOnED(float intrestOnED) {
        this.intrestOnED = intrestOnED;
    }

    public float getDiffintariff() {
        return Diffintariff;
    }

    public void setDiffintariff(float diffintariff) {
        Diffintariff = diffintariff;
    }

    public float getCustomcolumn1() {
        return customcolumn1;
    }

    public void setCustomcolumn1(float customcolumn1) {
        this.customcolumn1 = customcolumn1;
    }

    public float getCustomcolumn2() {
        return customcolumn2;
    }

    public void setCustomcolumn2(float customcolumn2) {
        this.customcolumn2 = customcolumn2;
    }

    public float getAcd() {
        return acd;
    }

    public void setAcd(float acd) {
        this.acd = acd;
    }

    public long getOldavg() {
        return oldavg;
    }

    public void setOldavg(long oldavg) {
        this.oldavg = oldavg;
    }

    public float getAvgpf() {
        return avgpf;
    }

    public void setAvgpf(float avgpf) {
        this.avgpf = avgpf;
    }

    public float getAvgmaxdemand() {
        return avgmaxdemand;
    }

    public void setAvgmaxdemand(float avgmaxdemand) {
        this.avgmaxdemand = avgmaxdemand;
    }

    public int getSflag() {
        return sflag;
    }

    public void setSflag(int sflag) {
        this.sflag = sflag;
    }

    public int getCapflag() {
        return capflag;
    }

    public void setCapflag(int capflag) {
        this.capflag = capflag;
    }

    public int getCat2HTFlag() {
        return cat2HTFlag;
    }

    public void setCat2HTFlag(int cat2HTFlag) {
        this.cat2HTFlag = cat2HTFlag;
    }

    public int getTriVectorFlag() {
        return triVectorFlag;
    }

    public void setTriVectorFlag(int triVectorFlag) {
        this.triVectorFlag = triVectorFlag;
    }

    public int getLvsideflag() {
        return lvsideflag;
    }

    public void setLvsideflag(int lvsideflag) {
        this.lvsideflag = lvsideflag;
    }

    public int getItsectorflag() {
        return itsectorflag;
    }

    public void setItsectorflag(int itsectorflag) {
        this.itsectorflag = itsectorflag;
    }

    public int getEdexflag() {
        return edexflag;
    }

    public void setEdexflag(int edexflag) {
        this.edexflag = edexflag;
    }

    public int getScstflag() {
        return scstflag;
    }

    public void setScstflag(int scstflag) {
        this.scstflag = scstflag;
    }

    public int getCheckdsnrflag() {
        return checkdsnrflag;
    }

    public void setCheckdsnrflag(int checkdsnrflag) {
        this.checkdsnrflag = checkdsnrflag;
    }

    public int getElcnonelecflag() {
        return elcnonelecflag;
    }

    public void setElcnonelecflag(int elcnonelecflag) {
        this.elcnonelecflag = elcnonelecflag;
    }

    public int getNetMeteringFlag() {
        return netMeteringFlag;
    }

    public void setNetMeteringFlag(int netMeteringFlag) {
        this.netMeteringFlag = netMeteringFlag;
    }

    public int getMeterChangeFlag() {
        return meterChangeFlag;
    }

    public void setMeterChangeFlag(int meterChangeFlag) {
        this.meterChangeFlag = meterChangeFlag;
    }

    public int getMC_UserChoice() {
        return MC_UserChoice;
    }

    public void setMC_UserChoice(int MC_UserChoice) {
        this.MC_UserChoice = MC_UserChoice;
    }

    public int getMeter_class() {
        return meter_class;
    }

    public void setMeter_class(int meter_class) {
        this.meter_class = meter_class;
    }

    public int getIrFlag() {
        return irFlag;
    }

    public void setIrFlag(int irFlag) {
        this.irFlag = irFlag;
    }

    public int getOccupancy_mode() {
        return occupancy_mode;
    }

    public void setOccupancy_mode(int occupancy_mode) {
        this.occupancy_mode = occupancy_mode;
    }

    public float getContractedLoad() {
        return contractedLoad;
    }

    public void setContractedLoad(float contractedLoad) {
        this.contractedLoad = contractedLoad;
    }

    public float getConnectedMD() {
        return connectedMD;
    }

    public void setConnectedMD(float connectedMD) {
        this.connectedMD = connectedMD;
    }

    public long getPreviousKvah() {
        return previousKvah;
    }

    public void setPreviousKvah(long previousKvah) {
        this.previousKvah = previousKvah;
    }

    public long getKwhfinalreading4() {
        return kwhfinalreading4;
    }

    public void setKwhfinalreading4(long kwhfinalreading4) {
        this.kwhfinalreading4 = kwhfinalreading4;
    }

    public long getKvahfinalreading4() {
        return kvahfinalreading4;
    }

    public void setKvahfinalreading4(long kvahfinalreading4) {
        this.kvahfinalreading4 = kvahfinalreading4;
    }

    public int getPts() {
        return pts;
    }

    public void setPts(int pts) {
        this.pts = pts;
    }

    public long getCalbuffunits() {
        return calbuffunits;
    }

    public void setCalbuffunits(long calbuffunits) {
        this.calbuffunits = calbuffunits;
    }

    public long getClubbingid() {
        return clubbingid;
    }

    public void setClubbingid(long clubbingid) {
        this.clubbingid = clubbingid;
    }

    public int getClubmainserviceflag() {
        return clubmainserviceflag;
    }

    public void setClubmainserviceflag(int clubmainserviceflag) {
        this.clubmainserviceflag = clubmainserviceflag;
    }

    public int getPoleno() {
        return poleno;
    }

    public void setPoleno(int poleno) {
        this.poleno = poleno;
    }

    public String getAgserno2() {
        return agserno2;
    }

    public void setAgserno2(String agserno2) {
        this.agserno2 = agserno2;
    }

    public String getAgserno1() {
        return agserno1;
    }

    public void setAgserno1(String agserno1) {
        this.agserno1 = agserno1;
    }

    public String getAgserno3() {
        return agserno3;
    }

    public void setAgserno3(String agserno3) {
        this.agserno3 = agserno3;
    }

    public float getAgarrears1() {
        return agarrears1;
    }

    public void setAgarrears1(float agarrears1) {
        this.agarrears1 = agarrears1;
    }

    public float getAgarrears2() {
        return agarrears2;
    }

    public void setAgarrears2(float agarrears2) {
        this.agarrears2 = agarrears2;
    }

    public float getAgarrears3() {
        return agarrears3;
    }

    public void setAgarrears3(float agarrears3) {
        this.agarrears3 = agarrears3;
    }

    public long getCases_amt() {
        return cases_amt;
    }

    public void setCases_amt(long cases_amt) {
        this.cases_amt = cases_amt;
    }

    public long getDc_caseamt() {
        return Dc_caseamt;
    }

    public void setDc_caseamt(long dc_caseamt) {
        Dc_caseamt = dc_caseamt;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSwversion() {
        return swversion;
    }

    public void setSwversion(String swversion) {
        this.swversion = swversion;
    }

    public String getEro() {
        return ero;
    }

    public void setEro(String ero) {
        this.ero = ero;
    }

    public String getErocode() {
        return erocode;
    }

    public void setErocode(String erocode) {
        this.erocode = erocode;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }


    public String getIntonsd() {
        return datasd;
    }
    public void setIntonsd(String datasd) {
        this.datasd = datasd;
    }


    public int getEro_code() {
        return ero_code;
    }

    public void setEro_code(int ero_code) {
        this.ero_code = ero_code;
    }

    public String getEroName() {
        return eroName;
    }

    public void setEroName(String eroName) {
        this.eroName = eroName;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public String getAaoCellNo() {
        return aaoCellNo;
    }

    public void setAaoCellNo(String aaoCellNo) {
        this.aaoCellNo = aaoCellNo;
    }

    public String getAdeCellNo() {
        return adeCellNo;
    }

    public void setAdeCellNo(String adeCellNo) {
        this.adeCellNo = adeCellNo;
    }

    public int getPmtrdd() {
        return pmtrdd;
    }

    public void setPmtrdd(int pmtrdd) {
        this.pmtrdd = pmtrdd;
    }

    public int getPmtrmm() {
        return pmtrmm;
    }

    public void setPmtrmm(int pmtrmm) {
        this.pmtrmm = pmtrmm;
    }

    public int getPmtryy() {
        return pmtryy;
    }

    public void setPmtryy(int pmtryy) {
        this.pmtryy = pmtryy;
    }

    public int getPmtrhh() {
        return pmtrhh;
    }

    public void setPmtrhh(int pmtrhh) {
        this.pmtrhh = pmtrhh;
    }

    public int getPmtrmin() {
        return pmtrmin;
    }

    public void setPmtrmin(int pmtrmin) {
        this.pmtrmin = pmtrmin;
    }

    public int getDisdd() {
        return disdd;
    }

    public void setDisdd(int disdd) {
        this.disdd = disdd;
    }

    public int getDismm() {
        return dismm;
    }

    public void setDismm(int dismm) {
        this.dismm = dismm;
    }

    public int getDisyy() {
        return disyy;
    }

    public void setDisyy(int disyy) {
        this.disyy = disyy;
    }

    public int getDuedd() {
        return duedd;
    }

    public void setDuedd(int duedd) {
        this.duedd = duedd;
    }

    public int getDuemm() {
        return duemm;
    }

    public void setDuemm(int duemm) {
        this.duemm = duemm;
    }

    public int getDueyy() {
        return dueyy;
    }

    public void setDueyy(int dueyy) {
        this.dueyy = dueyy;
    }

    public long getBno() {
        return bno;
    }

    public void setBno(long bno) {
        this.bno = bno;
    }

    public long getPmtrred() {
        return pmtrred;
    }

    public void setPmtrred(long pmtrred) {
        this.pmtrred = pmtrred;
    }

    public long getPresentExportReading() {
        return presentExportReading;
    }

    public void setPresentExportReading(long presentExportReading) {
        this.presentExportReading = presentExportReading;
    }

    public long getTemp_SolarReadingKwh() {
        return temp_SolarReadingKwh;
    }

    public void setTemp_SolarReadingKwh(long temp_SolarReadingKwh) {
        this.temp_SolarReadingKwh = temp_SolarReadingKwh;
    }

    public int getPmtrsts() {
        return pmtrsts;
    }

    public void setPmtrsts(int pmtrsts) {
        this.pmtrsts = pmtrsts;
    }

    public float getUnits() {
        return units;
    }

    public void setUnits(float units) {
        this.units = units;
    }

    public float getSubsidyunits() {
        return subsidyunits;
    }

    public void setSubsidyunits(float subsidyunits) {
        this.subsidyunits = subsidyunits;
    }

    public float getConsumerpayunits() {
        return consumerpayunits;
    }

    public void setConsumerpayunits(float consumerpayunits) {
        this.consumerpayunits = consumerpayunits;
    }

    public float getRecorded_units() {
        return recorded_units;
    }

    public void setRecorded_units(float recorded_units) {
        this.recorded_units = recorded_units;
    }

    public long getPresentKwh() {
        return presentKwh;
    }

    public void setPresentKwh(long presentKwh) {
        this.presentKwh = presentKwh;
    }

    public long getPreviousKwh() {
        return previousKwh;
    }

    public void setPreviousKwh(long previousKwh) {
        this.previousKwh = previousKwh;
    }

    public float getAdjusted_cc() {
        return adjusted_cc;
    }

    public void setAdjusted_cc(float adjusted_cc) {
        this.adjusted_cc = adjusted_cc;
    }

    public float getAdjusted_ed() {
        return adjusted_ed;
    }

    public void setAdjusted_ed(float adjusted_ed) {
        this.adjusted_ed = adjusted_ed;
    }

    public float getEngchg() {
        return engchg;
    }

    public void setEngchg(float engchg) {
        this.engchg = engchg;
    }

    public float getCuschg() {
        return cuschg;
    }

    public void setCuschg(float cuschg) {
        this.cuschg = cuschg;
    }

    public float getEdchg() {
        return edchg;
    }

    public void setEdchg(float edchg) {
        this.edchg = edchg;
    }

    public float getFixchg() {
        return fixchg;
    }

    public void setFixchg(float fixchg) {
        this.fixchg = fixchg;
    }

    public float getCapchg() {
        return capchg;
    }

    public void setCapchg(float capchg) {
        this.capchg = capchg;
    }

    public float getPamount() {
        return pamount;
    }

    public void setPamount(float pamount) {
        this.pamount = pamount;
    }

    public float getRebate() {
        return rebate;
    }

    public void setRebate(float rebate) {
        this.rebate = rebate;
    }

    public float getLLDSurchage() {
        return LLDSurchage;
    }

    public void setLLDSurchage(float LLDSurchage) {
        this.LLDSurchage = LLDSurchage;
    }

    public float getLoss_gain() {
        return loss_gain;
    }

    public void setLoss_gain(float loss_gain) {
        this.loss_gain = loss_gain;
    }

    public float getLLDUnits() {
        return LLDUnits;
    }

    public void setLLDUnits(float LLDUnits) {
        this.LLDUnits = LLDUnits;
    }

    public long getPavg() {
        return pavg;
    }

    public void setPavg(long pavg) {
        this.pavg = pavg;
    }

    public String getNewMeterNumber() {
        return newMeterNumber;
    }

    public void setNewMeterNumber(String newMeterNumber) {
        this.newMeterNumber = newMeterNumber;
    }

    public float getNewmf() {
        return newmf;
    }

    public void setNewmf(float newmf) {
        this.newmf = newmf;
    }

    public float getNewmetermd() {
        return newmetermd;
    }

    public void setNewmetermd(float newmetermd) {
        this.newmetermd = newmetermd;
    }

    public char getNewmeteraccuracy() {
        return newmeteraccuracy;
    }

    public void setNewmeteraccuracy(char newmeteraccuracy) {
        this.newmeteraccuracy = newmeteraccuracy;
    }

    public float getOldmetermd() {
        return oldmetermd;
    }

    public void setOldmetermd(float oldmetermd) {
        this.oldmetermd = oldmetermd;
    }

    public float getOldmeterpf() {
        return oldmeterpf;
    }

    public void setOldmeterpf(float oldmeterpf) {
        this.oldmeterpf = oldmeterpf;
    }

    public long getPresentKvah() {
        return presentKvah;
    }

    public void setPresentKvah(long presentKvah) {
        this.presentKvah = presentKvah;
    }

    public int getKvahMeterStatus() {
        return kvahMeterStatus;
    }

    public void setKvahMeterStatus(int kvahMeterStatus) {
        this.kvahMeterStatus = kvahMeterStatus;
    }

    public float getAvgkvah() {
        return avgkvah;
    }

    public void setAvgkvah(float avgkvah) {
        this.avgkvah = avgkvah;
    }

    public float getCat3BilledUnits() {
        return cat3BilledUnits;
    }

    public void setCat3BilledUnits(float cat3BilledUnits) {
        this.cat3BilledUnits = cat3BilledUnits;
    }

    public long getRecmendedunits() {
        return recmendedunits;
    }

    public void setRecmendedunits(long recmendedunits) {
        this.recmendedunits = recmendedunits;
    }

    public float getPf() {
        return pf;
    }

    public void setPf(float pf) {
        this.pf = pf;
    }

    public float getRecpf() {
        return recpf;
    }

    public void setRecpf(float recpf) {
        this.recpf = recpf;
    }

    public float getRecommendedMD() {
        return recommendedMD;
    }

    public void setRecommendedMD(float recommendedMD) {
        this.recommendedMD = recommendedMD;
    }

    public float getRecordedMD() {
        return recordedMD;
    }

    public void setRecordedMD(float recordedMD) {
        this.recordedMD = recordedMD;
    }

    public float getBilledRecordedMD() {
        return billedRecordedMD;
    }

    public void setBilledRecordedMD(float billedRecordedMD) {
        this.billedRecordedMD = billedRecordedMD;
    }

    public float getBilledDemand() {
        return billedDemand;
    }

    public void setBilledDemand(float billedDemand) {
        this.billedDemand = billedDemand;
    }

    public float getVoltage_R_VR() {
        return voltage_R_VR;
    }

    public void setVoltage_R_VR(float voltage_R_VR) {
        this.voltage_R_VR = voltage_R_VR;
    }

    public float getVoltage_Y_VY() {
        return voltage_Y_VY;
    }

    public void setVoltage_Y_VY(float voltage_Y_VY) {
        this.voltage_Y_VY = voltage_Y_VY;
    }

    public float getVoltage_B_VB() {
        return voltage_B_VB;
    }

    public void setVoltage_B_VB(float voltage_B_VB) {
        this.voltage_B_VB = voltage_B_VB;
    }

    public float getCurrent_R_IR() {
        return current_R_IR;
    }

    public void setCurrent_R_IR(float current_R_IR) {
        this.current_R_IR = current_R_IR;
    }

    public float getCurrent_Y_IY() {
        return current_Y_IY;
    }

    public void setCurrent_Y_IY(float current_Y_IY) {
        this.current_Y_IY = current_Y_IY;
    }

    public float getCurrent_B_IB() {
        return current_B_IB;
    }

    public void setCurrent_B_IB(float current_B_IB) {
        this.current_B_IB = current_B_IB;
    }

    public char getMeter_reading_mode() {
        return meter_reading_mode;
    }

    public void setMeter_reading_mode(char meter_reading_mode) {
        this.meter_reading_mode = meter_reading_mode;
    }

    public char getMtr_type() {
        return Mtr_type;
    }

    public void setMtr_type(char mtr_type) {
        Mtr_type = mtr_type;
    }

    public char getMeterPosition() {
        return meterPosition;
    }

    public void setMeterPosition(char meterPosition) {
        this.meterPosition = meterPosition;
    }

    public float getBilledKwhUnits() {
        return billedKwhUnits;
    }

    public void setBilledKwhUnits(float billedKwhUnits) {
        this.billedKwhUnits = billedKwhUnits;
    }

    public long getExportUnits() {
        return exportUnits;
    }

    public void setExportUnits(long exportUnits) {
        this.exportUnits = exportUnits;
    }

    public float getImportUnits() {
        return importUnits;
    }

    public void setImportUnits(float importUnits) {
        this.importUnits = importUnits;
    }

    public String getAdharcardid() {
        return adharcardid;
    }

    public void setAdharcardid(String adharcardid) {
        this.adharcardid = adharcardid;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getMtrmk() {
        return mtrmk;
    }

    public void setMtrmk(String mtrmk) {
        this.mtrmk = mtrmk;
    }

    public String getSbmunitnumber() {
        return sbmunitnumber;
    }

    public void setSbmunitnumber(String sbmunitnumber) {
        this.sbmunitnumber = sbmunitnumber;
    }

    public String getSbmunitmemory() {
        return sbmunitmemory;
    }

    public void setSbmunitmemory(String sbmunitmemory) {
        this.sbmunitmemory = sbmunitmemory;
    }

    public char getCompanyflag() {
        return companyflag;
    }

    public void setCompanyflag(char companyflag) {
        this.companyflag = companyflag;
    }

    public String getNew_ser_no() {
        return new_ser_no;
    }

    public void setNew_ser_no(String new_ser_no) {
        this.new_ser_no = new_ser_no;
    }

    public char getMinimumBilledFlag() {
        return MinimumBilledFlag;
    }

    public void setMinimumBilledFlag(char minimumBilledFlag) {
        MinimumBilledFlag = minimumBilledFlag;
    }

    public char getTcSealFlag() {
        return tcSealFlag;
    }

    public void setTcSealFlag(char tcSealFlag) {
        this.tcSealFlag = tcSealFlag;
    }

    public float getOldReadingKwh() {
        return oldReadingKwh;
    }

    public void setOldReadingKwh(float oldReadingKwh) {
        this.oldReadingKwh = oldReadingKwh;
    }

    public float getOldReadingKvah() {
        return oldReadingKvah;
    }

    public void setOldReadingKvah(float oldReadingKvah) {
        this.oldReadingKvah = oldReadingKvah;
    }

    public float getNewReadingKwh() {
        return NewReadingKwh;
    }

    public void setNewReadingKwh(float newReadingKwh) {
        NewReadingKwh = newReadingKwh;
    }

    public float getNewReadingKvah() {
        return NewReadingKvah;
    }

    public void setNewReadingKvah(float newReadingKvah) {
        NewReadingKvah = newReadingKvah;
    }

    public float getTemp_readingKwh() {
        return temp_readingKwh;
    }

    public void setTemp_readingKwh(float temp_readingKwh) {
        this.temp_readingKwh = temp_readingKwh;
    }

    public float getTemp_readingKvah() {
        return temp_readingKvah;
    }

    public void setTemp_readingKvah(float temp_readingKvah) {
        this.temp_readingKvah = temp_readingKvah;
    }

    public long getPresent_average_units() {
        return present_average_units;
    }

    public void setPresent_average_units(long present_average_units) {
        this.present_average_units = present_average_units;
    }

    public float getMinimumUnits() {
        return minimumUnits;
    }

    public void setMinimumUnits(float minimumUnits) {
        this.minimumUnits = minimumUnits;
    }

    public float getRecordedEnergyCharges() {
        return recordedEnergyCharges;
    }

    public void setRecordedEnergyCharges(float recordedEnergyCharges) {
        this.recordedEnergyCharges = recordedEnergyCharges;
    }

    public int getBill_flag() {
        return bill_flag;
    }

    public void setBill_flag(int bill_flag) {
        this.bill_flag = bill_flag;
    }

    public float getTotalAgricultureArrears() {
        return totalAgricultureArrears;
    }

    public void setTotalAgricultureArrears(float totalAgricultureArrears) {
        this.totalAgricultureArrears = totalAgricultureArrears;
    }

    public char getMalflag() {
        return malflag;
    }

    public void setMalflag(char malflag) {
        this.malflag = malflag;
    }

    public float getMTRaccuracy() {
        return MTRaccuracy;
    }

    public void setMTRaccuracy(float MTRaccuracy) {
        this.MTRaccuracy = MTRaccuracy;
    }

    public float getLtBillTotal() {
        return ltBillTotal;
    }

    public void setLtBillTotal(float ltBillTotal) {
        this.ltBillTotal = ltBillTotal;
    }

    public float getHtBillTotal() {
        return htBillTotal;
    }

    public void setHtBillTotal(float htBillTotal) {
        this.htBillTotal = htBillTotal;
    }

    public float getHT_engchg() {
        return HT_engchg;
    }

    public void setHT_engchg(float HT_engchg) {
        this.HT_engchg = HT_engchg;
    }

    public float getHT_cuschg() {
        return HT_cuschg;
    }

    public void setHT_cuschg(float HT_cuschg) {
        this.HT_cuschg = HT_cuschg;
    }

    public float getHT_fixchg() {
        return HT_fixchg;
    }

    public void setHT_fixchg(float HT_fixchg) {
        this.HT_fixchg = HT_fixchg;
    }

    public char getHt_billed_flag() {
        return ht_billed_flag;
    }

    public void setHt_billed_flag(char ht_billed_flag) {
        this.ht_billed_flag = ht_billed_flag;
    }

    public char getBILL_HT() {
        return BILL_HT;
    }

    public void setBILL_HT(char BILL_HT) {
        this.BILL_HT = BILL_HT;
    }

    public float getDemandCharges() {
        return demandCharges;
    }

    public void setDemandCharges(float demandCharges) {
        this.demandCharges = demandCharges;
    }

    public float getTemp_float() {
        return temp_float;
    }

    public void setTemp_float(float temp_float) {
        this.temp_float = temp_float;
    }

    public long getTemp_long() {
        return temp_long;
    }

    public void setTemp_long(long temp_long) {
        this.temp_long = temp_long;
    }

    public long getCarryForwardUnits() {
        return CarryForwardUnits;
    }

    public void setCarryForwardUnits(long carryForwardUnits) {
        CarryForwardUnits = carryForwardUnits;
    }

    public long getExportKVAH() {
        return ExportKVAH;
    }

    public void setExportKVAH(long exportKVAH) {
        ExportKVAH = exportKVAH;
    }

    public long getFinalSolarKVAH() {
        return FinalSolarKVAH;
    }

    public void setFinalSolarKVAH(long finalSolarKVAH) {
        FinalSolarKVAH = finalSolarKVAH;
    }

    public long getFinalSolarKWH() {
        return FinalSolarKWH;
    }

    public void setFinalSolarKWH(long finalSolarKWH) {
        FinalSolarKWH = finalSolarKWH;
    }

    public long getDisconnectionSolarKWH() {
        return DisconnectionSolarKWH;
    }

    public void setDisconnectionSolarKWH(long disconnectionSolarKWH) {
        DisconnectionSolarKWH = disconnectionSolarKWH;
    }

    public long getFinalCarryForwardExportUnits() {
        return FinalCarryForwardExportUnits;
    }

    public void setFinalCarryForwardExportUnits(long finalCarryForwardExportUnits) {
        FinalCarryForwardExportUnits = finalCarryForwardExportUnits;
    }

    public long getKwhfinalSolarreading4() {
        return kwhfinalSolarreading4;
    }

    public void setKwhfinalSolarreading4(long kwhfinalSolarreading4) {
        this.kwhfinalSolarreading4 = kwhfinalSolarreading4;
    }

    public float getPreviousSolarReadingKvah() {
        return previousSolarReadingKvah;
    }

    public void setPreviousSolarReadingKvah(float previousSolarReadingKvah) {
        this.previousSolarReadingKvah = previousSolarReadingKvah;
    }

    public float getBillAmount() {
        return BillAmount;
    }

    public void setBillAmount(float billAmount) {
        BillAmount = billAmount;
    }

    public float getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        TotalAmount = totalAmount;
    }

    public float getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(float netAmount) {
        NetAmount = netAmount;
    }

    public float getTotalDue() {
        return TotalDue;
    }

    public void setTotalDue(float totalDue) {
        TotalDue = totalDue;
    }

    public float getAmountAfterRounding() {
        return AmountAfterRounding;
    }

    public void setAmountAfterRounding(float amountAfterRounding) {
        AmountAfterRounding = amountAfterRounding;
    }

    public float getSubsidybillamnt() {
        return Subsidybillamnt;
    }

    public void setSubsidybillamnt(float subsidybillamnt) {
        Subsidybillamnt = subsidybillamnt;
    }

    public float getConsumerpayable() {
        return consumerpayable;
    }

    public void setConsumerpayable(float consumerpayable) {
        this.consumerpayable = consumerpayable;
    }

    public float getSubsidybillechg() {
        return Subsidybillechg;
    }

    public void setSubsidybillechg(float subsidybillechg) {
        Subsidybillechg = subsidybillechg;
    }

    public float getConsumerechg() {
        return consumerechg;
    }

    public void setConsumerechg(float consumerechg) {
        this.consumerechg = consumerechg;
    }

    public char getIsMultipleBilledFlag() {
        return isMultipleBilledFlag;
    }

    public void setIsMultipleBilledFlag(char isMultipleBilledFlag) {
        this.isMultipleBilledFlag = isMultipleBilledFlag;
    }

    int current_record_value;
    int total_seq_count;
    String loc;
    String loc1;
    String serno;
    String service_number;
    String mtrno;
    String uscNo;
    String name;
    String add1;
    String add2;
    String add3;
    String acode;
    String structurecode;
    String group;
    int cat;
    String subcat;
    int sub_cat;
    int ph;
    float mf;
    long omtrred;
    long previousExportReading;
    int omtrsts;
    int omtrdd;
    int omtrmm;
    int omtryy;
    long frezdred;
    String lpdt;
    float arrearsBefore;
    float arrearsAfter;
    float additionalCharges;
    float intrestOnACD;
    float pedchg;
    float intrestOnED;
    float Diffintariff;
    float customcolumn1;
    float customcolumn2;
    float acd;
    long oldavg;
    float avgpf;
    float avgmaxdemand;
    int sflag;
    int capflag;
    int cat2HTFlag;
    int triVectorFlag;
    int lvsideflag;
    int itsectorflag;
    int edexflag;
    int scstflag;
    int checkdsnrflag;
    int elcnonelecflag;
    int netMeteringFlag;
    int meterChangeFlag;
    int MC_UserChoice;
    int meter_class;
    int irFlag;
    int occupancy_mode;
    float contractedLoad;
    float connectedMD;
    long previousKvah;
    long kwhfinalreading4;
    long kvahfinalreading4;
    int pts;
    long calbuffunits;
    long clubbingid;
    int clubmainserviceflag;
    int poleno;
    String agserno1;
    String agserno2;
    String agserno3;
    float agarrears1;
    float agarrears2;
    float agarrears3;
    long cases_amt;
    long Dc_caseamt;
    String version;
    String swversion;
    //INPUT STRUCTURE

    // ERO STRUCTURE
    String ero;
    String erocode;
    int ero_code;
    String eroName;
    String secName;
    String aaoCellNo;
    String adeCellNo;
    // ERO STRUCTURE

    //OUTPUT STRUCTURE
    int pmtrdd,pmtrmm,pmtryy;
    int pmtrhh,pmtrmin;
    int disdd,dismm;
    int disyy;
    int duedd,duemm,dueyy;
    long bno;
    long pmtrred;
    long presentExportReading;
    long temp_SolarReadingKwh;
    int  pmtrsts;
    //unsigned long units;
    float units,subsidyunits,consumerpayunits;
    float recorded_units;
    long presentKwh,previousKwh;
    float adjusted_cc;
    float adjusted_ed;
    float engchg;
    float cuschg;
    float edchg;
    float fixchg;
    float capchg;
    float pamount;
    float rebate;
    float LLDSurchage;
    float loss_gain;
    float LLDUnits;
    long pavg;
    String newMeterNumber;
    float newmf;
    float newmetermd;
    char newmeteraccuracy;
    float oldmetermd;
    float oldmeterpf;
    long presentKvah;
    int kvahMeterStatus;
    float avgkvah;
    float cat3BilledUnits;
    long recmendedunits;
    float pf;
    float recpf;
    float recommendedMD;
    float recordedMD;
    float billedRecordedMD;
    float billedDemand;
    float voltage_R_VR;
    float voltage_Y_VY;
    float voltage_B_VB;
    float current_R_IR;
    float current_Y_IY;
    float current_B_IB;
    char meter_reading_mode;
    char Mtr_type;
    char meterPosition;
    float billedKwhUnits;
    long exportUnits;
    float importUnits;
    String adharcardid;
    String phoneno;
    String mtrmk;
    String sbmunitnumber;
    String sbmunitmemory;
    char companyflag;
    String new_ser_no;
    //OUTPUT STRUCTURE

    //OTHER TEMPORARY STRUCT VARIABLES
    char MinimumBilledFlag,tcSealFlag;
    // STATUS 4  VARIABLES
    float oldReadingKwh,oldReadingKvah;
    float NewReadingKwh,NewReadingKvah;
    float temp_readingKwh,temp_readingKvah;
    // STATUS 4  VARIABLES
    long present_average_units;
    float minimumUnits,recordedEnergyCharges;
    int bill_flag;
    float totalAgricultureArrears;
    char malflag;
    float MTRaccuracy;
    float ltBillTotal;
    float htBillTotal;
    float HT_engchg;
    float HT_cuschg;
    float HT_fixchg;
    char ht_billed_flag;
    char BILL_HT;
    float demandCharges;
    float temp_float;
    long temp_long;
    long CarryForwardUnits;
    long ExportKVAH;
    long FinalSolarKVAH;
    long FinalSolarKWH;
    long DisconnectionSolarKWH;
    long FinalCarryForwardExportUnits;
    long kwhfinalSolarreading4;
    float previousSolarReadingKvah;
    float BillAmount,TotalAmount,NetAmount,TotalDue,AmountAfterRounding,Subsidybillamnt,consumerpayable,Subsidybillechg,consumerechg;
    char isMultipleBilledFlag;
}
