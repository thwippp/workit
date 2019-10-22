package Model;

import com.mysql.cj.jdbc.ConnectionImpl;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Schaffeld, B. (000790777);
 */
public class DBConnection {

    // Connection credentials
    private static final String DB = "workit";
    private static final String IP = "localhost";
    private static final String URL = "jdbc:mysql://"+ IP +":3306/" + DB;
    private static final String USER = "test";  // TODO-- make config file or default user
    private static final String PASSWORD = "test";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    // Connection
    public static Connection conn;

    public static Connection getConnection() throws Exception {
        Class.forName(DRIVER);
        conn = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setCatalog(DB);  // Sets default schema to be the same as the constant above

        System.out.println("Connection Made!");
        return conn;
    }

    public static boolean closeConnection() throws Exception {
        DBConnection.conn.close();
        System.out.println("Connection closed? "  + conn.isClosed());
        return DBConnection.conn.isClosed();
    }

}
