package Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Schaffeld, B. (000790777);
 */
public class MYSQL {

    private Statement stmt;
    private ResultSet rs;

    public boolean addUsersFromQuery(String query) throws Exception {
        try {
            // opening database connection to MySQL server 
            Connection conn = DBConnection.getConnection();
            // getting Statement object to execute query 
            stmt = conn.createStatement();
            // executing SELECT query 
            rs = stmt.executeQuery(query);

            ResultSetMetaData rsmd = rs.getMetaData();

            int rsCols = getColumnCount(rs);

            while (rs.next()) {

//                ArrayList<String> row = new ArrayList<String>();
//
//                for (int i = 0; i < rsCols; i++) {
//                    row.add(rs.getString(i + 1));
//                }
                Master.addUser(
                        new User(
                                rs.getInt("id"),
                                rs.getString("firstName"),
                                rs.getString("lastName"),
                                rs.getString("username"),
                                rs.getString("email")
                        )
                );

            }

        } catch (Exception ex) {
            // do nothing
            System.out.println("Catch...");
            return false;
        }
        System.out.println("return...");
        DBConnection.closeConnection();
        return true;

    }

    public boolean addTasksFromQuery(String query) throws Exception {
        try {
            System.out.println("Starting addTasksFromQuery...");

            // opening database connection to MySQL server 
            Connection conn = DBConnection.getConnection();
            // getting Statement object to execute query 
            stmt = conn.createStatement();
            // executing SELECT query 
            rs = stmt.executeQuery(query);

            ResultSetMetaData rsmd = rs.getMetaData();

            int rsCols = getColumnCount(rs);

            while (rs.next()) {

                Master.addTask(
                        new Task(rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getDate("dueDate"),
                                rs.getDouble("scope"),
                                rs.getDouble("severity"),
                                rs.getDouble("priority"),
                                rs.getInt("userId"),
                                rs.getString("username")
                        )
                );

            }

        } catch (Exception ex) {
            // do nothing
            System.out.println("Catch...");
            System.out.println(ex);
            return false;
        }
        System.out.println("return...");
        DBConnection.closeConnection();
        return true;

    }

    public ObservableList<ArrayList> query(String query) throws Exception {

        ObservableList<ArrayList> table = FXCollections.observableArrayList();

        System.out.println("Starting try...");
        try {
            // opening database connection to MySQL server 
            Connection conn = DBConnection.getConnection();
            // getting Statement object to execute query 
            stmt = conn.createStatement();
            // executing SELECT query 
            rs = stmt.executeQuery(query);

            ResultSetMetaData rsmd = rs.getMetaData();

            int rsCols = getColumnCount(rs);

            for (int i = 0; i < rsCols; i++) {
                String colName = rsmd.getColumnName(i + 1);
            }

            while (rs.next()) {
                ArrayList<String> row = new ArrayList<String>();

                for (int i = 0; i < rsCols; i++) {
                    row.add(rs.getString(i + 1));
                }
                table.add(row);
            }

        } catch (Exception e) {
            // do nothing
            System.out.println("Catch...");
            System.out.println(e);
            return null;
        }
        System.out.println("return...");
        DBConnection.closeConnection();
        return table;
    }

    public boolean booleanQuery(String query) throws Exception {
        // opening database connection to MySQL server 
        Connection conn = DBConnection.getConnection();
        // getting Statement object to execute query 
        stmt = conn.createStatement();
        // executing SELECT query 
        rs = stmt.executeQuery(query);

        ResultSetMetaData rsmd = rs.getMetaData();

        int rsCols = getColumnCount(rs);

        while (rs.next()) {

            ArrayList<String> row = new ArrayList<String>();

            for (int i = 0; i < rsCols; i++) {
                String cString = rs.getString(i + 1);
                row.add(cString); // DOES SOMETHING.... TODO

            }

            // TODO SOMETHING
        }
        System.out.println("return...");
        DBConnection.closeConnection();
        return true;
    }

    public static int getColumnCount(ResultSet resultSet) throws SQLException {
        return resultSet.getMetaData().getColumnCount();
    }

    public static int getRowCount(ResultSet resultSet) {
        int size = 0;

        try {
            resultSet.last();
            size = resultSet.getRow();
            resultSet.beforeFirst();
        } catch (SQLException ex) {
            return 0;
        }
        return size;
    }

}
