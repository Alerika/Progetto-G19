package it.unipv.conversion;

import it.unipv.gui.login.User;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;
import it.unipv.utils.DataReferences;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilizzata per diversi scopi:
 *    1) salvare all'interno di un file .txt le informazioni riguardanti un utente di cui si è deciso di ricordarsi i dati
 *    2) controllare se già esiste un file di informazioni salvate
 *    3) eliminare, se già esistente, un file di informazioni salvate
 */
public class UserInfo {

    /**
     * Metodo che salva all'interno di un file .txt le informazioni di un utente di cui si è deciso di ricordarsi i dati
     *    Per ora lo salviamo all'interno della cartella utenti/ del progetto, ma sarebbe meglio spostarlo
     *    all'interno di una cartella di roaming come %APPDATA%, che ancora devo capire quale sia su linux
     * @param username -> nickname dell'utente
     * @param password -> password dell'utente
     */
    public static void createUserInfoFileInUserDir(String username, String password, String email, String codice) {
        PrintWriter writer = null;
        try {
            File dir = new File(DataReferences.INFOUSERDIR);
            if(!dir.exists()) {
                Files.createDirectories(Paths.get(DataReferences.INFOUSERDIR));
            }
            writer = new PrintWriter(DataReferences.INFOUSERFILE, "UTF-8");
            writer.println(username);
            writer.println(password);
            writer.println(email);
            writer.println(codice);
        } catch (FileNotFoundException e) {
            throw new ApplicationException("File " + DataReferences.INFOUSERFILE + " non trovato!", e);
        } catch (IOException e) {
            throw new ApplicationException(e);
        } finally {
            CloseableUtils.close(writer);
        }
    }

    /**
     * Metodo utilizzato per rimuovere il file di informazioni salvate, se esiste
     */
    public static void deleteUserInfoFileInUserDir() {
        File info = new File(DataReferences.INFOUSERFILE);
        if(info.exists()) {
            if(info.delete()) {
                System.out.println("File " + info.getPath() + " rimosso con successo!");
            } else {
                throw new ApplicationException("Errore durante la rimozione del file " + info.getPath());
            }
        }
    }

    /**
     * Metodo utilizzato per verificare l'esistenza del file di info
     * @return -> true se esiste, false altrimenti
     */
    public static boolean checkIfUserInfoFileExists() {
        return new File(DataReferences.INFOUSERFILE).exists();
    }

    /**
     * Metodo che instanzia un utente con le informazioni salvate all'interno del file di infomazioni .txt
     * @return -> utente instanziato con le informazioni presenti nel .txt
     */
    private static User getUserInfoFromFile() {
        BufferedReader br = null;
        InputStreamReader isr = null;
        FileInputStream fis = null;
        try {
            User res = new User();
            fis = new FileInputStream(DataReferences.INFOUSERFILE);
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
            List<String> infoFromFile = new ArrayList<>();
            String line = br.readLine();

            while (line != null) {
                infoFromFile.add(line.trim());
                line = br.readLine();
            }

            if(infoFromFile.size()!=4) {
                throw new ApplicationException("Impossibile, formato file inatteso!");
            } else {
                res.setName(infoFromFile.get(0));
                res.setPassword(infoFromFile.get(1));
                res.setEmail(infoFromFile.get(2));
                res.setCodice(infoFromFile.get(3));
            }

            return res;
        } catch (IOException e) {
            throw new ApplicationException(e);
        } finally {
            CloseableUtils.close(br, isr, fis);
        }
    }

    /**
     * Metodo che ritorna l'utente se il file .txt di info esiste
     * @return -> ritorna l'utente instanziato se il file .txt di info esiste
     */
    public static User getUserInfo() {
        if(checkIfUserInfoFileExists()) {
            return getUserInfoFromFile();
        } else {
            throw new ApplicationException("Impossibile trovare info user perché non esiste neanche il file delle info!");
        }
    }
}
