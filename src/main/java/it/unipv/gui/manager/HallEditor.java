package it.unipv.gui.manager;

import it.unipv.conversion.CSVToDraggableSeats;
import it.unipv.conversion.DraggableSeatsToCSV;
import it.unipv.gui.common.MyDraggableSeat;
import it.unipv.utils.DataReferences;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


class HallEditor extends JFrame {

    private DraggableSeatPanel draggableSeatsPanel;
    private boolean isSomethingChanged = false;
    private String nomeSala;
    private ManagerForm summoner;


    HallEditor( String nomeSala
            , ManagerForm summoner
            , boolean wasItAlreadyCreated) {
        this.nomeSala = nomeSala;
        this.summoner = summoner;

        initMenuBar();
        initDraggableSeatsPanel(wasItAlreadyCreated);
        initFrame();
    }

    HallEditor( String nomeSala
            , ManagerForm summoner
            , int rows
            , int columns) {
        this.nomeSala = nomeSala;
        this.summoner = summoner;
        initMenuBar();
        initDraggableSeatsPanel(rows, columns);
        initFrame();
        isSomethingChanged = true;
    }


    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem saveItem = new JMenuItem("Salva");
        fileMenu.add(saveItem);
        saveItem.addActionListener(e -> draggableSeatsPanel.doSave());

        JMenu editMenu = new JMenu("Modifica");
        menuBar.add(editMenu);

        JMenuItem insertNewSeatItem = new JMenuItem("Aggiungi");
        editMenu.add(insertNewSeatItem);
        insertNewSeatItem.addActionListener(e -> draggableSeatsPanel.createNewDraggableSeat());
    }



    private void initDraggableSeatsPanel(boolean wasItAlreayCreated) {
        draggableSeatsPanel = new DraggableSeatPanel(wasItAlreayCreated);
    }

    private void initDraggableSeatsPanel(int rows, int columns) {
        draggableSeatsPanel = new DraggableSeatPanel(rows, columns);
    }


    private void initFrame() {
        setTitle("Editor " + nomeSala);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                doDisposeOnExit();
            }
        });
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(1, 1));
        add(draggableSeatsPanel);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void doDisposeOnExit() {
        if(isSomethingChanged) {
            int reply = JOptionPane.showConfirmDialog(null, "Salvare le modifiche prima di uscire?", "", JOptionPane.YES_NO_OPTION);
            if(reply == JOptionPane.YES_OPTION) {
                draggableSeatsPanel.doSave();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    private class DraggableSeatPanel extends JPanel {

        private List<String> createdSeatsName = new ArrayList<>();
        private List<MyDraggableSeat> draggableSeatsList = new ArrayList<>();

        DraggableSeatPanel(boolean wasItAlreadyCreated) {
            if(wasItAlreadyCreated) { initDraggableSeatsList(); }
            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseMotionHandler);
            setOpaque(false);
            setLayout(null);
        }

        DraggableSeatPanel(int rows, int columns) {
            initDraggableSeatsGrid(rows, columns);
            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseMotionHandler);
            setOpaque(false);
            setLayout(null);
        }

        private void initDraggableSeatsGrid(int rows, int columns) {
            int x = 5;
            int y = 5;
            int cont = 0;
            for(int i=0; i<rows; i++) {
                if(i>0) {
                    y += DataReferences.MYDRAGGABLESEATHEIGTH + 5;
                    x = 5;
                }
                if(cont>25) { cont = 0; }
                for(int j=0; j<columns; j++) {
                    if(j>0) { x += DataReferences.MYDRAGGABLESEATWIDTH+5; }
                    MyDraggableSeat mds = new MyDraggableSeat(x,y);
                    configureMDS(mds, DataReferences.ALPHABET[cont]+""+(j+1));
                    draggableSeatsList.add(mds);
                }
                cont++;
            }
        }

        private void initDraggableSeatsList() {
            draggableSeatsList = CSVToDraggableSeats.getMyDraggableSeatListFromCSV(DataReferences.PIANTINAFOLDERPATH+nomeSala+".csv");
            for(MyDraggableSeat mds : draggableSeatsList) {
                configureMDS(mds, mds.getText());
            }
        }

        private void configureMDS(MyDraggableSeat mds, String name) {
            createdSeatsName.add(mds.getText());
            mds.setText(name);
            mds.setComponentPopupMenu(initJPopupMenu(name));
            initMouseListenerForMDS(mds);
            add(mds);
        }

        private void removeSeat(String name) {
            MyDraggableSeat toRemove = null;
            for(MyDraggableSeat mds : draggableSeatsList) {
                if(name.trim().equalsIgnoreCase(mds.getText().trim())) {
                    toRemove = mds;
                    break;
                }
            }
            if(toRemove!=null) {
                createdSeatsName.remove(name);
                draggableSeatsList.remove(toRemove);
                remove(toRemove);
                isSomethingChanged = true;
                repaint();
            }
        }

        private void createNewDraggableSeat() {
            MyDraggableSeat mds;

            boolean killLoop = false;
            while(!killLoop) {
                boolean alreadyThere = false;
                boolean isAGoodName = false;

                String name = JOptionPane.showInputDialog(this, "Inserisci il nome del posto");
                if(!name.equalsIgnoreCase("") || name.trim().length()!=0) {
                    isAGoodName=true;
                }
                for(String s : createdSeatsName) {
                    if(name.trim().toLowerCase().equalsIgnoreCase(s.trim().toLowerCase())) {
                        alreadyThere = true;
                    }
                }
                if(isAGoodName) {
                    if(!alreadyThere) {
                        mds = new MyDraggableSeat(0,0);
                        configureMDS(mds, name);
                        draggableSeatsList.add(mds);
                        isSomethingChanged = true;
                        killLoop = true;
                    } else {
                        JOptionPane.showMessageDialog(this, "Nome già esistente!");
                        killLoop = false;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Inserisci un nome!");
                    killLoop = false;
                }
            }
        }

        private JPopupMenu initJPopupMenu(String name) {
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem deleteItem = new JMenuItem("Rimuovi Posto");
            deleteItem.addActionListener(e -> removeSeat(name));
            popupMenu.add(deleteItem);
            return popupMenu;
        }


        private void doSave() {
            DraggableSeatsToCSV.createCSVFromDraggableSeatsList( draggableSeatsList
                    , DataReferences.PIANTINAFOLDERPATH + nomeSala +".csv"
                    , false);
            JOptionPane.showMessageDialog(this, "Piantina salvata con successo!");
            summoner.triggerModificationToHallList();
            isSomethingChanged = false;
        }

        private int screenX = 0;
        private int screenY = 0;
        private int myX = 0;
        private int myY = 0;
        private List<Integer> myXList = new ArrayList<>();
        private List<Integer> myYList = new ArrayList<>();
        private void initMouseListenerForMDS(MyDraggableSeat mds) {
            mds.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    screenX = e.getXOnScreen();
                    screenY = e.getYOnScreen();
                    myX = mds.getX();
                    myY = mds.getY();
                    myXList.clear();
                    myYList.clear();
                    for(MyDraggableSeat mds : selectedMDSList) {
                        myXList.add(mds.getX());
                        myYList.add(mds.getY());
                    }
                }
            });

            mds.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    int deltaX = e.getXOnScreen() - screenX;
                    int deltaY = e.getYOnScreen() - screenY;

                    if(selectedMDSList.size()==0) {
                        mds.setLocation(myX + deltaX, myY + deltaY);
                    } else {
                        for(int i = 0; i<selectedMDSList.size(); i++) {
                            if(((LineBorder)selectedMDSList.get(i).getBorder()).getLineColor()== Color.CYAN) {
                                selectedMDSList.get(i).setLocation(myXList.get(i) + deltaX, myYList.get(i) + deltaY);
                            }
                        }
                    }
                    isSomethingChanged = true;
                }
            });
        }

        private boolean dontCreateBox = false;
        private boolean isNewRect = false;
        private Rectangle mouseRect = new Rectangle();
        private Point mousePt = new Point();
        private List<MyDraggableSeat> selectedMDSList = new ArrayList<>();

        private MouseListener mouseHandler = new MouseAdapter() {
            @Override
            public  void mouseClicked(MouseEvent e) {
                for(MyDraggableSeat mds : selectedMDSList) {
                    if(((LineBorder)mds.getBorder()).getLineColor()== Color.CYAN) {
                        mds.setBorder(new LineBorder(Color.BLUE, 3));
                    }
                }
                selectedMDSList.clear();
                dontCreateBox = false;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(!dontCreateBox) {
                    isNewRect = true;
                    mousePt = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(!dontCreateBox) {
                    isNewRect = true;
                    mouseRect.setBounds(0, 0, 0, 0);
                    repaint();
                }

                for(MyDraggableSeat mds : draggableSeatsList) {
                    if(((LineBorder)mds.getBorder()).getLineColor()== Color.CYAN) {
                        selectedMDSList.remove(mds);
                    }
                }

            }
        };

        private MouseMotionListener mouseMotionHandler = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(!dontCreateBox) {
                    isNewRect = false;
                    mouseRect.setBounds(
                            Math.min(mousePt.x, e.getX()),
                            Math.min(mousePt.y, e.getY()),
                            Math.abs(mousePt.x - e.getX()),
                            Math.abs(mousePt.y - e.getY()));

                    Rectangle selectionBox = new Rectangle(mouseRect.x, mouseRect.y, mouseRect.width, mouseRect.height);
                    for(MyDraggableSeat mds : draggableSeatsList) {
                        if(selectionBox.intersects(mds.getX(), mds.getY(), mds.getWidth(), mds.getHeight())){
                            selectedMDSList.add(mds);
                            mds.setBorder(new LineBorder(Color.CYAN, 3));
                        } else {
                            selectedMDSList.remove(mds);
                            mds.setBorder(new LineBorder(Color.BLUE, 3));
                        }
                    }
                    repaint();
                }
            }
        };

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2D = (Graphics2D) g;
            Composite originalComposite = g2D.getComposite();
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2D.setColor(new Color(0x0073b5e9));

            if (!isNewRect && !dontCreateBox) {
                g2D.fill(new Rectangle(mouseRect.x, mouseRect.y,mouseRect.width, mouseRect.height));
                super.paintComponent(g2D);
            }

            g2D.setComposite(originalComposite);
        }

    }
}