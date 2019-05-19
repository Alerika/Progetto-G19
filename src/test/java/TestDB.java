import it.unipv.DB.PricesOperations;
import it.unipv.gui.common.Prices;
import it.unipv.DB.DBConnection;
import it.unipv.utils.ApplicationException;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.PreparedStatement;
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

    @Test
    public void provaInserimento() {
        Prices p = new Prices(7,5,3,1);
        DBConnection dbConnection = new DBConnection();
        try {
            String query = "INSERT INTO z6xOH9WKhI.PRICES(BASE, VIP, THREED, REDUCED) values (?,?,?,?)";
            PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query);
            ps.setDouble(1, p.getBase());
            ps.setDouble(2, p.getVip());
            ps.setDouble(3, p.getThreed());
            ps.setDouble(4, p.getReduced());
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        } finally {
            dbConnection.close();
        }
    }

    @Test
    public void provaTruncate() {
        DBConnection dbConnection = new DBConnection();
        try {
            String query = "TRUNCATE z6xOH9WKhI.PRICES";
            dbConnection.getPreparedStatementFromQuery(query).execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        } finally {
            dbConnection.close();
        }
    }

    @Test
    public void testPricesOperations() {
        PricesOperations po = new PricesOperations();
        Prices p = po.getPrices();
        System.out.println(p.toString());
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
