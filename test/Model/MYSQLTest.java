package Model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Schaffeld, B. (000790777);
 */
public class MYSQLTest {
    
    public MYSQLTest() {
    }

    /**
     * Test of query method, of class MYSQL.
     */
    @Test
    public void testQuery() throws Exception {
        MYSQL m = new MYSQL();
        boolean expected = true;
        boolean actual = m.query("select * from task limit 1;");
        assertEquals(expected, actual);
    }
    
}
