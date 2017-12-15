package com.jimi.smt.eps_appclient;

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

import com.jimi.smt.eps_appclient.Adapter.MaterialAdapter;
import com.jimi.smt.eps_appclient.Func.AlarmUtil;
import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Views.InfoDialog;

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
    private final String TAG = this.getClass().getSimpleName();

    //全局变量
    GlobalData globalData;

    //上料视图
    private View vFeedMaterialFragment;
    //操作员　站位　料号
    private TextView edt_Operation;
    private EditText edt_LineSeat;
    private EditText edt_Material;

    //上料列表
    private ListView lv_FeedMaterial;
    private MaterialAdapter materialAdapter;

    //当前上料时用到的排位料号表
    private List<MaterialItem> lFeedMaterialItem = new ArrayList<MaterialItem>();
    //扫描的站位在列表中的位置
    private ArrayList<Integer> scanLineIndex = new ArrayList<Integer>();

    //当前上料项
    private int curFeedMaterialId = 0;
    //成功上料数
    private int sucFeedCount = 0;
    //料号表的总数
    private int allCount = 0;
    //匹配的站位表项
    private int matchFeedMaterialId = -1;
    //上料结果
    private boolean feedResult = true;
    private GlobalFunc globalFunc;

    private static final int STORE_SUCCESS = 108;
    private static final int STORE_FAIL = 109;
    private Handler feedHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case STORE_SUCCESS:
                    //更新显示
                    materialAdapter.notifyDataSetChanged();
                    edt_Material.requestFocus();
                    break;
                case STORE_FAIL:
                    ArrayList<Integer> sameLineIndex = (ArrayList<Integer>) msg.obj;
                    //未发料
                    for (int  a = 0;a < scanLineIndex.size();a++){
                        lFeedMaterialItem.get(scanLineIndex.get(a))
                                .setResultRemark("WARN","该料号未发料");
                    }
                    //更新显示
                    materialAdapter.notifyDataSetChanged();
                    feedNextMaterial();
                    break;
            }
        }
    };
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
        Intent intent=getActivity().getIntent();
        savedInstanceState=intent.getExtras();
        Log.i(TAG,"curOderNum::"+savedInstanceState.getString("orderNum")+" -- curOperatorNUm::"+savedInstanceState.getString("operatorNum"));

        globalData = (GlobalData) getActivity().getApplication();
        globalData.setOperator(savedInstanceState.getString("operatorNum"));
        //设置报警状态为未报警
        globalData.setAlarmState(1);
//        globalData.setOperType(Constants.FEEDMATERIAL);
        globalFunc = new GlobalFunc(getActivity());

        initViews(savedInstanceState);
        initEvents();
        initData();//初始化数据

//        edt_LineSeat.requestFocus();
        return vFeedMaterialFragment;

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
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }


    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化控件
     */
    private void initViews(Bundle bundle) {
        Log.i(TAG, "initViews");
        TextView tv_feed_order= (TextView) vFeedMaterialFragment.findViewById(R.id.tv_feed_order);
        edt_Operation = (TextView) vFeedMaterialFragment.findViewById(R.id.tv_feed_Operation);
        edt_LineSeat = (EditText) vFeedMaterialFragment.findViewById(R.id.edt_feed_lineseat);
        edt_Material = (EditText) vFeedMaterialFragment.findViewById(R.id.edt_feed_material);

        lv_FeedMaterial = (ListView) vFeedMaterialFragment.findViewById(R.id.feed_list_view);
        tv_feed_order.setText(bundle.getString("orderNum"));
        edt_Operation.setText(bundle.getString("operatorNum"));

        edt_LineSeat.requestFocus();
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
                if (hasFocus){
                    if (TextUtils.isEmpty(edt_LineSeat.getText().toString().trim())){
                        edt_Material.setCursorVisible(false);
                        edt_LineSeat.setText("");
                        edt_LineSeat.requestFocus();
                    }else {
                        edt_Material.setCursorVisible(true);
                    }
                }
            }
        });

        edt_LineSeat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG,"edt_LineSeat-onFocusChange-"+hasFocus);
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
        curFeedMaterialId=0;
        sucFeedCount = 0;
        //填充数据
        lFeedMaterialItem.clear();
        List<MaterialItem> materialItems = globalData.getMaterialItems();
        for (MaterialItem materialItem : materialItems) {
            MaterialItem feedMaterialItem = new MaterialItem(materialItem.getFileId(),materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(), "", "", "", "");
            lFeedMaterialItem.add(feedMaterialItem);
        }
        /*
        //测试用
        for (int i=0;i < 10;i++){
            MaterialItem feedMaterialItem = new MaterialItem(materialItems.get(i).getFileId(),
            materialItems.get(i).getOrgLineSeat(), materialItems.get(i).getOrgMaterial(), "", "", "", "");
            lFeedMaterialItem.add(feedMaterialItem);
        }
        */
        allCount = lFeedMaterialItem.size();
        //设置Adapter
        materialAdapter = new MaterialAdapter(this.getActivity(), lFeedMaterialItem);
        lv_FeedMaterial.setAdapter(materialAdapter);

        matchFeedMaterialId=-1;
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
                    if (globalFunc.isNetWorkConnected()){
                        Log.d(TAG,"matchFeedMaterialId-"+matchFeedMaterialId);
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
                                if (matchFeedMaterialId < 0){
                                    //报警
                                    new AlarmUtil(globalData).turnOnAlarm(Constants.alarmIp,0);
                                    //重新上料
                                    feedNextMaterial();
                                    return true;
                                } else{
                                    /*
                                    //检验该站位是否发料(主替料即多个相同站位)
                                    boolean storeIssue = false;
                                    List<Integer> operateType = new DBService().getLastOperateType(lFeedMaterialItem.get(matchFeedMaterialId));
                                    Log.d(TAG,"programId-"+lFeedMaterialItem.get(matchFeedMaterialId).getFileId());
                                    Log.d(TAG,"lineseat-"+lFeedMaterialItem.get(matchFeedMaterialId).getOrgLineSeat());
                                    Log.d(TAG,"operateType-size-"+operateType.size());
                                    for (Integer type:operateType) {
                                        if (type == Constants.STORE_ISSUE){
                                            storeIssue = true;
                                        }
                                        Log.d(TAG,"operateType--"+operateType);
                                    }
                                    //已发料
                                    if (storeIssue){
                                        //更新显示
                                        materialAdapter.notifyDataSetChanged();
                                        edt_Material.requestFocus();
                                    }else {
                                        //未发料
                                        for (int  a = 0;a < sameLineIndex.size();a++){
                                            lFeedMaterialItem.get(sameLineIndex.get(a))
                                                    .setResultRemark("WARN","该站位的料号未发料");
                                        }
                                        //更新显示
                                        materialAdapter.notifyDataSetChanged();
                                        feedNextMaterial();
                                        return true;
                                    }
                                    */
                                    //关闭报警
                                    new AlarmUtil(globalData).turnOffAlarm(Constants.alarmIp,1);
                                    //检验该站位是否发料
                                    boolean storeIssue = getOperateLastType(lFeedMaterialItem.get(matchFeedMaterialId),scanLineIndex);
                                    Log.d(TAG,"storeIssue--"+storeIssue);
                                }
                                break;

                            case R.id.edt_feed_material:
                                //站位存在且已发料才会进入这里
                                scanValue = globalFunc.getMaterial(scanValue);
                                textView.setText(scanValue);
                                //站位不存在
                                if (matchFeedMaterialId < 0){
                                    feedNextMaterial();
                                    return true;
                                }
                                //扫料号后的结果
                                boolean materialResult;
                                //比对料号，包含替代料
                                matchFeedMaterialId = -1;

                                if (scanLineIndex.size() == 1){
                                    MaterialItem singleMaterialItem=lFeedMaterialItem.get(scanLineIndex.get(0));
                                    singleMaterialItem.setScanMaterial(scanValue);
                                    if (singleMaterialItem.getOrgMaterial().equalsIgnoreCase(scanValue)){
                                        singleMaterialItem.setResultRemark("PASS","上料成功");
                                        //成功次数加1
                                        sucFeedCount++;
                                        materialResult = true;
                                    }else {
                                        singleMaterialItem.setResultRemark("FAIL","料号与站位不相符");
                                        materialResult = false;
                                    }
                                    //添加显示日志
                                    globalData.setUpdateType(Constants.FEEDMATERIAL);
                                    globalFunc.updateVisitLog(globalData,singleMaterialItem);
                                    //当前上料索引
                                    matchFeedMaterialId = scanLineIndex.get(0);
                                }else {
                                    materialResult = checkMultiItem(scanLineIndex,scanValue);
                                }

                                if (matchFeedMaterialId < 0){
                                    feedNextMaterial();
                                    return true;
                                }

                                if (materialResult){
                                    //关闭报警
                                    new AlarmUtil(globalData).turnOffAlarm(Constants.alarmIp,1);
                                }else {
                                    //报警
                                    new AlarmUtil(globalData).turnOnAlarm(Constants.alarmIp,0);
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
                    }else {
                        globalFunc.showInfo("警告","请检查网络连接是否正常!","请连接网络!");
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

    //检验该站位是否发料
    private boolean getOperateLastType(final MaterialItem materialItem, final ArrayList<Integer> sameLineIndex){
        final boolean[] storeIssue = {false};
        //判断工位检测功能是否打开
        if (Constants.isCheckWorkType){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //检验该站位是否发料(主替料即多个相同站位)
                    List<Byte> storeResult = new DBService().getStoreResult(materialItem);
                    Log.d(TAG,"programId-"+materialItem.getFileId());
                    Log.d(TAG,"lineseat-"+materialItem.getOrgLineSeat());
                    Log.d(TAG,"operateType-size-"+storeResult.size());
                    for (Byte result:storeResult) {
                        //不为空
                        if (result == 1){
                            storeIssue[0] = true;
                        }
                        Log.d(TAG,"storeIssue[0]--"+storeIssue[0]);
                        Log.d(TAG,"result--"+result);
                    }
                    Message message = Message.obtain();
                    if (storeIssue[0]){
                        message.what = STORE_SUCCESS;
                    }else {
                        message.what = STORE_FAIL;
                    }
                    feedHandler.sendMessage(message);
                }
            }).start();
        }else {
            //更新显示
            materialAdapter.notifyDataSetChanged();
            edt_Material.requestFocus();
        }

        return storeIssue[0];
    }

    private boolean checkMultiItem(ArrayList<Integer> integers,String mScanValue){
        boolean result = true;
        //多个相同的站位,即有替换料
        for (int z = 0;z < integers.size();z++){
            MaterialItem multiMaterialItem = lFeedMaterialItem.get(integers.get(z));
            multiMaterialItem.setScanMaterial(mScanValue);
            if (multiMaterialItem.getOrgMaterial().equalsIgnoreCase(multiMaterialItem.getScanMaterial())){
                for (int x = 0;x < integers.size();x++){
                    MaterialItem innerMaterialItem = lFeedMaterialItem.get(integers.get(x));
                    innerMaterialItem.setScanMaterial(mScanValue);
                    if (x == z){
                        innerMaterialItem.setResult("PASS");
                        innerMaterialItem.setRemark("主替有一项成功");
                        matchFeedMaterialId = integers.get(x);
                    }else {
                        innerMaterialItem.setResult("PASS");
                        innerMaterialItem.setRemark("主替有一项成功");
                    }
                    //成功次数加1
                    sucFeedCount++;
                    //添加显示日志
                    globalData.setUpdateType(Constants.FEEDMATERIAL);
                    globalFunc.updateVisitLog(globalData,innerMaterialItem);
                }
                result = true;
                //跳出循环
                return true;
            }else {
                multiMaterialItem.setResult("FAIL");
                multiMaterialItem.setRemark("料号与站位不相符");
                //都不相符,默认操作的是第一个
                matchFeedMaterialId = integers.get(0);
                //添加显示日志
                globalData.setUpdateType(Constants.FEEDMATERIAL);
                globalFunc.updateVisitLog(globalData,multiMaterialItem);
                result = false;
            }
        }

        return result;
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
        } else{
            //排位表不存在此站位或料号
            curFeedMaterialId--;
            //弹出提示框
            String titleMsg[]=new String[]{"提示","排位表不存在此站位或料号!!!"};
            int msgStyle[]=new int[]{22,Color.RED};
            showInfo(titleMsg,msgStyle,false,0);
        }

        Log.d(TAG,"sucFeedCount-"+sucFeedCount
                +"\nlFeedMaterialItem-"+lFeedMaterialItem.size()+"\nallCount-"+allCount);
        if (sucFeedCount < lFeedMaterialItem.size() || sucFeedCount < allCount){
            clearLineSeatMaterialScan();
        }else {
            //上料结束,显示结果
            showFeedMaterialResult();
        }
    }

    //显示上料结果
    private void showFeedMaterialResult() {
        //默认上料结果是PASS
        feedResult = true;
        for (MaterialItem feedMaterialItem : lFeedMaterialItem) {
            if (!feedMaterialItem.getResult().equalsIgnoreCase("PASS")) {
                feedResult = false;
                break;
            }
        }
        //弹出上料结果
        String titleMsg[];
        int msgStyle[];
        if (feedResult){
            titleMsg=new String[]{"上料结果","PASS"};
            msgStyle=new int[]{66,Color.argb(255,102,153,0)};
        }else {
            titleMsg=new String[]{"上料失败，请检查!","FAIL"};
            msgStyle=new int[]{66,Color.RED};
        }
        showInfo(titleMsg,msgStyle,feedResult,1);
    }

    //弹出消息窗口
    private boolean showInfo(String[] titleMsg, int[] msgStyle, final boolean result, final int resultType){
        //对话框所有控件id
        int itemResIds[]=new int[]{R.id.dialog_title_view,
                R.id.dialog_title,R.id.tv_alert_info,R.id.info_trust};

        InfoDialog infoDialog=new InfoDialog(getActivity(),
                R.layout.info_dialog_layout,itemResIds,titleMsg,msgStyle);

        infoDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()){
                    case R.id.info_trust:
                        if (result){
                            dialog.dismiss();
                            clearLineSeatMaterialScan();
                            initData();
                        }else{
                            dialog.dismiss();
                            clearLineSeatMaterialScan();
                            if (resultType == 1){
                                //将未成功数加到
                                for (MaterialItem feedMaterialItem : lFeedMaterialItem) {
                                    if (!feedMaterialItem.getResult().equalsIgnoreCase("PASS")) {
                                        allCount++;
                                    }
                                }
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

    //监听菜单按钮事件
    private View.OnKeyListener menuListener =new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP){
                if (keyCode == KeyEvent.KEYCODE_MENU){//菜单按键
                    Log.d(TAG,"按下按钮了！！！哈哈哈哈哈哈哈哈......");
                    showFeedMaterialResult();
                }
            }
            return true;
        }
    };


}
