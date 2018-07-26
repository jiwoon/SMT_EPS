package com.jimi.smt.eps.display.util;


import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpHelper {
	
	private static final String URL = "http://localhost:8080/eps_server/program/";
	
	private final OkHttpClient client = new OkHttpClient();
	
	/**
	 * 无参数的http请求
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public void requestHttp(String action,Callback callback) throws IOException {
		Request request = new Request.Builder().url(URL + action).build();
		Call call = client.newCall(request);
		call.enqueue(callback);	
	}
	
	/**
	 * 带参数的http请求，参数要求MAP，键为参数名，值为参数值
	 * @param action
	 * @param args
	 * @return
	 * @throws IOException 
	 */
	public void requestHttp(String action, Map <String, String> args ,Callback callback) throws IOException{
		Builder requestBuilder = new FormBody.Builder();
		if (args != null) {
			for (Map.Entry <String, String> entry : args.entrySet()) {
				requestBuilder.add(entry.getKey(), entry.getValue());
			}
		}
		RequestBody requestBody = requestBuilder.build();
		Request request = new Request.Builder().url(URL + action).post(requestBody).build();
		Call call = client.newCall(request);
		call.enqueue(callback);	
	}

}
