package it.unipv.gui.login;

/**
 * Oggetto che rappresenta il singolo utente.
 *    Per ora sono presenti solamente due parametri:
 *       name -> il nickname che si sceglie al momento della registrazione
 *       password -> la password che si sceglie al momento della registrazione
 */
public class User implements Comparable<User> {
    private String name;
    private String password;
    private String email;
    private String codice;

    User() {}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(String name, String password, String email, String codice) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.codice = codice;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getCodice() { return codice; }

    public void setCodice(String codice) { this.codice = codice; }

    @Override
    public int compareTo(User o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }

    @Override
    public String toString() {
        return
                "Username: " + this.name + "\n"
              + "Password: " + this.password + "\n"
              + "E-mail " + this.email
              + "Codice " + this.codice;
    }
}
