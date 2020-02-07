package org.mThree.ControllerFiles;

import java.sql.*;

    public class Controller {

        void setDataByWeek(String s, double open, double high, double low, double close, int volume){
            String insertStmt = "INSERT INTO week_data VALUES (?, ?, ?, ?, ?, ?)";
            try(Connection con = DriverManager.getConnection(
                    "jdbc:mysql://3.89.243.51:3306/employees","mysql","mysql");
                    PreparedStatement insertByWeek =
                            con.prepareStatement(insertStmt)
            ){
                insertByWeek.setString(1, s);
                insertByWeek.setDouble(2, open);
                insertByWeek.setDouble(3, high);
                insertByWeek.setDouble(4, low);
                insertByWeek.setDouble(5, close);
                insertByWeek.setInt(6, volume);

                insertByWeek.executeUpdate();
            } catch (SQLException e) {e.printStackTrace();}
        }

        void setDataByDay(String s, double open, double high, double low, double close, int volume){
            String insertStmt = "INSERT INTO day_data VALUES (?, ?, ?, ?, ?, ?)";
            try(Connection con = DriverManager.getConnection(
                    "jdbc:mysql://3.89.243.51:3306/employees","mysql","mysql");
                PreparedStatement insertByDay =
                        con.prepareStatement(insertStmt)
            ){
                insertByDay.setString(1, s);
                insertByDay.setDouble(2, open);
                insertByDay.setDouble(3, high);
                insertByDay.setDouble(4, low);
                insertByDay.setDouble(5, close);
                insertByDay.setInt(6, volume);

                insertByDay.executeUpdate();
            } catch (SQLException e) {e.printStackTrace();}
        }

        void setDataBy5Min(String s, double open, double high, double low, double close, int volume){
            String insertStmt = "INSERT INTO 5_min_data VALUES (?, ?, ?, ?, ?, ?)";

            try(Connection con = DriverManager.getConnection(
                    "jdbc:mysql://3.89.243.51:3306/employees","mysql","mysql");
                PreparedStatement insertBy5Min =
                        con.prepareStatement(insertStmt)
            ){
                insertBy5Min.setString(1, s);
                insertBy5Min.setDouble(2, open);
                insertBy5Min.setDouble(3, high);
                insertBy5Min.setDouble(4, low);
                insertBy5Min.setDouble(5, close);
                insertBy5Min.setInt(6, volume);

                insertBy5Min.executeUpdate();

            } catch (SQLException e) {e.printStackTrace();}
        }

        String getDataByWeek(String s){
            String query = "SELECT * FROM week_data WHERE week = ?";
            String result = null;

            try(Connection con = DriverManager.getConnection(
                    "jdbc:mysql://3.89.243.51:3306/employees","mysql","mysql");
                PreparedStatement selectStmt=
                        con.prepareStatement(query)
            ){

                selectStmt.setString(1, s);
                ResultSet rs = selectStmt.executeQuery();

                result = rs.getDouble("open") + ", "+
                        rs.getDouble("high") + ", "+
                        rs.getDouble("low") + ", "+
                        rs.getDouble("close") + ", " +
                        rs.getLong("volume");

            } catch (SQLException e) {e.printStackTrace();}

            return result;
        }

       // String getDdataByDay(String s){}

        //String getDataBy5Min(String s){}

        /*
        List<String> getAllWeek();
        List<String> getAllDay();
        List<String> getAll5Min();
        */
        public static void main(String[] args) {
            System.out.println("asdas");
            try{
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://3.89.243.51:3306/employees","mysql","mysql");

                Class.forName("com.mysql.jdbc.Driver");
                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery("SELECT * FROM employees");

                while(rs.next())
                    System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
                con.close();
            }catch(Exception e){ System.out.println(e);}
        }

    }

