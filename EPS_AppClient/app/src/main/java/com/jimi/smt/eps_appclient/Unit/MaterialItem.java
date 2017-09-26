package com.jimi.smt.eps_appclient.Unit;

/**
 * Created by think on 2017/9/20.
 */
public class MaterialItem {
    String OrgLineSeat;
    String OrgMaterial;
    String ScanLineSeat;
    String ScanMaterial;
    String Result;
    String Remark;

    public MaterialItem() {
    }

    public MaterialItem(String orgLineSeat, String orgMaterial, String scanLineSeat, String scanMaterial, String result, String remark) {
        OrgLineSeat = orgLineSeat;
        OrgMaterial = orgMaterial;
        ScanLineSeat = scanLineSeat;
        ScanMaterial = scanMaterial;
        Result = result;
        Remark = remark;
    }

    public String getOrgLineSeat() {
        return OrgLineSeat;
    }

    public String getOrgMaterial() {
        return OrgMaterial;
    }

    public String getScanLineSeat() {
        return ScanLineSeat;
    }

    public String getScanMaterial() {
        return ScanMaterial;
    }

    public String getResult() {
        return Result;
    }

    public String getRemark() {
        return Remark;
    }

    public void setOrgLineSeat(String orgLineSeat) {
        OrgLineSeat = orgLineSeat;
    }

    public void setOrgMaterial(String orgMaterial) {
        OrgMaterial = orgMaterial;
    }

    public void setScanLineSeat(String scanLineSeat) {
        ScanLineSeat = scanLineSeat;
    }

    public void setScanMaterial(String scanMaterial) {
        ScanMaterial = scanMaterial;
    }

    public void setResult(String result) {
        Result = result;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
