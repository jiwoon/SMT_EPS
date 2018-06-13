package com.jimi.smt.eps_appclient.Unit;

import android.support.annotation.NonNull;

/**
 * 类名:MaterialItem
 * 创建人:Connie
 * 创建时间:2017-10-13
 * 描述:防错结果
 * 版本号:V1.0
 * 修改记录:
 */
public class MaterialItem implements Comparable<MaterialItem> {
    private String Order;           //工单
    private int BoardType;       //版面类型
    private String Line;            //线号
    private int SerialNo;           //流水号
    private Byte Alternative;    //是否属于替料
    private String OrgLineSeat;    //当前站位表站位
    private String OrgMaterial;    //当前站位表料号

    private String ScanLineSeat;   //扫描的站位
    private String ScanMaterial;   //扫描的料号
    private String Result;         //结果
    private String Remark;         //失败原因

    public MaterialItem() {
    }

    public MaterialItem(String order, int boardType, String line, int serialNo, Byte alternative, String orgLineSeat,
                        String orgMaterial, String scanLineSeat, String scanMaterial, String result, String remark) {
        Order = order;
        BoardType = boardType;
        Line = line;
        SerialNo = serialNo;
        Alternative = alternative;
        OrgLineSeat = orgLineSeat;
        OrgMaterial = orgMaterial;
        ScanLineSeat = scanLineSeat;
        ScanMaterial = scanMaterial;
        Result = result;
        Remark = remark;
    }

    public String getOrder() {
        return Order;
    }

    public void setOrder(String order) {
        Order = order;
    }

    public int getBoardType() {
        return BoardType;
    }

    public void setBoardType(int boardType) {
        BoardType = boardType;
    }

    public String getLine() {
        return Line;
    }

    public void setLine(String line) {
        Line = line;
    }

    public int getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(int serialNo) {
        SerialNo = serialNo;
    }

    public Byte getAlternative() {
        return Alternative;
    }

    public void setAlternative(Byte alternative) {
        Alternative = alternative;
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

    public void setResultRemark(String result, String remark) {
        Result = result;
        Remark = remark;
    }

    @Override
    public boolean equals(Object obj) {
        MaterialItem materialItem = ((MaterialItem) obj);
        int oldHasCode = materialItem.hashCode();
        int newHasCode = hashCode();
        return (oldHasCode == newHasCode);
    }

    @Override
    public int hashCode() {
        return (Order + Line + BoardType + OrgLineSeat + OrgMaterial + SerialNo + Alternative).hashCode();
    }

    public String getMaterialStr() {
        return "line - " + getLine() + " - "
                + "order - " + getOrder() + " - "
                + "boardType - " + getBoardType() + " - "
                + "serialNo - " + getSerialNo() + " - "
                + "lineSeat - " + getOrgLineSeat() + " - "
                + "material - " + getOrgMaterial() + " - "
                + "scan lineSeat - " + getScanLineSeat() + " - "
                + "scan material - " + getScanMaterial() + " - "
                + "result - " + getResult() + " - "
                + "remark - " + getRemark() + " - "
                + "alternative - " + getAlternative();
    }

    @Override
    public int compareTo(@NonNull MaterialItem materialItem) {
        return 1;
    }
}
