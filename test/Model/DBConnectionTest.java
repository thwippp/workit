package Model;

import static Model.DBConnection.conn;
import com.mysql.cj.jdbc.ConnectionImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Schaffeld, B. (000790777);
 */
public class DBConnectionTest {

    public DBConnectionTest() {
    }

    /**
     * Test of getConnection method, of class DBConnection.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetConnectionValidUsernameValidPassword() throws Exception {
        boolean expResult = true;
        boolean result = DBConnection.getConnection().getClass() == ConnectionImpl.class;
        assertEquals(expResult, result);
    }

    @Test
    public void testGetConnectionValidUsernameInvalidPassword() throws Exception {
        boolean expResult = false;
        String username = "t";
        String password = "test";
        try {
            boolean result = dbConnectionVariables(username, password).getClass() == ConnectionImpl.class;
            assertEquals(expResult, result);
        } catch (SQLException e) {
            assertEquals(expResult, false);
        }
    }

    @Test
    public void testGetConnectionInvalidUsernameValidPassword() throws Exception {
        boolean expResult = false;
        String username = "asdf";
        String password = "test";
        try {
            boolean result = dbConnectionVariables(username, password).getClass() == ConnectionImpl.class;
            assertEquals(expResult, result);
        } catch (SQLException e) {
            assertEquals(expResult, false);
        }
    }

    @Test
    public void testGetConnectionInvalidUsernameInvalidPassword() throws Exception {
        boolean expResult = false;
        String username = "asdf";
        String password = "asdf";
        try {
            boolean result = dbConnectionVariables(username, password).getClass() == ConnectionImpl.class;
            assertEquals(expResult, result);
        } catch (SQLException e) {
            assertEquals(expResult, false);
        }
    }

    /**
     * Test of closeConnection method, of class DBConnection.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCloseConnection() throws Exception {
        Connection conn = DBConnection.getConnection();
        boolean expResult = true;
        boolean actual = DBConnection.closeConnection();
        assertEquals(expResult, actual);
    }

    public Connection dbConnectionVariables(String user, String password) throws SQLException, ClassNotFoundException {
        String DB = "test";
        String IP = "localhost";
        String URL = "jdbc:mysql://" + IP + ":3306/" + DB;
        String USER = user;
        String PASSWORD = password;
        String DRIVER = "com.mysql.cj.jdbc.Driver";

        Class.forName(DRIVER);
        conn = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setCatalog(DB);  // Sets default schema to be the same as the constant above

        System.out.println("Connection Made!");
        return conn;
    }

}
