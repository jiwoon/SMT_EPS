package com.jimi.smt.eps_appclient.Unit;

import java.sql.Timestamp;

/**
 * 类名:OperLogItem
 * 创建人:Liang GuoChang
 * 创建时间:2017/9/25 18:46
 * 描述:
 * 版本号:
 * 修改记录:
 */
public class OperLogItem {

    private String Operator;
    private Timestamp Time;
    private int Type;
    private String Result;
    private String Lineseat;
    private String Material_no;
    private String Old_material_no;
    private String ScanLineseat;
    private String Remark;
    private String FileId;
    private String line;
    private String work_order;
    private int board_type;

    public OperLogItem() {
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public Timestamp getTime() {
        return Time;
    }

    public void setTime(Timestamp time) {
        Time = time;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getLineseat() {
        return Lineseat;
    }

    public void setLineseat(String lineseat) {
        Lineseat = lineseat;
    }

    public String getMaterial_no() {
        return Material_no;
    }

    public void setMaterial_no(String material_no) {
        Material_no = material_no;
    }

    public String getOld_material_no() {
        return Old_material_no;
    }

    public void setOld_material_no(String old_material_no) {
        Old_material_no = old_material_no;
    }

    public String getScanLineseat() {
        return ScanLineseat;
    }

    public void setScanLineseat(String scanLineseat) {
        ScanLineseat = scanLineseat;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getFileId() {
        return FileId;
    }

    public void setFileId(String fileId) {
        FileId = fileId;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getWork_order() {
        return work_order;
    }

    public void setWork_order(String work_order) {
        this.work_order = work_order;
    }

    public int getBoard_type() {
        return board_type;
    }

    public void setBoard_type(int board_type) {
        this.board_type = board_type;
    }
}
