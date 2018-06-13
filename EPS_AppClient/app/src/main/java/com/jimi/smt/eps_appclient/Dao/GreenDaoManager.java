package com.jimi.smt.eps_appclient.Dao;

import com.jimi.smt.eps_appclient.gen.DaoMaster;
import com.jimi.smt.eps_appclient.gen.DaoSession;
import com.jimi.smt.eps_appclient.Unit.GlobalData;

/**
 * 类名:GreenDaoManager
 * 创建人:Liang GuoChang
 * 创建时间:2018/3/26 16:35
 * 描述:GreenDao管理类
 * 版本号:
 * 修改记录:
 */

public class GreenDaoManager {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static GreenDaoManager mInstance;//单例

    private GreenDaoManager() {
        if (mInstance == null) {
            DaoMaster.DevOpenHelper devOpenHelper =
                    new DaoMaster.DevOpenHelper(GlobalData.getAppContext(), "user_test_db", null);
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDb());
            mDaoSession = mDaoMaster.newSession();
        }
    }

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {//保证异步处理安全操作
                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }

    public DaoMaster getmDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}
