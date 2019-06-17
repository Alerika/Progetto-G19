package it.unipv.DB;

import it.unipv.gui.common.Movie;
import it.unipv.gui.common.MovieStatusTYPE;
import it.unipv.gui.common.MovieTYPE;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;
import it.unipv.utils.DataReferences;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieOperations {
    private DBConnection dbConnection;

    public MovieOperations(DBConnection dbConnection) { this.dbConnection = dbConnection; }

    public List<Movie> retrieveCompleteMovieList(double requestedWidth, double requestedHeight, boolean preserveRation, boolean smooth) {
        try {
            return retrieveCompleteMoviesFromResultSet( dbConnection.getResultFromQuery("SELECT * FROM " + DataReferences.DBNAME + ".FILM")
                                                      , requestedWidth
                                                      , requestedHeight
                                                      , preserveRation
                                                      , smooth);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    public List<Movie> retrieveMovieListWithoutPoster() {
        try {
            return retrieveMoviesWithoutPosterFromResultSet(dbConnection.getResultFromQuery("SELECT * FROM " + DataReferences.DBNAME + ".FILM"));
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    public void insertNewMovie(Movie toInsert, FileInputStream posterStream) {
        try {
            doInsertNewMovie(toInsert, posterStream);
        } finally {
            CloseableUtils.close(posterStream);
        }
    }

    public void updateMovieButNotPoster(Movie toUpdate) {
        doUpdateMovieButNotPoster(toUpdate);
    }

    public void updateMovie(Movie toUpdate, FileInputStream posterStream) {
        try {
            doUpdateMovie(toUpdate, posterStream);
        } finally {
            CloseableUtils.close(posterStream);
        }
    }

    public void deleteMovie(Movie toDelete) {
        doDeleteMovie(toDelete);
    }

    private void doDeleteMovie(Movie toDelete) {
        String query = "DELETE FROM "+ DataReferences.DBNAME + ".FILM where CODICE = '" + toDelete.getCodice() + "';";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private void doUpdateMovieButNotPoster(Movie toUpdate) {
        String query = "UPDATE " + DataReferences.DBNAME + ".FILM SET TITOLO = ?"
                                                                 + ", GENERE = ?"
                                                                 + ", REGIA = ?"
                                                                 + ", CAST = ?"
                                                                 + ", DURATA = ?"
                                                                 + ", ANNO = ?"
                                                                 + ", TRAMA = ?"
                                                                 + ", TIPO = ?"
                                                                 + ", STATUS = ? "
                     + "WHERE CODICE = '" + toUpdate.getCodice() + "';";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.setString(1, toUpdate.getTitolo());
            ps.setString(2, toUpdate.getGenere());
            ps.setString(3, toUpdate.getRegia());
            ps.setString(4, toUpdate.getCast());
            ps.setString(5, toUpdate.getDurata());
            ps.setString(6, toUpdate.getAnno());
            ps.setString(7, toUpdate.getTrama());
            ps.setString(8, toUpdate.getTipo().name());
            ps.setString(9, toUpdate.getStatus().name());
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private void doUpdateMovie(Movie toUpdate, FileInputStream posterStream) {
        String query = "UPDATE " + DataReferences.DBNAME + ".FILM SET LOCANDINA = ?"
                                                         + ", TITOLO = ?"
                                                         + ", GENERE = ?"
                                                         + ", REGIA = ?"
                                                         + ", CAST = ?"
                                                         + ", DURATA = ?"
                                                         + ", ANNO = ?"
                                                         + ", TRAMA = ?"
                                                         + ", TIPO = ?"
                                                         + ", STATUS = ? "
                     + "WHERE CODICE = '" + toUpdate.getCodice() + "';";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.setBinaryStream(1, posterStream, posterStream.available());
            ps.setString(2, toUpdate.getTitolo());
            ps.setString(3, toUpdate.getGenere());
            ps.setString(4, toUpdate.getRegia());
            ps.setString(5, toUpdate.getCast());
            ps.setString(6, toUpdate.getDurata());
            ps.setString(7, toUpdate.getAnno());
            ps.setString(8, toUpdate.getTrama());
            ps.setString(9, toUpdate.getTipo().name());
            ps.setString(10, toUpdate.getStatus().name());
            ps.execute();
        } catch (SQLException | IOException e) {
            throw new ApplicationException(e);
        }
    }

    private void doInsertNewMovie(Movie toInsert, FileInputStream posterStream) {
        String query = "insert into " + DataReferences.DBNAME + ".FILM (CODICE, LOCANDINA, TITOLO, GENERE, REGIA, CAST, DURATA, ANNO, TRAMA, TIPO, STATUS) values (?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.setString(1, toInsert.getCodice());
            ps.setBinaryStream(2, posterStream, posterStream.available());
            ps.setString(3, toInsert.getTitolo());
            ps.setString(4, toInsert.getGenere());
            ps.setString(5, toInsert.getRegia());
            ps.setString(6, toInsert.getCast());
            ps.setString(7, toInsert.getDurata());
            ps.setString(8, toInsert.getAnno());
            ps.setString(9, toInsert.getTrama());
            ps.setString(10, toInsert.getTipo().name());
            ps.setString(11, toInsert.getStatus().name());
            ps.execute();
        } catch (SQLException | IOException e) {
            throw new ApplicationException(e);
        }
    }

    private List<Movie> retrieveMoviesWithoutPosterFromResultSet(ResultSet resultSet) throws SQLException {
        List<Movie> res = new ArrayList<>();
        try {
            while(resultSet.next()) {
                res.add(getMovieWithoutPoster(resultSet));
            }
            return res;
        } finally {
            resultSet.close();
        }
    }

    private List<Movie> retrieveCompleteMoviesFromResultSet(ResultSet resultSet, double requestedWidth, double requestedHeight, boolean preserveRation, boolean smooth) throws SQLException {
        List<Movie> res = new ArrayList<>();
        try {
            while(resultSet.next()) {
                res.add(getMovie(resultSet, requestedWidth, requestedHeight, preserveRation, smooth));
            }
            return res;
        } finally {
            resultSet.close();
        }
    }

    private Movie getMovie(ResultSet resultSet, double requestedWidth, double requestedHeight, boolean preserveRation, boolean smooth) throws SQLException {
        Movie toAdd = getMovieWithoutPoster(resultSet);
        toAdd.setLocandina(getImageFromBlob(resultSet, requestedWidth, requestedHeight, preserveRation, smooth));
        return toAdd;
    }

    private Image getImageFromBlob(ResultSet resultSet, double requestedWidth, double requestedHeight, boolean preserveRation, boolean smooth) throws SQLException {
        Blob blob = resultSet.getBlob("LOCANDINA");
        InputStream in = blob.getBinaryStream(1, Objects.requireNonNull(blob).length());
        Image res = new Image(in, requestedWidth, requestedHeight, preserveRation, smooth);
        CloseableUtils.close(in);
        return res;
    }

    private Movie getMovieWithoutPoster(ResultSet resultSet) throws SQLException {
        Movie toAdd = new Movie();
        toAdd.setCodice( resultSet.getString("CODICE"));
        toAdd.setTitolo(resultSet.getString("TITOLO"));
        toAdd.setGenere(resultSet.getString("GENERE"));
        toAdd.setRegia(resultSet.getString("REGIA"));
        toAdd.setCast(resultSet.getString("CAST"));
        toAdd.setDurata(resultSet.getString("DURATA"));
        toAdd.setAnno(resultSet.getString("ANNO"));
        toAdd.setTrama(resultSet.getString("TRAMA"));

        String x = resultSet.getString("TIPO");
        if(x.equalsIgnoreCase("TWOD")) {
            toAdd.setTipo(MovieTYPE.TWOD);
        } else if(x.equalsIgnoreCase("THREED")) {
            toAdd.setTipo(MovieTYPE.THREED);
        } else {
            toAdd.setTipo(MovieTYPE.NOTRECOGNIZED);
        }

        x = resultSet.getString("STATUS");
        if(x.equalsIgnoreCase("AVAILABLE")) {
            toAdd.setStatus(MovieStatusTYPE.AVAILABLE);
        } else if(x.equalsIgnoreCase("NOT_AVAILABLE")) {
            toAdd.setStatus(MovieStatusTYPE.NOT_AVAILABLE);
        } else {
            toAdd.setStatus(MovieStatusTYPE.NOT_RECOGNIZE);
        }
        return toAdd;
    }
}
