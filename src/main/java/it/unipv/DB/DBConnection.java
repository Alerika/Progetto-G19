package it.unipv.DB;

import it.unipv.utils.ApplicationException;
import it.unipv.utils.DataReferences;

import java.sql.*;

public class DBConnection {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public DBConnection() { connect(); }

    private void connect(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://remotemysql.com?" + "user=" + DataReferences.DBNAME + "&password=" + DataReferences.DBPASS);
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new ApplicationException("Errore durante la connessione al Database", e);
        }
    }

    public void close() {
        try {
            if (connection != null) { connection.close(); }
            if (statement != null) { statement.close(); }
            if (resultSet != null) { resultSet.close(); }
        } catch (SQLException e) {
            throw new ApplicationException("Errore durante la chiusura della connessione al Database", e);
        }
    }

    public ResultSet getResultFromQuery(String query) {
        try {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            throw new ApplicationException("Errore durante l'esecuzione della query", e);
        }
    }

    public PreparedStatement getPreparedStatementFromQuery(String query) {
        try{
            return connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new ApplicationException("Errore durante l'esecuzione della query", e);
        }
    }

}
