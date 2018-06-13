package com.jimi.smt.eps_appclient.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jimi.smt.eps_appclient.Dao.FLCheckAll;
import com.jimi.smt.eps_appclient.Dao.Feed;
import com.jimi.smt.eps_appclient.Dao.GreenDaoUtil;
import com.jimi.smt.eps_appclient.Dao.QcCheckAll;
import com.jimi.smt.eps_appclient.Dao.Ware;
import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.GlobalData;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.EvenBusTest;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RefreshCacheService extends Service {
    private static final String TAG = "RefreshCacheService";
    private GlobalData globalData;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        globalData = (GlobalData) getApplication();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return new Binder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (Constants.liveUpdate) {
            getRefreshProgram(globalData);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public final class LocalBinder extends Binder {
        public RefreshCacheService getRefreshCacheService() {
            return RefreshCacheService.this;
        }
    }

    private void getRefreshProgram(final GlobalData globalData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onStartCommand - getRefreshProgram");

                    EvenBusTest evenBusTest = new EvenBusTest();
                    //当前的料号表
                    List<MaterialItem> itemList = globalData.getMaterialItems();
                    //数据库上最新的料号表
//                    List<MaterialItem> newItemList = new DBService().getMaterial(itemList.get(0).getLine(),
//                            itemList.get(0).getOrder(),itemList.get(0).getBoardType());
                    List<MaterialItem> newItemList = new DBService().getMaterial(globalData.getLine(),
                            globalData.getWork_order(), globalData.getBoard_type());

                    //判断两个列表是否一致
                    boolean isEqual = compare(itemList, newItemList);

                    //更新
                    if (!isEqual) {
                        evenBusTest.setUpdated(0);
                        //更新全局变量
                        globalData.setMaterialItems(newItemList);
                        //更新本地数据库
                        if (Constants.isCache) {
                            List<MaterialItem> updateItems = getUpdateMaterials(itemList, newItemList);
                            List<String> seats = getUpdateSeats(updateItems);
                            if ((globalData.getUserType() == Constants.WARE_HOUSE)
                                    || (globalData.getUserType() == Constants.ADMIN && globalData.getAdminOperType() == Constants.ADMIN_WARE_HOUSE)) {
                                //发料纪录
                                List<Ware> wares = new GreenDaoUtil().queryWareRecord(globalData.getOperator(), globalData.getWork_order(),
                                        globalData.getLine(), globalData.getBoard_type());
                                //发料
                                if (wares != null && wares.size() > 0) {
                                    //先删除更新的对应站位的数据
                                    boolean delete = new GreenDaoUtil().deleteWareBySeat(seats);
                                    Log.d(TAG, "发料 删除 - " + delete);
                                    //再更新本地数据库
                                    boolean update = new GreenDaoUtil().updateOrInsertWare(getUpdateWares(seats, newItemList));
                                    Log.d(TAG, "发料 更新本地数据库 - " + update);
                                    //获取本地数据库最新的发料数据
                                    List<Ware> newWares = new GreenDaoUtil().queryWareRecord(globalData.getOperator(), globalData.getWork_order()
                                            , globalData.getLine(), globalData.getBoard_type());
                                    //保存到订阅事件中
                                    evenBusTest.setWareList(newWares);
                                }
                            } else if ((globalData.getUserType() == Constants.FACTORY)
                                    || (globalData.getUserType() == Constants.ADMIN && globalData.getAdminOperType() == Constants.ADMIN_FACTORY)) {
                                //上料纪录、操作员全检纪录
                                List<Feed> feeds = new GreenDaoUtil().queryFeedRecord(globalData.getOperator(), globalData.getWork_order(),
                                        globalData.getLine(), globalData.getBoard_type());
                                List<FLCheckAll> flCheckAlls = new GreenDaoUtil().queryFLCheckRecord(globalData.getOperator(), globalData.getWork_order(),
                                        globalData.getLine(), globalData.getBoard_type());
                                //上料
                                if (feeds != null && feeds.size() > 0) {
                                    //先删除更新的对应站位的数据
                                    boolean delete = new GreenDaoUtil().deleteFeedBySeat(seats);
                                    Log.d(TAG, "上料 删除 - " + delete);
                                    //再更新本地数据库
                                    boolean update = new GreenDaoUtil().updateOrInsertFeed(getUpdateFeeds(seats, newItemList));
                                    Log.d(TAG, "上料 更新本地数据库 - " + update);
                                    //获取本地数据库最新的上料数据
                                    List<Feed> newFeeds = new GreenDaoUtil().queryFeedRecord(globalData.getOperator(), globalData.getWork_order()
                                            , globalData.getLine(), globalData.getBoard_type());
                                    //保存到订阅事件中
                                    evenBusTest.setFeedList(newFeeds);
                                }
                                //操作员全检
                                if (flCheckAlls != null && flCheckAlls.size() > 0) {
                                    //先删除更新的对应站位的数据
                                    boolean delete = new GreenDaoUtil().deleteFLCheckBySeat(seats);
                                    Log.d(TAG, "操作员全检 删除 - " + delete);
                                    //再更新本地数据库
                                    boolean update = new GreenDaoUtil().updateOrInsertFLCheck(getUpdateFLChecks(seats, newItemList));
                                    Log.d(TAG, "操作员全检 更新本地数据库 - " + update);
                                    //获取本地数据库最新的操作员全检数据
                                    List<FLCheckAll> newFLCheckAlls = new GreenDaoUtil().queryFLCheckRecord(globalData.getOperator(), globalData.getWork_order()
                                            , globalData.getLine(), globalData.getBoard_type());
                                    //保存到订阅事件中
                                    evenBusTest.setFlCheckAllList(newFLCheckAlls);
                                }
                            } else if ((globalData.getUserType() == Constants.QC)
                                    || (globalData.getUserType() == Constants.ADMIN && globalData.getAdminOperType() == Constants.ADMIN_QC)) {
                                //QC全检纪录
                                List<QcCheckAll> qcCheckAlls = new GreenDaoUtil().queryQcCheckRecord(globalData.getOperator(), globalData.getWork_order()
                                        , globalData.getLine(), globalData.getBoard_type());
                                if (qcCheckAlls != null && qcCheckAlls.size() > 0) {
                                    //先删除更新的对应站位的数据
                                    boolean delete = new GreenDaoUtil().deleteQcCheckBySeat(seats);
                                    Log.d(TAG, "QC全检纪录 删除 - " + delete);
                                    //再更新本地数据库
                                    boolean update = new GreenDaoUtil().updateOrInsertQcCheck(getUpdateQcChecks(seats, newItemList));
                                    Log.d(TAG, "QC全检纪录 更新本地数据库 - " + update);
                                    //获取本地数据库最新的QC全检纪录数据
                                    List<QcCheckAll> newQcCheckAlls = new GreenDaoUtil().queryQcCheckRecord(globalData.getOperator(), globalData.getWork_order()
                                            , globalData.getLine(), globalData.getBoard_type());
                                    //保存到订阅事件中
                                    evenBusTest.setQcCheckAllList(newQcCheckAlls);
                                }
                            }
                        }
                        globalData.setUpdateProgram(true);
                    }
                    //未更新
                    else {
                        evenBusTest.setUpdated(1);
                    }

                    //发送消息
                    EventBus.getDefault().post(evenBusTest);
                }
            }
        }).start();
    }

    private boolean compare(List<MaterialItem> a, List<MaterialItem> b) {
        if (a.size() != b.size())
            return false;
        for (int i = 0; i < a.size(); i++) {
            //序列号
            if (a.get(i).getSerialNo() != (b.get(i).getSerialNo())) {
                Log.d(TAG, "itemList - " + a.get(i).getSerialNo());
                Log.d(TAG, "NewItemList - " + b.get(i).getSerialNo());
                return false;
            }
            //原始站位
            if (!a.get(i).getOrgLineSeat().equalsIgnoreCase(b.get(i).getOrgLineSeat())) {
                Log.d(TAG, "itemList - " + a.get(i).getOrgLineSeat());
                Log.d(TAG, "NewItemList - " + b.get(i).getOrgLineSeat());
                return false;
            }
            //原始料号
            if (!a.get(i).getOrgMaterial().equalsIgnoreCase(b.get(i).getOrgMaterial())) {
                Log.d(TAG, "itemList - " + a.get(i).getOrgMaterial());
                Log.d(TAG, "NewItemList - " + b.get(i).getOrgMaterial());
                return false;
            }
            //主替料
            if (!a.get(i).getAlternative().equals(b.get(i).getAlternative())) {
                Log.d(TAG, "itemList - " + a.get(i).getAlternative());
                Log.d(TAG, "NewItemList - " + b.get(i).getAlternative());
                return false;
            }
        }
        return true;
    }

    //获取更新或删除的项
    private List<MaterialItem> getUpdateMaterials(List<MaterialItem> oldList, List<MaterialItem> newList) {
        HashSet<MaterialItem> updateItemsHas = new HashSet<MaterialItem>();
        List<MaterialItem> updateItems = new ArrayList<MaterialItem>();
        for (MaterialItem materialItem : newList) {
            if (!oldList.contains(materialItem)) {
                updateItemsHas.add(materialItem);
                Log.d(TAG, "不同的 - " + materialItem.getMaterialStr());
            }
        }
        for (MaterialItem materialItem : oldList) {
            if (!newList.contains(materialItem)) {
                updateItemsHas.add(materialItem);
                Log.d(TAG, "不同的 = " + materialItem.getMaterialStr());
            }
        }
        updateItems.addAll(updateItemsHas);
        Log.d(TAG, "getUpdateMaterials - " + updateItems.size());
        return updateItems;
    }

    //获取更新的站位
    private List<String> getUpdateSeats(List<MaterialItem> updateItems) {
        List<String> seatStrings = new ArrayList<String>();
        HashSet<String> seatHashSet = new HashSet<String>();
        for (MaterialItem materialItem : updateItems) {
            seatHashSet.add(materialItem.getOrgLineSeat());
        }
        seatStrings.addAll(seatHashSet);
        return seatStrings;
    }

    //获取更新数据库的发料实体
    private List<Ware> getUpdateWares(List<String> seats, List<MaterialItem> newList) {
        List<Ware> wares = new ArrayList<Ware>();
        for (String s : seats) {
            for (MaterialItem materialItem : newList) {
                if (materialItem.getOrgLineSeat().equalsIgnoreCase(s)) {
                    Ware ware = new Ware(null, globalData.getWork_order(), globalData.getOperator(), globalData.getBoard_type(),
                            globalData.getLine(), materialItem.getSerialNo(), materialItem.getAlternative(), materialItem.getOrgLineSeat(),
                            materialItem.getOrgMaterial(), "", "", "", "");
                    wares.add(ware);
                }
            }
        }
        Log.d(TAG, "getUpdateWares - " + wares.size());
        return wares;
    }

    //获取更新数据库的上料实体
    private List<Feed> getUpdateFeeds(List<String> seats, List<MaterialItem> newList) {
        List<Feed> feeds = new ArrayList<Feed>();
        for (String s : seats) {
            for (MaterialItem materialItem : newList) {
                if (materialItem.getOrgLineSeat().equalsIgnoreCase(s)) {
                    Feed feed = new Feed(null, globalData.getWork_order(), globalData.getOperator(), globalData.getBoard_type(),
                            globalData.getLine(), materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(),
                            "", "", "", "", materialItem.getSerialNo(), materialItem.getAlternative());
                    feeds.add(feed);
                }
            }
        }
        Log.d(TAG, "getUpdateFeeds - " + feeds.size());
        return feeds;
    }

    //获取更新数据库的操作员全检实体
    private List<FLCheckAll> getUpdateFLChecks(List<String> seats, List<MaterialItem> newList) {
        List<FLCheckAll> flCheckAlls = new ArrayList<FLCheckAll>();
        for (String s : seats) {
            for (MaterialItem materialItem : newList) {
                if (materialItem.getOrgLineSeat().equalsIgnoreCase(s)) {
                    FLCheckAll flCheckAll = new FLCheckAll(null, globalData.getWork_order(), globalData.getOperator(), globalData.getBoard_type(),
                            globalData.getLine(), materialItem.getSerialNo(), materialItem.getAlternative(), materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(),
                            "", "", "", "");
                    flCheckAlls.add(flCheckAll);
                }
            }
        }
        Log.d(TAG, "getUpdateFLChecks - " + flCheckAlls.size());
        return flCheckAlls;
    }

    //获取更新数据库的操作员全检实体
    private List<QcCheckAll> getUpdateQcChecks(List<String> seats, List<MaterialItem> newList) {
        List<QcCheckAll> qcCheckAlls = new ArrayList<QcCheckAll>();
        for (String s : seats) {
            for (MaterialItem materialItem : newList) {
                if (materialItem.getOrgLineSeat().equalsIgnoreCase(s)) {
                    QcCheckAll qcCheckAll = new QcCheckAll(null, globalData.getWork_order(), globalData.getOperator(), globalData.getBoard_type(),
                            globalData.getLine(), materialItem.getSerialNo(), materialItem.getAlternative(), materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(),
                            "", "", "", "");
                    qcCheckAlls.add(qcCheckAll);
                }
            }
        }
        Log.d(TAG, "getUpdateQcChecks - " + qcCheckAlls.size());
        return qcCheckAlls;
    }

    /*private static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if(a.size() != b.size())
            return false;
        Collections.sort(a);
        Collections.sort(b);
        for(int i=0;i<a.size();i++){
            if(!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }*/


    //发送广播
    private void sendContentBroadcast(int update) {
        Intent intent = new Intent();
        intent.setAction("com.jimi.smt.eps_appclient.update_program");
        intent.putExtra("update", update);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        stopSelf();
        super.onDestroy();
    }
}
