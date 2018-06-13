package com.jimi.smt.eps_appclient.Unit;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 类名:FeedCacheBean
 * 创建人:Liang GuoChang
 * 创建时间:2018/3/24 16:56
 * 描述:
 * 版本号:
 * 修改记录:
 */

public class FeedCacheBean implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;

    //当前上料时用到的排位料号表
    private ArrayList<MaterialItem> lFeedMaterialItem = new ArrayList<MaterialItem>();
    //扫描的站位在列表中的位置
    private ArrayList<Integer> scanLineIndex = new ArrayList<Integer>();
    //program_item_visit表
    private ArrayList<ProgramItemVisit> programItemVisits = new ArrayList<ProgramItemVisit>();
    //当前上料项
    private int curFeedMaterialId = 0;
    //成功上料数
    private int sucFeedCount = 0;
    //料号表的总数
    private int allCount = 0;
    //匹配的站位表项
    private int matchFeedMaterialId = -1;

    public ArrayList<MaterialItem> getlFeedMaterialItem() {
        return lFeedMaterialItem;
    }

    public void setlFeedMaterialItem(ArrayList<MaterialItem> lFeedMaterialItem) {
        this.lFeedMaterialItem = lFeedMaterialItem;
    }

    public ArrayList<Integer> getScanLineIndex() {
        return scanLineIndex;
    }

    public void setScanLineIndex(ArrayList<Integer> scanLineIndex) {
        this.scanLineIndex = scanLineIndex;
    }

    public ArrayList<ProgramItemVisit> getProgramItemVisits() {
        return programItemVisits;
    }

    public void setProgramItemVisits(ArrayList<ProgramItemVisit> programItemVisits) {
        this.programItemVisits = programItemVisits;
    }

    public int getCurFeedMaterialId() {
        return curFeedMaterialId;
    }

    public void setCurFeedMaterialId(int curFeedMaterialId) {
        this.curFeedMaterialId = curFeedMaterialId;
    }

    public int getSucFeedCount() {
        return sucFeedCount;
    }

    public void setSucFeedCount(int sucFeedCount) {
        this.sucFeedCount = sucFeedCount;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getMatchFeedMaterialId() {
        return matchFeedMaterialId;
    }

    public void setMatchFeedMaterialId(int matchFeedMaterialId) {
        this.matchFeedMaterialId = matchFeedMaterialId;
    }
}
