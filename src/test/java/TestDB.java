import it.unipv.DB.DBConnection;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.ResultSet;
import java.sql.SQLException;

@RunWith(JUnit4.class)
public class TestDB extends TestCase {

    @Test
    public void provaLettura() {
        DBConnection dbConnection = new DBConnection();
        try {
            writeResultSet(dbConnection.getResultFromQuery("select * from z6xOH9WKhI.PIANTINE"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.close();
        }
    }

       private void writeResultSet(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            System.out.println("Nome Sala: " + resultSet.getString("NOME_SALA"));
            System.out.println("Nome Posto: " + resultSet.getString("NOME_POSTO"));
            System.out.println("Coordinata X: " + resultSet.getString("COORD_X"));
            System.out.println("Coordinata Y: " + resultSet.getString("COORD_Y"));
        }
    }
}
