package com.jimi.smt.eps_appclient.Unit;

/**
 * 类名:EpsAppVersion
 * 创建人:Liang GuoChang
 * 创建时间:2017/10/30 16:02
 * 描述:app版本信息
 * 版本号:v1.1
 * 修改记录:
 */

public class EpsAppVersion {
    private int versionCode;
    private String versionName;
    private String versionDes;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionDes() {
        return versionDes;
    }

    public void setVersionDes(String versionDes) {
        this.versionDes = versionDes;
    }
}
