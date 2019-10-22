package View_Controller;

import Model.DBConnection;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Schaffeld, B. (000790777);
 */
public class LoginControllerTest {

    public LoginControllerTest() {
    }

    /**
     * Test of loginButtonAction.
     */
    @Test
    public void loginButtonActionValidUsernameValidPassword() throws IOException, SQLException, Exception {
        String username = "test";
        String password = "test";
        boolean expected = true;
        boolean isAuthenticated = false;
        isAuthenticated = testLoginAuthentication(username, password, isAuthenticated);
        assertEquals(expected, isAuthenticated);
    }

    @Test
    public void loginButtonActionValidUsernameInvalidPassword() throws IOException, SQLException, Exception {
        String username = "test";
        String password = "t";
        boolean expected = false;
        boolean isAuthenticated = false;
        isAuthenticated = testLoginAuthentication(username, password, isAuthenticated);
        assertEquals(expected, isAuthenticated);
    }

    @Test
    public void loginButtonActionInvalidUsernameValidPassword() throws IOException, SQLException, Exception {
        String username = "t";
        String password = "test";
        boolean expected = false;
        boolean isAuthenticated = false;
        isAuthenticated = testLoginAuthentication(username, password, isAuthenticated);
        assertEquals(expected, isAuthenticated);
    }

    @Test
    public void loginButtonActionInvalidUsernameInvalidPassword() throws IOException, SQLException, Exception {
        String username = "asdf";
        String password = "asdf";
        boolean expected = false;
        boolean isAuthenticated = false;
        isAuthenticated = testLoginAuthentication(username, password, isAuthenticated);
        assertEquals(expected, isAuthenticated);
    }

    public boolean testLoginAuthentication(String username, String password, boolean isAuthenticated) throws Exception {
        Connection conn = DBConnection.getConnection();
        String q = "{call authenticateUser(?,?)}";
        CallableStatement cs;
        cs = conn.prepareCall(q);
        cs.setString(1, username);
        cs.setString(2, password);
        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            isAuthenticated = rs.getBoolean("IsAuthenticated");
        }
        return isAuthenticated;
    }

}
