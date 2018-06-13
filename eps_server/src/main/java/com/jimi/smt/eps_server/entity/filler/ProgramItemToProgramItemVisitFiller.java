package com.jimi.smt.eps_server.entity.filler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.jimi.smt.eps_server.entity.ProgramItem;
import com.jimi.smt.eps_server.entity.ProgramItemVisit;
import com.jimi.smt.eps_server.util.EntityFieldFiller;

@Component
public class ProgramItemToProgramItemVisitFiller extends EntityFieldFiller<ProgramItem, ProgramItemVisit> {

	@Override
	public ProgramItemVisit fill(ProgramItem programItem) {
		ProgramItemVisit programItemVisit = new ProgramItemVisit();
		BeanUtils.copyProperties(programItem, programItemVisit);
		Method[] methods = ProgramItemVisit.class.getMethods();
		for (Method method : methods) {
			if(method.getName().endsWith("Result") && method.getName().startsWith("set")){
				try {
					method.invoke(programItemVisit, new Integer(2));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			if(method.getName().endsWith("Time") && method.getName().startsWith("set")){
				try {
					method.invoke(programItemVisit, new Date());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		programItemVisit.setLastOperationTime(null);
		return programItemVisit;
	}

}
