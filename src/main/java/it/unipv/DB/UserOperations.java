package it.unipv.DB;

import it.unipv.gui.login.User;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.DataReferences;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe fa riferimento alla tabella UTENTI
 * Si occupa di inserire/recuperare/aggiornare/eliminare i dati riguardanti gli utenti.
 */
public class UserOperations {
    private DBConnection dbConnection;

    public UserOperations(DBConnection dbConnection) { this.dbConnection = dbConnection; }

    public List<User> retrieveUserList() { return doRetrieveUserList(); }

    public void insertNewUser(User toInsert) { doInsertNewUser(toInsert); }

    public void deleteUser(User toDelete) { doDeleteUser(toDelete); }

    public void updateUser(User toUpdate) { doUpdateUser(toUpdate); }

    /**
     * L'update, in questo caso, prevede solo l'aggiornamento della password.
     * @param toUpdate -> l'utente da aggiornare con la nuova password.
     */
    private void doUpdateUser(User toUpdate) {
        String query = "UPDATE " + DataReferences.DBNAME + ".UTENTI SET PASSWORD = ? WHERE CODICE = ?";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.setString(1, toUpdate.getPassword());
            ps.setString(2, toUpdate.getCodice());
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private void doDeleteUser(User toDelete) {
        String query = "DELETE FROM " + DataReferences.DBNAME + ".UTENTI where CODICE = ?";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.setString(1, toDelete.getCodice());
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private void doInsertNewUser(User toInsert) {
        String query = "INSERT INTO " + DataReferences.DBNAME + ".UTENTI (CODICE, NOME, PASSWORD, EMAIL) values (?,?,?,?)";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.setString(1, toInsert.getCodice());
            ps.setString(2, toInsert.getName());
            ps.setString(3, toInsert.getPassword());
            ps.setString(4, toInsert.getEmail());
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private List<User> doRetrieveUserList() {
        try {
            return getUsersFromResultSet(dbConnection.getResultFromQuery("SELECT * FROM " + DataReferences.DBNAME + ".UTENTI"));
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private List<User> getUsersFromResultSet(ResultSet resultSet) throws SQLException {
        try {
            List<User> res = new ArrayList<>();
            while(resultSet.next()) {
                res.add(new User( resultSet.getString("NOME")
                                , resultSet.getString("PASSWORD")
                                , resultSet.getString("EMAIL")
                                , resultSet.getString("CODICE")));
            }
            return res;
        } finally {
            resultSet.close();
        }
    }
}
