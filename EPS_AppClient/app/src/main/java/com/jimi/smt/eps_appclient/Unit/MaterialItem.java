package com.jimi.smt.eps_appclient.Unit;

/**
 * 类名:MaterialItem
 * 创建人:Connie
 * 创建时间:2017-10-13
 * 描述:防错结果
 * 版本号:V1.0
 * 修改记录:
 */
public class MaterialItem {
    String FileId;          //当前站位表文件ID
    String OrgLineSeat;    //当前站位表站位
    String OrgMaterial;    //当前站位表料号
    String ScanLineSeat;   //扫描的站位
    String ScanMaterial;   //扫描的料号
    String Result;         //结果
    String Remark;         //失败原因

    public MaterialItem() {
    }

    public MaterialItem(String fileId,String orgLineSeat, String orgMaterial, String scanLineSeat, String scanMaterial, String result, String remark) {
        FileId=fileId;
        OrgLineSeat = orgLineSeat;
        OrgMaterial = orgMaterial;
        ScanLineSeat = scanLineSeat;
        ScanMaterial = scanMaterial;
        Result = result;
        Remark = remark;
    }

    public String getFileId() {
        return FileId;
    }

    public void setFileId(String fileId) {
        FileId = fileId;
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

    public void setResultRemark(String result,String remark){
        Result=result;
        Remark=remark;
    }
}
