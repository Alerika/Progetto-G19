package it.unipv.gui.common;

/**
 * Oggetto che rappresenta il singolo utente.
 *    Per ora sono presenti solamente due parametri:
 *       name -> il nickname che si sceglie al momento della registrazione
 *       password -> la password che si sceglie al momento della registrazione
 */
public class User {
    private String name;
    private String password;

    public User() {}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }
}
