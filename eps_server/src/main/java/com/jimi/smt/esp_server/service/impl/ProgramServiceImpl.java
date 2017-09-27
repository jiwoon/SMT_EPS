package com.jimi.smt.esp_server.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jimi.smt.esp_server.entity.Program;
import com.jimi.smt.esp_server.entity.ProgramBackup;
import com.jimi.smt.esp_server.entity.ProgramExample;
import com.jimi.smt.esp_server.entity.ProgramItem;
import com.jimi.smt.esp_server.entity.ProgramItemBackup;
import com.jimi.smt.esp_server.entity.ProgramItemExample;
import com.jimi.smt.esp_server.exception.XLSException;
import com.jimi.smt.esp_server.mapper.ProgramBackupMapper;
import com.jimi.smt.esp_server.mapper.ProgramItemBackupMapper;
import com.jimi.smt.esp_server.mapper.ProgramItemMapper;
import com.jimi.smt.esp_server.mapper.ProgramMapper;
import com.jimi.smt.esp_server.service.ProgramService;
import com.jimi.smt.esp_server.util.FieldUtil;
import com.jimi.smt.esp_server.util.UuidUtil;

@Service
public class ProgramServiceImpl implements ProgramService {

	@Autowired
	private ProgramMapper programMapper;
	@Autowired
	private ProgramItemMapper programItemMapper;
	@Autowired
	private ProgramBackupMapper programBackupMapper;
	@Autowired
	private ProgramItemBackupMapper programItemBackupMapper;
	
	@Override
	public void upload(MultipartFile programFile) throws IOException,XLSException {
		try {
			Workbook workbook = null;
			//判断格式
			if(programFile.getOriginalFilename().endsWith(".xlsx")){
				workbook = new XSSFWorkbook(programFile.getInputStream());
			}else {
				workbook = new HSSFWorkbook(programFile.getInputStream());
			}
			Sheet sheet = workbook.getSheetAt(0);
			//校验
			final String header = "Uni skill Electronics(Huizhou)Co.,Ltd";
			if(!header.equals(sheet.getRow(0).getCell(0).getStringCellValue())) {
				throw new XLSException("头部错误：没有找到\"Uni skill Electronics(Huizhou)Co.,Ltd\"标记");
			}
			//填充表头
			Program program = new Program();
			program.setId(UuidUtil.get32UUID());
			program.setClient(sheet.getRow(2).getCell(1).getStringCellValue());
			program.setMachineName(sheet.getRow(2).getCell(4).getStringCellValue());
			program.setVersion(sheet.getRow(2).getCell(6).getStringCellValue());
			program.setMachineConfig(sheet.getRow(3).getCell(1).getStringCellValue());
			program.setProgramNo(sheet.getRow(3).getCell(4).getStringCellValue());
			program.setLine(String.valueOf(sheet.getRow(3).getCell(6).getNumericCellValue()).split("\\.")[0]);
			program.setEffectiveDate(sheet.getRow(4).getCell(1).getDateCellValue().toString());
			program.setPcbNo(sheet.getRow(4).getCell(4).getStringCellValue());
			program.setBom(sheet.getRow(5).getCell(1).getStringCellValue());
			program.setProgramName(sheet.getRow(6).getCell(1).getStringCellValue());
			program.setAuditor(sheet.getRow(7).getCell(4).getStringCellValue().substring(3));
			program.setCreateTime(new Date());
			program.setFileName(programFile.getOriginalFilename());
			//转移表
			ProgramExample programExample = new ProgramExample();
			programExample.createCriteria().andLineEqualTo(program.getLine());
			List<Program> programs = programMapper.selectByExample(programExample);
			for (Program p : programs) {
				ProgramItemExample programItemExample = new ProgramItemExample();
				programItemExample.createCriteria().andProgramIdEqualTo(p.getId());
				List<ProgramItem> programItems = programItemMapper.selectByExample(programItemExample);
				for (ProgramItem pi : programItems) {
					ProgramItemBackup pib = new ProgramItemBackup();
					BeanUtils.copyProperties(pi, pib);
					programItemBackupMapper.insert(pib);
					programItemMapper.deleteByPrimaryKey(pi);
				}
				ProgramBackup pb = new ProgramBackup();
				BeanUtils.copyProperties(p, pb);
				programBackupMapper.insert(pb);
				programMapper.deleteByPrimaryKey(p.getId());
			}
			//插入新表
			programMapper.insert(program);
			FieldUtil.print(program);
			//填充表项
			for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
				sheet = workbook.getSheetAt(i);
				for(int j = 9; j < sheet.getLastRowNum() - 4; j++) {
					ProgramItem programItem = new ProgramItem();
					programItem.setProgramId(program.getId());
					programItem.setLineseat(formatLineseat(sheet.getRow(j).getCell(0).getStringCellValue()));
					programItem.setMaterialNo(sheet.getRow(j).getCell(1).getStringCellValue());
					programItem.setAlternative(sheet.getRow(j).getCell(2).getCellType() == 0 ? false : true);
					programItem.setSpecitification(sheet.getRow(j).getCell(3).getStringCellValue());
					programItem.setPosition(sheet.getRow(j).getCell(4).getStringCellValue());
					programItem.setQuantity((int) sheet.getRow(j).getCell(5).getNumericCellValue());
					//插入表项
					programItemMapper.insert(programItem);
					FieldUtil.print(programItem);
				}
			}
		}catch (RuntimeException e) {
			throw new XLSException(e.getMessage());
		}
		
	}

	
	private String formatLineseat(String in) {
		String[] array = in.split("-");
		array[0] = Integer.valueOf(array[0]) <= 9 ? "0" + array[0] : array[0];
		array[1] = Integer.valueOf(array[1]) <= 9 ? "0" + array[1] : array[1];
		return array[0] + "-" + array[1];
	}
	
}
