package com.jimi.smt.esp_server.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.jimi.smt.esp_server.entity.vo.ProgramVO;

/**
 * 排位表业务接口
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public interface ProgramService {
	
	/**
	 * 列出所有线上的排位表
	 * @return
	 */
	List<ProgramVO> list();
	
	
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	boolean delete(String id);

	
	/**
	 * 上传并解析文件
	 * @param programFile 文件
	 * @return 
	 * @throws IOException IO异常
	 * @throws XLSException 解析异常
	 */
	Map<String, Object> upload(MultipartFile programFile, Integer boardType) throws IOException, RuntimeException;
}
