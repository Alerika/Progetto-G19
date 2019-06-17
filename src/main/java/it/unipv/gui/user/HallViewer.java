package it.unipv.gui.user;

import it.unipv.DB.DBConnection;
import it.unipv.DB.HallOperations;
import it.unipv.gui.common.Seat;
import it.unipv.gui.common.SeatTYPE;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class HallViewer extends JFrame {
    private String nomeSala;
    private JPanel undraggableSeatsPanel;
    private List<Seat> undraggableSeats = new ArrayList<>();
    private List<Seat> selectedMDS = new ArrayList<>();
    private MoviePrenotationController moviePrenotationController;
    private boolean isSomethingChanged = false;
    private HallOperations ho;

    HallViewer(MoviePrenotationController moviePrenotationController, String nomeSala, List<String> occupiedSeatNames, DBConnection dbConnection) {
        this.moviePrenotationController = moviePrenotationController;
        this.nomeSala = nomeSala;
        ho = new HallOperations(dbConnection);
        initDraggableSeatsList();
        initUndraggableSeatsPanel();
        setUnselectableSeat(occupiedSeatNames);
        initMenuBar();
        initFrame();
    }

    HallViewer(MoviePrenotationController moviePrenotationController, String nomeSala, List<Seat> selectedMDS, List<String> occupiedSeatNames, DBConnection dbConnection) {
        this.moviePrenotationController = moviePrenotationController;
        this.nomeSala = nomeSala;
        this.selectedMDS = selectedMDS;
        ho = new HallOperations(dbConnection);
        initDraggableSeatsList();
        initUndraggableSeatsPanel();
        setSelectedMDS();
        setUnselectableSeat(occupiedSeatNames);
        initMenuBar();
        initFrame();
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("Modifica");
        menuBar.add(fileMenu);

        JMenuItem saveItem = new JMenuItem("Conferma");
        fileMenu.add(saveItem);
        saveItem.addActionListener(e -> {
            doConfirmSelectedSeats();
            isSomethingChanged = false;
        });
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
    }

    private void setUnselectableSeat(List<String> names) {
        if(names.size()>0) {
            for(Seat mds : undraggableSeats) {
                for(String s : names) {
                    if(mds.getText().trim().equalsIgnoreCase(s)) {
                        mds.setType(SeatTYPE.OCCUPATO);
                        mds.updateBackgroundForChangingType();
                    }
                }
            }
        }
    }

    private void setSelectedMDS() {
        List<Seat> toAdd = new ArrayList<>();
        for(Seat mds : undraggableSeats) {
            for(Seat smds : selectedMDS) {
                if(smds.getText().trim().equals(mds.getText().trim())) {
                    mds.setBorder(new LineBorder(Color.CYAN,3));
                    toAdd.add(mds);
                }
            }
        }
        selectedMDS.clear();
        selectedMDS.addAll(toAdd);
    }

    private void initDraggableSeatsList() {
        undraggableSeats = ho.retrieveSeats(nomeSala);
    }

    private void initUndraggableSeatsPanel() {
        undraggableSeatsPanel = new JPanel();
        undraggableSeatsPanel.setMinimumSize(new Dimension(300, 150));
        undraggableSeatsPanel.setLayout(null);

        for(Seat mds : undraggableSeats) {
            undraggableSeatsPanel.add(mds);
            mds.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(!mds.getType().equals(SeatTYPE.OCCUPATO)) {
                        if(selectedMDS.contains(mds)) {
                            mds.setBorder(new LineBorder(Color.BLUE,3));
                            selectedMDS.remove(mds);
                        } else {
                            mds.setBorder(new LineBorder(Color.CYAN, 3));
                            selectedMDS.add(mds);
                        }
                        isSomethingChanged = true;
                    }
                }
            });
            repaint();
        }
    }

    private void initFrame() {
        setTitle(nomeSala + ": seleziona i posti!");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/GoldenMovieStudioIcon.png")));
        setLayout(new GridLayout(1,1));
        add(undraggableSeatsPanel);
        setSize(1200,720);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                doConfirmSelectedSeats();
            }
        });
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void doConfirmSelectedSeats() {
        if(isSomethingChanged) {
            int reply = JOptionPane.showConfirmDialog(this, "Confermi i posti selezionati?", "Conferma posti", JOptionPane.YES_NO_OPTION);
            if(reply == JOptionPane.YES_OPTION) {
                moviePrenotationController.triggerSelectedSeats(selectedMDS);
            }
        }
        moviePrenotationController.triggerClosingHallViewer();
    }
}
