package com.jimi.smt.eps_appclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.Adapter.MaterialAdapter;
import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Views.InfoDialog;
import com.jimi.smt.eps_appclient.Views.InputDialog;
import com.jimi.smt.eps_appclient.Views.LoadingDialog;

import java.util.ArrayList;
import java.util.List;


public class CheckAllMaterialFragment extends Fragment implements TextView.OnEditorActionListener {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    private LoadingDialog loadingDialog;
    //全局变量
    private GlobalData globalData;
    //上料视图
    private View vCheckAllMaterialFragment;
    //操作员　站位　料号
    private TextView edt_Operation;
    private EditText edt_ScanMaterial;
    //上料列表
    private ListView lv_CheckAllMaterial;
    private MaterialAdapter materialAdapter;
    //当前检料时用到的排位料号表
    private List<MaterialItem> lCheckAllMaterialItem = new ArrayList<MaterialItem>();
    //当前检料项
    private int curCheckId = 0;
    private android.app.AlertDialog dialog = null;
    //长按时选择的行
    private int selectRow=-1;
    private GlobalFunc globalFunc;
    //用户类型
    private int user_type;
    private boolean first_checkAll_result;
    private boolean feed_result;
    private static final int FEED_TRUE = 103;//上对
    private static final int FEED_FALSE = 104;//上错
    private static final int FIRST_CHECKALL_TRUE = 105;//首对
    private static final int FIRST_CHECKALL_FALSE = 106;//首错
    private int checkType;//0 首次访问数据库 ; 1 非首次访问数据库
    private Handler checkAllHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //取消弹出窗
            if (loadingDialog != null && loadingDialog.isShowing()){
                loadingDialog.cancel();
                loadingDialog.dismiss();
            }
            switch (msg.what){
                case FEED_TRUE://上对
                    setFeed_result(true);
                    getFirstCheckAllResult(lCheckAllMaterialItem.get(0).getFileId());
                    break;
                case FEED_FALSE://上错
                    setFeed_result(false);
                    edt_ScanMaterial.setText("");
                    if (checkType == 0){
                        if (user_type == 2){
                            showInfo("提示","操作员未完成上料!","将进行首次全检！",2);
                        }
                    }
                    break;
                case FIRST_CHECKALL_TRUE://首对
                    setFirst_checkAll_result(true);
                    break;
                case FIRST_CHECKALL_FALSE://首错
                    setFirst_checkAll_result(false);
                    if (user_type == 1){
                        edt_ScanMaterial.setText("");
                        if (checkType == 0){
                            showInfo("提示","IPQC未做首次全检","请联系管理员！",1);
                        }
                    }else if (user_type == 2){
                        showInfo("提示","将进行首次全检","将进行首次全检！",3);
                    }

                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        vCheckAllMaterialFragment = inflater.inflate(R.layout.checkallmaterial_layout, container, false);

        context=EPS_MainActivity.context;
        //获取工单号和操作员
        Intent intent=getActivity().getIntent();
        savedInstanceState=intent.getExtras();
        Log.i(TAG,"curOderNum::"+savedInstanceState.getString("orderNum")+" -- curOperatorNUm::"+savedInstanceState.getString("operatorNum"));

        globalData = (GlobalData) getActivity().getApplication();
//        globalData.setOperType(Constants.CHECKALLMATERIAL);
        globalData.setOperator(savedInstanceState.getString("operatorNum"));
        user_type = globalData.getUserType();
        Log.d(TAG,"用户类型UserType："+globalData.getUserType());
        globalFunc = new GlobalFunc(getActivity());

        initViews(savedInstanceState);
        initData();//初始化数据
        initEvents();
        return vCheckAllMaterialFragment;
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化控件
     */
    private void initViews(Bundle bundle) {
        Log.i(TAG, "initViews");
        TextView tv_checkAll_order= (TextView) vCheckAllMaterialFragment.findViewById(R.id.tv_checkAll_order);
        edt_Operation = (TextView) vCheckAllMaterialFragment.findViewById(R.id.tv_checkAll_Operation);
        edt_ScanMaterial = (EditText) vCheckAllMaterialFragment.findViewById(R.id.edt_material);
        lv_CheckAllMaterial = (ListView) vCheckAllMaterialFragment.findViewById(R.id.list_view);
        tv_checkAll_order.setText(bundle.getString("orderNum"));
        edt_Operation.setText(bundle.getString("operatorNum"));
        edt_ScanMaterial.requestFocus();
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化事件
     */
    private void initEvents() {
        Log.i(TAG, "initEvents");
        edt_ScanMaterial.setOnEditorActionListener(this);
        edt_ScanMaterial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Log.d(TAG,"globalData-OperType:"+globalData.getOperType());
                    Log.d(TAG,"用户类型UserType："+user_type);
                    //操作员1，ipqc 2
                    checkType = 0;
                    if (user_type == 1){
                        getFirstCheckAllResult(lCheckAllMaterialItem.get(0).getFileId());
                    }else if (user_type == 2){
                        getFeedResult(lCheckAllMaterialItem.get(0).getFileId());
                    }
                }
            }
        });
        //长按弹出框
        lv_CheckAllMaterial.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int row, long l) {
                //若未扫过不弹出框
                if (isFirstScaned()){
                    //弹出对话框
                    selectRow=row;
                    showLongClickDialog("请重新扫描新的料号");
                }
                return true;
            }
            });
    }

    //是否扫过了
    private boolean isFirstScaned(){
        boolean firstScaned = false;
        for (MaterialItem materialItem:lCheckAllMaterialItem) {
            if (!(materialItem.getResult().equalsIgnoreCase(""))){
                firstScaned = true;
                Log.d(TAG,"isFirstScaned::"+true);
                break;
            }
        }
        Log.d(TAG,"isFirstScaned::"+firstScaned);
        return firstScaned;
    }

    //弹出长按对话框
    private void showLongClickDialog(String title){
        InputDialog inputDialog=new InputDialog(getActivity(),
                R.layout.input_dialog_layout,new int[]{R.id.input_dialog_title,R.id.et_input},title);
        inputDialog.show();
        inputDialog.setOnDialogEditorActionListener(new InputDialog.OnDialogEditorActionListener() {
            @Override
            public boolean OnDialogEditorAction(InputDialog inputDialog,final TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null && event.getKeyCode() == event.KEYCODE_ENTER)) {

                    switch (event.getAction()) {
                        //按下
                        case KeyEvent.ACTION_UP:
                            //先判断是否联网
                            if (globalFunc.isNetWorkConnected()){
                                if ((user_type == 1) && (!isFirst_checkAll_result())){
                                    showInfo("提示","IPQC未做首次全检","请联系管理员！",1);
                                }
                                else if ((user_type == 2) && (!isFeed_result())){
                                    showInfo("提示","操作员未完成上料!","将进行首次全检！",2);
                                }
                                else {
                                    switch (v.getId()){
                                        case R.id.et_input:
                                            //扫描内容
                                            String scanValue = String.valueOf(((EditText) v).getText());
                                            scanValue = globalFunc.getMaterial(scanValue);
                                            v.setText(scanValue);
                                            MaterialItem checkAllMaterialItem = lCheckAllMaterialItem.get(selectRow);
                                            checkAllMaterialItem.setScanMaterial(scanValue);
                                            //调用全捡方法
                                            checkAllItems(checkAllMaterialItem,selectRow,scanValue);
                                            materialAdapter.notifyDataSetChanged();
                                            //增加数据库日志
                                            new GlobalFunc().AddDBLog(globalData, lCheckAllMaterialItem.get(selectRow));
                                            //判断全是否全部正确
                                            showCheckAllMaterialResult(1);
                                            inputDialog.dismiss();
                                            edt_ScanMaterial.requestFocus();
                                            break;
                                    }
                                }
                            }else {
                                globalFunc.showInfo("警告","请检查网络连接是否正常!","请连接网络!");
                                v.setText("");
                            }
                            return true;
                        default:
                            return true;
                    }
                }
                return false;
            }
        });
    }


    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化数据
     */
    private void initData() {
        Log.i(TAG, "initData");
        //填充数据
        curCheckId = 0;
        lCheckAllMaterialItem.clear();
        List<MaterialItem> materialItems = globalData.getMaterialItems();
        for (MaterialItem materialItem : materialItems) {
            MaterialItem feedMaterialItem;
            feedMaterialItem = new MaterialItem(materialItem.getFileId(),materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(), "", "", "", "");
            lCheckAllMaterialItem.add(feedMaterialItem);
        }
        /*
        //测试用
        for (int k=0;k < 10;k++){
            MaterialItem feedMaterialItem;
            feedMaterialItem = new MaterialItem(materialItems.get(k).getFileId(),materialItems.get(k).getOrgLineSeat(),
                    materialItems.get(k).getOrgMaterial(), "", "", "", "");
            lCheckAllMaterialItem.add(feedMaterialItem);
        }*/
        //设置Adapter
        materialAdapter = new MaterialAdapter(getActivity(), lCheckAllMaterialItem);
        lv_CheckAllMaterial.setAdapter(materialAdapter);
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
        Log.i("ACTION_DOWN::onEditorAction-",textView.getText());
        Log.i("ACTION_DOWN::onEditorAction-",i);
        //回车键
        if (i == EditorInfo.IME_ACTION_SEND ||
                (keyEvent != null && keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER)) {
            switch (keyEvent.getAction()) {
                //按下
                case KeyEvent.ACTION_UP:
                    //先判断是否联网
                    if (globalFunc.isNetWorkConnected()){
                        if ((user_type == 1) && (!isFirst_checkAll_result())){
                            showInfo("提示","IPQC未做首次全检","请联系管理员！",1);
                        }
                        else if ((user_type == 2) && (!isFeed_result())){
                            showInfo("提示","操作员未完成上料!","将进行首次全检！",2);
                        }
                        else {
                            //扫描内容
                            String scanValue = String.valueOf(((EditText) textView).getText());
                            scanValue = scanValue.replaceAll("\r", "");
                            Log.i(TAG, "scan Value:" + scanValue);
                            textView.setText(scanValue);

                            //将扫描的内容更新至列表中
                            MaterialItem checkAllMaterialItem = lCheckAllMaterialItem.get(curCheckId);

                            switch (textView.getId()) {
                                case R.id.edt_material:
                                    //料号,若为二维码则提取@@前的料号
                                    scanValue = globalFunc.getMaterial(scanValue);
                                    textView.setText(scanValue);
                                    //当前操作的站位
                                    String curCheckLineSeat = checkAllMaterialItem.getOrgLineSeat();
                                    //相同站位的索引数组
                                    ArrayList<Integer> sameLineSeatIndexs = new ArrayList<Integer>();
                                    //当前操作的位置
                                    int curOperateIndex = curCheckId;
                                    curCheckId=curCheckId-1;
                                    //向上遍历所有相同站位的位置
                                    for (int j = curOperateIndex-1; j >= 0; j--){
                                        if (lCheckAllMaterialItem.get(j).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                                            sameLineSeatIndexs.add(j);
                                        }else {
                                            break;
                                        }
                                    }
                                    //向下遍历所有相同的站位位置
                                    for (int k = curOperateIndex; k < lCheckAllMaterialItem.size();k++){
                                        if (lCheckAllMaterialItem.get(k).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                                            sameLineSeatIndexs.add(k);
                                            //将检查的位置往后移
                                            curCheckId ++;
                                        }
                                    }
                                    //根据站位索引数组检查料号与扫到的料号比对
                                    if (sameLineSeatIndexs.size() == 1){
                                        //只有一个站位
                                        MaterialItem singleMaterialItem = lCheckAllMaterialItem.get(sameLineSeatIndexs.get(0));
                                        singleMaterialItem.setScanMaterial(scanValue);
                                        if (singleMaterialItem.getOrgMaterial().equalsIgnoreCase(singleMaterialItem.getScanMaterial())){
                                            singleMaterialItem.setResult("PASS");
                                            singleMaterialItem.setRemark("站位和料号正确");
                                        }else {
                                            singleMaterialItem.setResult("FAIL");
                                            singleMaterialItem.setRemark("料号与站位不对应");
                                        }
                                        //更新显示日志
                                        updateVisitLog(singleMaterialItem);
                                    }else if (sameLineSeatIndexs.size() > 1){
                                        //多个相同的站位,即有替换料
                                        checkMultiItem(sameLineSeatIndexs,scanValue);
                                    }

                                    materialAdapter.notifyDataSetChanged();

                                    //增加数据库日志
                                    globalData.setOperType(Constants.CHECKALLMATERIAL);
                                    globalFunc.AddDBLog(globalData, lCheckAllMaterialItem.get(curOperateIndex));
                                    //清空站位
                                    sameLineSeatIndexs.clear();
                                    //检查下一个料
                                    checkNextMaterial();
                                    break;
                            }
                        }
                    }else {
                        globalFunc.showInfo("警告","请检查网络连接是否正常!","请连接网络!");
                        clearLineSeatMaterialScan();
                    }
                    return true;
                default:
                    return true;
            }
        }
        return false;
    }

    private void checkMultiItem(ArrayList<Integer> integers,String mScanValue){
        //多个相同的站位,即有替换料
        for (int z = 0;z < integers.size();z++){
            MaterialItem multiMaterialItem = lCheckAllMaterialItem.get(integers.get(z));
            multiMaterialItem.setScanMaterial(mScanValue);
            if (multiMaterialItem.getOrgMaterial().equalsIgnoreCase(multiMaterialItem.getScanMaterial())){
                for (int x = 0;x < integers.size();x++){
                    MaterialItem innerMaterialItem = lCheckAllMaterialItem.get(integers.get(x));
                    innerMaterialItem.setScanMaterial(mScanValue);
                    if (x == z){
                        innerMaterialItem.setResult("PASS");
                        innerMaterialItem.setRemark("主替有一项成功");
                    }else {
                        innerMaterialItem.setResult("PASS");
                        innerMaterialItem.setRemark("主替有一项成功");
                    }
                    //更新显示日志
                    updateVisitLog(innerMaterialItem);
                }
                //跳出循环
                return;
            }else {
                multiMaterialItem.setResult("FAIL");
                multiMaterialItem.setRemark("料号与站位不对应");
                //更新显示日志
                updateVisitLog(multiMaterialItem);
            }
        }
    }

    private void checkAllItems(MaterialItem materialItem,int curCheckIndex,String mScanValue){

        //当前操作的站位
        String curCheckLineSeat = materialItem.getOrgLineSeat();
        //相同站位的索引数组
        ArrayList<Integer> sameLineSeatIndexs = new ArrayList<Integer>();
        //当前操作的位置
        int curOperateIndex = curCheckIndex;
        //向上遍历所有相同站位的位置
        for (int j = curOperateIndex-1; j >= 0; j--){
            if (lCheckAllMaterialItem.get(j).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                sameLineSeatIndexs.add(j);
            }else {
                break;
            }
        }
        //向下遍历所有相同的站位位置
        for (int k = curOperateIndex; k < lCheckAllMaterialItem.size();k++){
            if (lCheckAllMaterialItem.get(k).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                sameLineSeatIndexs.add(k);
            }
        }
        //根据站位索引数组检查料号与扫到的料号比对
        if (sameLineSeatIndexs.size() == 1){
            //只有一个站位
            MaterialItem singleMaterialItem = lCheckAllMaterialItem.get(sameLineSeatIndexs.get(0));
            singleMaterialItem.setScanMaterial(mScanValue);
            if (singleMaterialItem.getOrgMaterial().equalsIgnoreCase(singleMaterialItem.getScanMaterial())){
                singleMaterialItem.setResult("PASS");
                singleMaterialItem.setRemark("站位和料号正确");
            }else {
                singleMaterialItem.setResult("FAIL");
                singleMaterialItem.setRemark("料号与站位不对应");
            }
            //更新显示日志
            updateVisitLog(singleMaterialItem);
        }else if (sameLineSeatIndexs.size() > 1){
            //多个相同的站位,即有替换料
            checkMultiItem(sameLineSeatIndexs,mScanValue);
        }

    }

    //更新显示日志
    private void updateVisitLog(MaterialItem materialItem){
        Log.d(TAG,"materialItem-lineseat-"+materialItem.getOrgLineSeat());
        Log.d(TAG,"materialItem-lineseat-"+materialItem.getOrgLineSeat());
        Log.d(TAG,"updateVisitLog-first_checkAll_result-"+isFirst_checkAll_result());
        if (isFirst_checkAll_result()){
            //非首次全检
            globalData.setUpdateType(Constants.CHECKALLMATERIAL);
        }else {
            //首次全检
            globalData.setUpdateType(Constants.FIRST_CHECK_ALL);
        }
        Log.d(TAG,"updateVisitLog-UpdateType-"+globalData.getUpdateType());
        globalFunc.updateVisitLog(globalData,materialItem);
    }

    //判断是否全部上料
    private void getFeedResult(final String programId){
        //判断工位检测功能是否打开
        if (Constants.isCheckWorkType){
            if (!isFeed_result()){
                loadingDialog = new LoadingDialog(getActivity(),"正在加载...");
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.show();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"getFeedResult-programId-"+programId);
                    boolean feedResult = new DBService().isFeeded(programId);
                    Message message = Message.obtain();
                    if (feedResult){
                        message.what = FEED_TRUE;
                    }else {
                        message.what = FEED_FALSE;
                    }
                    checkAllHandler.sendMessage(message);
                }
            }).start();
        }else {
            //工位检测功能未打开
            Message message = Message.obtain();
            message.what = FEED_TRUE;
            checkAllHandler.sendMessage(message);
        }
    }

    //判断是否全部进行了首次全检
    private void getFirstCheckAllResult(final String programId){
        //判断工位检测功能是否打开
        if (Constants.isCheckWorkType || (user_type == 2)){
            //工位检测功能打开 或者 用户是IPQC
            if (!isFirst_checkAll_result()){
                loadingDialog = new LoadingDialog(getActivity(),"正在加载...");
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.show();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"getFirstCheckAllResult-programId-"+programId);
                    boolean firstResult = new DBService().isOrderFirstCheckAll(programId);
                    Message message = Message.obtain();
                    if (firstResult){
                        message.what = FIRST_CHECKALL_TRUE;
                    }else {
                        message.what = FIRST_CHECKALL_FALSE;
                    }
                    checkAllHandler.sendMessage(message);
                }
            }).start();
        }else {
            //工位检测功能未打开
            setFirst_checkAll_result(true);
        }
    }

    //IPQC未做首次全检
    public void showInfo(String title, String message, final String netFailToastStr, final int userType){
        //对话框所有控件id
        int itemResIds[]=new int[]{R.id.dialog_title_view,
                R.id.dialog_title,R.id.tv_alert_info,R.id.info_trust};
        //标题和内容
        String titleMsg[]=new String[]{title,message};
        //内容的样式
        int msgStype[];

        if (userType == 3){
            msgStype=new int[]{22, Color.argb(255,102,153,0)};
        }else {
            msgStype=new int[]{22, Color.RED};
        }

        InfoDialog infoDialog=new InfoDialog(getActivity(),
                R.layout.info_dialog_layout,itemResIds,titleMsg,msgStype);

        infoDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()){
                    case R.id.info_trust:
                        dialog.dismiss();
//                        Toast.makeText(getActivity(),netFailToastStr,Toast.LENGTH_LONG).show();
                        clearLineSeatMaterialScan();
                        checkType = 1;
                        //操作员1，ipqc 2
                        if (userType == 1){
                            getFirstCheckAllResult(lCheckAllMaterialItem.get(0).getFileId());
                        }else if (userType == 2){
                            getFeedResult(lCheckAllMaterialItem.get(0).getFileId());
                        }
                        break;
                }
            }
        });
        infoDialog.show();
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 上下一个料
     */
    private void checkNextMaterial() {
        lv_CheckAllMaterial.setSelection(curCheckId);
        if (curCheckId < lCheckAllMaterialItem.size() - 1) {
            curCheckId++;
            clearLineSeatMaterialScan();
        } else {
            showCheckAllMaterialResult(0);
        }
        Log.i(TAG, "checkNextMaterial:" + curCheckId);
    }

    private void showCheckAllMaterialResult(int showType) {
        boolean feedResult = true;
        for (MaterialItem feedMaterialItem : lCheckAllMaterialItem) {
            if (!(feedMaterialItem.getResult().equals("PASS"))) {
                feedResult = false;
                break;
            }
        }
        final boolean finalResult = feedResult;
        if ((showType == 0) || (feedResult && (showType == 1))){
            //通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            //设置Title的图标
            builder.setIcon(android.R.drawable.ic_dialog_info);
            //设置Title的内容
            builder.setTitle("全检料结果");
            View view = View.inflate(getActivity(), R.layout.materialresult_dialog, null);
            builder.setView(view);
            final EditText etResult = (EditText) view.findViewById(R.id.et_result);

            //设置Content来显示一个信息
            if (feedResult) {
                etResult.setText("PASS");
                etResult.setBackgroundColor(Color.GREEN);
            } else {
                etResult.setText("FAIL");
                etResult.setBackgroundColor(Color.RED);
            }

            //设置一个PositiveButton
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //获取全检结果
                    checkType = 1;
                    getFirstCheckAllResult(lCheckAllMaterialItem.get(0).getFileId());
                    dialog.dismiss();
                    //全检结果全部正确
                    if (finalResult){
                        clearMaterialInfo();
                    }else {
                        edt_ScanMaterial.setText("");
                        edt_ScanMaterial.requestFocus();
                    }
                }
            });
            //显示出该对话框
            builder.show();
        }
    }

    /**
     * @method resetTestPar
     * @author connie
     * @time 2017-9-26
     * @describe 清空之前的全检信息进入下一轮全检
     */
    private void clearMaterialInfo() {
        clearLineSeatMaterialScan();
        edt_ScanMaterial.setText("");
        edt_ScanMaterial.requestFocus();
        curCheckId = 0;
        initData();
    }

    private void clearLineSeatMaterialScan() {
//        edt_LineSeat.setText("");
        edt_ScanMaterial.setText("");
        edt_ScanMaterial.requestFocus();
    }

    private boolean isFeed_result() {
        return feed_result;
    }

    private void setFeed_result(boolean feed_result) {
        this.feed_result = feed_result;
    }

    private boolean isFirst_checkAll_result() {
        return first_checkAll_result;
    }

    private void setFirst_checkAll_result(boolean first_checkAll_result) {
        this.first_checkAll_result = first_checkAll_result;
    }
}
