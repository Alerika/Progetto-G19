package it.unipv.gui.common;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MyDraggableSeat extends JLabel implements MouseListener, MouseMotionListener {
    private int screenX = 0;
    private int screenY = 0;
    private int myX = 0;
    private int myY = 0;
    private boolean isItDraggable;

    public MyDraggableSeat(int x, int y) {
        setBorder(new LineBorder(Color.BLUE, 3));
        setBackground(Color.WHITE);
        setBounds(x, y, 30, 25);
        setOpaque(true);
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Viene utilizzato per impostare se il posto è trascinabile o meno;
     * Ha senso trascinarlo quando si è nella parte di modifica della piantina
     *   mentre ha poco senso trascinarlo quando la si sta solo visualizzando.
     * @param status -> true se lo si vuole trascinabile, altrimenti false.
     */
    public void setIsItDraggable(boolean status) {
        this.isItDraggable = status;
    }

    // Per ora non ha molto senso che il gestore possa selezionare il posto ma è un test
    Point coord;
    @Override
    public void mouseClicked(MouseEvent e) {

        if(e.getClickCount()==2) {
            if(getBackground().equals(Color.WHITE)) {
                setBackground(Color.GREEN);
            } else {
                setBackground(Color.WHITE);
            }
        }
    }

    // Appena premo sul posto mi salvo le sue coordinate
    @Override
    public void mousePressed(MouseEvent e) {
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();

        myX = getX();
        myY = getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /* Se siamo nella situazione di posto trascinabile
        quando il mouse entra nel posto si "illumina"
        quando esce ritorna normale
    */
    @Override
    public void mouseEntered(MouseEvent e) {
        if(isItDraggable) {
            setBorder(new LineBorder(Color.MAGENTA, 3));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(isItDraggable) {
            setBorder(new LineBorder(Color.BLUE, 3));
        }
    }

    // È la parte che si occupa di trascinare il posto a sedere
    double previousX;
    double previousY;
    @Override
    public void mouseDragged(MouseEvent e) {
        if(isItDraggable) {
            int deltaX = e.getXOnScreen() - screenX;
            int deltaY = e.getYOnScreen() - screenY;
            setLocation(myX + deltaX, myY + deltaY);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}