package it.unipv.DB;

import it.unipv.gui.manager.Prices;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.DataReferences;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PricesOperations {
    private DBConnection dbConnection = new DBConnection();

    public Prices getPrices() { return doGetPrices(); }

    public void updatePrices(Prices p) {
        doTruncate();
        doInsert(p);
    }

    private Prices doGetPrices() {
        try {
            return getPricesFromResultSet(dbConnection.getResultFromQuery("select * from " + DataReferences.DBNAME + ".PRICES"));
        } catch (SQLException e) {
            throw new ApplicationException(e);
        } finally {
            dbConnection.close();
        }
    }

    private Prices getPricesFromResultSet(ResultSet resultSet) throws SQLException {
        Prices res = null;
        while(resultSet.next()) {
            res = new Prices( resultSet.getDouble("BASE")
                            , resultSet.getDouble("VIP")
                            , resultSet.getDouble("THREED")
                            , resultSet.getDouble("REDUCED"));
        }
        return res;
    }

    private void doInsert(Prices p){
        try {
            String query = "INSERT INTO " + DataReferences.DBNAME + ".PRICES(BASE, VIP, THREED, REDUCED) values (?,?,?,?)";
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

    private void doTruncate() {
        try {
            String query = "TRUNCATE "+ DataReferences.DBNAME + ".PRICES";
            dbConnection.getPreparedStatementFromQuery(query).execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        } finally {
            dbConnection.close();
        }
    }
}
