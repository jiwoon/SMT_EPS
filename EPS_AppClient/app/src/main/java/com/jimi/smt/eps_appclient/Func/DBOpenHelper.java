package com.jimi.smt.eps_appclient.Func;

import com.mysql.jdbc.Connection;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.mysql.jdbc.PreparedStatement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *类名:DBOpenHelper
 *创建人:Liang GuoChang
 *创建时间:2017/9/25 9:22
 *描述:
 *版本号:
 *修改记录:
 */
public class DBOpenHelper {
    private static String TAG = "DBOpenHelper";
    private static String driver = "com.mysql.jdbc.Driver";//MySQL 驱动
//    private static String url = "jdbc:mysql://39.108.231.15:3306/smt_eps";//MYSQL数据库连接Url
//    private static String url = "jdbc:mysql://10.10.11.111:3306/smt_eps";//MYSQL测试数据库连接Url
//    private static String url = "jdbc:mysql://192.168.2.9:3306/smt_eps";//工厂用内网
//    private static String url = "jdbc:mysql://120.198.40.186:28883/smt_eps";//工厂用外网
//    private static String user = "root";//用户名
//    private static String password = "newttl!@#$1234";//密码

/*

    private static String url = "jdbc:mysql://10.10.11.120:3306/smt_eps";//本地
    private static String user = "root";//用户名
    private static String password = "123456";//密码
*/

    /**
     * 连接数据库
     */

    public static Connection getConn(){
        Connection conn = null;
        try {
            Class.forName(driver);//获取MYSQL驱动
            conn = (Connection) DriverManager.getConnection(Constants.dataBaseUrl, Constants.user, Constants.password);//获取连接
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG,"getConn - "+e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG,"getConn - "+e.getMessage());
        }
        return conn;
    }

    /**
     * 关闭数据库
     * */

    public static void closeAll(Connection conn, PreparedStatement ps){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 关闭数据库
     */

    public static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
