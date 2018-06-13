package com.jimi.smt.eps_appclient.Unit;

/**
 * 类名:Program
 * 创建人:Liang GuoChang
 * 创建时间:2017/10/24 13:14
 * 描述:数据库中 program 表对象
 * 版本号: v-1.0
 * 修改记录:
 */

public class Program {
    //文件id
    private String programID;
    //工单号
    private String work_order;
    //版面类型
    private int board_type;
    //线号
    private String line;
    //选中状态
    private boolean checked;

    public String getProgramID() {
        return programID;
    }

    public void setProgramID(String programID) {
        this.programID = programID;
    }

    public String getWork_order() {
        return work_order;
    }

    public void setWork_order(String work_order) {
        this.work_order = work_order;
    }

    public int getBoard_type() {
        return board_type;
    }

    public void setBoard_type(int board_type) {
        this.board_type = board_type;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
