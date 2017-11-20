package com.jimi.smt.eps_appclient.Unit;

import java.sql.Timestamp;

/**
 * 类名:ProgramItemVisit
 * 创建人:Liang GuoChang
 * 创建时间:2017/11/11 15:20
 * 描述:表program_item_visit对象,用于pc端实时显示日志
 * 版本号:v1.0
 * 修改记录:
 */

public class ProgramItemVisit {
    //对应的工单主键
    private String program_id;
    //原始操作站位
    private String lineseat;
    //原始操作料号
    private String material_no;
    //扫描的站位
    private String scan_lineseat;
    //扫描的料号
    private String scan_material_no;
    //发料结果
    private byte store_issue_result;
    //发料时间
    private Timestamp store_issue_time;
    //上料结果
    private byte feed_result;
    //上料时间
    private Timestamp feed_time;
    //换料结果
    private byte change_result;
    //换料时间
    private Timestamp change_time;
    //检料结果
    private byte check_result;
    //检料时间
    private Timestamp check_time;
    //首次全检结果
    private byte first_check_all_result;
    //首次全检时间
    private Timestamp first_check_all_time;
    //全检结果
    private byte check_all_result;
    //全检时间
    private Timestamp check_all_time;
    //最后一次操作类型
    private int last_operation_type;
    //最后一次操作时间
    private Timestamp last_operation_time;

    public String getProgram_id() {
        return program_id;
    }

    public void setProgram_id(String program_id) {
        this.program_id = program_id;
    }

    public String getLineseat() {
        return lineseat;
    }

    public void setLineseat(String lineseat) {
        this.lineseat = lineseat;
    }

    public String getMaterial_no() {
        return material_no;
    }

    public void setMaterial_no(String material_no) {
        this.material_no = material_no;
    }

    public String getScan_lineseat() {
        return scan_lineseat;
    }

    public void setScan_lineseat(String scan_lineseat) {
        this.scan_lineseat = scan_lineseat;
    }

    public String getScan_material_no() {
        return scan_material_no;
    }

    public void setScan_material_no(String scan_material_no) {
        this.scan_material_no = scan_material_no;
    }

    public byte getStore_issue_result() {
        return store_issue_result;
    }

    public void setStore_issue_result(byte store_issue_result) {
        this.store_issue_result = store_issue_result;
    }

    public Timestamp getStore_issue_time() {
        return store_issue_time;
    }

    public void setStore_issue_time(Timestamp store_issue_time) {
        this.store_issue_time = store_issue_time;
    }

    public byte getFeed_result() {
        return feed_result;
    }

    public void setFeed_result(byte feed_result) {
        this.feed_result = feed_result;
    }

    public Timestamp getFeed_time() {
        return feed_time;
    }

    public void setFeed_time(Timestamp feed_time) {
        this.feed_time = feed_time;
    }

    public byte getChange_result() {
        return change_result;
    }

    public void setChange_result(byte change_result) {
        this.change_result = change_result;
    }

    public Timestamp getChange_time() {
        return change_time;
    }

    public void setChange_time(Timestamp change_time) {
        this.change_time = change_time;
    }

    public byte getCheck_result() {
        return check_result;
    }

    public void setCheck_result(byte check_result) {
        this.check_result = check_result;
    }

    public byte getCheck_all_result() {
        return check_all_result;
    }

    public void setCheck_all_result(byte check_all_result) {
        this.check_all_result = check_all_result;
    }

    public Timestamp getCheck_all_time() {
        return check_all_time;
    }

    public void setCheck_all_time(Timestamp check_all_time) {
        this.check_all_time = check_all_time;
    }

    public Timestamp getCheck_time() {
        return check_time;
    }

    public void setCheck_time(Timestamp check_time) {
        this.check_time = check_time;
    }

    public int getLast_operation_type() {
        return last_operation_type;
    }

    public void setLast_operation_type(int last_operation_type) {
        this.last_operation_type = last_operation_type;
    }

    public byte getFirst_check_all_result() {
        return first_check_all_result;
    }

    public void setFirst_check_all_result(byte first_check_all_result) {
        this.first_check_all_result = first_check_all_result;
    }

    public Timestamp getFirst_check_all_time() {
        return first_check_all_time;
    }

    public void setFirst_check_all_time(Timestamp first_check_all_time) {
        this.first_check_all_time = first_check_all_time;
    }

    public Timestamp getLast_operation_time() {
        return last_operation_time;
    }

    public void setLast_operation_time(Timestamp last_operation_time) {
        this.last_operation_time = last_operation_time;
    }
}
