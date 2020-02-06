package org.mThree;

import javax.swing.plaf.nimbus.State;

import static java.lang.System.*;

import java.sql.*;

/**
     *
     * @author pooya
     */
    public class Controller {

        static private Connection con;
        { try { con = DriverManager.getConnection(
                "jdbc:mysql://3.89.243.51:3306/employees","mysql","mysql");
            } catch (SQLException e) { e.printStackTrace();} }


    public static void main(String[] args) {
            System.out.println("asdas");
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery("SELECT * FROM employees");
                while(rs.next())
                    System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
                con.close();
            }catch(Exception e){ System.out.println(e);}
        }

        void setDataByWeek(String s, double open, double high, double low, double close, int volume) throws SQLException {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO table_name VALUES (?, ?, ?, ?, ?, ?) ");

            stmt.setString(1,s);
            stmt.setDouble(2, open);
            stmt.setDouble(3, high);
            stmt.setDouble(4, low);
            stmt.setDouble(5, close);
            stmt.setInt(6, volume);
        }


    }

