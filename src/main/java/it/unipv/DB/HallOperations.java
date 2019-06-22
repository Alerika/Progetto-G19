package it.unipv.DB;

import it.unipv.gui.common.Seat;
import it.unipv.gui.common.SeatTYPE;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;
import it.unipv.utils.DataReferences;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Questa classe fa riferimento alle tabelle PIANTINE e PIANTINEPREVIEW
 * Si occupa di inserire/recuperare/aggiornare/eliminare i dati riguardanti le sale, come i posti a sedere (Seat) e le immagini di anteprima.
 */
public class HallOperations {

    private DBConnection dbConnection;

    public HallOperations(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<Seat> retrieveSeats(String hallName) {
        try {
            return retrieveSeatsFromResultSet(dbConnection.getResultFromQuery("select * from " + DataReferences.DBNAME + ".PIANTINE where NOME_SALA = '" + hallName + "';"));
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    public List<String> retrieveHallNames() {
        try {
            return retrieveHallNamesFromResultSet(dbConnection.getResultFromQuery("select distinct NOME_SALA from " + DataReferences.DBNAME + ".PIANTINE"));
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * Questo metodo viene utilizzato per recuperare la preview delle sale come Image con dimensioni impostate come parametri;
     * @param hallName -> la sala di cui si vuole recuperare l'anteprima;
     * @param requestedWidth -> la larghezza che vogliamo dare alla nostra Image;
     * @param requestedHeight -> l'altezza che voglia dare alla nostra Image;
     * @param preserveRatio -> decidere se mantenere l'aspect ratio o meno
     * @param smooth -> indica in generale se applicare un algoritmo di miglioramento dell'Image finale;
     * @return -> ritorna la preview della sala come Image con i parametri da noi specificati
     */
    public Image retrieveHallPreviewAsImage(String hallName, double requestedWidth, double requestedHeight, boolean preserveRatio, boolean smooth) {
        try {
            return getHallPreviewFromResultSetAsImage( dbConnection.getResultFromQuery("select PREVIEW from " + DataReferences.DBNAME + ".PIANTINEPREVIEW where NOME_SALA = '" + hallName + "';")
                                                     , requestedWidth
                                                     , requestedHeight
                                                     , preserveRatio
                                                     , smooth);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * Questo metodo viene principalmente utilizzato dalla Home, quando si vuole visualizzare la piantina di una sala:
     * mi faccio restituire uno stream perché così ho l'immagine vera e propria, e non un oggetto Image.
     * @param hallName -> è la sala di cui si vuole recuperare l'anteprima come stream
     * @return -> ritorna l'anteprima come InputStream.
     */
    public InputStream retrieveHallPreviewAsStream(String hallName) {
        try {
            return retrieveHallPreviewFromResultSetAsStream(dbConnection.getResultFromQuery("select PREVIEW from " + DataReferences.DBNAME + ".PIANTINEPREVIEW where NOME_SALA = '" + hallName + "';"));
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private InputStream retrieveHallPreviewFromResultSetAsStream(ResultSet resultSet) throws SQLException {
        try {
            Blob blob = null;
            while(resultSet.next()) {
                blob = resultSet.getBlob("PREVIEW");
            }
            return blob.getBinaryStream(1, Objects.requireNonNull(blob).length());
        } finally {
            resultSet.close();
        }
    }

    /**
     * Cancello tutta la sala e la ricreo perché non è un vero e proprio update:
     * possono capitare volte in cui modifico posti già esistenti, ma anche volte in cui inserisco nuovi posti
     * @param hallName -> sala da aggiornare
     * @param toUpdate -> lista posti aggiornati da inserire nuovamente
     */
    public void updateHallSeats(String hallName, List<Seat> toUpdate) {
        try {
            doRemoveSeats(hallName);
            doInsertSeats(hallName, toUpdate);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    public void updateHallPreview(String hallName, ByteArrayInputStream previewStream) {
        try {
            doUpdateHallPreview(hallName, previewStream);
        } finally {
            CloseableUtils.close(previewStream);
        }
    }

    public void removeHallAndPreview(String hallName) {
        doRemoveSeats(hallName);
        doRemovePreview(hallName);
    }

    public void insertNewHall(String hallName, List<Seat> toInsert) {
        try{
            doInsertSeats(hallName, toInsert);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    public void insertNewHallpreview(String hallName, ByteArrayInputStream previewStream) {
        try {
            doInsertHallPreview(hallName, previewStream);
        } finally {
            CloseableUtils.close(previewStream);
        }
    }

    public void renameHallAndPreview(String oldHallName, String newHallName) {
        doRenameHall(oldHallName, newHallName);
        doRenamePreview(oldHallName, newHallName);
    }

    private void doRenameHall(String oldHallName, String newHallName) {
        String query = "UPDATE " + DataReferences.DBNAME + ".PIANTINE SET NOME_SALA = ? WHERE NOME_SALA = '" + oldHallName + "';";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.setString(1, newHallName);
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private void doRenamePreview(String oldHallName, String newHallName) {
        String query = "UPDATE " + DataReferences.DBNAME + ".PIANTINEPREVIEW SET NOME_SALA = ? WHERE NOME_SALA = '" + oldHallName + "';";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.setString(1, newHallName);
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private void doInsertHallPreview(String hallName, ByteArrayInputStream previewStream) {
        String query = "insert into " + DataReferences.DBNAME + ".PIANTINEPREVIEW (NOME_SALA, PREVIEW) values (?,?)";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.setString(1, hallName);
            ps.setBinaryStream(2, previewStream, previewStream.available());
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private void doRemovePreview(String hallName) {
        String query = "delete from "+ DataReferences.DBNAME + ".PIANTINEPREVIEW where NOME_SALA = '" + hallName + "';";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }

    }

    private void doUpdateHallPreview(String hallName, ByteArrayInputStream previewStream) {
        String query = "UPDATE " + DataReferences.DBNAME + ".PIANTINEPREVIEW SET PREVIEW = ? WHERE NOME_SALA = '" + hallName + "';";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.setBinaryStream(1, previewStream, previewStream.available());
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private void doInsertSeats(String hallName, List<Seat> toUpdate) throws SQLException {
        PreparedStatement ps = null;
        try {
            String query = "INSERT INTO " + DataReferences.DBNAME + ".PIANTINE(NOME_SALA,NOME_POSTO,COORD_X, COORD_Y, TIPO_POSTO) values (?,?,?,?,?)";
            for(Seat s : toUpdate) {
                ps = dbConnection.getPreparedStatementFromQuery(query);
                ps.setString(1, hallName);
                ps.setString(2, s.getText());
                ps.setInt(3, s.getX());
                ps.setInt(4, s.getY());

                switch(s.getType()) {
                    case NORMALE:
                        ps.setString(5, "NORMALE");
                        break;

                    case DISABILE:
                        ps.setString(5, "DISABILE");
                        break;

                    case VIP:
                        ps.setString(5, "VIP");
                        break;

                    case OCCUPATO:
                        ps.setString(5, "OCCUPATO");
                        break;

                    default:
                        throw new ApplicationException("Tipo " + s.getType() + " non riconosciuto!");
                }

                ps.execute();
            }
        } catch (SQLException e) {
            throw new ApplicationException(e);
        } finally {
            if(ps!=null) { ps.close(); }
        }
    }

    private void doRemoveSeats(String hallName) {
        String query = "delete from "+ DataReferences.DBNAME + ".PIANTINE where NOME_SALA = '" + hallName + "';";
        try (PreparedStatement ps = dbConnection.getPreparedStatementFromQuery(query)) {
            ps.execute();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private Image getHallPreviewFromResultSetAsImage(ResultSet resultSet, double requestedWidth, double requestedHeight, boolean preserveRation, boolean smooth) throws SQLException{
        try {
            Blob blob = null;
            while(resultSet.next()) {
                blob = resultSet.getBlob("PREVIEW");
            }
            InputStream in = blob.getBinaryStream(1, Objects.requireNonNull(blob).length());
            Image result = new Image(in, requestedWidth, requestedHeight, preserveRation, smooth);
            CloseableUtils.close(in);
            return result;
        } finally {
            resultSet.close();
        }

    }

    private List<String> retrieveHallNamesFromResultSet(ResultSet resultSet) throws SQLException {
        try {
            List<String> res = new ArrayList<>();
            while(resultSet.next()) {
                res.add(resultSet.getString("NOME_SALA"));
            }
            return res;
        } finally {
            resultSet.close();
        }
    }

    private List<Seat> retrieveSeatsFromResultSet(ResultSet resultSet) throws SQLException {
        try {
            List<Seat> res = new ArrayList<>();
            while(resultSet.next()) {
                Seat s;
                switch (resultSet.getString("TIPO_POSTO")) {
                    case "NORMALE":
                        s = new Seat(resultSet.getInt("COORD_X"), resultSet.getInt("COORD_Y"), SeatTYPE.NORMALE);
                        break;

                    case "VIP":
                        s = new Seat(resultSet.getInt("COORD_X"), resultSet.getInt("COORD_Y"), SeatTYPE.VIP);
                        break;

                    case "DISABILE":
                        s = new Seat(resultSet.getInt("COORD_X"), resultSet.getInt("COORD_Y"), SeatTYPE.DISABILE);
                        break;

                    case "OCCUPATO":
                        s = new Seat(resultSet.getInt("COORD_X"), resultSet.getInt("COORD_Y"), SeatTYPE.OCCUPATO);
                        break;

                    default:
                        throw new ApplicationException("Tipo " + resultSet.getString("TIPO_POSTO") + " non riconosciuto!");
                }
                s.setText(resultSet.getString("NOME_POSTO"));
                res.add(s);
            }
            return res;
        } finally {
            resultSet.close();
        }

    }
}
