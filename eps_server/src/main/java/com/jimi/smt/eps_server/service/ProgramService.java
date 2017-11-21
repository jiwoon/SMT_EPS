package com.jimi.smt.eps_server.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.jimi.smt.eps_server.entity.vo.ProgramItemVO;
import com.jimi.smt.eps_server.entity.vo.ProgramVO;

/**
 * 排位表业务接口
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public interface ProgramService {
	
	/**
	 * 根据条件列出线上的排位表
	 * @return
	 */
	List<ProgramVO> list(String programName, String fileName, String line, String workOrder, Integer state, String ordBy);
	
	
	/**
	 * 上传并解析文件，如果“未开始”的工单列表中存在板面类型、工单号、线号同时一致的工单项目，将被新文件内容覆盖
	 * @param programFile 文件
	 * @return 
	 * @throws IOException IO异常
	 * @throws XLSException 解析异常
	 */
	List<Map<String, Object>> upload(MultipartFile programFile, Integer boardType) throws IOException, RuntimeException;


	/**
	 * 取消指定工单
	 * @param workOrder
	 * @return
	 */
	boolean cancel(String id);


	/**
	 * 完成指定工单
	 * @param workOrder
	 * @return
	 */
	boolean finish(String id);


	/**
	 * 开始指定工单
	 * @param workOrder
	 * @return
	 */
	boolean start(String id);


	/**
	 * 列出指定id排位表的所有子项目
	 * @param id
	 * @return
	 */
	List<ProgramItemVO> listItem(String id);
}
