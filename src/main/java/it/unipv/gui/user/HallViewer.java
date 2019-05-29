package it.unipv.gui.user;

import it.unipv.conversion.CSVToDraggableSeats;
import it.unipv.gui.common.MyDraggableSeat;
import it.unipv.gui.common.SeatTYPE;
import it.unipv.utils.DataReferences;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

class HallViewer extends JFrame {
    private String nomeSala;
    private JPanel undraggableSeatsPanel;
    private List<MyDraggableSeat> undraggableSeats = new ArrayList<>();
    private List<MyDraggableSeat> selectedMDS = new ArrayList<>();
    private MoviePrenotationController moviePrenotationController;
    private boolean isSomethingChanged = false;

    HallViewer(MoviePrenotationController moviePrenotationController, String nomeSala, List<String> occupiedSeatNames) {
        this.moviePrenotationController = moviePrenotationController;
        this.nomeSala = nomeSala;
        initDraggableSeatsList();
        initUndraggableSeatsPanel();
        setUnselectableSeat(occupiedSeatNames);
        initFrame();
    }

    HallViewer(MoviePrenotationController moviePrenotationController, String nomeSala, List<MyDraggableSeat> selectedMDS, List<String> occupiedSeatNames) {
        this.moviePrenotationController = moviePrenotationController;
        this.nomeSala = nomeSala;
        this.selectedMDS = selectedMDS;
        initDraggableSeatsList();
        initUndraggableSeatsPanel();
        setSelectedMDS();
        setUnselectableSeat(occupiedSeatNames);
        initFrame();
    }

    private void setUnselectableSeat(List<String> names) {
        if(names.size()>0) {
            for(MyDraggableSeat mds : undraggableSeats) {
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
        List<MyDraggableSeat> toAdd = new ArrayList<>();
        for(MyDraggableSeat mds : undraggableSeats) {
            for(MyDraggableSeat smds : selectedMDS) {
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
        undraggableSeats = CSVToDraggableSeats.getMyDraggableSeatListFromCSV(DataReferences.PIANTINEFOLDERPATH+nomeSala+".csv");
    }

    private void initUndraggableSeatsPanel() {
        undraggableSeatsPanel = new JPanel();
        undraggableSeatsPanel.setMinimumSize(new Dimension(300, 150));
        undraggableSeatsPanel.setLayout(null);

        for(MyDraggableSeat mds : undraggableSeats) {
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
                doDisposeOnExit();
            }
        });
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void doDisposeOnExit() {
        if(isSomethingChanged) {
            int reply = JOptionPane.showConfirmDialog(this, "Confermi i posti selezionati?", "Conferma posti", JOptionPane.YES_NO_OPTION);
            if(reply == JOptionPane.YES_OPTION) {
                moviePrenotationController.triggerSelectedSeats(selectedMDS);
            }
        }
        moviePrenotationController.triggerClosingHallViewer();
    }
}
