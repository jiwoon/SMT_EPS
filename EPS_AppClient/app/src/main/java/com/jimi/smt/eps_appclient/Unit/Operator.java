package com.jimi.smt.eps_appclient.Unit;

/**
 * 类名:Operator
 * 创建人:Liang GuoChang
 * 创建时间:2017/10/19 17:32
 * 描述:操作员信息
 * 版本号:V-1.0
 * 修改记录:
 */

public class Operator {
    //操作员id
    private String id;
    //在职状态,true为在职,false为离职
    private byte enabled;
    //操作员名字
    private String name;
    //操作员类型
    private int type;
    //操作员密码
    private String pwd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte isEnabled() {
        return enabled;
    }

    public void setEnabled(byte enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getOperator() {
        return "操作员:\n" + "id:" + getId() + "\nname:" + getName() + "\n在职状态:" + isEnabled()
                + "\n类型:" + getType() + "\n密码:" + getPwd();

    }
}
