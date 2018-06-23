package com.mark.bus;

import java.sql.*;

public class Database {

    private Connection conn = null;

    protected Database(String user_name, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cloud", user_name, password);
        } catch (SQLException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ResultSet runSql(String sql) throws SQLException {
        Statement sta = conn.createStatement();
        return sta.executeQuery(sql);
    }

    public int runPreparedSql(String sql) throws SQLException {
        Statement st = conn.createStatement();
        return st.executeUpdate(sql);
    }



    @Override
    protected void finalize() throws Throwable {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

}
