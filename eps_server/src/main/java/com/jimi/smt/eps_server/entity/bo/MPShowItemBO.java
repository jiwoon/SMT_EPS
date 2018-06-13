package com.jimi.smt.eps_server.entity.bo;

/**
 * 公粽号报表子项目业务层对象（按线别分组）
 * <br>
 * <b>2017年12月25日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class MPShowItemBO {
	
	private String line;

	private int allsSuc;

	private int allsFail;

	private int feedSuc;

	private int feedFail;

	private int changeSuc;

	private int changeFail;

	private int someSuc;

	private int someFail;

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public int getAllsSuc() {
		return allsSuc;
	}

	public void setAllsSuc(int allsSuc) {
		this.allsSuc = allsSuc;
	}

	public int getAllsFail() {
		return allsFail;
	}

	public void setAllsFail(int allsFail) {
		this.allsFail = allsFail;
	}

	public int getFeedSuc() {
		return feedSuc;
	}

	public void setFeedSuc(int feedSuc) {
		this.feedSuc = feedSuc;
	}

	public int getFeedFail() {
		return feedFail;
	}

	public void setFeedFail(int feedFail) {
		this.feedFail = feedFail;
	}

	public int getChangeSuc() {
		return changeSuc;
	}

	public void setChangeSuc(int changeSuc) {
		this.changeSuc = changeSuc;
	}

	public int getChangeFail() {
		return changeFail;
	}

	public void setChangeFail(int changeFail) {
		this.changeFail = changeFail;
	}

	public int getSomeSuc() {
		return someSuc;
	}

	public void setSomeSuc(int someSuc) {
		this.someSuc = someSuc;
	}

	public int getSomeFail() {
		return someFail;
	}

	public void setSomeFail(int someFail) {
		this.someFail = someFail;
	}
	
	
	
}
