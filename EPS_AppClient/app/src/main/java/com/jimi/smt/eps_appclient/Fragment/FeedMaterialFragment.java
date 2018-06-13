package com.jimi.smt.eps_appclient.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.jimi.smt.eps_appclient.Activity.FactoryLineActivity;
import com.jimi.smt.eps_appclient.Adapter.MaterialAdapter;
import com.jimi.smt.eps_appclient.Dao.Feed;
import com.jimi.smt.eps_appclient.Dao.GreenDaoUtil;
import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.EvenBusTest;
import com.jimi.smt.eps_appclient.Unit.FeedCacheBean;
import com.jimi.smt.eps_appclient.Unit.GlobalData;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Unit.ProgramItemVisit;
import com.jimi.smt.eps_appclient.Views.InfoDialog;
import com.jimi.smt.eps_appclient.Views.LoadingDialog;
import com.jimi.smt.eps_appclient.Views.MyEditTextDel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名:FeedMaterialFragment
 * 创建人:Connie
 * 创建时间:2017-9-21
 * 描述:上料Fragment
 * 版本号:V1.0
 * 修改记录:
 */
public class FeedMaterialFragment extends Fragment implements OnEditorActionListener {
    private transient final String TAG = this.getClass().getSimpleName();
    //全局变量
    private transient GlobalData globalData;
    //FeedMaterialFragment缓存对象
    private FeedCacheBean feedCacheBean;

    //上料视图
    private transient View vFeedMaterialFragment;
    //操作员　站位　料号
    private transient TextView edt_Operation;
    private transient MyEditTextDel edt_LineSeat;
    private transient MyEditTextDel edt_Material;
    private transient FactoryLineActivity mActivity;

    //上料列表
    private transient ListView lv_FeedMaterial;
    private transient MaterialAdapter materialAdapter;

    //上料本地数据表
    private List<Feed> feedList = new ArrayList<Feed>();
    //全部上料结果
    private boolean allFeedResult = false;
    //是否恢复缓存
    private boolean isRestoreCache = false;
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
    private transient GlobalFunc globalFunc;
    private transient LoadingDialog loadingDialog;

    private static final int STORE_ALL_FAIL = 101;//未全部发料
    private static final int PROGRAM_UPDATE = 102;//站位表更新
    private static final int STORE_SUCCESS = 108;
    private static final int STORE_FAIL = 109;
    private static final int REFRESH_ITEMS = 110;
    private static final int RESET = 111;//重置
    private static final int OPERATING = 112;//操作
    private static final int RESET_SHOUWTIP = 113;//重置并提示

    private boolean mHidden = false;
    @SuppressLint("HandlerLeak")
    private transient Handler feedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //取消弹出窗
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.cancel();
                loadingDialog.dismiss();
            }
            switch (msg.what) {
                case STORE_SUCCESS:
                    //更新显示
                    materialAdapter.notifyDataSetChanged();
                    edt_Material.requestFocus();
                    break;

                case STORE_FAIL:
                    //未发料
                    for (int a = 0; a < scanLineIndex.size(); a++) {
                        lFeedMaterialItem.get(scanLineIndex.get(a))
                                .setResultRemark("WARN", "该料号未发料");
                    }
                    //更新显示
                    materialAdapter.notifyDataSetChanged();
                    feedNextMaterial();
                    break;

                case REFRESH_ITEMS:
                    //全部上料完成
                    if (getFeedResult(programItemVisits)) {
                        //清空上料页面结果
                        clearFeedDisplay();
                        //跳转到上料登录
                        mActivity.setTabSelection(0);
                    } else {
                        //未完成上料 todo

                    }
                    break;

                case PROGRAM_UPDATE:
                    String titleMsg[] = new String[]{"站位表更新!", "仓库未完成发料!"};
                    int msgStyle[] = new int[]{22, Color.argb(255, 219, 201, 36)};
                    showInfo(titleMsg, msgStyle, false, 2);
                    break;

                case STORE_ALL_FAIL:
                    String titleMsgStr[] = new String[]{"提示", "仓库未完成发料!"};
                    int msgStyles[] = new int[]{22, Color.argb(255, 219, 201, 36)};
                    showInfo(titleMsgStr, msgStyles, false, 2);
                    break;

                case RESET://重置
                    clearFeedDisplay();
                    clearLineSeatMaterialScan();
                    break;

                case OPERATING://操作
                    beginOperat((String) msg.obj);
                    break;

                case RESET_SHOUWTIP://重置并提示
                    String titleMsgs[] = new String[]{"提示", "仓库未完成发料!"};
                    int msgStyleStr[] = new int[]{22, Color.argb(255, 219, 201, 36)};
                    showInfo(titleMsgs, msgStyleStr, false, 2);
                    clearFeedDisplay();
                    clearLineSeatMaterialScan();
                    break;

            }
        }
    };

    //判断所有上料结果
    private boolean getFeedResult(List<ProgramItemVisit> programItemVisits) {
        boolean feedResult = true;
        if (programItemVisits != null && programItemVisits.size() > 0) {
            for (ProgramItemVisit itemVisit : programItemVisits) {
                if (itemVisit.getFeed_result() != 1) {
                    feedResult = false;
                    break;
                }
            }
        }
        return feedResult;
    }

    //判断所有发料结果
    private boolean getWareResult(List<ProgramItemVisit> programItemVisits) {
        boolean wareResult = true;
        if (programItemVisits != null && programItemVisits.size() > 0) {
            for (ProgramItemVisit itemVisit : programItemVisits) {
                if (itemVisit.getStore_issue_result() != 1) {
                    wareResult = false;
                    break;
                }
            }
        }
        return wareResult;
    }

    //清空上料页面结果
    private void clearFeedDisplay() {
        sucFeedCount = 0;
        allCount = 0;
        for (int i = 0; i < lFeedMaterialItem.size(); i++) {
            MaterialItem materialItem = lFeedMaterialItem.get(i);
            materialItem.setScanLineSeat("");
            materialItem.setScanMaterial("");
            materialItem.setResult("");
            materialItem.setRemark("");
            if (Constants.isCache) {
                Feed feed = feedList.get(i);
                feed.setScanLineSeat("");
                feed.setScanMaterial("");
                feed.setResult("");
                feed.setRemark("");
            }

            allCount++;
        }
        materialAdapter.notifyDataSetChanged();
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * @author connie
     * @time 2017-9-22
     * @describe 创建视图
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        vFeedMaterialFragment = inflater.inflate(R.layout.feedmaterial_layout, container, false);
        //获取工单号和操作员
        Intent intent = getActivity().getIntent();
        savedInstanceState = intent.getExtras();

        globalData.setOperator(savedInstanceState.getString("operatorNum"));

        if (mActivity.updateDialog != null && mActivity.updateDialog.isShowing()) {
            mActivity.updateDialog.cancel();
            mActivity.updateDialog.dismiss();
        }
        getVisitsAndInitCache("", globalData.getLine(), globalData.getWork_order(), globalData.getBoard_type(), 2);

        initViews(savedInstanceState);
        initEvents();
        initData();//初始化数据

        return vFeedMaterialFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        //注册订阅
        EventBus.getDefault().register(this);
        mActivity = (FactoryLineActivity) getActivity();
        globalData = (GlobalData) getActivity().getApplication();
        globalFunc = new GlobalFunc(getActivity());

        if (Constants.isCache) {
            //查询本地数据库是否存在缓存
            List<Feed> feeds = new GreenDaoUtil().queryFeedRecord(globalData.getOperator(), globalData.getWork_order()
                    , globalData.getLine(), globalData.getBoard_type());

            //数据库存在缓存数据
            if (feeds.size() != 0) {
                //保存缓存
                feedList.addAll(feeds);
                isRestoreCache = true;
            } else {
                //不存在缓存数据,删除之前的数据
                boolean result = new GreenDaoUtil().deleteAllFeedData();
                Log.d(TAG, "deleteAllFeedData - " + result);
                isRestoreCache = false;
            }
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i(TAG, "onHiddenChanged - " + hidden);
        this.mHidden = hidden;
        if (!hidden) {
            if (mActivity.updateDialog != null && mActivity.updateDialog.isShowing()) {
                mActivity.updateDialog.cancel();
                mActivity.updateDialog.dismiss();
            }
            getVisitsAndInitCache("", globalData.getLine(), globalData.getWork_order(), globalData.getBoard_type(), 2);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(TAG, "onViewStateRestored");
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach");
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        //注销订阅
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化控件
     */
    private void initViews(Bundle bundle) {
        Log.i(TAG, "initViews");
        TextView tv_feed_order = (TextView) vFeedMaterialFragment.findViewById(R.id.tv_feed_order);
        edt_Operation = (TextView) vFeedMaterialFragment.findViewById(R.id.tv_feed_Operation);
        edt_LineSeat = (MyEditTextDel) vFeedMaterialFragment.findViewById(R.id.edt_feed_lineseat);
        edt_Material = (MyEditTextDel) vFeedMaterialFragment.findViewById(R.id.edt_feed_material);

        lv_FeedMaterial = (ListView) vFeedMaterialFragment.findViewById(R.id.feed_list_view);
        tv_feed_order.setText(bundle.getString("orderNum"));
        edt_Operation.setText(bundle.getString("operatorNum"));

        edt_LineSeat.requestFocus();
    }

    //监听订阅的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EvenBusTest event) {
        Log.d(TAG, "onEventMainThread - " + event.getUpdated());
        if (event.getUpdated() == 0) {
            if (Constants.isCache) {
                if (event.getFeedList() != null && event.getFeedList().size() > 0) {
                    //更新页面
                    feedList.clear();
                    feedList.addAll(event.getFeedList());
                    curFeedMaterialId = 0;
                    sucFeedCount = 0;
                    //填充数据
                    lFeedMaterialItem.clear();
                    for (Feed feed : feedList) {
                        MaterialItem materialItem = new MaterialItem(feed.getOrder(), feed.getBoard_type(), feed.getLine(),
                                feed.getSerialNo(), feed.getAlternative(), feed.getOrgLineSeat(), feed.getOrgMaterial(),
                                feed.getScanLineSeat(), feed.getScanMaterial(), feed.getResult(), feed.getRemark());
                        lFeedMaterialItem.add(materialItem);
                        //获取成功上料
                        if (feed.getResult().equalsIgnoreCase("PASS")) {
                            sucFeedCount++;
                        }
                    }
                    allCount = lFeedMaterialItem.size();
                    //更新显示
                    materialAdapter.notifyDataSetChanged();
                    //重新开始扫描站位
                    clearLineSeatMaterialScan();
                }
                Log.d(TAG, "mHidden - " + mHidden);
                if (!mHidden) {
                    //更新站位表后是否发料完成
                    if (mActivity.updateDialog != null && mActivity.updateDialog.isShowing()) {
                        mActivity.updateDialog.cancel();
                        mActivity.updateDialog.dismiss();
                    }
                    getVisitsAndInitCache("", globalData.getLine(), globalData.getWork_order(), globalData.getBoard_type(), 1);
                }
            }

        }
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化事件
     */
    private void initEvents() {
        Log.i(TAG, "initEvents");
        edt_LineSeat.setOnEditorActionListener(this);
        edt_Material.setOnEditorActionListener(this);
        edt_Material.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(edt_LineSeat.getText().toString().trim())) {
                        edt_Material.setCursorVisible(false);
                        edt_LineSeat.setText("");
                        edt_LineSeat.requestFocus();
                    } else {
                        edt_Material.setCursorVisible(true);
                    }
                }
            }
        });

        //解决站位失去焦点问题
        lv_FeedMaterial.setFocusable(false);
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化数据
     */
    private void initData() {
        Log.i(TAG, "initData");
        allFeedResult = false;
        curFeedMaterialId = 0;
        sucFeedCount = 0;
        //填充数据
        lFeedMaterialItem.clear();

        //没有缓存
        if (!isRestoreCache) {
            List<MaterialItem> materialItems = globalData.getMaterialItems();
            for (MaterialItem materialItem : materialItems) {
                MaterialItem feedMaterialItem = new MaterialItem(materialItem.getOrder(), materialItem.getBoardType(),
                        materialItem.getLine(), materialItem.getSerialNo(), materialItem.getAlternative(), materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(),
                        "", "", "", "");
                lFeedMaterialItem.add(feedMaterialItem);

                if (Constants.isCache) {
                    //保存缓存到数据库中
                    Feed feed = new Feed(null, globalData.getWork_order(), globalData.getOperator(), globalData.getBoard_type(),
                            globalData.getLine(), materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(),
                            "", "", "", "", materialItem.getSerialNo(), materialItem.getAlternative());
                    feedList.add(feed);
                }
            }
            //保存到数据库中
            if (Constants.isCache) {
                boolean cacheResult = new GreenDaoUtil().insertMultiFeedMaterial(feedList);
                Log.d(TAG, "cacheResult - " + cacheResult);
            }
        }
        //存在缓存
        else {
            for (Feed feed : feedList) {
                MaterialItem materialItem = new MaterialItem(feed.getOrder(), feed.getBoard_type(), feed.getLine(),
                        feed.getSerialNo(), feed.getAlternative(), feed.getOrgLineSeat(), feed.getOrgMaterial(),
                        feed.getScanLineSeat(), feed.getScanMaterial(), feed.getResult(), feed.getRemark());
                lFeedMaterialItem.add(materialItem);
                //获取成功上料
                if (feed.getResult().equalsIgnoreCase("PASS")) {
                    sucFeedCount++;
                }
            }

            //todo 需要更新全局变量为本地数据库的
            globalData.setMaterialItems(lFeedMaterialItem);
        }

        allCount = lFeedMaterialItem.size();
        //设置Adapter
        materialAdapter = new MaterialAdapter(this.getActivity(), lFeedMaterialItem);
        lv_FeedMaterial.setAdapter(materialAdapter);

        matchFeedMaterialId = -1;
    }


    /**
     * @param textView 　触发的控件
     * @param i
     * @param keyEvent 　key事件
     * @return
     */
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        Log.i(TAG, "onEditorAction");
        //回车键
        if (i == EditorInfo.IME_ACTION_SEND ||
                (keyEvent != null && keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER)) {
            Log.i(TAG, "keyEvent:" + keyEvent.getAction());
            switch (keyEvent.getAction()) {
                //抬上
                case KeyEvent.ACTION_UP:
                    //先判断是否联网
                    if (globalFunc.isNetWorkConnected()) {
                        Log.d(TAG, "matchFeedMaterialId-" + matchFeedMaterialId);
                        //扫描内容
                        String scanValue = String.valueOf(((EditText) textView).getText());
                        scanValue = scanValue.replaceAll("\r", "");
                        Log.i(TAG, "scan Value:" + scanValue);
                        textView.setText(scanValue);

                        switch (textView.getId()) {
                            case R.id.edt_feed_lineseat:
                                matchFeedMaterialId = -1;
                                //站位
                                scanValue = globalFunc.getLineSeat(scanValue);
                                edt_LineSeat.setText(scanValue);
                                getVisitsAndInitCache(scanValue, globalData.getLine(), globalData.getWork_order(), globalData.getBoard_type(), 3);
                                /*
                                Log.i(TAG, "lineseat:" + scanValue);
                                //相同站位在列表中的位置
                                for (int j = 0; j < lFeedMaterialItem.size(); j++) {
                                    MaterialItem materialItem = lFeedMaterialItem.get(j);
                                    //扫到的站位在表中存在
                                    if (materialItem.getOrgLineSeat().equalsIgnoreCase(scanValue)) {
                                        scanLineIndex.add(j);
                                        matchFeedMaterialId = j;
                                        materialItem.setScanLineSeat(scanValue);
                                    }
                                }
                                //无匹配的站位
                                if (matchFeedMaterialId < 0) {
                                    //重新上料
                                    feedNextMaterial();
                                    return true;
                                } else {
                                    //检验该站位是否发料
                                    boolean storeIssue = getOperateLastType(lFeedMaterialItem.get(matchFeedMaterialId), scanLineIndex);
                                    Log.d(TAG, "storeIssue--" + storeIssue);
                                }
                                */

                                break;

                            case R.id.edt_feed_material:
                                //站位存在且已发料才会进入这里
                                scanValue = globalFunc.getMaterial(scanValue);
                                textView.setText(scanValue);
                                //站位不存在
                                if (matchFeedMaterialId < 0) {
                                    feedNextMaterial();
                                    return true;
                                }
                                //比对料号，包含替代料
                                matchFeedMaterialId = -1;

                                //只有一个料
                                if (scanLineIndex.size() == 1) {
                                    MaterialItem singleMaterialItem = lFeedMaterialItem.get(scanLineIndex.get(0));
                                    singleMaterialItem.setScanMaterial(scanValue);
                                    if (singleMaterialItem.getOrgMaterial().equalsIgnoreCase(scanValue)) {
                                        singleMaterialItem.setResultRemark("PASS", "上料成功");
                                        //成功次数加1
                                        sucFeedCount++;
                                    } else {
                                        singleMaterialItem.setResultRemark("FAIL", "料号与站位不相符");
                                    }
                                    //保存缓存
                                    cacheFeedResult(scanLineIndex.get(0), singleMaterialItem);
                                    //添加显示日志
                                    globalData.setUpdateType(Constants.FEEDMATERIAL);
                                    globalFunc.updateVisitLog(globalData, singleMaterialItem);
                                    //当前上料索引
                                    matchFeedMaterialId = scanLineIndex.get(0);
                                }
                                //存在主替料
                                else {
                                    checkMultiItem(scanLineIndex, scanValue);
                                }

                                if (matchFeedMaterialId < 0) {
                                    feedNextMaterial();
                                    return true;
                                }

                                //更新数据显示
                                materialAdapter.notifyDataSetChanged();
                                //增加数据库日志
                                globalData.setOperType(Constants.FEEDMATERIAL);
                                globalFunc.AddDBLog(globalData, lFeedMaterialItem.get(matchFeedMaterialId));
                                //上下一个料
                                feedNextMaterial();
                                break;
                        }
                    } else {
                        globalFunc.showInfo("警告", "请检查网络连接是否正常!", "请连接网络!");
                        clearLineSeatMaterialScan();
                        matchFeedMaterialId = -1;
                    }

                    return true;
                default:
                    return true;
            }
        }
        return false;
    }

    private boolean beginOperat(String scanValue) {
        Log.i(TAG, "lineseat:" + scanValue);
        //相同站位在列表中的位置
        for (int j = 0; j < lFeedMaterialItem.size(); j++) {
            MaterialItem materialItem = lFeedMaterialItem.get(j);
            //扫到的站位在表中存在
            if (materialItem.getOrgLineSeat().equalsIgnoreCase(scanValue)) {
                scanLineIndex.add(j);
                matchFeedMaterialId = j;
                materialItem.setScanLineSeat(scanValue);
            }
        }
        //无匹配的站位
        if (matchFeedMaterialId < 0) {
            //重新上料
            feedNextMaterial();
            return true;
        } else {
            //检验该站位是否发料
            boolean storeIssue = getOperateLastType(lFeedMaterialItem.get(matchFeedMaterialId), scanLineIndex);
            Log.d(TAG, "storeIssue--" + storeIssue);
        }
        return false;
    }

    //获取ProgramItemVisits同时清空缓存的上料结果
    private void getVisitsAndInitCache(String scanValue, final String line, final String order, final int boardType, int condition) {
        Log.d(TAG, "getVisitsAndInitCache - " + condition);
        loadingDialog = new LoadingDialog(getActivity(), "正在加载...");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.obj = scanValue;
                //检测是否重置
                int checkReset = DBService.getDbService().isProgramVisitReseted(globalData.getLine(), globalData.getWork_order(),
                        globalData.getBoard_type(), Constants.FEEDMATERIAL);
                //本地数据
                boolean reseted = true;
                for (MaterialItem materialItem : lFeedMaterialItem) {
                    if (materialItem.getResult().equalsIgnoreCase("PASS") || materialItem.getResult().equalsIgnoreCase("FAIL")) {
                        reseted = false;
                    }
                }
                Log.d(TAG, "getVisitsAndInitCache - condition - " + condition);
                Log.d(TAG, "getVisitsAndInitCache - checkReset - " + checkReset);
                Log.d(TAG, "getVisitsAndInitCache - reseted - " + reseted);
                if (condition == 3) {//扫站位时
                    if (checkReset == 1) {
                        if (!reseted) {
                            //重置
                            message.what = RESET;
                        } else {
                            //操作
                            message.what = OPERATING;
                        }
                    } else if (checkReset == 0) {
                        //操作
                        message.what = OPERATING;
                    }
                } else {
                    programItemVisits = new DBService().getProgramItemVisits(line, order, boardType);
                    boolean wareResult = getWareResult(programItemVisits);
                    if (condition == 0) {
                        if (Constants.isCache) {
                            boolean result = new GreenDaoUtil().updateAllFeed(feedList);
                            Log.d(TAG, "updateAllFeed - " + result);
                        }
                        message.what = REFRESH_ITEMS;
                    } else if (condition == 1) {
                        if (!wareResult) {
                            message.what = PROGRAM_UPDATE;
                        }
                    } else if (condition == 2) {
                        if (!wareResult) {
                            message.what = STORE_ALL_FAIL;
                            if (checkReset == 1 && !reseted) {
                                message.what = RESET_SHOUWTIP;
                            }
                        } else {
                            if (checkReset == 1 && !reseted) {
                                message.what = RESET;
                            }
                        }
                    }
                }
                feedHandler.sendMessage(message);
            }
        }).start();
    }

    //检验该站位是否发料
    private boolean getOperateLastType(final MaterialItem materialItem, final ArrayList<Integer> sameLineIndex) {
        final boolean storeIssue[] = {false};
        //判断工位检测功能是否打开
        if (Constants.isCheckWorkType) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //检验该站位是否发料(主替料即多个相同站位)
                    List<Integer> storeResult = new DBService().getStoreResult(materialItem);
                    for (Integer result : storeResult) {
                        //不为空
                        if (result == 1) {
                            storeIssue[0] = true;
                        }
                        Log.d(TAG, "storeIssue[0]--" + storeIssue[0]);
                        Log.d(TAG, "result--" + result);
                    }
                    Message message = Message.obtain();
                    if (storeIssue[0]) {
                        message.what = STORE_SUCCESS;
                    } else {
                        message.what = STORE_FAIL;
                    }
                    feedHandler.sendMessage(message);
                }
            }).start();
        } else {
            //更新显示
            materialAdapter.notifyDataSetChanged();
            edt_Material.requestFocus();
        }

        return storeIssue[0];
    }

    private boolean checkMultiItem(ArrayList<Integer> integers, String mScanValue) {
        boolean result = true;
        //多个相同的站位,即有替换料
        for (int z = 0; z < integers.size(); z++) {
            MaterialItem multiMaterialItem = lFeedMaterialItem.get(integers.get(z));
            int serialNo;
            byte alternative;
            multiMaterialItem.setScanMaterial(mScanValue);
            if (multiMaterialItem.getOrgMaterial().equalsIgnoreCase(multiMaterialItem.getScanMaterial())) {
                serialNo = multiMaterialItem.getSerialNo();
                alternative = multiMaterialItem.getAlternative();
                Log.d(TAG, "alternative - " + alternative);
                for (int x = 0; x < integers.size(); x++) {
                    MaterialItem innerMaterialItem = lFeedMaterialItem.get(integers.get(x));
                    innerMaterialItem.setScanMaterial(mScanValue);
                    innerMaterialItem.setResult("PASS");
                    if (x == z) {
                        //扫描的是该料号
                        matchFeedMaterialId = integers.get(x);
                        innerMaterialItem.setRemark("上料成功");
                    } else {
                        //扫描的不是该料号
                        if (alternative == 1) {
                            //上的料是替换料
                            innerMaterialItem.setRemark("替料" + serialNo + "上料成功");
                        } else {
                            //上的料是主料
                            innerMaterialItem.setRemark("主料" + serialNo + "上料成功");
                        }
                    }

                    //保存缓存
                    cacheFeedResult(integers.get(x), innerMaterialItem);

                    //成功次数加1
                    sucFeedCount++;
                    //添加显示日志
                    globalData.setUpdateType(Constants.FEEDMATERIAL);
                    globalFunc.updateVisitLog(globalData, innerMaterialItem);
                }
                result = true;
                //跳出循环
                return true;
            } else {
                multiMaterialItem.setResult("FAIL");
                multiMaterialItem.setRemark("料号与站位不相符");
                //都不相符,默认操作的是第一个
                matchFeedMaterialId = integers.get(0);
                //保存缓存
                cacheFeedResult(integers.get(z), multiMaterialItem);
                //添加显示日志
                globalData.setUpdateType(Constants.FEEDMATERIAL);
                globalFunc.updateVisitLog(globalData, multiMaterialItem);
                result = false;
            }
        }

        return result;
    }

    //更新上料缓存
    private void cacheFeedResult(int index, MaterialItem materialItem) {
        if (Constants.isCache) {
            //保存缓存
            Feed feed = feedList.get(index);
            feed.setScanLineSeat(materialItem.getScanLineSeat());
            feed.setScanMaterial(materialItem.getScanMaterial());
            feed.setResult(materialItem.getResult());
            feed.setRemark(materialItem.getRemark());
            new GreenDaoUtil().updateFeed(feed);
        }
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 上下一个料
     */
    private void feedNextMaterial() {
        //清空站位位置表
        scanLineIndex.clear();
        Log.i(TAG, "feedNextMaterial:" + matchFeedMaterialId);
        //显示最新的上料结果
        if (matchFeedMaterialId >= 0) {
            lv_FeedMaterial.setSelection(matchFeedMaterialId);
            matchFeedMaterialId = -1;
        } else {
            //排位表不存在此站位或料号
            curFeedMaterialId--;
            //弹出提示框
            String titleMsg[] = new String[]{"提示", "排位表不存在此站位或料号!!!"};
            int msgStyle[] = new int[]{22, Color.RED};
            showInfo(titleMsg, msgStyle, false, 0);
        }

        Log.d(TAG, "sucFeedCount-" + sucFeedCount
                + "\nlFeedMaterialItem-" + lFeedMaterialItem.size() + "\nallCount-" + allCount);
        if (sucFeedCount < lFeedMaterialItem.size() || sucFeedCount < allCount) {
            clearLineSeatMaterialScan();
        } else {
            //上料结束,显示结果
            showFeedMaterialResult();
        }
    }

    //显示上料结果
    private void showFeedMaterialResult() {
        //默认上料结果是PASS
        boolean feedResult = true;
        for (MaterialItem feedMaterialItem : lFeedMaterialItem) {
            if (!feedMaterialItem.getResult().equalsIgnoreCase("PASS")) {
                feedResult = false;
                break;
            }
        }
        //弹出上料结果
        String titleMsg[];
        int msgStyle[];
        if (feedResult) {
            titleMsg = new String[]{"上料结果", "PASS"};
            msgStyle = new int[]{66, Color.argb(255, 102, 153, 0)};
        } else {
            titleMsg = new String[]{"上料失败，请检查!", "FAIL"};
            msgStyle = new int[]{66, Color.RED};
        }
        showInfo(titleMsg, msgStyle, feedResult, 1);
    }

    //弹出消息窗口
    private boolean showInfo(String[] titleMsg, int[] msgStyle, final boolean result, final int resultType) {
        /*
        if (loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.cancel();
            loadingDialog.dismiss();
        }
        */
        //对话框所有控件id
        int itemResIds[] = new int[]{R.id.dialog_title_view,
                R.id.dialog_title, R.id.tv_alert_info, R.id.info_trust};

        InfoDialog infoDialog = new InfoDialog(getActivity(),
                R.layout.info_dialog_layout, itemResIds, titleMsg, msgStyle);

        infoDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.info_trust:
                        dialog.dismiss();
                        clearLineSeatMaterialScan();
                        if (result) {
                            //获取ProgramItemVisits同时清空缓存的上料结果
                            getVisitsAndInitCache("", globalData.getLine(), globalData.getWork_order(), globalData.getBoard_type(), 0);
                        } else {
                            if (resultType == 1) {
                                //将未成功数加到
                                for (MaterialItem feedMaterialItem : lFeedMaterialItem) {
                                    if (!feedMaterialItem.getResult().equalsIgnoreCase("PASS")) {
                                        allCount++;
                                    }
                                }
                            } else if (resultType == 2) {
                                //未发料
                            }
                        }
                        break;
                }
            }
        });
        infoDialog.show();

        return true;
    }

    //清除扫描数据
    private void clearLineSeatMaterialScan() {
        edt_LineSeat.setText("");
        edt_Material.setText("");
        edt_LineSeat.requestFocus();
    }


}
