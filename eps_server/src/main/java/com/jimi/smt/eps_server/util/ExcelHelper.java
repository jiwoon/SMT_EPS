package com.jimi.smt.eps_server.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 处理Excel表的常用工具类
 * <br>
 * 所需第三方依赖：
 * <br>
 * log4j
 * <br>
 * spring
 * <br>
 * poi
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class ExcelHelper{

	/**
	 * 标记了该注解的属性（Fields）所属的类的实例，在调用ExcelHelper类实例的fill方法时，可以被作为参数传入，完成excel表格填写
	 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Excel{
		String head();
		int col();
	}
	
	private static Logger logger = LogManager.getLogger();
	
	private enum RequireType{
		/**
		 * 优先尝试转换成整型
		 */
		DOUBLE,
		
		/**
		 * 优先尝试转换成日期型
		 */
		STRING,
		
		/**
		 * 优先尝试转换成双精度浮点型
		 */
		DATE,
		
		/**
		 * 优先尝试转换成字符串型
		 */
		INT,
		
		/**
		 * 优先尝试转换成布尔型
		 */
		BOOLEAN,
	}
	
	
	private Workbook workbook;
	
	private CellStyle headStyle;
	
	private CellStyle bodyStyle;
	
	private int currentSheetNum;
	
	
	/**
	 * 传入一个excel表格，构造Helper
	 */
	public static ExcelHelper from(File file) throws IOException {
		return new ExcelHelper(file);
	}
	
	
	/**
	 * 传入一个excel表格，构造Helper
	 */
	public static ExcelHelper from(MultipartFile file) throws IOException {
		return new ExcelHelper(file);
	}
	
	
	/**
	 * 规定excel表格属于2007版之前还是之后，构造Helper
	 */
	public static ExcelHelper create(boolean isNewVersion) {
		return new ExcelHelper(isNewVersion);
	}
	
	
	/**
	 * 构造Helper，为旧版excel
	 */
	public static ExcelHelper create() {
		return new ExcelHelper(false);
	}
	
	
	/**
	 * 输出到流，可以设置是否自动列宽
	 * @param outputStream
	 * @param autoColumnWidth
	 * @throws IOException
	 */
	public void write(OutputStream outputStream, boolean autoColumnWidth) throws IOException {
		if(autoColumnWidth) {
			autoColumnWidth();
		}
		workbook.write(outputStream);
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
	
	
	/**
	 * 获取一个值
	 */
	public String getString(int rowNum, int colNum) {
		return (String) get(rowNum, colNum, RequireType.STRING);
	}
	
	
	/**
	 * 获取一个值
	 */
	public  int getInt(int rowNum, int colNum) {
		return (int) get(rowNum, colNum, RequireType.INT);
	}
	
	
	/**
	 * 获取一个值
	 */
	public Date getDate(int rowNum, int colNum) {
		return (Date) get(rowNum, colNum, RequireType.DATE);
	}
	
	
	/**
	 * 获取一个值
	 */
	public Double getDouble(int rowNum, int colNum) {
		return (Double) get(rowNum, colNum, RequireType.DOUBLE);
	}
	
	
	/**
	 * 获取一个值
	 */
	public boolean getBoolean(int rowNum, int colNum) {
		return (boolean) get(rowNum, colNum, RequireType.BOOLEAN);
	}
	
	
	/**
	 * 在指定位置设置一个值，使用Body样式
	 */
	public void setBody(int rowNum, int colNum, Object value) {
		set(rowNum, colNum, value, bodyStyle);
	}
	
	
	/**
	 * 在指定位置设置一个值，使用Head样式
	 */
	public void setHead(int rowNum, int colNum, Object value) {
		set(rowNum, colNum, value, headStyle);
	}
	
	
	/**
	 * 在指定位置设置一个值，不带样式
	 */
	public void set(int rowNum, int colNum, Object value) {
		set(rowNum, colNum, value, null);
	}
	
	
	/**
	 * 在指定位置设置一个值
	 */
	public void set(int rowNum, int colNum, Object value, CellStyle style) {
		Row row = workbook.getSheetAt(currentSheetNum).getRow(rowNum);
		if(row == null) {
			row = workbook.getSheetAt(currentSheetNum).createRow(rowNum);
		}
		Cell cell = row.getCell(colNum);
		if(cell == null) {
			cell = row.createCell(colNum);
		}
		if(style != null) {
			cell.setCellStyle(style);
		}
		if(value == null) {
			return;
		}
		final String valueClassName = value.getClass().getName();
		switch (valueClassName) {
			case "java.util.Date":
				cell.setCellValue((Date)value);
				break;
			case "double":
				cell.setCellValue((double) value);
				break;
			case "int":
				cell.setCellValue((int)value);
				break;
			case "boolean":
				cell.setCellValue((boolean)value);
				break;
			case "java.lang.String":
				cell.setCellValue((String)value);
				break;
			default:
				break;
		}
	}
	
	
	/**
	 * 在指定行写多个值
	 */
	public void setHeadLine(int rowNum, Object... values) {
		setLine(rowNum, headStyle, values);
	}
	
	
	/**
	 * 在指定行写多个值
	 */
	public void setBodyLine(int rowNum, Object... values) {
		setLine(rowNum, bodyStyle, values);
	}
	
	
	/**
	 * 在指定行写多个值
	 */
	public void setLine(int rowNum, Object... values) {
		setLine(rowNum, null, values);
	}
	

	/**
	 * 在指定行写多个值
	 */
	public void setLine(int rowNum, CellStyle style, Object... values) {
		for (int i = 0; i < values.length; i++) {
			set(rowNum, i, values[i], style);
		}
	}

	
	/**
	 * 根据实体类填写指定行，不带样式
	 * 需要在实体类的每个属性中注解@Excel，col属性代表列号，从0开始，head属性表示该字段的表头描述
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void fill(int rowNum, Object entity){
		fill(rowNum, null, entity);
	}
	
	
	/**
	 * 根据实体类填写指定行，使用Body样式
	 * 需要在实体类的每个属性中注解@Excel，col属性代表列号，从0开始，head属性表示该字段的表头描述
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void fillBody(int rowNum, Object entity){
		fill(rowNum, bodyStyle, entity);
	}
	
	
	/**
	 * 根据实体类填写指定行，使用自定义样式
	 * 需要在实体类的每个属性中注解@Excel，col属性代表列号，从0开始，head属性表示该字段的表头描述
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void fill(int rowNum, CellStyle style, Object entity){
		Field[] fields = entity.getClass().getDeclaredFields();
		for (Field field : fields) {
			Excel e = field.getAnnotation(Excel.class);
			//填写表体
			field.setAccessible(true);
			try {
				set(rowNum, e.col(), field.get(entity), style);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				logger.error("调用ExcelHelper.fill()中field.get()方法时出错");
				e1.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 根据实体类列表填写表格，使用默认样式
	 * 需要在实体类的每个属性中注解@Excel，col属性代表列号，从0开始，head属性表示该字段的表头描述
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void fill(List<?> entities){
		fill(headStyle, bodyStyle, entities);
	}
	
	
	/**
	 * 根据实体类列表填写表格，使用自定义样式
	 * 需要在实体类的每个属性中注解@Excel，col属性代表列号，从0开始，head属性表示该字段的表头描述
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void fill(CellStyle headStyle, CellStyle bodyStyle, List<?> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Object entity = entities.get(i);
			Field[] fields = entity.getClass().getDeclaredFields();
			for (Field field : fields) {
				Excel e = field.getAnnotation(Excel.class);
				if(e == null) {
					continue;
				}
				//如果是第一行则填写表头
				if(i == 0) {
					set(i, e.col(), e.head(), headStyle);
				}
				//填写表体
				field.setAccessible(true);
				try {
					set(i+1, e.col(), field.get(entity), bodyStyle);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					logger.error("调用ExcelHelper.fill()中field.get()方法时出错");
					e1.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * 根据提供的Class类，解析出报表实例列表
	 * @throws 表头解析错误时抛出
	 */
	public <T> List<T> unfill(Class<T> clazz) throws Exception{
		List<T> entities = new ArrayList<T>();
		for (int i = 0; i < workbook.getSheetAt(currentSheetNum).getLastRowNum(); i++) {
			T entity = null;
			try {
				entity = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e2) {
				e2.printStackTrace();
			}
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				Excel e = field.getAnnotation(Excel.class);
				if(e == null) {
					continue;
				}
				//如果是第一行则校验表头
				if(i == 0) {
					if(!e.head().equals(getString(i, e.col()))){
						throw new Exception("表头校验失败");
					}
				}
				//填充list
				field.setAccessible(true);
				try {
					Object value = get(i+1, e.col(), RequireType.values()[workbook.getSheetAt(currentSheetNum).getRow(i).getCell(e.col()).getCellType()]);
					field.set(entity, value);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					logger.error("调用ExcelHelper.fill()中field.get()方法时出错");
					e1.printStackTrace();
				}
			}
			entities.add(entity);
		}
		return entities;
	}
	

	/**
	 * 获取workbook
	 */
	public Workbook getBook() {
		return workbook;
	}

	/**
	 * 切换至指定sheet，失败返回false
	 */
	public boolean switchSheet(int sheetNum) {
		if(workbook.getSheetAt(sheetNum) != null) {
			this.currentSheetNum = sheetNum;
			return true;
		}else {
			return false;
		}
	}

	
	/**
	 * 切换至指定sheet，如果不存在该sheet则创建一个
	 */
	public void switchSheet(String sheetName) {
		if(workbook.getSheet(sheetName) != null) {
			this.currentSheetNum = workbook.getSheetIndex(sheetName);
		}else {
			workbook.createSheet(sheetName);
			switchSheet(sheetName);
		}
	}

	
	private Object get(int rowNum, int colNum, RequireType requireType) {
		try {
			Cell cell = workbook.getSheetAt(currentSheetNum).getRow(rowNum).getCell(colNum);
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN:
				switch (requireType) {
				case BOOLEAN:
					return cell.getBooleanCellValue();
				case STRING:
					return cell.getBooleanCellValue() ? "true" : "false";
				case INT:
					return cell.getBooleanCellValue() ? 1 : 0;
				case DATE:
					logger.error("无法把坐标为("+currentSheetNum+","+rowNum+","+colNum+")的布尔值转成日期");
					return null;
				case DOUBLE:
					return cell.getBooleanCellValue() ? 1.0d : 0.0d;
				}
			case Cell.CELL_TYPE_NUMERIC:
				switch (requireType) {
				case BOOLEAN:
					return cell.getNumericCellValue() != 0 ? true : false;
				case STRING:
					return String.valueOf((int)cell.getNumericCellValue());
				case INT:
					return (int)cell.getNumericCellValue();
				case DATE:
					return cell.getDateCellValue();
				case DOUBLE:
					return cell.getNumericCellValue();
				}
			case Cell.CELL_TYPE_FORMULA:
			case Cell.CELL_TYPE_STRING:
				switch (requireType) {
				case BOOLEAN:
					return cell.getStringCellValue().equals("") ? false : true;
				case STRING:
					return cell.getStringCellValue();
				case INT:
					return Integer.parseInt(cell.getStringCellValue());
				case DATE:
					return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cell.getStringCellValue());
				case DOUBLE:
					return Double.parseDouble(cell.getStringCellValue());
				}
			default:
				return null;
			}
		}catch (NullPointerException e) {
			logger.error("空指针异常，无法获取坐标为("+currentSheetNum+","+rowNum+","+colNum+")的值");
			e.printStackTrace();
			return null;
		}catch (NumberFormatException e) {
			logger.error("无法把坐标为("+currentSheetNum+","+rowNum+","+colNum+")的数值转成字符串");
			e.printStackTrace();
			return null;
		}catch (ParseException e) {
			logger.error("无法把坐标为("+currentSheetNum+","+rowNum+","+colNum+")的字符串转成日期");
			e.printStackTrace();
			return null;
		}
	}


	private ExcelHelper(boolean isNewVersion) {
		//判断格式
		if(isNewVersion){
			workbook = new XSSFWorkbook();
		}else {
			workbook = new HSSFWorkbook();
		}
		workbook.createSheet();
		init();
	}


	private ExcelHelper(MultipartFile file) throws IOException {
		//判断格式
		if(file.getOriginalFilename().endsWith(".xlsx")){
			workbook = new XSSFWorkbook(file.getInputStream());
		}else {
			workbook = new HSSFWorkbook(file.getInputStream());
		}
		init();
	}

	
	private ExcelHelper(File file) throws IOException {
		//判断格式
		if(file.getName().endsWith(".xlsx")){
			workbook = new XSSFWorkbook(new FileInputStream(file));
		}else {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		}
		init();
	}
	

	private void init() {
		//默认表
		currentSheetNum = 0;
		//默认样式
		//创建表头样式
		headStyle = workbook.createCellStyle();
		headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
		headStyle.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
		headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		Font headFont = workbook.createFont();
		headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headFont.setFontName("Arial");
		headFont.setFontHeightInPoints((short) 12);
		headStyle.setFont(headFont);
		//创建数据样式
		bodyStyle = workbook.createCellStyle();
		bodyStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		Font bodyFont = workbook.createFont();
		bodyFont.setFontName("Arial");
		bodyFont.setFontHeightInPoints((short) 10);
		bodyStyle.setFont(bodyFont);
	}
	
	
	private void autoColumnWidth() {
		try {
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
					sheet.autoSizeColumn(j);
					sheet.setColumnWidth(j, sheet.getColumnWidth(j) + 4 *256);
					//设置上限
					if(sheet.getColumnWidth(j) >= 80 * 256) {
						sheet.setColumnWidth(j, 80 * 256);
					}
				}
			}
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
}
