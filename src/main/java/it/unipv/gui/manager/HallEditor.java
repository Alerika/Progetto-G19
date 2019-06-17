package it.unipv.gui.manager;

import it.unipv.DB.DBConnection;
import it.unipv.DB.HallOperations;
import it.unipv.gui.common.Seat;
import it.unipv.gui.common.SeatTYPE;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.DataReferences;
import javafx.application.Platform;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class HallEditor extends JFrame {

    private DraggableSeatPanel draggableSeatsPanel;
    private boolean isSomethingChanged = false;
    private String nomeSala;
    private HallPanelController hallPanelController;
    private HallEditor hallEditor = this;
    private boolean wasItAlreadyCreated;
    private HallOperations ho;

    HallEditor( String nomeSala
              , HallPanelController hallPanelController
              , boolean wasItAlreadyCreated
              , DBConnection dbConnection) {
        this.nomeSala = nomeSala;
        this.hallPanelController = hallPanelController;
        this.wasItAlreadyCreated = wasItAlreadyCreated;
        ho = new HallOperations(dbConnection);

        initMenuBar();
        initDraggableSeatsPanel(wasItAlreadyCreated);
        initFrame();
    }

    HallEditor( String nomeSala
              , HallPanelController hallPanelController
              , int rows
              , int columns
              , DBConnection dbConnection) {
        this.nomeSala = nomeSala;
        this.hallPanelController = hallPanelController;
        ho = new HallOperations(dbConnection);
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
        saveItem.addActionListener(e -> {
            if(draggableSeatsPanel.areAllSeatsBeenNamed()) {
                draggableSeatsPanel.doSave();
            } else {
                JOptionPane.showMessageDialog(this, "Devi dare un nome a tutti i posti prima di poter salvare!");
            }
        });
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

        JMenu questionMenu = new JMenu("?");
        menuBar.add(questionMenu);

        JMenuItem guideItem = new JMenuItem("Guida");
        questionMenu.add(guideItem);
        guideItem.addActionListener(e -> JOptionPane.showMessageDialog(hallEditor, usage(), "Guida all'Editor", JOptionPane.INFORMATION_MESSAGE));
        guideItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_MASK));
    }

    private String usage() {
        return
                "Scorciatoie:\n"
              + "   Salva:  CTRL+S\n"
              + "   Inserisci singolo posto:    CTRL+N\n"
              + "   Inserisci griglia posti:    CTRL+M\n"
              + "   Cancellare più posti alla volta:    dopo averli selezionati, premere DEL (CANC)\n"
              + "   Copia incolla di posti:    dopo averli selezionati, premere CTRL+C e successivamente CTRL+V\n"
              + "   Settare a NORMALE più posti alla volta:    dopo averli selezionati, premere ALT+N\n"
              + "   Settare a VIP più posti alla volta:    dopo averli selezionati, premere ALT+M\n"
              + "   Settare a DISABILE più posti alla volta:    dopo averli selezionati, premere ALT+D\n"
              + "\n"
              + "Modifiche ai posti:\n"
              + "   Rinominare un posto:    click destro sul posto e scegliere \"Rinomina\"\n"
              + "   Eliminare un posto:    click destro sul posto e scegliere \"Elimina\"\n"
              + "   Modifica tipologia di un posto:    click destro sul posto e scegliere \"Modifica tipo\"\n"  ;
    }

    private void initDraggableSeatsPanel(boolean wasItAlreayCreated) {
        draggableSeatsPanel = new DraggableSeatPanel(wasItAlreayCreated);
    }

    private void initDraggableSeatsPanel(int rows, int columns) {
        draggableSeatsPanel = new DraggableSeatPanel(rows, columns);
    }


    private void initFrame() {
        setTitle("Editor " + nomeSala);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/GoldenMovieStudioIcon.png")));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                doDisposeOnExit();
            }
        });
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new GridLayout(1, 1));
        add(draggableSeatsPanel);
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setVisible(true);
        addKeyListener(keyHandler);
    }

    private void doDisposeOnExit() {
        if(!draggableSeatsPanel.areAllSeatsBeenNamed()) {
            int reply = JOptionPane.showConfirmDialog(this, "La piantina non verrà salvata perché non a tutti i posti è stato assegnato un nome.\nSicuro di voler uscire?", "Scegli", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                dispose();
            }
        } else {
            if (isSomethingChanged) {
                int reply = JOptionPane.showConfirmDialog(this, "Salvare le modifiche prima di uscire?", "Scegli", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    draggableSeatsPanel.doSave();
                }
            }
            dispose();
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

            if((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)) {
                draggableSeatsPanel.updateSelectedSeatsType(SeatTYPE.VIP);
            }

            if((e.getKeyCode() == KeyEvent.VK_N) && ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)) {
                draggableSeatsPanel.updateSelectedSeatsType(SeatTYPE.NORMALE);
            }

            if((e.getKeyCode() == KeyEvent.VK_D) && ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)) {
                draggableSeatsPanel.updateSelectedSeatsType(SeatTYPE.DISABILE);
            }
        }
    };


    /***********************************************************************************************************************************************************************/
    private class DraggableSeatPanel extends JPanel {

        private List<String> createdSeatsName = new ArrayList<>();
        private List<Seat> draggableSeatsList = new ArrayList<>();

        DraggableSeatPanel(boolean wasItAlreadyCreated) {
            if(wasItAlreadyCreated) { initDraggableSeatsList(); }
            initSeatPanel();
        }

        DraggableSeatPanel(int rows, int columns) {
            initDraggableSeatsGrid(rows, columns);
            initSeatPanel();
        }

        private void initSeatPanel() {
            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseMotionHandler);
            setOpaque(false);
            setLayout(null);
            pack();
        }

        private void initDraggableSeatsGrid(int rows, int columns) {
            draggableSeatsList.addAll(initGrid(rows, columns, true));
        }

        private List<Seat> initGrid(int rows, int columns, boolean doYouWantName) {
            List<Seat> res = new ArrayList<>();
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
                    Seat mds = new Seat(x,y, SeatTYPE.NORMALE);
                    configureMDS( mds
                                , doYouWantName ? DataReferences.ALPHABET[cont]+""+(j+1) : ""
                                , doYouWantName);
                    res.add(mds);
                }
                cont++;
            }
            return res;
        }

        private void initDraggableSeatsList() {
            draggableSeatsList = ho.retrieveSeats(nomeSala);
            for(Seat mds : draggableSeatsList) {
                configureMDS(mds, mds.getText(), true);
            }
        }

        private void configureMDS(Seat mds, String name, boolean doYouWantName) {
            if(doYouWantName) {
                createdSeatsName.add(mds.getText());
                mds.setText(name);
            }
            mds.setComponentPopupMenu(initJPopupMenu(mds));
            initMouseListenerForMDS(mds);
            add(mds);
        }

        private void removeSeat(int x, int y) {
            Seat toRemove = null;
            for(Seat mds : draggableSeatsList) {
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
                for (Seat mds : selectedMDSList) {
                    createdSeatsName.remove(mds.getText());
                    draggableSeatsList.remove(mds);
                    remove(mds);
                    repaint();
                }
            }
            isSomethingChanged = true;
            selectedMDSList.clear();
        }

        private List<Seat> mdsToPaste = new ArrayList<>();
        private void copySelectedSeats() {
            if(selectedMDSList.size()>0) {
                for(Seat mds : selectedMDSList) {
                    if(!mds.getIsCopied()) {
                        mdsToPaste.add(mds);
                        mds.setIsCopied(true);
                    }
                }
            }
        }

        private void pasteCopiedSeats() {
            List<Seat> pasted = new ArrayList<>();
            if(mdsToPaste.size()>0) {
                for(Seat mds : mdsToPaste) {
                    Seat copy = new Seat(mds.getX()-15, mds.getY()-15, mds.getType());
                    configureMDS(copy, mds.getText()+" copy", true);
                    repaint();
                    mds.setIsCopied(false);
                    pasted.add(copy);
                }
            }

            if(selectedMDSList.size()>0) {
                for(Seat mds : selectedMDSList) {
                    mds.setBorder(new LineBorder(Color.BLUE, 3));
                }
                selectedMDSList.clear();
            }

            for(Seat mds : pasted) {
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
            Seat mds = new Seat(0,0, SeatTYPE.NORMALE);
            configureMDS(mds, "", false);
            for(Seat m : selectedMDSList) {
                m.setBorder(new LineBorder(Color.BLUE, 3));
            }
            selectedMDSList.clear();
            mds.setBorder(new LineBorder(Color.CYAN, 3));
            selectedMDSList.add(mds);
            draggableSeatsList.add(mds);
            isSomethingChanged = true;
            repaint();
        }

        int rows = 0;
        int columns = 0;
        boolean canceled;
        void addMultipleSeats() {
            configureGridJOptionPaneMenu();
            if(!canceled) {
                List<Seat> grid = initGrid(rows, columns, false);
                draggableSeatsList.addAll(grid);
                for (Seat mds : selectedMDSList) {
                    mds.setBorder(new LineBorder(Color.BLUE, 3));
                }
                selectedMDSList.clear();
                for (Seat mds : grid) {
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
                if(rows.getText().trim().equalsIgnoreCase("") || columns.getText().trim().equalsIgnoreCase("")) {
                    JOptionPane.showMessageDialog(hallEditor, "Devi inserire entrambi i dati!");
                    canceled = true;
                } else {
                    this.rows = Integer.parseInt(rows.getText());
                    this.columns = Integer.parseInt(columns.getText());
                    canceled = false;
                }
            } else {
                canceled = true;
            }
        }

        private JPopupMenu initJPopupMenu(Seat mds) {
            JPopupMenu popupMenu = new JPopupMenu();

            JMenuItem deleteItem = new JMenuItem("Elimina");
            deleteItem.addActionListener(e -> removeSeat(mds.getX(), mds.getY()));
            popupMenu.add(deleteItem);

            JMenuItem renameItem = new JMenuItem("Rinomina");
            renameItem.addActionListener(e -> renameSeat(mds) );
            popupMenu.add(renameItem);

            JMenuItem modifySeatType = new JMenuItem("Modifica tipo");
            modifySeatType.addActionListener(e -> updateSeatType(mds, configureTypeSelectionJOptionPaneMenu()));
            popupMenu.add(modifySeatType);
            return popupMenu;
        }


        private void updateSeatType(Seat mds, SeatTYPE type) {
            if(type!=null) {
                mds.setType(type);
                mds.updateBackgroundForChangingType();
                isSomethingChanged = true;
            }
        }

        private SeatTYPE configureTypeSelectionJOptionPaneMenu() {
            JComboBox<SeatTYPE> typeSelector = new JComboBox<>(new SeatTYPE[] {SeatTYPE.NORMALE, SeatTYPE.VIP, SeatTYPE.DISABILE});
            Object[] message = { "Tipo: ", typeSelector, };

            int option = JOptionPane.showConfirmDialog(hallEditor, message, "Inserisci nuova tipologia del posto", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                canceled = false;
                return (SeatTYPE) typeSelector.getSelectedItem();
            } else {
                canceled = true;
                return null;
            }
        }

        void updateSelectedSeatsType(SeatTYPE type) {
            for(Seat mds : selectedMDSList) {
                updateSeatType(mds, type);
            }
        }

        private void renameSeat(Seat mds) {
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
            }
        }

        private void doSave() {
            if(wasItAlreadyCreated) {
                ho.updateHallSeats(nomeSala, draggableSeatsList);
                ho.updateHallPreview(nomeSala, saveSnapshot(this));
                JOptionPane.showMessageDialog(hallEditor, "Piantina sovrascritta con successo!");
            } else {
                ho.insertNewHall(nomeSala, draggableSeatsList);
                ho.insertNewHallpreview(nomeSala, saveSnapshot(this));
                JOptionPane.showMessageDialog(hallEditor, "Piantina creata con successo!");
                wasItAlreadyCreated = true;
            }
            Platform.runLater(() -> hallPanelController.triggerModificationToHallList());
            isSomethingChanged = false;
        }

        private ByteArrayInputStream saveSnapshot(Component c) {
            try {
                BufferedImage img = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D graphs = img.createGraphics();
                graphs.setBackground(Color.WHITE);
                graphs.clearRect(0, 0, c.getWidth(), c.getHeight());
                c.paint(graphs);

                final ByteArrayOutputStream output = new ByteArrayOutputStream() {
                    @Override
                    public synchronized byte[] toByteArray() {
                        return this.buf;
                    }
                };

                ImageIO.write(img, "jpg", output);
                return new ByteArrayInputStream(output.toByteArray(), 0, output.size());
            } catch (IOException e) {
                throw new ApplicationException("Errore durante il salvataggio dello snapshot", e);
            }
        }

        private boolean areAllSeatsBeenNamed() {
            boolean canExit = true;
            for(Seat s : draggableSeatsList) {
                if(s.getText().isEmpty()) {
                    canExit = false;
                    break;
                }
            }
            return canExit;
        }

        private int screenX = 0;
        private int screenY = 0;
        private int myX = 0;
        private int myY = 0;
        private List<Integer> myXList = new ArrayList<>();
        private List<Integer> myYList = new ArrayList<>();
        private void initMouseListenerForMDS(Seat mds) {
            mds.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    screenX = e.getXOnScreen();
                    screenY = e.getYOnScreen();
                    myX = mds.getX();
                    myY = mds.getY();
                    myXList.clear();
                    myYList.clear();
                    for(Seat mds : selectedMDSList) {
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
                            selectedMDSList.get(i).setLocation(myXList.get(i) + deltaX, myYList.get(i) + deltaY);

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
        private List<Seat> selectedMDSList = new ArrayList<>();

        private MouseListener mouseHandler = new MouseAdapter() {
            @Override
            public  void mouseClicked(MouseEvent e) {
                for(Seat mds : selectedMDSList) {
                    mds.setBorder(new LineBorder(Color.BLUE, 3));
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
                    for(Seat mds : draggableSeatsList) {
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

        //Override di Paint e non PaintComponent perché se no disegna il selection box sotto ai posti e non sopra
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2D = (Graphics2D) g;
            Composite originalComposite = g2D.getComposite();
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2D.setColor(new Color(0x0073b5e9));

            if (!isNewRect && !dontCreateBox) {
                g2D.fill(new Rectangle(mouseRect.x, mouseRect.y,mouseRect.width, mouseRect.height));
                super.paint(g2D);
            }

            g2D.setComposite(originalComposite);
        }
    }
}