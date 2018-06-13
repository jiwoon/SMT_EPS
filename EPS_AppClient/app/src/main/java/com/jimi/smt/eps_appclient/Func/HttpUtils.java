package com.jimi.smt.eps_appclient.Func;

import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 类名:HttpUtils
 * 创建人:Liang GuoChang
 * 创建时间:2018/4/27 20:06
 * 描述:发起http请求
 * 版本号:
 * 修改记录:
 */

public class HttpUtils {

    private static final String TAG = "HttpUtils";

    private static HttpUtils httpUtils = null;

    public HttpUtils() {

    }

    public static HttpUtils getHttpUtils() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }

    private static final String operateUrl = Constants.urlBase + "/program/operate";
    private static final String resetUrl = Constants.urlBase + "/program/reset";

    /**
     * 插入结果到visit表
     *
     * @param materialItem
     * @param operatType   操作类型
     * @return
     */
    public int operate(MaterialItem materialItem, int operatType) {
        Log.d(TAG, "operate");
        String operationResult = materialItem.getResult();
        if (operationResult.equalsIgnoreCase("FAIL")) {
            operationResult = "0";
        } else if (operationResult.equalsIgnoreCase("PASS")) {
            operationResult = "1";
        }

        int operateResult = 0;
        OkHttpUtils.post().url(operateUrl)
                .addParams("line", materialItem.getLine())
                .addParams("workOrder", materialItem.getOrder())
                .addParams("boardType", String.valueOf(materialItem.getBoardType()))
                .addParams("type", String.valueOf(operatType))
                .addParams("lineseat", materialItem.getOrgLineSeat())
                .addParams("materialNo", materialItem.getOrgMaterial())
                .addParams("scanLineseat", materialItem.getScanLineSeat())
                .addParams("scanMaterialNo", materialItem.getScanMaterial())
                .addParams("operationResult", operationResult)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "onError - " + e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "onResponse - " + response);
            }
        });
        return operateResult;
    }

    /**
     * 重置visit表
     *
     * @param line
     * @param workOrder
     * @param boardType
     * @return
     */
    public int reset(String line, String workOrder, int boardType) {
        Log.d(TAG, "reset");
        int resetResult = 0;
        OkHttpUtils.post().url(resetUrl)
                .addParams("line", line)
                .addParams("workOrder", workOrder)
                .addParams("boardType", String.valueOf(boardType))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "onError - " + e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "onResponse - " + response);
            }
        });
        return resetResult;
    }
}
