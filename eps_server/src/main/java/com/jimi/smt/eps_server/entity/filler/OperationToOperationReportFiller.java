package com.jimi.smt.eps_server.entity.filler;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jimi.smt.eps_server.entity.Operation;
import com.jimi.smt.eps_server.entity.ProgramBackup;
import com.jimi.smt.eps_server.entity.ProgramItemBackup;
import com.jimi.smt.eps_server.entity.vo.OperationReport;
import com.jimi.smt.eps_server.mapper.ProgramBackupMapper;
import com.jimi.smt.eps_server.mapper.ProgramItemBackupMapper;
import com.jimi.smt.eps_server.util.VoFieldFiller;

@Component
public class OperationToOperationReportFiller extends VoFieldFiller<Operation, OperationReport> {

	@Autowired
	private ProgramItemBackupMapper programItemBackupMapper;
	
	private List<ProgramItemBackup> programItemBackups;
	
	@Autowired
	private ProgramBackupMapper programBackupMapper;
	
	private List<ProgramBackup> programBackups;
	
	@PostConstruct
	public void init() {
		programItemBackups = programItemBackupMapper.selectByExample(null);
		programBackups = programBackupMapper.selectByExample(null);
	}
		
	@Override
	public OperationReport fill(Operation operation) {
		OperationReport operationReport = new OperationReport();
		BeanUtils.copyProperties(operation, operationReport);
		
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(operation.getTime());
		operationReport.setTime(time);
			
		//匹配程序表子项目和操作日志
		for (ProgramItemBackup programItemBackup : programItemBackups) {
			if(programItemBackup.getProgramId().equals(operation.getFileid()) 
				&& programItemBackup.getLineseat().equals(operation.getLineseat())
				&& programItemBackup.getMaterialNo().equals(operation.getMaterialNo())
			) {
				//解析料描述和料规格
				String specitification = programItemBackup.getSpecitification();
				try {
					String materialDescription = specitification.substring(0, specitification.indexOf(","));
					String temp = specitification.substring(specitification.indexOf(";") + 5, specitification.lastIndexOf(";") - 4);
					if(!temp.equals(materialDescription)) {
						operationReport.setMaterialDescription("-");
						operationReport.setMaterialSpecitification(specitification);
					}else {
						String materialSpecitification = specitification.substring(specitification.indexOf(",") + 1, specitification.indexOf(";"));
						operationReport.setMaterialDescription(materialDescription);
						operationReport.setMaterialSpecitification(materialSpecitification);
					}
					break;
				}catch (StringIndexOutOfBoundsException e) {
					operationReport.setMaterialDescription("-");
					operationReport.setMaterialSpecitification(specitification);
				}
				
			}
		}
			
		//填写线别
		for (ProgramBackup programBackup : programBackups) {
			if(programBackup.getId().equals(operation.getFileid())){
				operationReport.setLine(programBackup.getLine());
				operationReport.setWorkOrderNo(programBackup.getWorkOrder());
				break;
			}
		}
		
		return operationReport;
	}

}
