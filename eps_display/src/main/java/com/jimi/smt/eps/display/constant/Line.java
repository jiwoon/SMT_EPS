package com.jimi.smt.eps.display.constant;

/**
 * 线别枚举 <br>
 * <b>2017年12月29日</b>
 * 
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public enum Line {

	LX(0), L1(1), L2(2), L3(3), L4(4), L5(5), L6(6), L7(7), L8(8);
	
	private int index;
	
	private Line(int index){
        this.index = index;
    }
	
	public static Line getLine(int i) {
		for (Line line : Line.values()) {
			if (line.index == i) {
				return line;
			}
		}
		return null;
	}
}
