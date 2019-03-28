package it.unipv.gui.user;

import it.unipv.gui.login.LoginForm;

import javax.swing.*;

public class UserForm extends JFrame {

    public UserForm(LoginForm loginForm, String username) {
        initMenuBar(loginForm);
        initFrame(username);
    }

    private void initMenuBar(LoginForm loginForm) {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu questionMenu = new JMenu("?");
        menuBar.add(questionMenu);

        JMenuItem logoutItem = new JMenuItem("Logout");
        questionMenu.add(logoutItem);
        logoutItem.addActionListener( e -> {
            setVisible(false);
            loginForm.triggerLogoutEvent();
        });

    }

    private void initFrame(String username) {
        setTitle("User: " + username);
        setVisible(true);
        setSize(700,700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
