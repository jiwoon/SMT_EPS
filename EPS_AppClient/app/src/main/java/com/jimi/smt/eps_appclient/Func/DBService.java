package com.jimi.smt.eps_appclient.Func;

import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Unit.OperLogItem;
import com.jimi.smt.eps_appclient.Unit.Operator;
import com.jimi.smt.eps_appclient.Unit.Program;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by think on 2017/9/25.
 */
public class DBService {
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
            String sql="INSERT INTO operation (operator,time,type,result,lineseat,material_no,old_material_no,scanlineseat,remark,fileid) VALUES (?,?,?,?,?,?,?,?,?,?)";
            try {
                boolean closed=conn.isClosed();
                if((conn!=null)&&(!closed)){
                    for(OperLogItem operLogItem:list){
                        ps= (PreparedStatement) conn.prepareStatement(sql);
                        String FileId=operLogItem.getFileId();
                        String Operator=operLogItem.getOperator();
                        Timestamp Time=operLogItem.getTime();
                        int Type=operLogItem.getType();
                        String Result=operLogItem.getResult();
                        String Lineseat=operLogItem.getLineseat();
                        String material_no=operLogItem.getMaterial_no();
                        String oldMaterialNo=operLogItem.getOld_material_no();
                        String ScanLineseat=operLogItem.getScanLineseat();
                        String Remark=operLogItem.getRemark();

                        ps.setString(1, Operator);//第一个参数 name 规则同上
                        ps.setTimestamp(2, Time);
                        ps.setInt(3, Type);
                        ps.setString(4,Result);//第四个参数 state 规则同上
                        ps.setString(5,Lineseat);//第二个参数 phone 规则同上
                        ps.setString(6,material_no);//第三个参数 content 规则同上
                        ps.setString(7,oldMaterialNo);//第四个参数 state 规则同上
                        ps.setString(8,ScanLineseat);
                        ps.setString(9,Remark);
                        ps.setString(10,FileId);
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
     * 获取所有工单号
     * @return List<Program>
     */
    public List<Program> getProgramOrder(){
        List<Program> programList=new ArrayList<Program>();
        String sql="select program.id,program.work_order from program";
        conn=DBOpenHelper.getConn();
        try {
            if (conn != null && !(conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                if (ps != null){
                    rs = (ResultSet) ps.executeQuery();
                    if (rs != null){
                        while (rs.next()){
                            Program program=new Program();
                            program.setProgramID(rs.getString("id"));
                            program.setWork_order(rs.getString("work_order"));
                            programList.add(program);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return programList;
    }

    /**
     * 修改数据库中某个对象的状态   改
     * */

//    public int updateUserData(String phone){
//        int result=-1;
//        if(!StringUtils.isEmpty(phone)){
//            //获取链接数据库对象
//            conn= DBOpenHelper.getConn();
//            //MySQL 语句
//            String sql="update user set state=? where phone=?";
//            try {
//                boolean closed=conn.isClosed();
//                if(conn!=null&&(!closed)){
//                    ps= (PreparedStatement) conn.prepareStatement(sql);
//                    ps.setString(1,"1");//第一个参数state 一定要和上面SQL语句字段顺序一致
//
//
//                    ps.setString(2,phone);//第二个参数 phone 一定要和上面SQL语句字段顺序一致
//
//                    result=ps.executeUpdate();//返回1 执行成功
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
//        return result;
//    }

    /**
     * 批量向数据库插入数据   增
     * */

//    public int insertUserData(List<User> list){
//        int result=-1;
//        if((list!=null)&&(list.size()>0)){
//            //获取链接数据库对象
//            conn= DBOpenHelper.getConn();
//            //MySQL 语句
//            String sql="INSERT INTO user (name,phone,content,state) VALUES (?,?,?,?)";
//            try {
//                boolean closed=conn.isClosed();
//                if((conn!=null)&&(!closed)){
//                    for(User user:list){
//                        ps= (PreparedStatement) conn.prepareStatement(sql);
//                        String name=user.getName();
//                        String phone=user.getPhone();
//                        String content=user.getContent();
//                        String state=user.getState();
//                        ps.setString(1,name);//第一个参数 name 规则同上
//                        ps.setString(2,phone);//第二个参数 phone 规则同上
//                        ps.setString(3,content);//第三个参数 content 规则同上
//                        ps.setString(4,state);//第四个参数 state 规则同上
//                        result=ps.executeUpdate();//返回1 执行成功
//                    }
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
//        return result;
//
//
//
//    }

    /**
     * 删除数据  删
     * */

//    public int delUserData(String phone){
//        int result=-1;
//        if((!StringUtils.isEmpty(phone))&&(PhoneNumberUtils.isMobileNumber(phone))){
//            //获取链接数据库对象
//            conn= DBOpenHelper.getConn();
//            //MySQL 语句
//            String sql="delete from user where phone=?";
//            try {
//                boolean closed=conn.isClosed();
//                if((conn!=null)&&(!closed)){
//                    ps= (PreparedStatement) conn.prepareStatement(sql);
//                    ps.setString(1, phone);
//                    result=ps.executeUpdate();//返回1 执行成功
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
//        return result;
//    }
}
