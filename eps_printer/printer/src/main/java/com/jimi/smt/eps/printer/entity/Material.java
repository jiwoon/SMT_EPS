package com.jimi.smt.eps.printer.entity;

import com.jimi.smt.eps.printer.util.ExcelHelper.Excel;

/**
 * 物料实体类
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class Material {

	@Excel(col=1, head="物料编码")
	private String no;
	@Excel(col=2, head="名称")
	private String name;
	@Excel(col=3, head="描述")
	private String description;
	@Excel(col=4, head="位号")
	private String seat;
	@Excel(col=5, head="数量")
	private String quantity;
	@Excel(col=6, head="备注")
	private String remark;
	
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSeat() {
		return seat;
	}
	public void setSeat(String seat) {
		this.seat = seat;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
