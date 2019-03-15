package com.focussend.mysql.testor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by steven.zhao on 2019/2/25.
 */

public class Test1 {
    private Connection mConnection;
    PreparedStatement mPreparedStatement;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Test1 testor=new Test1();
        testor.saveToDb();
    }

    /**
     *  构建一个连接
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void buildConnection() throws SQLException, ClassNotFoundException {
        if (mConnection == null) {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            mConnection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mycat",
//                    "root", "123456");

//            mPreparedStatement = (PreparedStatement) mConnection.prepareStatement("INSERT INTO mycat.name " +
//                    " (name) values (?)");

            //mycat test
            mConnection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:8066/TESTDB",
                    "root", "123456");

            mPreparedStatement = (PreparedStatement) mConnection.prepareStatement("INSERT INTO contact(id,email) " +
                    "  values (?,?)");

        }
    }

    /**
     * 写入数据库
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void saveToDb() throws SQLException, ClassNotFoundException {
        buildConnection();

        for(int i=0;i<10;i++) {
            mPreparedStatement.setInt(1,i);
            mPreparedStatement.setString(2, "steven-"+i);
            int inserted = mPreparedStatement.executeUpdate();
            System.out.println("inserted:" + inserted);
        }
    }

}

