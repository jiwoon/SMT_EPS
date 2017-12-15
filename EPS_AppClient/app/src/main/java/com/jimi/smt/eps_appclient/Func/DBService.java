package com.jimi.smt.eps_appclient.Func;

import com.jimi.smt.eps_appclient.Unit.EpsAppVersion;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Unit.OperLogItem;
import com.jimi.smt.eps_appclient.Unit.Operator;
import com.jimi.smt.eps_appclient.Unit.Program;
import com.jimi.smt.eps_appclient.Unit.ProgramItemVisit;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by think on 2017/9/25.
 */
public class DBService {
    private final String TAG="DBService";
    private Connection conn = null; //打开数据库对象
    private PreparedStatement ps = null;//操作整合sql语句的对象
    private ResultSet rs = null;//查询结果的集合

    //DBService 对象
    public static DBService dbService = null;

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
     * @param programID
     * @return List<MaterialItem>
     */

    public List<MaterialItem> getMaterial(String programID){
        //结果存放集合
        List<MaterialItem> list=new ArrayList<MaterialItem>();
        //MySQL 语句 22ee4c9f99c9421cb48920d37acd4590
//        String sql="select program_item.program_id, program_item.lineseat as lineseat,program_item.material_no as material from program_item,program where program_id=id and line='"+strLineNo+"'";
        String sql="select program_item.program_id, program_item.lineseat, program_item.material_no from program_item where program_id=?";
        //获取链接数据库对象
        conn= DBOpenHelper.getConn();
        try {
            if(conn!=null && (!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1,programID);
                if(ps!=null){
                    rs= (ResultSet) ps.executeQuery();
                    if(rs!=null){
                        while(rs.next()){
                            MaterialItem materialItem=new MaterialItem();
                            materialItem.setFileId(rs.getString("program_id"));
                            materialItem.setOrgLineSeat(rs.getString("lineseat"));
                            materialItem.setOrgMaterial(rs.getString("material_no"));
                            list.add(materialItem);
                        }
                    }
                }
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn,ps,rs);//关闭相关操作
        return list;
    }

    /**
     *插入日志到数据库
     * @param list
     * @return　int
     */
    public int inserOpertLog(List<OperLogItem> list){
        int result=-1;
        if((list!=null)&&(list.size()>0)){
            //获取链接数据库对象
            conn= DBOpenHelper.getConn();
            //MySQL 语句
            String sql="INSERT INTO operation (operator,time,type,result,lineseat,material_no,old_material_no,scanlineseat,remark,program_id,line,work_order,board_type) VALUES (?,NOW(),?,?,?,?,?,?,?,?,?,?,?)";
            try {
                boolean closed=conn.isClosed();
                if((conn!=null)&&(!closed)){
                    for(OperLogItem operLogItem:list){
                        ps= (PreparedStatement) conn.prepareStatement(sql);
                        Log.d(TAG,"inserOpertLog::"+operLogItem.getType());
                        String FileId = operLogItem.getFileId();
                        String Operator = operLogItem.getOperator();
//                        Timestamp Time = operLogItem.getTime();
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

                        ps.setString(1, Operator);//第一个参数 name 规则同上
//                        ps.setTimestamp(2, Time);
                        ps.setInt(2, Type);
                        ps.setString(3,Result);//第四个参数 state 规则同上
                        ps.setString(4,Lineseat);//第二个参数 phone 规则同上
                        ps.setString(5,material_no);//第三个参数 content 规则同上
                        ps.setString(6,oldMaterialNo);//第四个参数 state 规则同上
                        ps.setString(7,ScanLineseat);
                        ps.setString(8,Remark);
                        ps.setString(9,FileId);
                        ps.setString(10,line);
                        ps.setString(11,work_order);
                        ps.setInt(12,board_type);
                        result=ps.executeUpdate();//返回1 执行成功
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }

    /**
     * 更新操作日志
     * @param programItemVisit
     * @return int
     */
    public int updateItemVisitLog(ProgramItemVisit programItemVisit){
        int result=-1;
        String sql = "";
        //获取链接数据库对象
        conn= DBOpenHelper.getConn();
        try {
            if(conn!=null && (!conn.isClosed())){
                Log.d(TAG,"updateItemVisitLog::"+programItemVisit.getLast_operation_type());
                switch (programItemVisit.getLast_operation_type()){
                    case 0://上料
                        sql="UPDATE program_item_visit SET last_operation_type = 0, last_operation_time = NOW(), feed_result = ?,feed_time = NOW(),scan_lineseat = ?,scan_material_no = ? WHERE program_id = ? AND lineseat = ? AND material_no = ?";
                        ps= (PreparedStatement) conn.prepareStatement(sql);
//                        ps.setTimestamp(1,programItemVisit.getLast_operation_time());
                        ps.setByte(1,programItemVisit.getFeed_result());
//                        ps.setTimestamp(3,programItemVisit.getFeed_time());
                        ps.setString(2,programItemVisit.getScan_lineseat());
                        ps.setString(3,programItemVisit.getScan_material_no());
                        ps.setString(4,programItemVisit.getProgram_id());
                        ps.setString(5,programItemVisit.getLineseat());
                        ps.setString(6,programItemVisit.getMaterial_no());
                        break;
                    case 1://换料,同时核料置为false
                        sql="UPDATE program_item_visit SET last_operation_type = 1, last_operation_time = NOW(), change_result = ?,check_result = ?,change_time = NOW(),scan_lineseat = ?,scan_material_no = ? WHERE program_id = ? AND lineseat = ?";
                        ps= (PreparedStatement) conn.prepareStatement(sql);
//                        ps.setTimestamp(1,programItemVisit.getLast_operation_time());
                        ps.setByte(1,programItemVisit.getChange_result());
                        ps.setByte(2,programItemVisit.getCheck_result());
//                        ps.setTimestamp(3,programItemVisit.getChange_time());
                        ps.setString(3,programItemVisit.getScan_lineseat());
                        ps.setString(4,programItemVisit.getScan_material_no());
                        ps.setString(5,programItemVisit.getProgram_id());
                        ps.setString(6,programItemVisit.getLineseat());
//                        ps.setString(8,programItemVisit.getMaterial_no());
                        break;
                    case 2://抽检
                        sql="UPDATE program_item_visit SET last_operation_type = 2, last_operation_time = NOW(), check_result = ?,check_time = NOW(),scan_lineseat = ?,scan_material_no = ? WHERE program_id = ? AND lineseat = ?";
                        ps= (PreparedStatement) conn.prepareStatement(sql);
//                        ps.setTimestamp(1,programItemVisit.getLast_operation_time());
                        ps.setByte(1,programItemVisit.getCheck_result());
//                        ps.setTimestamp(3,programItemVisit.getCheck_time());
                        ps.setString(2,programItemVisit.getScan_lineseat());
                        ps.setString(3,programItemVisit.getScan_material_no());
                        ps.setString(4,programItemVisit.getProgram_id());
                        ps.setString(5,programItemVisit.getLineseat());
//                        ps.setString(8,programItemVisit.getMaterial_no());
                        break;
                    case 3://全检
                        sql="UPDATE program_item_visit SET last_operation_type = 3, last_operation_time = NOW(), check_all_result = ?,check_all_time = NOW(),scan_lineseat = ?,scan_material_no = ? WHERE program_id = ? AND lineseat = ? AND material_no = ?";
                        ps= (PreparedStatement) conn.prepareStatement(sql);
//                        ps.setTimestamp(1,programItemVisit.getLast_operation_time());
                        ps.setByte(1,programItemVisit.getCheck_all_result());
//                        ps.setTimestamp(3,programItemVisit.getCheck_all_time());
                        ps.setString(2,programItemVisit.getScan_lineseat());
                        ps.setString(3,programItemVisit.getScan_material_no());
                        ps.setString(4,programItemVisit.getProgram_id());
                        ps.setString(5,programItemVisit.getLineseat());
                        ps.setString(6,programItemVisit.getMaterial_no());
                        break;
                    case 4://发料
                        sql="UPDATE program_item_visit SET last_operation_type = 4, last_operation_time = NOW(), store_issue_result = ?,store_issue_time = NOW(),scan_lineseat = ?,scan_material_no = ? WHERE program_id = ? AND lineseat = ?";
                        ps= (PreparedStatement) conn.prepareStatement(sql);
//                        ps.setTimestamp(1,programItemVisit.getLast_operation_time());
                        ps.setByte(1,programItemVisit.getStore_issue_result());
//                        ps.setTimestamp(3,programItemVisit.getStore_issue_time());
                        ps.setString(2,programItemVisit.getScan_lineseat());
                        ps.setString(3,programItemVisit.getScan_material_no());
                        ps.setString(4,programItemVisit.getProgram_id());
                        ps.setString(5,programItemVisit.getLineseat());
//                        ps.setString(8,programItemVisit.getMaterial_no());
                        break;
                    case 5://首检
                        sql="UPDATE program_item_visit SET last_operation_type = 5, last_operation_time = NOW(), first_check_all_result = ?,first_check_all_time = NOW(),scan_lineseat = ?,scan_material_no = ? WHERE program_id = ? AND lineseat = ? AND material_no = ?";
                        ps= (PreparedStatement) conn.prepareStatement(sql);
//                        ps.setTimestamp(1,programItemVisit.getLast_operation_time());
                        ps.setByte(1,programItemVisit.getFirst_check_all_result());
//                        ps.setTimestamp(3,programItemVisit.getFirst_check_all_time());
                        ps.setString(2,programItemVisit.getScan_lineseat());
                        ps.setString(3,programItemVisit.getScan_material_no());
                        ps.setString(4,programItemVisit.getProgram_id());
                        ps.setString(5,programItemVisit.getLineseat());
                        ps.setString(6,programItemVisit.getMaterial_no());
                        break;
                }

                result = ps.executeUpdate();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }

    /**
     * 查询某站位的是否发料(包括替换料情况)
     * @param materialItem
     * @return List<Byte>
     */
     public List<Byte> getStoreResult(MaterialItem materialItem){
         Log.d(TAG,"materialItem-"+materialItem.getFileId());
         Log.d(TAG,"materialItem-"+materialItem.getOrgLineSeat());
         List<Byte> storeResult = new ArrayList<Byte>();
         String sql = "SELECT program_item_visit.store_issue_result FROM program_item_visit WHERE program_id = ? AND lineseat = ?";
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
                             boolean result = storeResult.add(rs.getByte("store_issue_result"));
                             Log.d(TAG,"store_issue_result-"+rs.getByte("store_issue_result")+"-result-"+result);
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

    /**
     * 查询某站位的是否上料(包括替换料情况)
     * @param materialItem
     * @return List<Byte>
     */
    public List<Byte> getFeedResult(MaterialItem materialItem){
        Log.d(TAG,"materialItem-"+materialItem.getFileId());
        Log.d(TAG,"materialItem-"+materialItem.getOrgLineSeat());
        List<Byte> storeResult = new ArrayList<Byte>();
        String sql = "SELECT program_item_visit.feed_result FROM program_item_visit WHERE program_id = ? AND lineseat = ?";
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
                            boolean result = storeResult.add(rs.getByte("feed_result"));
                            Log.d(TAG,"feed_result-"+rs.getByte("feed_result")+"-result-"+result);
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


    /**
     * 查询某站位的是否首次全检(包括替换料情况)
     * @param materialItem
     * @return List<Byte>
     */
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

    /**
     * 判断某个programId的站位表是否全部进行了上料
     * @param programId
     * @return
     */
    public boolean isFeeded(String programId){
        boolean result = true;
        String sql = "SELECT program_item_visit.feed_result FROM program_item_visit WHERE program_id = ?";
        //获取链接数据库对象
        conn= DBOpenHelper.getConn();
        try {
            if(conn!=null && (!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1,programId);
                if (ps != null){
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null){
//                        Log.d(TAG,"programId:"+programId);
//                        Log.d(TAG,"rs.next():"+rs.next());
                        while (rs.next()){
//                            Log.d(TAG,"feed_result:"+rs.next());
//                            Log.d(TAG,"feed_result-"+rs.getBoolean("feed_result"));
                            if (rs.getByte("feed_result") == 0){
                                result = false;
                                Log.d(TAG,"feed_result-"+rs.getByte("feed_result")+"-result-"+ result);
                                return false;
                            }
                        }
                    }else {
                        Log.d(TAG,"rs == null");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG,"SQLException-"+e.toString());
        }
        return result;
    }

    /**
     * 判断某个programId的站位表是否全部进行了首次全检
     * @param programId
     * @return boolean
     */
    public boolean isOrderFirstCheckAll(String programId){
        boolean result = true;
        String sql = "SELECT program_item_visit.first_check_all_result FROM program_item_visit WHERE program_id = ?";
        //获取链接数据库对象
        conn= DBOpenHelper.getConn();
        try {
            if(conn!=null && (!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1,programId);
                if (ps != null){
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null){
//                        Log.d(TAG,"programId:"+programId);
//                        Log.d(TAG,"rs.next():"+rs.next());
                        while (rs.next()){
//                            Log.d(TAG,"first_check_all_result:"+rs.next());
//                            Log.d(TAG,"first_check_all_result-"+rs.getBoolean("first_check_all_result"));
                            if (rs.getByte("first_check_all_result") == 0){
                                result = false;
                                Log.d(TAG,"first_check_all_result-"+rs.getByte("first_check_all_result")+"-result-"+ result);
                                return false;
                            }
                        }
                    }else {
                        Log.d(TAG,"rs == null");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG,"SQLException-"+e.toString());
        }
//        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }

    /**
     * @author Liang GuoChang
     * @描述 根据操作员ID 查找该操作员
     * @param operatorId 操作员ID
     * @return 操作员
     */
    public Operator getCurOperator(String operatorId){
        Operator operator=null;
        String sqlStr="SELECT * from user WHERE id=?";
        conn=DBOpenHelper.getConn();
        if ((conn != null) && !(conn.isClosed())){
            try {
                ps= (PreparedStatement) conn.prepareStatement(sqlStr);
                if (ps != null){
                    ps.setString(1,operatorId);
                    rs= (ResultSet) ps.executeQuery();
                    if (rs != null){
                        while (rs.next()){
                            operator=new Operator();
                            operator.setId(rs.getString("id"));
                            operator.setEnabled(rs.getBytes("enabled")[0]);
                            operator.setName(rs.getString("name"));
                            operator.setType(rs.getInt("type"));
                            operator.setPwd(rs.getString("password"));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return operator;
    }

    /**
     * 获取对应线号的所有工单号
     * @return List<Program>
     */
    public List<Program> getProgramOrder(String line){
        List<Program> programList=new ArrayList<Program>();
        String sql="select program.id,program.work_order,program.board_type,program.line from program where line=? and state=1";
//        String sql="select program.id,program.work_order,program.board_type,program.line from program where line=?";
        conn=DBOpenHelper.getConn();
        try {
            if (conn != null && !(conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1,line);
                if (ps != null){
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null){
                        while (rs.next()){
                            Program program=new Program();
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
        }
        DBOpenHelper.closeAll(conn,ps);//关闭相关操作

        return programList;
    }

    //获取数据库中版本信息的最后一条,即是最新的一条
    public EpsAppVersion getAppVersion(){
        EpsAppVersion epsAppVersion=null;
        String sql="SELECT epsAppVersion.version_code,epsAppVersion.version_name,epsAppVersion.version_des FROM epsAppVersion ORDER BY id DESC LIMIT 1";
        conn=DBOpenHelper.getConn();
        try {
            if ((conn != null) && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                if (ps != null){
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null){
                        while (rs.next()){
                            epsAppVersion=new EpsAppVersion();
                            epsAppVersion.setVersionCode(rs.getInt("version_code"));
                            epsAppVersion.setVersionName(rs.getString("version_name"));
                            epsAppVersion.setVersionDes(rs.getString("version_des"));
                            Log.d(TAG,rs.getInt("version_code"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            epsAppVersion = null;
            Log.d(TAG,"SQLException---"+e);
        }
        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return  epsAppVersion;
    }

}
