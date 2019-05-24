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

    User() {}

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    @Override
    public int compareTo(User o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }
}
