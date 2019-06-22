package it.unipv.dao;

import it.unipv.db.DBConnection;
import it.unipv.model.MovieSchedule;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.DataReferences;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe fa riferimento alla tabella PROGRAMMAZIONIFILM
 * Si occupa di inserire/recuperare/eliminare i dati riguardanti le programmazioni dei film.
 */
public class ScheduleDaoImpl implements ScheduleDao {
    private DBConnection dbConnection;

    public ScheduleDaoImpl(DBConnection dbConnection) { this.dbConnection = dbConnection; }

    @Override public List<MovieSchedule> retrieveMovieSchedules() {
        return doRetrieveMovieSchedules();
    }

    @Override public void insertNewMovieSchedule(MovieSchedule toInsert) {
        doInsertNewMovieSchedule(toInsert);
    }

    @Override public void deleteMovieSchedule(MovieSchedule toDelete) {
        doDeleteMovieSchedule(toDelete);
    }

    private void doDeleteMovieSchedule(MovieSchedule toDelete) {
        String query = "DELETE FROM "+ DataReferences.DBNAME + ".PROGRAMMAZIONIFILM where CODICE_FILM = ? AND DATA = ? AND ORA = ? AND SALA = ?";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.setString(1, toDelete.getMovieCode());
            ps.setString(2, toDelete.getDate());
            ps.setString(3, toDelete.getTime());
            ps.setString(4, toDelete.getHallName());
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private void doInsertNewMovieSchedule(MovieSchedule toInsert) {
        String query = "INSERT INTO " + DataReferences.DBNAME + ".PROGRAMMAZIONIFILM (CODICE_FILM, DATA, ORA, SALA) values (?,?,?,?)";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.setString(1, toInsert.getMovieCode());
            ps.setString(2, toInsert.getDate());
            ps.setString(3, toInsert.getTime());
            ps.setString(4, toInsert.getHallName());
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private List<MovieSchedule> doRetrieveMovieSchedules() {
        try {
            return getMovieSchedulesFromResultSet(dbConnection.getResultFromQuery("SELECT * FROM " + DataReferences.DBNAME + ".PROGRAMMAZIONIFILM"));
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private List<MovieSchedule> getMovieSchedulesFromResultSet(ResultSet resultSet) throws SQLException {
        try {
            List<MovieSchedule> res = new ArrayList<>();
            while(resultSet.next()) {
                MovieSchedule toAdd = new MovieSchedule();
                toAdd.setMovieCode(resultSet.getString("CODICE_FILM"));
                toAdd.setDate(resultSet.getString("DATA"));
                toAdd.setTime(resultSet.getString("ORA"));
                toAdd.setHallName(resultSet.getString("SALA"));
                res.add(toAdd);
            }
            return res;
        } finally {
            resultSet.close();
        }
    }
}
