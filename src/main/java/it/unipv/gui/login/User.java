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

    User() {}

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

    @Override
    public int compareTo(User o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }
}
