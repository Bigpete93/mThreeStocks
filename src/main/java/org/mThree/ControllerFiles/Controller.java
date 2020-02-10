package org.mThree.ControllerFiles;

import java.sql.*;
import java.util.ArrayList;

public class Controller {

        static String dbUrl = "jdbc:mysql://54.145.49.152:3306/project";
        static String username = "mysql";
        static String password = "mysql";

        public void setDataByWeek(String s, double open, double high, double low, double close, long volume){
            String insertStmt = "INSERT INTO week_data VALUES (?, ?, ?, ?, ?, ?)";
            try(Connection con = DriverManager.getConnection(dbUrl,username,password);
                PreparedStatement insertByWeek = con.prepareStatement(insertStmt)
            ){
                insertByWeek.setDate(1, java.sql.Date.valueOf(s));
                insertByWeek.setDouble(2, open);
                insertByWeek.setDouble(3, high);
                insertByWeek.setDouble(4, low);
                insertByWeek.setDouble(5, close);
                insertByWeek.setLong(6, volume);

                insertByWeek.executeUpdate();
            } catch (SQLException e) {e.printStackTrace();}
        }

        public void setDataByDay(String s, double open, double high, double low, double close, long volume){
            String insertStmt = "INSERT INTO day_data VALUES (?, ?, ?, ?, ?, ?)";
            try(Connection con = DriverManager.getConnection(dbUrl,username,password);
                PreparedStatement insertByDay = con.prepareStatement(insertStmt)
            ){
                insertByDay.setDate(1, java.sql.Date.valueOf(s));
                insertByDay.setDouble(2, open);
                insertByDay.setDouble(3, high);
                insertByDay.setDouble(4, low);
                insertByDay.setDouble(5, close);
                insertByDay.setLong(6, volume);

                insertByDay.executeUpdate();
            } catch (SQLException e) {e.printStackTrace();}
        }

        public void setDataBy5Min(String s, double open, double high, double low, double close, long volume){
            String insertStmt = "INSERT INTO five_min_data VALUES (?, ?, ?, ?, ?, ?)";

            String [] timeAndDate = s.split(" ");
            String date = timeAndDate[0];
            String time = timeAndDate[1];

            try(Connection con = DriverManager.getConnection(dbUrl,username,password);
                PreparedStatement insertBy5Min = con.prepareStatement(insertStmt)
            ){
                insertBy5Min.setDate(1, java.sql.Date.valueOf(date));
                insertBy5Min.setTime(2, java.sql.Time.valueOf(time));
                insertBy5Min.setDouble(3, open);
                insertBy5Min.setDouble(4, high);
                insertBy5Min.setDouble(5, low);
                insertBy5Min.setDouble(6, close);
                insertBy5Min.setLong(7, volume);

                insertBy5Min.executeUpdate();

            } catch (SQLException e) {e.printStackTrace();}
        }

        public String getDataByWeek(String s){
            String query = "SELECT * FROM week_data WHERE week_date = ?";
            String result = null;

            try(Connection con = DriverManager.getConnection(dbUrl,username,password);
                PreparedStatement selectStmt = con.prepareStatement(query)
            ){

                selectStmt.setDate(1, java.sql.Date.valueOf(s));
                ResultSet rs = selectStmt.executeQuery();

                result = rs.getDouble("open") + ", "+
                        rs.getDouble("high") + ", "+
                        rs.getDouble("low") + ", "+
                        rs.getDouble("close") + ", " +
                        rs.getLong("volume");

            } catch (SQLException se) {se.printStackTrace();
            } catch (Exception e) {return null;}

            return result;
        }

       public String getDataByDay(String s){
           String query = "SELECT * FROM day_data WHERE day_date = ?";
           String result = null;

           try(Connection con = DriverManager.getConnection(dbUrl,username,password);
               PreparedStatement selectStmt= con.prepareStatement(query)
           ){

               selectStmt.setDate(1, java.sql.Date.valueOf(s));
               ResultSet rs = selectStmt.executeQuery();

               result = rs.getDouble("open") + ", "+
                       rs.getDouble("high") + ", "+
                       rs.getDouble("low") + ", "+
                       rs.getDouble("close") + ", " +
                       rs.getLong("volume");

           } catch (SQLException se) {se.printStackTrace();
           } catch (Exception e) {return null;}

           return result;
       }

        public String getDataBy5Min(String s){
            String query = "SELECT * FROM week_data WHERE five_min_date = ? AND five_min_time = ?";
            String result = null;

            String [] timeAndDate = s.split(" ");
            String date = timeAndDate[0];
            String time = timeAndDate[1];

            try(Connection con = DriverManager.getConnection(dbUrl, username, password);
                PreparedStatement selectStmt= con.prepareStatement(query)
            ){

                selectStmt.setDate(1, java.sql.Date.valueOf(date));
                selectStmt.setTime(2, java.sql.Time.valueOf(time));
                ResultSet rs = selectStmt.executeQuery();

                result = rs.getDouble("open") + ", "+
                        rs.getDouble("high") + ", "+
                        rs.getDouble("low") + ", "+
                        rs.getDouble("close") + ", " +
                        rs.getLong("volume");

            } catch (SQLException se) {se.printStackTrace();
            } catch (Exception e) {return null;}

            return result;
         }

         public ArrayList<String> getAll5Min() {
             ArrayList result = new ArrayList<String>();

             try {Connection con = DriverManager.getConnection(dbUrl, username, password);
                 String query = "SELECT * FROM five_min_data";

                 PreparedStatement selectStmt = con.prepareStatement(query);

                 ResultSet rs = selectStmt.executeQuery();

                 while (rs.next()) {
                         String temp = rs.getString("five_min_date") + ", " +
                                 rs.getString("five_min_time") + ", " +
                                 rs.getString("high") + ", " +
                                 rs.getString("low") + ", " +
                                 rs.getString("open") + ", " +
                                 rs.getString("close") + ", " +
                                 rs.getString("volume");
                         result.add(temp);
                     }

             }

             catch (SQLException e) {
                 e.printStackTrace();
             }

             return result;
         }

         public ArrayList<String> getAllDay() {
             ArrayList result = new ArrayList<String>();

             try {Connection con = DriverManager.getConnection(dbUrl, username, password);
                 String query = "SELECT * FROM day_data";

                 PreparedStatement selectStmt = con.prepareStatement(query);

                 ResultSet rs = selectStmt.executeQuery();

                 while (rs.next()) {
                     String temp = rs.getString("day_date") + ", " +
                             rs.getString("high") + ", " +
                             rs.getString("low") + ", " +
                             rs.getString("open") + ", " +
                             rs.getString("close") + ", " +
                             rs.getString("volume");
                     result.add(temp);
                 }

             }

             catch (SQLException e) {
                 e.printStackTrace();
             }

             return result;
         }

         public ArrayList<String> getAllWeek() {
             ArrayList result = new ArrayList<String>();

             try {Connection con = DriverManager.getConnection(dbUrl, username, password);
                 String query = "SELECT * FROM week_data";

                 PreparedStatement selectStmt = con.prepareStatement(query);

                 ResultSet rs = selectStmt.executeQuery();

                 while (rs.next()) {
                     String temp = rs.getString("week_date") + ", " +
                             rs.getString("high") + ", " +
                             rs.getString("low") + ", " +
                             rs.getString("open") + ", " +
                             rs.getString("close") + ", " +
                             rs.getString("volume");
                     result.add(temp);
                 }

             }

             catch (SQLException e) {
                 e.printStackTrace();
             }

             return result;
         }

    }
