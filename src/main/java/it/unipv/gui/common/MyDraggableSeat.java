package it.unipv.gui.common;

import it.unipv.utils.DataReferences;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Questa classe rappresenta il singolo posto a sedere:
 * è una JLabel che presenta un bordo quadrato blu
 * (che diventa viola [per ora] quando selezionato)
 * Il singolo posto a sedere è trascinabile all'interno della piantina
 * TODO: fare in modo che non possa essere trascinato all'esterno della piantina stessa
 */
public class MyDraggableSeat extends JLabel implements MouseListener, MouseMotionListener {

    public MyDraggableSeat(int x, int y) {
        setBorder(new LineBorder(Color.BLUE, 3));
        setBackground(Color.WHITE);
        setBounds(x, y, DataReferences.MYDRAGGABLESEATWIDTH, DataReferences.MYDRAGGABLESEATHEIGTH);
        setSize(DataReferences.MYDRAGGABLESEATWIDTH, DataReferences.MYDRAGGABLESEATHEIGTH);
        setOpaque(true);
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // Per ora non ha molto senso che il gestore possa selezionare il posto ma è un test
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            if (getBackground().equals(Color.WHITE)) {
                setBackground(Color.GREEN);
            } else {
                setBackground(Color.WHITE);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {


    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}