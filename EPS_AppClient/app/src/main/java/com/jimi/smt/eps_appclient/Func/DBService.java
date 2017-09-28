package com.jimi.smt.eps_appclient.Func;

import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Unit.OperLogItem;
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
     * 获取要发送短信的患者信息    查
     * */

    public List<MaterialItem> getMaterial(String strLineNo){
        //结果存放集合
        List<MaterialItem> list=new ArrayList<MaterialItem>();
        //MySQL 语句
        String sql="select program_item.lineseat as lineseat,program_item.material_no as material from program_item,program where program_id=id and line='"+strLineNo+"'";
        //获取链接数据库对象
        conn= DBOpenHelper.getConn();
        try {
            if(conn!=null&&(!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                if(ps!=null){
                    rs= (ResultSet) ps.executeQuery();
                    if(rs!=null){
                        while(rs.next()){
                            MaterialItem materialItem=new MaterialItem();
                            materialItem.setOrgLineSeat(rs.getString("lineseat"));
                            materialItem.setOrgMaterial(rs.getString("material"));
//                            u.setId(rs.getString("id"));
//                            u.setName(rs.getString("name"));
//                            u.setPhone(rs.getString("phone"));
//                            u.setContent(rs.getString("content"));
//                            u.setState(rs.getString("state"));
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
     * 批量向数据库插入数据   增
     * */

    public int inserOpertLog(List<OperLogItem> list){
        int result=-1;
        if((list!=null)&&(list.size()>0)){
            //获取链接数据库对象
            conn= DBOpenHelper.getConn();
            //MySQL 语句
            String sql="INSERT INTO operation (operator,time,type,result,lineseat,material_no,old_material_no,scanlineseat,remark) VALUES (?,?,?,?,?,?,?,?,?)";
            try {
                boolean closed=conn.isClosed();
                if((conn!=null)&&(!closed)){
                    for(OperLogItem operLogItem:list){
                        ps= (PreparedStatement) conn.prepareStatement(sql);
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
