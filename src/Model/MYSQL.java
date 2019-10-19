package Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Schaffeld, B. (000790777);
 */
public class MYSQL {

    private Statement stmt;
    private ResultSet rs;

    public boolean query(String query) throws Exception {
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
                row.add(rs.getString(i + 1)); // DOES SOMETHING.... TODO
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
