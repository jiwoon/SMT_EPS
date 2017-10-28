package com.jimi.smt.eps_server.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jimi.smt.eps_server.entity.Program;
import com.jimi.smt.eps_server.entity.ProgramBackup;
import com.jimi.smt.eps_server.entity.ProgramExample;
import com.jimi.smt.eps_server.entity.ProgramItem;
import com.jimi.smt.eps_server.entity.ProgramItemBackup;
import com.jimi.smt.eps_server.entity.ProgramItemExample;
import com.jimi.smt.eps_server.entity.filler.ProgramToProgramVOFiller;
import com.jimi.smt.eps_server.entity.vo.ProgramVO;
import com.jimi.smt.eps_server.mapper.ProgramBackupMapper;
import com.jimi.smt.eps_server.mapper.ProgramItemBackupMapper;
import com.jimi.smt.eps_server.mapper.ProgramItemMapper;
import com.jimi.smt.eps_server.mapper.ProgramMapper;
import com.jimi.smt.eps_server.service.ProgramService;
import com.jimi.smt.eps_server.util.FieldUtil;
import com.jimi.smt.eps_server.util.UuidUtil;

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
	@Autowired
	private ProgramToProgramVOFiller filler;
	
	@Override
	public Map<String, Object> upload(MultipartFile programFile, Integer boardType) throws IOException {
		Workbook workbook = null;
		//判断格式
		if(programFile.getOriginalFilename().endsWith(".xlsx")){
			workbook = new XSSFWorkbook(programFile.getInputStream());
		}else {
			workbook = new HSSFWorkbook(programFile.getInputStream());
		}
		Sheet sheet = workbook.getSheetAt(0);
		//校验
		final String header = "SMT FEEDER LIST";
		if(!header.equals(sheet.getRow(1).getCell(0).getStringCellValue())) {
			throw new RuntimeException("头部错误：没有找到\"SMT FEEDER LIST\"标题栏");
		}
		//填充表头
		Program program = new Program();
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
		program.setFileName(programFile.getOriginalFilename());
		program.setId(UuidUtil.get32UUID());
		program.setCreateTime(new Date());
		program.setBoardType(boardType);
		program.setWorkOrder(sheet.getRow(4).getCell(6).getStringCellValue());
		
//		//转移表
//		ProgramExample programExample = new ProgramExample();
//		programExample.createCriteria().andLineEqualTo(program.getLine());
//		List<Program> programs = programMapper.selectByExample(programExample);
//		for (Program p : programs) {
//			ProgramItemExample programItemExample = new ProgramItemExample();
//			programItemExample.createCriteria().andProgramIdEqualTo(p.getId());
//			List<ProgramItem> programItems = programItemMapper.selectByExample(programItemExample);
//			for (ProgramItem pi : programItems) {
//				ProgramItemBackup pib = new ProgramItemBackup();
//				BeanUtils.copyProperties(pi, pib);
//				programItemBackupMapper.insert(pib);
//				programItemMapper.deleteByPrimaryKey(pi);
//			}
//			ProgramBackup pb = new ProgramBackup();
//			BeanUtils.copyProperties(p, pb);
//			programBackupMapper.insert(pb);
//			programMapper.deleteByPrimaryKey(p.getId());
//		}
		//初始化结果
		Map<String, Object> result = new HashMap<String , Object>();
		int sum = workbook.getNumberOfSheets();
		result.put("real_parse_num", sum);
		result.put("plan_parse_num", sum);
		result.put("action_name", "上传");
		
		//填充表项
		for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
			sheet = workbook.getSheetAt(i);
			for(int j = 9; j < sheet.getLastRowNum() - 3; j++) {
				ProgramItem programItem = new ProgramItem();
				//空表判断
				if(sheet.getRow(j).getCell(0).getStringCellValue().equals("")) {
					int temp = (int) result.get("real_parse_num");
					result.put("real_parse_num", temp--);
					break;
				}
				programItem.setLineseat(formatLineseat(sheet.getRow(j).getCell(0).getStringCellValue()));
				programItem.setMaterialNo(sheet.getRow(j).getCell(1).getStringCellValue());
				programItem.setAlternative(sheet.getRow(j).getCell(2).getCellType() == 0 ? false : true);
				programItem.setSpecitification(sheet.getRow(j).getCell(3).getStringCellValue());
				programItem.setPosition(sheet.getRow(j).getCell(4).getStringCellValue());
				programItem.setQuantity((int) sheet.getRow(j).getCell(5).getNumericCellValue());
				//设置programId
				programItem.setProgramId(program.getId());
				//忽略重复项
				try {
					//插入表项
					programItemMapper.insert(programItem);
					//备份表项
					ProgramItemBackup programItemBackup = new ProgramItemBackup();
					BeanUtils.copyProperties(programItem, programItemBackup);
					programItemBackupMapper.insert(programItemBackup);
					//打印到控制台
					FieldUtil.print(programItem);
				}catch (DuplicateKeyException e) {
				}
			}
		}
		
		//重复文件移除（根据工单和板面类型）
		ProgramExample programExample = new ProgramExample();
		programExample.createCriteria()
			.andWorkOrderEqualTo(program.getWorkOrder())
			.andBoardTypeEqualTo(program.getBoardType());
		List<Program> programs = programMapper.selectByExample(programExample);
		if(!programs.isEmpty()) {
			Program p = programs.get(0);
			programMapper.deleteByPrimaryKey(p.getId());
			ProgramItemExample programItemExample = new ProgramItemExample();
			programItemExample.createCriteria().andProgramIdEqualTo(p.getId());
			programItemMapper.deleteByExample(programItemExample);
			result.put("action_name", "覆盖");
		}
		
		//插入新表
		programMapper.insert(program);
		//备份表
		ProgramBackup programBackup = new ProgramBackup();
		BeanUtils.copyProperties(program, programBackup);
		programBackupMapper.insert(programBackup);
		//打印到控制台
		FieldUtil.print(program);
		
		return result;
	}

	
	@Override
	public List<ProgramVO> list() {
		return filler.fill(programMapper.selectByExample(null));
	}


	@Override
	public boolean delete(String id) {
		if(programMapper.deleteByPrimaryKey(id) == 1) {
			ProgramItemExample programItemExample = new ProgramItemExample();
			programItemExample.createCriteria().andProgramIdEqualTo(id);
			if(programItemMapper.deleteByExample(programItemExample) != 0) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}


	private String formatLineseat(String in) {
		try {
			String[] array = in.split("-");
			array[0] = Integer.valueOf(array[0]) <= 9 ? "0" + array[0] : array[0];
			array[1] = Integer.valueOf(array[1]) <= 9 ? "0" + array[1] : array[1];
			return array[0] + "-" + array[1];
		}catch (NumberFormatException | PatternSyntaxException e) {
			return in;
		}
	}
	
}
