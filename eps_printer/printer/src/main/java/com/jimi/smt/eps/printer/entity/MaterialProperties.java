package com.jimi.smt.eps.printer.entity;

import javafx.beans.property.SimpleStringProperty;

/**
 * 物料实体类属性集
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class MaterialProperties {

	private SimpleStringProperty no;
	
	private SimpleStringProperty name;
	
	private SimpleStringProperty description;
	
	private SimpleStringProperty seat;
	
	private SimpleStringProperty quantity;
	
	private SimpleStringProperty remark;

	
	public MaterialProperties(Material material) {
		no = new SimpleStringProperty(material.getNo());
		name = new SimpleStringProperty(material.getName());
		description = new SimpleStringProperty(material.getDescription());
		seat = new SimpleStringProperty(material.getSeat());
		quantity = new SimpleStringProperty(material.getQuantity());
		remark = new SimpleStringProperty(material.getRemark());
	}


	public String getNo() {
		return no.get();
	}


	public String getName() {
		return name.get();
	}


	public String getDescription() {
		return description.get();
	}


	public String getSeat() {
		return seat.get();
	}


	public String getQuantity() {
		return quantity.get();
	}


	public String getRemark() {
		return remark.get();
	}
	
}
