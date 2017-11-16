package com.jimi.smt.eps_server.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 处理Excel表的常用工具类（Spring版）
 * <br>
 * 所需第三方依赖：
 * <br>
 *	spring
 * <br>
 * ExcelHepler
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class ExcelSpringHelper extends ExcelHelper{


	/**
	 * 传入一个excel表格，构造Helper
	 */
	public static ExcelSpringHelper from(MultipartFile file) throws IOException {
		return new ExcelSpringHelper(file);
	}

	
	
	/**
	 * 规定excel表格属于2007版之前还是之后，构造Helper
	 */
	public static ExcelSpringHelper create(boolean isNewVersion) {
		return new ExcelSpringHelper(isNewVersion);
	}
	
	
	/**
	 * 构造Helper，为旧版excel
	 */
	public static ExcelSpringHelper create() {
		return new ExcelSpringHelper(false);
	}
	
	
	/**
	 * 获取下载实体，注意文件名编码必须为UTF-8，需要自行填写后缀名；可以设置是否自动列宽
	 * @param downloadFileName
	 * @param autoColumnWidth
	 * @return
	 * @throws IOException 
	 */
	public ResponseEntity<byte[]> getDownloadEntity(String downloadFileName, boolean autoColumnWidth) throws IOException {
		//设置头信息	
		HttpHeaders headers = new HttpHeaders(); 
		String filename = new String((downloadFileName).getBytes("utf-8"), "iso-8859-1");
		headers.setContentDispositionFormData("attachment", filename);   
		headers.setContentType(MediaType.parseMediaType("application/x-xls")); 
		//返回流
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		write(bos, autoColumnWidth);
		return new ResponseEntity<byte[]>(bos.toByteArray(), headers, HttpStatus.CREATED);    
	}
	
	
	private ExcelSpringHelper(MultipartFile file) throws IOException {
		//判断格式
		if(file.getOriginalFilename().endsWith(".xlsx")){
			workbook = new XSSFWorkbook(file.getInputStream());
		}else {
			workbook = new HSSFWorkbook(file.getInputStream());
		}
		init();
	}
	
	
	private ExcelSpringHelper(boolean isNewVersion) {
		super(isNewVersion);
	}


}
