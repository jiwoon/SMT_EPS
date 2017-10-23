package com.jimi.smt.esp_server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jimi.smt.esp_server.base.BaseJunit4Test;
import com.jimi.smt.esp_server.service.OperationService;
import com.jimi.smt.esp_server.util.FieldUtil;

public class DisplayReportTest extends BaseJunit4Test {

	@Autowired
	private OperationService operationService;
	
	@Test
	public void list() {
		com.jimi.smt.esp_server.entity.vo.DisplayReport displayReport = operationService.listDisplayReport("308");
		FieldUtil.print(displayReport);
	}
	
}
