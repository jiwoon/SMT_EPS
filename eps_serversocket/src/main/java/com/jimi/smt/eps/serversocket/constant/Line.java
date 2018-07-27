package com.jimi.smt.eps.serversocket.constant;


/**
 * 线别枚举 <br>
 * <b>2017年12月29日</b>
 * 
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public enum Line {
	L30X(0), L301(1), L302(2), L303(3), L304(4), L305(5), L306(6), L307(7), L308(8);
    private int index;
    private Line(int index) {
        this.index = index;
    }
    
    public static Line getLine(int i) {
        for(Line line : Line.values()) {
            if(line.index == i) {
                return line;
            }
        }
        return null;
    }
    
    public int getIndex() {
        return this.index;
    }
    
}
