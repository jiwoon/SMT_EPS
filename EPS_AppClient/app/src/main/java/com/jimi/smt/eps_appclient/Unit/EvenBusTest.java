package com.jimi.smt.eps_appclient.Unit;

import com.jimi.smt.eps_appclient.Dao.FLCheckAll;
import com.jimi.smt.eps_appclient.Dao.Feed;
import com.jimi.smt.eps_appclient.Dao.QcCheckAll;
import com.jimi.smt.eps_appclient.Dao.Ware;

import java.util.List;

/**
 * 类名:EvenBusTest
 * 创建人:Liang GuoChang
 * 创建时间:2018/4/3 10:53
 * 描述:
 * 版本号:
 * 修改记录:
 */

public class EvenBusTest {
    private int busType;
    //0 更新; 1 未更新
    private int updated;
    //0 未重置;1 重置
    private int reseted;
    private List<Feed> feedList;
    private List<Ware> wareList;
    private List<FLCheckAll> flCheckAllList;
    private List<QcCheckAll> qcCheckAllList;

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getReseted() {
        return reseted;
    }

    public void setReseted(int reseted) {
        this.reseted = reseted;
    }

    public int getBusType() {
        return busType;
    }

    public void setBusType(int busType) {
        this.busType = busType;
    }

    public List<Feed> getFeedList() {
        return feedList;
    }

    public void setFeedList(List<Feed> feedList) {
        this.feedList = feedList;
    }

    public List<Ware> getWareList() {
        return wareList;
    }

    public void setWareList(List<Ware> wareList) {
        this.wareList = wareList;
    }

    public List<FLCheckAll> getFlCheckAllList() {
        return flCheckAllList;
    }

    public void setFlCheckAllList(List<FLCheckAll> flCheckAllList) {
        this.flCheckAllList = flCheckAllList;
    }

    public List<QcCheckAll> getQcCheckAllList() {
        return qcCheckAllList;
    }

    public void setQcCheckAllList(List<QcCheckAll> qcCheckAllList) {
        this.qcCheckAllList = qcCheckAllList;
    }
}
