package com.jimi.smt.eps_appclient.Func;

import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.EpsAppVersion;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Unit.OperLogItem;
import com.jimi.smt.eps_appclient.Unit.Operator;
import com.jimi.smt.eps_appclient.Unit.Program;
import com.jimi.smt.eps_appclient.Unit.ProgramItemVisit;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 类名:DBService
 * 创建人:Liang GuoChang
 * 创建时间:2017/9/25 17:10
 * 描述:
 * 版本号:
 * 修改记录:
 */
public class DBService {
    private final String TAG = "DBService";
    private Connection conn = null; //打开数据库对象
    private PreparedStatement ps = null;//操作整合sql语句的对象
    private ResultSet rs = null;//查询结果的集合

    //DBService 对象
    private static DBService dbService = null;

    /**
     * 构造方法 私有化
     */
    public DBService() {

    }

    /**
     * 获取MySQL数据库单例类对象
     */
    public static DBService getDbService() {
        if (dbService == null) {
            dbService = new DBService();
        }
        return dbService;
    }


    /**
     * 获取获取对应工单号的料号表
     *
     * @param line
     * @param order
     * @param boardType
     * @return
     * @ 3/28
     */
    public List<MaterialItem> getMaterial(String line, String order, int boardType) {
        //结果存放集合
        List<MaterialItem> list = new ArrayList<MaterialItem>();
        String sql = "select program_item.lineseat, program_item.material_no, program_item.serial_no,program_item.alternative from program_item,program where program_id = id and line = ? and work_order = ? and board_type = ? and state = 1 ORDER BY lineseat,serial_no,material_no";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1, line);
                ps.setString(2, order);
                ps.setInt(3, boardType);
                if (ps != null) {
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            MaterialItem materialItem = new MaterialItem();
                            materialItem.setOrder(order);
                            materialItem.setBoardType(boardType);
                            materialItem.setLine(line);
                            materialItem.setSerialNo(rs.getInt("serial_no"));
                            materialItem.setAlternative(rs.getBytes("alternative")[0]);
                            materialItem.setOrgLineSeat(rs.getString("lineseat"));
                            materialItem.setOrgMaterial(rs.getString("material_no"));
                            materialItem.setScanLineSeat("");
                            materialItem.setScanMaterial("");
                            materialItem.setResult("");
                            materialItem.setRemark("");

//                            MaterialItem feedMaterialItem = new MaterialItem(materialItem.getOrder(),materialItem.getBoardType(),
//                                    materialItem.getLine(),materialItem.getSerialNo(),materialItem.getAlternative(),materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(),
//                                    "", "", "", "");

                            list.add(materialItem);
                        }
                    }
                }
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, "getMaterial - " + e.toString());
        } finally {
            DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        }
        return list;
    }

    /**
     * 获取站位表全部操作结果 todo
     *
     * @param line
     * @param order
     * @param boardType
     * @return
     * @ 3/28
     */
    public ArrayList<ProgramItemVisit> getProgramItemVisits(String line, String order, int boardType) {
        ArrayList<ProgramItemVisit> list = new ArrayList<ProgramItemVisit>();
        String sql = "SELECT program_id,lineseat,material_no,scan_lineseat,scan_material_no,store_issue_result,feed_result,change_result,check_result,check_all_result,first_check_all_result FROM program_item_visit,program WHERE program_id = id AND line = ? AND work_order = ? AND board_type = ? AND state = 1";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1, line);
                ps.setString(2, order);
                ps.setInt(3, boardType);
                if (ps != null) {
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            ProgramItemVisit itemVisit = new ProgramItemVisit();
                            itemVisit.setProgram_id(rs.getString("program_id"));
                            itemVisit.setLineseat(rs.getString("lineseat"));
                            itemVisit.setMaterial_no(rs.getString("material_no"));
                            itemVisit.setScan_lineseat(rs.getString("scan_lineseat"));
                            itemVisit.setScan_material_no(rs.getString("scan_material_no"));
                            itemVisit.setStore_issue_result(rs.getInt("store_issue_result"));
                            itemVisit.setFeed_result(rs.getInt("feed_result"));
                            itemVisit.setChange_result(rs.getInt("change_result"));
                            itemVisit.setCheck_result(rs.getInt("check_result"));
                            itemVisit.setCheck_all_result(rs.getInt("check_all_result"));
                            itemVisit.setFirst_check_all_result(rs.getInt("first_check_all_result"));
                            list.add(itemVisit);
                        }
                    }
                }
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        }
        return list;
    }

    /**
     * 插入日志到数据库
     *
     * @param list
     * @return　int
     * @ 3/28
     */
    public int inserOpertLog(List<OperLogItem> list) {
        int result = -1;
        if ((list != null) && (list.size() > 0)) {
            //获取链接数据库对象
            conn = DBOpenHelper.getConn();
            //MySQL 语句
            String sql = "INSERT INTO operation (operator,time,type,result,lineseat,material_no,old_material_no,scanlineseat,remark,program_id,line,work_order,board_type) VALUES (?,NOW(),?,?,?,?,?,?,?,?,?,?,?)";
            try {
                if ((conn != null) && (!conn.isClosed())) {
                    for (OperLogItem operLogItem : list) {
                        ps = (PreparedStatement) conn.prepareStatement(sql);
                        Log.d(TAG, "inserOpertLog::" + operLogItem.getType());
                        String FileId = operLogItem.getFileId();
                        String Operator = operLogItem.getOperator();
                        int Type = operLogItem.getType();
                        String Result = operLogItem.getResult();
                        String Lineseat = operLogItem.getLineseat();
                        String material_no = operLogItem.getMaterial_no();
                        String oldMaterialNo = operLogItem.getOld_material_no();
                        String ScanLineseat = operLogItem.getScanLineseat();
                        String Remark = operLogItem.getRemark();
                        String line = operLogItem.getLine();
                        String work_order = operLogItem.getWork_order();
                        int board_type = operLogItem.getBoard_type();

                        ps.setString(1, Operator);
                        ps.setInt(2, Type);
                        ps.setString(3, Result);
                        ps.setString(4, Lineseat);
                        ps.setString(5, material_no);
                        ps.setString(6, oldMaterialNo);
                        ps.setString(7, ScanLineseat);
                        ps.setString(8, Remark);
                        ps.setString(9, FileId);
                        ps.setString(10, line);
                        ps.setString(11, work_order);
                        ps.setInt(12, board_type);
                        result = ps.executeUpdate();//返回1 执行成功
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
            }
        }
        return result;
    }

    /**
     * 根据工单号、线号、版面类型、状态
     * 获取工单id
     *
     * @param order
     * @param line
     * @param boardType
     * @param state
     * @return
     */
    public String getOrderId(String order, String line, int boardType, int state) {
        String sql = "SELECT id FROM program WHERE work_order = ? AND board_type = ? AND line = ? AND state = ?";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        String orderId = "";
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1, order);
                ps.setInt(2, boardType);
                ps.setString(3, line);
                ps.setInt(4, state);
                if (ps != null) {
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            orderId = rs.getString("id");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        }
        return orderId;
    }

    /**
     * 更新操作日志
     *
     * @param programItemVisit
     * @return int
     * @ 3/28
     */
    public int updateItemVisitLog(ProgramItemVisit programItemVisit) {
        int result = -1;
        String sql = "";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                Log.d(TAG, "updateItemVisitLog::" + programItemVisit.getLast_operation_type());
                switch (programItemVisit.getLast_operation_type()) {
                    case 0://上料
                        sql = "UPDATE program_item_visit SET last_operation_type = 0, last_operation_time = NOW(), feed_result = ?,feed_time = NOW(),scan_lineseat = ?,scan_material_no = ? WHERE program_id = ? AND lineseat = ? AND material_no = ?";
                        ps = (PreparedStatement) conn.prepareStatement(sql);
                        ps.setInt(1, programItemVisit.getFeed_result());
                        ps.setString(2, programItemVisit.getScan_lineseat());
                        ps.setString(3, programItemVisit.getScan_material_no());
                        ps.setString(4, programItemVisit.getProgram_id());
                        ps.setString(5, programItemVisit.getLineseat());
                        ps.setString(6, programItemVisit.getMaterial_no());
                        break;
                    case 1://换料,置核料为初始状态
                        sql = "UPDATE program_item_visit SET last_operation_type = 1, last_operation_time = NOW(), change_result = ?,check_result = 2,change_time = NOW(),scan_lineseat = ?,scan_material_no = ? WHERE program_id = ? AND lineseat = ?";
                        ps = (PreparedStatement) conn.prepareStatement(sql);
                        ps.setInt(1, programItemVisit.getChange_result());
                        ps.setString(2, programItemVisit.getScan_lineseat());
                        ps.setString(3, programItemVisit.getScan_material_no());
                        ps.setString(4, programItemVisit.getProgram_id());
                        ps.setString(5, programItemVisit.getLineseat());
                        break;
                    case 2://核料
                        if (programItemVisit.getCheck_result() == 0) {
                            sql = "UPDATE program_item_visit SET last_operation_type = 2, last_operation_time = NOW(), check_result = ?,check_time = NOW(),scan_lineseat = ?,scan_material_no = ? WHERE program_id = ? AND lineseat = ?";
                            ps = (PreparedStatement) conn.prepareStatement(sql);
                            ps.setInt(1, programItemVisit.getCheck_result());
                            ps.setString(2, programItemVisit.getScan_lineseat());
                            ps.setString(3, programItemVisit.getScan_material_no());
                            ps.setString(4, programItemVisit.getProgram_id());
                            ps.setString(5, programItemVisit.getLineseat());
                        }
                        //成功时同时置换料成功
                        else if (programItemVisit.getCheck_result() == 1) {
                            sql = "UPDATE program_item_visit SET last_operation_type = 2, last_operation_time = NOW(),change_result = ?,check_result = ?,check_time = NOW(),scan_lineseat = ?,scan_material_no = ? WHERE program_id = ? AND lineseat = ?";
                            ps = (PreparedStatement) conn.prepareStatement(sql);
                            ps.setInt(1, programItemVisit.getChange_result());
                            ps.setInt(2, programItemVisit.getCheck_result());
                            ps.setString(3, programItemVisit.getScan_lineseat());
                            ps.setString(4, programItemVisit.getScan_material_no());
                            ps.setString(5, programItemVisit.getProgram_id());
                            ps.setString(6, programItemVisit.getLineseat());
                        }

                        break;
                    case 3://全检
                        sql = "UPDATE program_item_visit SET last_operation_type = 3, last_operation_time = NOW(), check_all_result = ?,check_all_time = NOW(),scan_lineseat = ?,scan_material_no = ? WHERE program_id = ? AND lineseat = ? AND material_no = ?";
                        ps = (PreparedStatement) conn.prepareStatement(sql);
                        ps.setInt(1, programItemVisit.getCheck_all_result());
                        ps.setString(2, programItemVisit.getScan_lineseat());
                        ps.setString(3, programItemVisit.getScan_material_no());
                        ps.setString(4, programItemVisit.getProgram_id());
                        ps.setString(5, programItemVisit.getLineseat());
                        ps.setString(6, programItemVisit.getMaterial_no());
                        break;
                    case 4://发料
                        sql = "UPDATE program_item_visit SET last_operation_type = 4, last_operation_time = NOW(), store_issue_result = ?,store_issue_time = NOW(),scan_lineseat = ?,scan_material_no = ? WHERE program_id = ? AND lineseat = ?";
                        ps = (PreparedStatement) conn.prepareStatement(sql);
                        ps.setInt(1, programItemVisit.getStore_issue_result());
                        ps.setString(2, programItemVisit.getScan_lineseat());
                        ps.setString(3, programItemVisit.getScan_material_no());
                        ps.setString(4, programItemVisit.getProgram_id());
                        ps.setString(5, programItemVisit.getLineseat());
                        break;
                    case 5://首检
                        sql = "UPDATE program_item_visit SET last_operation_type = 5, last_operation_time = NOW(), first_check_all_result = ?,first_check_all_time = NOW(),scan_lineseat = ?,scan_material_no = ? WHERE program_id = ? AND lineseat = ? AND material_no = ?";
                        ps = (PreparedStatement) conn.prepareStatement(sql);
                        ps.setInt(1, programItemVisit.getFirst_check_all_result());
                        ps.setString(2, programItemVisit.getScan_lineseat());
                        ps.setString(3, programItemVisit.getScan_material_no());
                        ps.setString(4, programItemVisit.getProgram_id());
                        ps.setString(5, programItemVisit.getLineseat());
                        ps.setString(6, programItemVisit.getMaterial_no());
                        break;
                }

                result = ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        }
        return result;
    }

    /**
     * 查询某站位的是否发料(包括替换料情况)
     *
     * @param materialItem
     * @return List<Byte>
     * @ 3/28 已修改
     */
    public List<Integer> getStoreResult(MaterialItem materialItem) {
        List<Integer> storeResult = new ArrayList<Integer>();
        String sql = "SELECT program_item_visit.store_issue_result FROM program_item_visit,program WHERE program_id = id AND lineseat = ? AND line = ? AND work_order = ? AND board_type = ? AND state = 1";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1, materialItem.getOrgLineSeat());
                ps.setString(2, materialItem.getLine());
                ps.setString(3, materialItem.getOrder());
                ps.setInt(4, materialItem.getBoardType());
                if (ps != null) {
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            storeResult.add(rs.getInt("store_issue_result"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, "SQLException-" + e.toString());
        } finally {
            DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        }
        return storeResult;
    }

    /**
     * 判断是否重置了
     *
     * @param line
     * @param order
     * @param boardType
     * @return 1 表示重置了; 0 表示未重置
     */
    public int isProgramVisitReseted(String line, String order, int boardType, int operateType) {
        Log.d(TAG, "isProgramVisitReseted");
        List<Integer> results = new ArrayList<Integer>();
        List<Integer> lastTimes = new ArrayList<Integer>();
        int lastResult = 0;
        int result = 1;
        int last_time = 0;
        String sql = "";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        switch (operateType) {
            case Constants.FEEDMATERIAL:
                sql = "select feed_result,last_operation_time from program_item_visit where program_id = (select id from program where line = ? and work_order = ? and board_type = ? AND state = 1)";
                break;

            case Constants.CHECKALLMATERIAL:
                sql = "select check_all_result,last_operation_time from program_item_visit where program_id = (select id from program where line = ? and work_order = ? and board_type = ? AND state = 1)";
                break;

            case Constants.FIRST_CHECK_ALL:
                sql = "select first_check_all_result,last_operation_time from program_item_visit where program_id = (select id from program where line = ? and work_order = ? and board_type = ? AND state = 1)";
                break;
        }
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1, line);
                ps.setString(2, order);
                ps.setInt(3, boardType);
                if (ps != null) {
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            int operateResult = rs.getInt(1);
                            Timestamp last_operation_time = rs.getTimestamp(2);
                            results.add(operateResult);
                            if (last_operation_time == null) {
                                lastTimes.add(0);
                            } else {
                                lastTimes.add(1);
                            }
                        }
                        for (Integer integer : results) {
                            if ((integer != 2) && (integer != 3)) {
                                result = 0;
                                break;
                            }
                        }

                        for (Integer integer : lastTimes) {
                            if (integer == 1) {
                                last_time = 1;
                                break;
                            }
                        }
                        if (result == 1 && last_time == 1) {
                            lastResult = 1;
                        }

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, "SQLException-" + e.toString());
        } finally {
            DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        }
        return lastResult;
    }

    /**
     * 在首检完成后重置全检的时间
     * @param line
     * @param order
     * @param boardType
     * @return
     */
    public int resetCheckAllTime(String line, String order, int boardType){
        int result = -1;
        String sql = "update program_item_visit set check_all_time = now(),check_all_result = 1 where program_id = (select id from program where line = ? and work_order = ? and board_type = ? AND state = 1)";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1, line);
                ps.setString(2, order);
                ps.setInt(3, boardType);
                if (ps != null) {
                    result = ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, "SQLException-" + e.toString());
        } finally {
            DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        }
        return result;
    }

    /**
     * 再次上料,初始化全部操作结果
     *
     * @param line order boardType
     * @return
     * @ 3/29
     */
    public int initAllResult(String line, String order, int boardType) {
        int result = -1;
        String sql = "UPDATE program_item_visit SET last_operation_type = null,last_operation_time = NOW(),feed_result = 2,feed_time = NOW(),change_result = 2,change_time = NOW(),check_result = 2,check_time = NOW(),check_all_result = 2,check_all_time = NOW(),first_check_all_result = 2,first_check_all_time = NOW() WHERE program_id = (select id from program where line = ? and work_order = ? and board_type = ? AND state = 1)";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1, line);
                ps.setString(2, order);
                ps.setInt(3, boardType);
                if (ps != null) {
                    result = ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, "SQLException-" + e.toString());
        } finally {
            DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        }
        return result;
    }

    /**
     * 核料时做判断
     * 获取某站位的换料结果(包括替换料情况)
     *
     * @param materialItem
     * @return List<Byte>
     */
    public List<Integer> getChangeResult(MaterialItem materialItem) {
        List<Integer> changeResult = new ArrayList<Integer>();
        String sql = "SELECT program_item_visit.change_result FROM program_item_visit,program WHERE program_id = id AND lineseat = ? AND line = ? AND work_order = ? AND board_type = ? AND state = 1";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1, materialItem.getOrgLineSeat());
                ps.setString(2, materialItem.getLine());
                ps.setString(3, materialItem.getOrder());
                ps.setInt(4, materialItem.getBoardType());
                if (ps != null) {
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            changeResult.add(rs.getInt("change_result"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, "SQLException-" + e.toString());
        } finally {
            DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        }
        return changeResult;
    }


    /**
     * 查询某站位的是否首次全检(包括替换料情况)
     * @param materialItem
     * @return List<Byte>
     */
    /*
    public List<Byte> getFirstCheckAllResult(MaterialItem materialItem){
        Log.d(TAG,"materialItem-"+materialItem.getFileId());
        Log.d(TAG,"materialItem-"+materialItem.getOrgLineSeat());
        List<Byte> storeResult = new ArrayList<Byte>();
        String sql = "SELECT program_item_visit.first_check_all_result FROM program_item_visit WHERE program_id = ? AND lineseat = ?";
        //获取链接数据库对象
        conn= DBOpenHelper.getConn();
        try {
            if(conn!=null && (!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1,materialItem.getFileId());
                ps.setString(2,materialItem.getOrgLineSeat());
                if (ps != null){
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null){
                        while (rs.next()){
                            boolean result = storeResult.add(rs.getByte("first_check_all_result"));
                            Log.d(TAG,"first_check_all_result-"+rs.getByte("first_check_all_result")+"-result-"+result);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG,"SQLException-"+e.toString());
        }
        return storeResult;
    }
    */


    /**
     * 判断某个programId的站位表是否全部进行了上料
     *
     * @param line, order, boardType
     * @return
     * @ 3/29
     */
    public boolean isFeeded(String line, String order, int boardType) {
        boolean result = true;
        String sql = "SELECT program_item_visit.feed_result FROM program_item_visit,program WHERE program_id = id AND line = ? AND work_order = ? AND board_type = ? AND state = 1";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1, line);
                ps.setString(2, order);
                ps.setInt(3, boardType);
                if (ps != null) {
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            if (rs.getInt("feed_result") != 1) {
                                result = false;
                                Log.d(TAG, "feed_result-" + rs.getInt("feed_result") + "-result-" + result);
                                return false;
                            }
                        }
                    } else {
                        Log.d(TAG, "rs == null");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, "SQLException-" + e.toString());
        } finally {
            DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        }
        return result;
    }

    /**
     * 判断某个的站位表是否全部进行了首次全检
     *
     * @param line, order, boardType
     * @return boolean
     * @ 3/29
     */
    public boolean isOrderFirstCheckAll(String line, String order, int boardType) {
        boolean result = true;
        String sql = "SELECT program_item_visit.first_check_all_result FROM program_item_visit,program WHERE program_id = id AND line = ? AND work_order = ? AND board_type = ? AND state = 1";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1, line);
                ps.setString(2, order);
                ps.setInt(3, boardType);
                if (ps != null) {
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            if (rs.getInt("first_check_all_result") != 1) {
                                result = false;
                                return false;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, "SQLException-" + e.toString());
        } finally {
            DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        }
        return true;
    }

    /**
     * @param operatorId 操作员ID
     * @return 操作员
     * @author Liang GuoChang
     * @描述 根据操作员ID 查找该操作员
     */
    public Operator getCurOperator(String operatorId) {
        Operator operator = null;
        String sqlStr = "SELECT * from user WHERE id=?";
        conn = DBOpenHelper.getConn();
        try {
            if ((conn != null) && !(conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sqlStr);
                if (ps != null) {
                    ps.setString(1, operatorId);
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            operator = new Operator();
                            operator.setId(rs.getString("id"));
                            operator.setEnabled(rs.getBytes("enabled")[0]);
                            operator.setName(rs.getString("name"));
                            operator.setType(rs.getInt("type"));
                            operator.setPwd(rs.getString("password"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
//            e.printStackTrace();
            Log.d(TAG, "getCurOperator-" + e.toString());
        } finally {
            DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        }
        return operator;
    }

    /**
     * 获取对应线号的所有工单号
     *
     * @return List<Program>
     */
    public List<Program> getProgramOrder(String line) {
        List<Program> programList = new ArrayList<Program>();
        String sql = "select program.id,program.work_order,program.board_type,program.line from program where line=? and state=1";
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && !(conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1, line);
                if (ps != null) {
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            Program program = new Program();
                            program.setProgramID(rs.getString("id"));
                            program.setWork_order(rs.getString("work_order"));
                            program.setBoard_type(rs.getInt("board_type"));
                            program.setLine(rs.getString("line"));
                            program.setChecked(false);
                            programList.add(program);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        }

        return programList;
    }

    //获取数据库中版本信息的最后一条,即是最新的一条
    public EpsAppVersion getAppVersion() {
        EpsAppVersion epsAppVersion = null;
        String sql = "SELECT epsAppVersion.version_code,epsAppVersion.version_name,epsAppVersion.version_des FROM epsAppVersion ORDER BY id DESC LIMIT 1";
        conn = DBOpenHelper.getConn();
        try {
            if ((conn != null) && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                if (ps != null) {
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            epsAppVersion = new EpsAppVersion();
                            epsAppVersion.setVersionCode(rs.getInt("version_code"));
                            epsAppVersion.setVersionName(rs.getString("version_name"));
                            epsAppVersion.setVersionDes(rs.getString("version_des"));
                            Log.d(TAG, rs.getInt("version_code"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            epsAppVersion = null;
            Log.d(TAG, "SQLException---" + e);
        } finally {
            DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        }
        return epsAppVersion;
    }

}
