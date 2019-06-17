package it.unipv.DB;

import it.unipv.gui.common.Prices;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.DataReferences;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PricesOperations {
    private DBConnection dbConnection;

    public PricesOperations(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public Prices retrievePrices() { return doRetrievePrices(); }

    public void updatePrices(Prices p) {
        doTruncate();
        doInsert(p);
    }

    private Prices doRetrievePrices() {
        try {
            return retrievePricesFromResultSet(dbConnection.getResultFromQuery("select * from " + DataReferences.DBNAME + ".PRICES"));
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private Prices retrievePricesFromResultSet(ResultSet resultSet) throws SQLException {
        Prices res = null;
        try {
            while(resultSet.next()) {
                res = new Prices( resultSet.getDouble("BASE")
                        , resultSet.getDouble("VIP")
                        , resultSet.getDouble("THREED")
                        , resultSet.getDouble("REDUCED"));
            }
            return res;
        } finally {
            resultSet.close();
        }
    }

    private void doInsert(Prices p){
        String query = "INSERT INTO " + DataReferences.DBNAME + ".PRICES(BASE, VIP, THREED, REDUCED) values (?,?,?,?)";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.setDouble(1, p.getBase());
            ps.setDouble(2, p.getVip());
            ps.setDouble(3, p.getThreed());
            ps.setDouble(4, p.getReduced());
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private void doTruncate() {
        String query = "TRUNCATE "+ DataReferences.DBNAME + ".PRICES";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }
}
