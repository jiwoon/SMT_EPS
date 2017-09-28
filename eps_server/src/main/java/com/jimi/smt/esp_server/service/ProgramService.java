package com.jimi.smt.esp_server.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * 排位表业务接口
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public interface ProgramService {

	/**
	 * 上传并解析文件
	 * @param programFile 文件
	 * @return 
	 * @throws IOException IO异常
	 * @throws XLSException 解析异常
	 */
	int upload(MultipartFile programFile) throws IOException, RuntimeException;
	
}
