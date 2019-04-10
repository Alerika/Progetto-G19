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
    private ManagerForm managerForm;
    private HallEditor hallEditor = this;


    HallEditor( String nomeSala
            , ManagerForm managerForm
            , boolean wasItAlreadyCreated) {
        this.nomeSala = nomeSala;
        this.managerForm = managerForm;

        initMenuBar();
        initDraggableSeatsPanel(wasItAlreadyCreated);
        initFrame();
    }

    HallEditor( String nomeSala
            , ManagerForm managerForm
            , int rows
            , int columns) {
        this.nomeSala = nomeSala;
        this.managerForm = managerForm;
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
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));

        JMenu editMenu = new JMenu("Modifica");
        menuBar.add(editMenu);

        JMenuItem insertNewSeatItem = new JMenuItem("Aggiungi");
        editMenu.add(insertNewSeatItem);
        insertNewSeatItem.addActionListener(e -> draggableSeatsPanel.createNewDraggableSeat());
        insertNewSeatItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));

        JMenuItem insertMultipleSeatItem = new JMenuItem("Aggiungi griglia");
        editMenu.add(insertMultipleSeatItem);
        insertMultipleSeatItem.addActionListener(e -> draggableSeatsPanel.addMultipleSeats());
        insertMultipleSeatItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
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
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setVisible(true);
        addKeyListener(keyHandler);
    }

    private void doDisposeOnExit() {
        if(isSomethingChanged) {
            int reply = JOptionPane.showConfirmDialog(hallEditor, "Salvare le modifiche prima di uscire?", "", JOptionPane.YES_NO_OPTION);
            if(reply == JOptionPane.YES_OPTION) {
                draggableSeatsPanel.doSave();
            }
        }
    }

    private KeyListener keyHandler = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_DELETE) {
                draggableSeatsPanel.removeSelectedSeats();
            }

            if((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                draggableSeatsPanel.copySelectedSeats();
            }

            if((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                draggableSeatsPanel.pasteCopiedSeats();
            }
        }
    };


    /***********************************************************************************************************************************************************************/
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
            draggableSeatsList.addAll(initGrid(rows, columns, true));
        }

        private List<MyDraggableSeat> initGrid(int rows, int columns, boolean doYouWantName) {
            List<MyDraggableSeat> res = new ArrayList<>();
            int x = 5;
            int y = 5;
            int cont = 0;
            for(int i=0; i<rows; i++) {
                if(i>0) {
                    y += DataReferences.MYDRAGGABLESEATHEIGTH + 5;
                    x = 5;
                }
                for(int j=0; j<columns; j++) {
                    if(j>0) { x += DataReferences.MYDRAGGABLESEATWIDTH+5; }
                    MyDraggableSeat mds = new MyDraggableSeat(x,y);
                    configureMDS(mds, DataReferences.ALPHABET[cont]+""+(j+1), doYouWantName);
                    res.add(mds);
                }
                cont++;
            }
            return res;
        }

        private void initDraggableSeatsList() {
            draggableSeatsList = CSVToDraggableSeats.getMyDraggableSeatListFromCSV(DataReferences.PIANTINAFOLDERPATH+nomeSala+".csv");
            for(MyDraggableSeat mds : draggableSeatsList) {
                configureMDS(mds, mds.getText(), true);
            }
        }

        private void configureMDS(MyDraggableSeat mds, String name, boolean doYouWantName) {
            if(doYouWantName) {
                createdSeatsName.add(mds.getText());
                mds.setText(name);
            }
            mds.setComponentPopupMenu(initJPopupMenu(mds));
            initMouseListenerForMDS(mds);
            add(mds);
        }

        private void removeSeat(int x, int y) {
            MyDraggableSeat toRemove = null;
            for(MyDraggableSeat mds : draggableSeatsList) {
                if((x==mds.getX())&&(y==mds.getY())) {
                    toRemove = mds;
                    break;
                }
            }
            if(toRemove!=null) {
                createdSeatsName.remove(toRemove.getText());
                draggableSeatsList.remove(toRemove);
                remove(toRemove);
                isSomethingChanged = true;
                repaint();
            }
        }

        private void removeSelectedSeats() {
            if (selectedMDSList.size() > 0) {
                for (MyDraggableSeat mds : selectedMDSList) {
                    if (((LineBorder) mds.getBorder()).getLineColor() == Color.CYAN) {
                        createdSeatsName.remove(mds.getText());
                        draggableSeatsList.remove(mds);
                        remove(mds);
                        repaint();
                    }
                }
            }
            isSomethingChanged = true;
            selectedMDSList.clear();
        }

        private List<MyDraggableSeat> mdsToPaste = new ArrayList<>();
        private void copySelectedSeats() {
            if(selectedMDSList.size()>0) {
                for(MyDraggableSeat mds : selectedMDSList) {
                    if (((LineBorder) mds.getBorder()).getLineColor() == Color.CYAN) {
                        if(!mds.getIsCopied()) {
                            mdsToPaste.add(mds);
                            mds.setIsCopied(true);
                        }
                    }
                }
            }
        }

        private void pasteCopiedSeats() {
            List<MyDraggableSeat> pasted = new ArrayList<>();
            if(mdsToPaste.size()>0) {
                for(MyDraggableSeat mds : mdsToPaste) {
                    MyDraggableSeat copy = new MyDraggableSeat(mds.getX()-15, mds.getY()-15);
                    configureMDS(copy, mds.getText()+" copy", true);
                    repaint();
                    mds.setIsCopied(false);
                    pasted.add(copy);
                }
            }

            if(selectedMDSList.size()>0) {
                for(MyDraggableSeat mds : selectedMDSList) {
                    mds.setBorder(new LineBorder(Color.BLUE, 3));
                }
                selectedMDSList.clear();
            }

            for(MyDraggableSeat mds : pasted) {
                selectedMDSList.add(mds);
                mds.setBorder(new LineBorder(Color.CYAN, 3));
            }

            isNewRect = true;
            isSomethingChanged = true;
            draggableSeatsList.addAll(pasted);
            mdsToPaste.clear();
            pasted.clear();
        }

        private void createNewDraggableSeat() {
            MyDraggableSeat mds = new MyDraggableSeat(0,0);
            configureMDS(mds, "", false);
            selectedMDSList.clear();
            mds.setBorder(new LineBorder(Color.CYAN, 3));
            selectedMDSList.add(mds);
            draggableSeatsList.add(mds);
            isSomethingChanged = true;
            repaint();
        }

        private JPopupMenu initJPopupMenu(MyDraggableSeat mds) {
            JPopupMenu popupMenu = new JPopupMenu();

            JMenuItem deleteItem = new JMenuItem("Rimuovi");
            deleteItem.addActionListener(e -> removeSeat(mds.getX(), mds.getY()));
            popupMenu.add(deleteItem);

            JMenuItem renameItem = new JMenuItem("Rinomina");
            renameItem.addActionListener(e -> renameSeat(mds) );
            popupMenu.add(renameItem);
            return popupMenu;
        }

        private void renameSeat(MyDraggableSeat mds) {
            String name = JOptionPane.showInputDialog(hallEditor, "Rinomina il posto!");
            if(name!=null) {
                if(!name.trim().equalsIgnoreCase("")) {
                    boolean alreadyThere = false;
                    for(String s : createdSeatsName) {
                        if(s.trim().equalsIgnoreCase(name)) {
                            alreadyThere = true;
                            break;
                        }
                    }
                    if(!alreadyThere) {
                        createdSeatsName.remove(mds.getText());
                        mds.setText(name);
                        createdSeatsName.add(name);
                        isSomethingChanged = true;
                    } else {
                        JOptionPane.showMessageDialog(hallEditor, "Nome già esistente!");
                    }
                } else {
                    JOptionPane.showMessageDialog(hallEditor, "Devi inserire un nome!");
                }
                isSomethingChanged = false;
            }
        }


        private void doSave() {
            DraggableSeatsToCSV.createCSVFromDraggableSeatsList( draggableSeatsList
                    , DataReferences.PIANTINAFOLDERPATH + nomeSala +".csv"
                    , false);
            JOptionPane.showMessageDialog(hallEditor, "Piantina salvata con successo!");
            managerForm.triggerModificationToHallList();
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
                            if(!mds.getIsSelected()) {
                                mds.setIsSelected(true);
                                mds.setBorder(new LineBorder(Color.CYAN, 3));
                                selectedMDSList.add(mds);
                            }
                        } else {
                            if(mds.getIsSelected()) {
                                selectedMDSList.remove(mds);
                                mds.setIsSelected(false);
                                mds.setBorder(new LineBorder(Color.BLUE, 3));
                            }
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

        int rows;
        int columns;
        boolean canceled;
        public void addMultipleSeats() {
            configureGridJOptionPaneMenu();
            if(!canceled) {
                List<MyDraggableSeat> grid = initGrid(rows, columns, false);
                draggableSeatsList.addAll(grid);
                for(MyDraggableSeat mds : selectedMDSList) {
                    if (((LineBorder) mds.getBorder()).getLineColor() == Color.CYAN) {
                        mds.setBorder(new LineBorder(Color.BLUE, 3));
                    }
                }
                selectedMDSList.clear();
                for(MyDraggableSeat mds : grid) {
                    mds.setBorder(new LineBorder(Color.CYAN, 3));
                }
                selectedMDSList.addAll(grid);
            }
            repaint();
        }

        private void configureGridJOptionPaneMenu() {
            JTextField rows = new JTextField();
            JTextField columns = new JTextField();
            Object[] message = {
                    "Righe:", rows,
                    "Colonne:", columns
            };

            int option = JOptionPane.showConfirmDialog(hallEditor, message, "Inserisci numero di righe e colonne", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                this.rows = Integer.parseInt(rows.getText());
                this.columns = Integer.parseInt(columns.getText());
                canceled = false;
            } else {
                canceled = true;
            }
        }
    }
}