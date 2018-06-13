package com.jimi.smt.eps_appclient.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 类名:Feed
 * 创建人:Liang GuoChang
 * 创建时间:2018/3/27 10:06
 * 描述:
 * 版本号:
 * 修改记录:
 */
@Entity
public class Feed {

    @Id(autoincrement = true)
    private Long feed_id;
    private String order;//工单号
    private String operator;//操作员
    private int board_type;//版面类型
    private String line;//线号
    private String OrgLineSeat;    //当前站位表站位
    private String OrgMaterial;    //当前站位表料号
    private String ScanLineSeat;   //扫描的站位
    private String ScanMaterial;   //扫描的料号
    private String Result;         //结果
    private String Remark;         //失败原因
    private int SerialNo;           //流水号
    private Byte Alternative;    //是否属于替料
    @Transient
    private int state;//工单状态,为1,不写进数据库

    @Generated(hash = 338756017)
    public Feed(Long feed_id, String order, String operator, int board_type,
                String line, String OrgLineSeat, String OrgMaterial,
                String ScanLineSeat, String ScanMaterial, String Result, String Remark,
                int SerialNo, Byte Alternative) {
        this.feed_id = feed_id;
        this.order = order;
        this.operator = operator;
        this.board_type = board_type;
        this.line = line;
        this.OrgLineSeat = OrgLineSeat;
        this.OrgMaterial = OrgMaterial;
        this.ScanLineSeat = ScanLineSeat;
        this.ScanMaterial = ScanMaterial;
        this.Result = Result;
        this.Remark = Remark;
        this.SerialNo = SerialNo;
        this.Alternative = Alternative;
    }

    @Generated(hash = 1810414124)
    public Feed() {
    }

    public Long getFeed_id() {
        return this.feed_id;
    }

    public void setFeed_id(Long feed_id) {
        this.feed_id = feed_id;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getBoard_type() {
        return this.board_type;
    }

    public void setBoard_type(int board_type) {
        this.board_type = board_type;
    }

    public String getLine() {
        return this.line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getOrgLineSeat() {
        return this.OrgLineSeat;
    }

    public void setOrgLineSeat(String OrgLineSeat) {
        this.OrgLineSeat = OrgLineSeat;
    }

    public String getOrgMaterial() {
        return this.OrgMaterial;
    }

    public void setOrgMaterial(String OrgMaterial) {
        this.OrgMaterial = OrgMaterial;
    }

    public String getScanLineSeat() {
        return this.ScanLineSeat;
    }

    public void setScanLineSeat(String ScanLineSeat) {
        this.ScanLineSeat = ScanLineSeat;
    }

    public String getScanMaterial() {
        return this.ScanMaterial;
    }

    public void setScanMaterial(String ScanMaterial) {
        this.ScanMaterial = ScanMaterial;
    }

    public String getResult() {
        return this.Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }

    public String getRemark() {
        return this.Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public int getSerialNo() {
        return this.SerialNo;
    }

    public void setSerialNo(int SerialNo) {
        this.SerialNo = SerialNo;
    }

    public Byte getAlternative() {
        return this.Alternative;
    }

    public void setAlternative(Byte Alternative) {
        this.Alternative = Alternative;
    }

}
