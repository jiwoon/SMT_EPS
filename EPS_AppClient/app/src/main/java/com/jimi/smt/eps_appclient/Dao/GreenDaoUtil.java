package com.jimi.smt.eps_appclient.Dao;

import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.gen.FLCheckAllDao;
import com.jimi.smt.eps_appclient.gen.FeedDao;
import com.jimi.smt.eps_appclient.gen.QcCheckAllDao;
import com.jimi.smt.eps_appclient.gen.WareDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 类名:GreenDaoUtil
 * 创建人:Liang GuoChang
 * 创建时间:2018/3/26 17:15
 * 描述:数据库操作类
 * 版本号:
 * 修改记录:
 */

public class GreenDaoUtil {

    private static String TAG = "GreenDaoUtil";

    public GreenDaoUtil() {

    }

    //更新数据
    /*public void updateData(){
        List<UserTest> userTests = getUserTestDao().loadAll();
        UserTest userTest = new UserTest(userTests.get(0).getId(),"更改后的数据1",22,true);
        getUserTestDao().update(userTest);
    }*/

    //查询数据详细 // TODO: 2018/3/26
    /*public void queryData(){
        List<UserTest> userTests = getUserTestDao().loadAll();
        for (UserTest userTest : userTests){
            Log.d(TAG,"userTest - id - "+userTest.getId()+" - name - "+userTest.getName());
        }
    }*/

    //根据条件查询数据库有无上料纪录
    public List<Feed> queryFeedRecord(String operator, String order, String line, int boardType) {
        QueryBuilder<Feed> feedQuery = getFeedDao().queryBuilder();
        feedQuery.where(FeedDao.Properties.Operator.eq(operator),
                FeedDao.Properties.Order.eq(order),
                FeedDao.Properties.Line.eq(line),
                FeedDao.Properties.Board_type.eq(boardType));
        List<Feed> feedList = feedQuery.orderAsc(FeedDao.Properties.OrgLineSeat)
                .orderAsc(FeedDao.Properties.SerialNo)
                .orderAsc(FeedDao.Properties.OrgMaterial)
                .list();
        Log.d(TAG, "queryFeedRecord - " + feedList.size());
        return feedList;
    }

    //根据条件查询数据库有无发料纪录
    public List<Ware> queryWareRecord(String operator, String order, String line, int boardType) {
        QueryBuilder<Ware> wareQuery = getWareDao().queryBuilder();
        wareQuery.where(WareDao.Properties.Operator.eq(operator),
                WareDao.Properties.Order.eq(order),
                WareDao.Properties.Line.eq(line),
                WareDao.Properties.Board_type.eq(boardType));
        List<Ware> wareList = wareQuery.orderAsc(WareDao.Properties.OrgLineSeat)
                .orderAsc(WareDao.Properties.SerialNo)
                .orderAsc(WareDao.Properties.OrgMaterial)
                .list();
        Log.d(TAG, "queryWareRecord - " + wareList.size());
        return wareList;
    }

    //根据条件查询数据库有无操作员全检纪录
    public List<FLCheckAll> queryFLCheckRecord(String operator, String order, String line, int boardType) {
        QueryBuilder<FLCheckAll> flCheckQuery = getFlCheckAllDao().queryBuilder();
        flCheckQuery.where(FLCheckAllDao.Properties.Operator.eq(operator),
                FLCheckAllDao.Properties.Order.eq(order),
                FLCheckAllDao.Properties.Line.eq(line),
                FLCheckAllDao.Properties.Board_type.eq(boardType));
        List<FLCheckAll> flCheckList = flCheckQuery.orderAsc(FLCheckAllDao.Properties.OrgLineSeat)
                .orderAsc(FLCheckAllDao.Properties.SerialNo)
                .orderAsc(FLCheckAllDao.Properties.OrgMaterial)
                .list();
        Log.d(TAG, "queryFLCheckRecord - " + flCheckList.size());
        return flCheckList;
    }

    //根据条件查询数据库有无IPQC全检纪录
    public List<QcCheckAll> queryQcCheckRecord(String operator, String order, String line, int boardType) {
        QueryBuilder<QcCheckAll> qcCheckQuery = getQcCheckAllDao().queryBuilder();
        qcCheckQuery.where(QcCheckAllDao.Properties.Operator.eq(operator),
                QcCheckAllDao.Properties.Order.eq(order),
                QcCheckAllDao.Properties.Line.eq(line),
                QcCheckAllDao.Properties.Board_type.eq(boardType));
        List<QcCheckAll> qcCheckList = qcCheckQuery.orderAsc(QcCheckAllDao.Properties.OrgLineSeat)
                .orderAsc(QcCheckAllDao.Properties.SerialNo)
                .orderAsc(QcCheckAllDao.Properties.OrgMaterial)
                .list();
        Log.d(TAG, "queryQcCheckRecord - " + qcCheckList.size());
        return qcCheckList;
    }

    //批量插入上料初始数据
    public boolean insertMultiFeedMaterial(final List<Feed> feedList) {
        boolean result = false;
        try {
            GreenDaoManager.getInstance().getmDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (Feed feed : feedList) {
                        getFeedDao().insertOrReplace(feed);
                    }
                }
            });
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "insertMultiFeedMaterial - " + e.toString());
        }
        return result;
    }

    //批量插入发料初始数据
    public boolean insertMultiWareMaterial(final List<Ware> wareList) {
        boolean result = false;
        try {
            GreenDaoManager.getInstance().getmDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (Ware ware : wareList) {
                        getWareDao().insertOrReplace(ware);
                    }
                }
            });
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "insertMultiWareMaterial - " + e.toString());
        }
        return result;
    }

    //批量插入操作员全检初始数据
    public boolean insertMultiFLCheckMaterial(final List<FLCheckAll> flCheckAllList) {
        boolean result = false;
        try {
            GreenDaoManager.getInstance().getmDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (FLCheckAll flCheckAll : flCheckAllList) {
                        getFlCheckAllDao().insertOrReplace(flCheckAll);
                    }
                }
            });
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "insertMultiFLCheckMaterial - " + e.toString());
        }
        return result;
    }

    //批量插入IPQC全检初始数据
    public boolean insertMultiQcCheckMaterial(final List<QcCheckAll> qcCheckAllList) {
        boolean result = false;
        try {
            GreenDaoManager.getInstance().getmDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (QcCheckAll qcCheckAll : qcCheckAllList) {
                        getQcCheckAllDao().insertOrReplace(qcCheckAll);
                    }
                }
            });
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "insertMultiQcCheckMaterial - " + e.toString());
        }
        return result;
    }

    //更新某一项上料结果
    public boolean updateFeed(Feed feed) {
        boolean updateRes = false;
        try {
            getFeedDao().update(feed);
            updateRes = true;
        } catch (Exception e) {
            Log.d(TAG, "updateFeed - " + e.toString());
        }
        return updateRes;
    }

    //更新某一项发料结果
    public boolean updateWare(Ware ware) {
        boolean updateRes = false;
        try {
            getWareDao().update(ware);
            updateRes = true;
        } catch (Exception e) {
            Log.d(TAG, "updateWare - " + e.toString());
        }
        return updateRes;
    }

    //更新某一项操作员全检结果
    public boolean updateFLCheck(FLCheckAll flCheckAll) {
        boolean updateRes = false;
        try {
            getFlCheckAllDao().update(flCheckAll);
            updateRes = true;
        } catch (Exception e) {
            Log.d(TAG, "updateFLCheck - " + e.toString());
        }
        return updateRes;
    }

    //更新某一项IPQC全检结果
    public boolean updateQcCheck(QcCheckAll qcCheckAll) {
        boolean updateRes = false;
        try {
            getQcCheckAllDao().update(qcCheckAll);
            updateRes = true;
        } catch (Exception e) {
            Log.d(TAG, "updateQcCheck - " + e.toString());
        }
        return updateRes;
    }

    //初始化所有上料结果
    public boolean updateAllFeed(List<Feed> feeds) {
        boolean updateRes = false;
        try {
            GreenDaoManager.getInstance().getmDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (Feed feed : feeds) {
                        feed.setScanLineSeat("");
                        feed.setScanMaterial("");
                        feed.setResult("");
                        feed.setRemark("");
                    }
                    getFeedDao().updateInTx(feeds);
                }
            });
            updateRes = true;
        } catch (Exception e) {
            Log.d(TAG, "updateAllFeed - " + e.toString());
        }
        return updateRes;
    }

    //初始化所有发料结果
    public boolean updateAllWare(List<Ware> wares) {
        boolean updateRes = false;
        try {
            GreenDaoManager.getInstance().getmDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (Ware ware : wares) {
                        ware.setScanLineSeat("");
                        ware.setScanMaterial("");
                        ware.setResult("");
                        ware.setRemark("");
                    }
                    getWareDao().updateInTx(wares);
                }
            });
            updateRes = true;
        } catch (Exception e) {
            Log.d(TAG, "updateAllWare - " + e.toString());
        }
        return updateRes;
    }

    //初始化所有操作员全检结果
    public boolean updateAllFLCheck(List<FLCheckAll> flCheckAlls) {
        boolean updateRes = false;
        try {
            GreenDaoManager.getInstance().getmDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (FLCheckAll flCheckAll : flCheckAlls) {
                        flCheckAll.setScanLineSeat("");
                        flCheckAll.setScanMaterial("");
                        flCheckAll.setResult("");
                        flCheckAll.setRemark("");
                    }
                    getFlCheckAllDao().updateInTx(flCheckAlls);
                }
            });
            updateRes = true;
        } catch (Exception e) {
            Log.d(TAG, "updateAllFLCheck - " + e.toString());
        }
        return updateRes;
    }

    //初始化所有IPQC全检结果
    public boolean updateAllQcCheck(List<QcCheckAll> qcCheckAlls) {
        boolean updateRes = false;
        try {
            GreenDaoManager.getInstance().getmDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (QcCheckAll qcCheckAll : qcCheckAlls) {
                        qcCheckAll.setScanLineSeat("");
                        qcCheckAll.setScanMaterial("");
                        qcCheckAll.setResult("");
                        qcCheckAll.setRemark("");
                    }
                    getQcCheckAllDao().updateInTx(qcCheckAlls);
                }
            });
            updateRes = true;
        } catch (Exception e) {
            Log.d(TAG, "updateAllQcCheck - " + e.toString());
        }
        return updateRes;
    }

    //删除所有上料结果
    /*public boolean deleteAllFeed(List<Feed> feeds){
        boolean updateRes = false;
        try {
            getFeedDao().deleteInTx(feeds);
            updateRes = true;
        }catch (Exception e){
            Log.d(TAG,"deleteAllFeed - "+e.toString());
        }
        return updateRes;
    }*/

    //删除上料数据库所有数据
    public boolean deleteAllFeedData() {
        boolean updateRes = false;
        try {
            getFeedDao().deleteAll();
            updateRes = true;
        } catch (Exception e) {
            Log.d(TAG, "deleteAllFeed - " + e.toString());
        }
        return updateRes;
    }

    //删除发料数据库所有数据
    public boolean deleteAllWareData() {
        boolean updateRes = false;
        try {
            getWareDao().deleteAll();
            updateRes = true;
        } catch (Exception e) {
            Log.d(TAG, "deleteAllWareData - " + e.toString());
        }
        return updateRes;
    }

    //删除操作员全检数据库所有数据
    public boolean deleteAllFLCheck() {
        boolean updateRes = false;
        try {
            getFlCheckAllDao().deleteAll();
            updateRes = true;
        } catch (Exception e) {
            Log.d(TAG, "deleteAllFLCheck - " + e.toString());
        }
        return updateRes;
    }

    //删除IPQC全检数据库所有数据
    public boolean deleteAllQcCheck() {
        boolean updateRes = false;
        try {
            getQcCheckAllDao().deleteAll();
            updateRes = true;
        } catch (Exception e) {
            Log.d(TAG, "deleteAllQcCheck - " + e.toString());
        }
        return updateRes;
    }

    //删除对应站位的上料纪录
    public boolean deleteFeedBySeat(List<String> seats) {
        boolean result = false;
        try {
            for (String s : seats) {
                List<Feed> feedList = getFeedDao().queryBuilder().where(FeedDao.Properties.OrgLineSeat.eq(s)).list();
                getFeedDao().deleteInTx(feedList);
            }
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "deleteFeedBySeat - " + e.toString());
        }
        return result;
    }

    //删除对应站位的发料纪录
    public boolean deleteWareBySeat(List<String> seats) {
        boolean result = false;
        try {
            for (String s : seats) {
                List<Ware> wareList = getWareDao().queryBuilder().where(WareDao.Properties.OrgLineSeat.eq(s)).list();
                getWareDao().deleteInTx(wareList);
            }
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "deleteWareBySeat - " + e.toString());
        }
        return result;
    }

    //删除对应站位的操作员全检纪录
    public boolean deleteFLCheckBySeat(List<String> seats) {
        boolean result = false;
        try {
            for (String s : seats) {
                List<FLCheckAll> flCheckAlls = getFlCheckAllDao().queryBuilder().where(FLCheckAllDao.Properties.OrgLineSeat.eq(s)).list();
                getFlCheckAllDao().deleteInTx(flCheckAlls);
            }
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "deleteFLCheckBySeat - " + e.toString());
        }
        return result;
    }

    //删除对应站位的IPQC全检纪录
    public boolean deleteQcCheckBySeat(List<String> seats) {
        boolean result = false;
        try {
            for (String s : seats) {
                List<QcCheckAll> qcCheckAlls = getQcCheckAllDao().queryBuilder().where(QcCheckAllDao.Properties.OrgLineSeat.eq(s)).list();
                getQcCheckAllDao().deleteInTx(qcCheckAlls);
            }
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "deleteQcCheckBySeat - " + e.toString());
        }
        return result;
    }

    //更新或插入新上料数据
    public boolean updateOrInsertFeed(List<Feed> feeds) {
        boolean result = false;
        try {
            for (Feed feed : feeds) {
                getFeedDao().insertOrReplace(feed);
            }
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "updateOrInsertFeed - " + e.toString());
        }
        return result;
    }

    //更新或插入新发料数据
    public boolean updateOrInsertWare(List<Ware> wares) {
        boolean result = false;
        try {
            for (Ware ware : wares) {
                getWareDao().insertOrReplace(ware);
            }
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "updateOrInsertWare - " + e.toString());
        }
        return result;
    }

    //更新或插入新操作员全检数据
    public boolean updateOrInsertFLCheck(List<FLCheckAll> flCheckAlls) {
        boolean result = false;
        try {
            for (FLCheckAll flCheckAll : flCheckAlls) {
                getFlCheckAllDao().insertOrReplace(flCheckAll);
            }
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "updateOrInsertFLCheck - " + e.toString());
        }
        return result;
    }

    //更新或插入新IPQC全检数据
    public boolean updateOrInsertQcCheck(List<QcCheckAll> qcCheckAlls) {
        boolean result = false;
        try {
            for (QcCheckAll qcCheckAll : qcCheckAlls) {
                getQcCheckAllDao().insertOrReplace(qcCheckAll);
            }
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "updateOrInsertQcCheck - " + e.toString());
        }
        return result;
    }


    private WareDao getWareDao() {
        return GreenDaoManager.getInstance().getmDaoSession().getWareDao();
    }

    private FeedDao getFeedDao() {
        return GreenDaoManager.getInstance().getmDaoSession().getFeedDao();
    }

    private FLCheckAllDao getFlCheckAllDao() {
        return GreenDaoManager.getInstance().getmDaoSession().getFLCheckAllDao();
    }

    private QcCheckAllDao getQcCheckAllDao() {
        return GreenDaoManager.getInstance().getmDaoSession().getQcCheckAllDao();
    }


}
