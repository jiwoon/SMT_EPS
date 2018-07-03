package com.jimi.smt.eps_server.entity.vo;

import java.util.List;

import com.jimi.smt.eps_server.entity.Page;

public class PageVO<T> {

	private Page page;
	
	private List<T> list;

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
			
}
