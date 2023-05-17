import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

// View observes Model for state changes
public class View implements Observer {
    boolean initialised;
    JFrame frame;
    JPanel outerPanel;
    JPanel boardPanel;
    JPanel player1Panel;
    JPanel player2Panel;
    Container container;
    int squareSize = 150;
    int propertiesPerSide = 9;
    ArrayList<JPanel> squares;
    int padding = 10;
    ImageIcon starIcon;
    Model model;
    Controller controller;

    public View(Model model, Controller controller) throws InterruptedException, InvocationTargetException {
        this.model = model;
        this.controller = controller;
        // View observes Model
        model.addObserver(this);
        this.squares = new ArrayList<JPanel>();

        // Use threads
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                // Create GUI when game starts
                createGUI();
                // Update all the squares so that they initially will show all their labels and icons
                for (int i = 0; i < 40; i++) {
                    updateSquare(i);
                }
            }

             });

    }

    private void updateButtons() {
        // Enable or disable buttons to match the model using variables in the Model (getCanPay() etc. returns a boolean)
        boardPanel.getComponent(2).setEnabled(model.getCanRollPass());
        boardPanel.getComponent(3).setEnabled(model.getCanBuy());
        boardPanel.getComponent(4).setEnabled(model.getCanPay());
    }

    private void updateSquare(int squareIndex) {
        JPanel square = this.squares.get(squareIndex);
        int price = model.getHotelPrice(squareIndex);
        if (price > 0) {
            // If there is a hotel on the square
            ((JLabel)square.getComponent(0)).setText("£"+Integer.toString(model.getHotelPrice(squareIndex)));
        }
        ((JLabel)square.getComponent(1)).setText(model.getSquareName(squareIndex));

        String owner = model.getHotelOwnerName(squareIndex);
        if (owner != null) {
            square.setBackground(model.getPlayerColor(owner));
            ((JLabel)square.getComponent(3)).setText(Integer.toString(model.getHotelRating(squareIndex)));
            square.getComponent(3).setVisible(owner != null);
        } else {
            square.setBackground(Color.white);
            if (square.getComponents().length > 3) {
                // Get star label
                square.getComponent(3).setVisible(owner != null);
            }
        }


        // Clear contents of previous label
        JLabel iconLabel = ((JLabel)square.getComponent(2));
        iconLabel.removeAll();
        for (String playername: this.model.getPlayerNamesOnSquare(squareIndex)) {
            ImageIcon playerCounter = this.model.getSmallImageIcon(playername);
            iconLabel.add(new JLabel(playerCounter));
        }
        square.repaint();


    }

    private void updatePlayerInfoPanel(int playerIndex) {
        JPanel playerPanel;
        if (playerIndex == 0) {
            // Player 1 panel
            playerPanel = this.player1Panel;
        } else {
            // Player 2 panel
            playerPanel = this.player2Panel;
        }

        String playerName = this.model.getPlayerName(playerIndex);
        ((JLabel)playerPanel.getComponent(0)).setText("Name: "+playerName);
        ((JLabel)playerPanel.getComponent(1)).setText("Bank: £"+this.model.getPlayerBalance(playerName));
        // Sort hotels owned into groups and seperate with <br>
        String hotelsOwned = new String("Hotels owned: ");
        String previousGroup = new String("_");
        // Get hotels owned by player
        for (String hotelName: model.getHotelsOwnedByPlayer(playerName)) {
            if (!hotelName.contains(previousGroup)) {
                // Seperate groups with breakline
                hotelsOwned += "<br>";
                previousGroup = hotelName.substring(0,1);
            }
            hotelsOwned += hotelName;
        }
        ((JLabel)playerPanel.getComponent(2)).setText("<html>"+hotelsOwned+"</html>");

        ImageIcon icon1 = this.model.getPlayerImageIcon(playerName);
        ((JLabel)playerPanel.getComponent(3)).setIcon(icon1);

    }

    private void createPlayerInfoPanels() {
        // This sets up the player info panels initially, but we will have to update
        // the panels when information updates in the model, so we'll use an Observer/Observable for that
        int rowHeight = 30;
        this.player1Panel.setBackground(model.getPlayerColor("player1"));
        JLabel nameLabel = new JLabel("Name: Player1");
        nameLabel.setBounds(padding,padding,400-padding,rowHeight);
        nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,20));
        this.player1Panel.add(nameLabel);

        JLabel bankLabel = new JLabel("Bank: ");
        bankLabel.setText("Bank: £"+this.model.getPlayerBalance("player1"));
        bankLabel.setBounds(padding,padding+(rowHeight+padding),400-padding,rowHeight);
        bankLabel.setFont(new Font(Font.SERIF,Font.BOLD,20));
        this.player1Panel.add(bankLabel);

        // Sort hotels owned into groups and seperate with <br>
        String hotelsOwned = new String("Hotels owned: ");
        String previousGroup = new String("_");
        // Get hotels owned by player
        for (String hotelName: model.getHotelsOwnedByPlayer("player1")) {
            if (!hotelName.contains(previousGroup)) {
                // Seperate groups with breakline
                hotelsOwned += "<br>";
                previousGroup = hotelName.substring(0,1);
            }
            hotelsOwned += hotelName;
        }

        JLabel hotelsOwnedLabel = new JLabel("<html>"+hotelsOwned+"</html>");
        hotelsOwnedLabel.setBounds(padding,padding+(rowHeight+padding)*2,400-padding,rowHeight*8);
        hotelsOwnedLabel.setFont(new Font(Font.SERIF,Font.BOLD,20));
        player1Panel.add(hotelsOwnedLabel);

        ImageIcon icon1 = this.model.getPlayerImageIcon("player1");
        JLabel iconLabel = new JLabel(icon1);
        iconLabel.setBounds(300-padding,padding,rowHeight*2,rowHeight*2);
        player1Panel.add(iconLabel);

        ////////////////////////////////////////////////////////////////// Player 2
        this.player2Panel.setBackground(model.getPlayerColor("player2"));
        JLabel nameLabel2 = new JLabel("Name: Player2");
        nameLabel2.setBounds(padding,padding,400-padding,rowHeight);
        nameLabel2.setFont(new Font(Font.SERIF,Font.BOLD,20));
        this.player2Panel.add(nameLabel2);

        JLabel bankLabel2 = new JLabel("Bank: ");
        bankLabel2.setText("Bank: £"+this.model.getPlayerBalance("player2"));
        bankLabel2.setBounds(padding,padding+(rowHeight+padding),400-padding,rowHeight);
        bankLabel2.setFont(new Font(Font.SERIF,Font.BOLD,20));
        this.player2Panel.add(bankLabel2);

        // Sort hotels owned into groups and seperate with <br>
        String hotelsOwned2 = new String("Hotels owned: ");
        String previousGroup2 = new String("_");
        // Get hotels owned by player
        for (String hotelName: model.getHotelsOwnedByPlayer("player2")) {
            if (!hotelName.contains(previousGroup2)) {
                // Seperate groups with breakline
                hotelsOwned2 += "<br>";
                previousGroup2 = hotelName.substring(0,1);
            }
            hotelsOwned2 += hotelName;
        }

        JLabel hotelsOwnedLabel2 = new JLabel("<html>"+hotelsOwned2+"</html>");
        hotelsOwnedLabel2.setBounds(padding,padding+(rowHeight+padding)*2,400-padding,rowHeight*8);
        hotelsOwnedLabel2.setFont(new Font(Font.SERIF,Font.BOLD,20));
        player2Panel.add(hotelsOwnedLabel2);

        ImageIcon icon2 = this.model.getPlayerImageIcon("player2");
        JLabel iconLabel2 = new JLabel(icon2);
        iconLabel2.setBounds(300-padding,padding,rowHeight*2,rowHeight*2);
        player2Panel.add(iconLabel2);
    }

    private void createButtonsAndLabels() {
        // Add label to display who's turn it is
        JLabel playerTurnLabel = new JLabel("Player 1 turn",SwingConstants.CENTER);
        playerTurnLabel.setBounds(squareSize*3/2,squareSize,squareSize*7/2,squareSize);
        playerTurnLabel.setFont(new Font(Font.SERIF,Font.BOLD,20));
        boardPanel.add(playerTurnLabel);

        // Add label to show messages from the model being updated
        JLabel userMessageLabel = new JLabel("Game started",SwingConstants.CENTER);
        userMessageLabel.setBounds(squareSize*3/2,squareSize*5/3,squareSize*7/2,squareSize);
        userMessageLabel.setFont(new Font(Font.SERIF,Font.BOLD,20));
        boardPanel.add(userMessageLabel);

        // Option buttons
        JButton rollDiceButton = new JButton("Roll/pass");
        rollDiceButton.setBounds(squareSize*3/2,squareSize*9/2+padding,squareSize,squareSize/2);
        rollDiceButton.setFont(new Font(Font.SERIF,Font.BOLD,20));
        rollDiceButton.setActionCommand("roll/pass");
        rollDiceButton.addActionListener(this.controller);
        boardPanel.add(rollDiceButton);

        JButton buyButton = new JButton("Buy");
        buyButton.setBounds(squareSize*11/4,squareSize*9/2+padding,squareSize,squareSize/2);
        buyButton.setFont(new Font(Font.SERIF,Font.BOLD,20));
        buyButton.setActionCommand("buy");
        buyButton.addActionListener(this.controller);
        boardPanel.add(buyButton);

        JButton payButton = new JButton("Pay");
        payButton.setBounds(squareSize*4,squareSize*9/2+padding,squareSize,squareSize/2);
        payButton.setFont(new Font(Font.SERIF,Font.BOLD,20));
        payButton.setActionCommand("pay");
        payButton.addActionListener(this.controller);
        boardPanel.add(payButton);

        this.updateButtons();

    }

    private void createSquares() {
        // Define smaller square size
        int propertyWidth = squareSize / 2;

        ////////////// All positions on board are calculated on basis of square size
        // Padding is a spacing used at the top and left hand side of board
        // GO square
        JPanel panelse = new JPanel();
        panelse.setLayout(null);

        // Set index number, the squares array changes dynamically so it increases
        // Setname sets index to be used when handling cheatmode requests
        panelse.setName(Integer.toString(this.squares.size()));
        // Configure for controller to handle mouseclicks on this panel/square
        panelse.addMouseListener((MouseListener) this.controller);

        // propertywidth is half of squareSize (it's the smaller squares)
        panelse.setBounds(padding+squareSize+propertiesPerSide*propertyWidth,padding+squareSize+propertiesPerSide*propertyWidth,squareSize,squareSize);
        panelse.setBackground(Color.white);
        panelse.setBorder(new LineBorder(Color.black,1));
        // Price label
        JLabel priceLabel = new JLabel("",SwingConstants.CENTER);
        priceLabel.setBounds(0,(squareSize*2)/3,squareSize,squareSize/3);
        panelse.add(priceLabel);
        // Name label
        JLabel nameLabel = new JLabel("",SwingConstants.CENTER);
        nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,70));
        nameLabel.setBounds(0,0,squareSize,squareSize);
        panelse.add(nameLabel);
        // Counter label
        JLabel counterLabel = new JLabel("",SwingConstants.CENTER);
        // Create a horizontal boxlayout to put 2 counters next to each other
        counterLabel.setLayout(new BoxLayout(counterLabel,BoxLayout.X_AXIS));
        counterLabel.setBounds(padding,0,squareSize,squareSize/3);
        panelse.add(counterLabel);
        this.squares.add(panelse);

        // This is the bottom row
        for (int i = propertiesPerSide-1; i >= 0; i--) {
            JPanel newpanel = new JPanel();

            // Set index number, the squares array changes dynamically so it increases
            newpanel.setName(Integer.toString(this.squares.size()));
            newpanel.addMouseListener((MouseListener) this.controller);

            newpanel.setLayout(null);
            // x,y,width,height
            newpanel.setBounds(padding+squareSize+i*propertyWidth,padding+squareSize+propertiesPerSide*propertyWidth,propertyWidth,squareSize);
            newpanel.setBorder(new LineBorder(Color.black,1));
            newpanel.setBackground(Color.white);
            this.squares.add(newpanel);
            // Price label
            priceLabel = new JLabel("",SwingConstants.CENTER);
            priceLabel.setBounds(0,(squareSize*2)/3,propertyWidth,squareSize/3);
            newpanel.add(priceLabel);
            // Name label
            nameLabel = new JLabel("",SwingConstants.CENTER);
            nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,30));
            nameLabel.setBounds(0,0,propertyWidth,propertyWidth/2);
            newpanel.add(nameLabel);
            // Counter label
            counterLabel = new JLabel("",SwingConstants.CENTER);
            // Create a horizontal boxlayout to put 2 counters next to each other
            counterLabel.setLayout(new BoxLayout(counterLabel,BoxLayout.X_AXIS));
            counterLabel.setBounds(padding/2,propertyWidth/2,propertyWidth,propertyWidth/2);
            newpanel.add(counterLabel);
            // Star rating
            JLabel starLabel = new JLabel("",this.starIcon,SwingConstants.CENTER);
            starLabel.setFont(new Font(Font.SERIF,Font.BOLD,15));
            starLabel.setText("0");
            starLabel.setBounds(0,propertyWidth,propertyWidth,propertyWidth/2);
            newpanel.add(starLabel);
            // Set starlabel to invisible and we can make it visible later
            starLabel.setVisible(false);

        }
        JPanel panelsw = new JPanel();
        panelsw.setLayout(null);

        // Set index number, the squares array changes dynamically so it increases
        panelsw.setName(Integer.toString(this.squares.size()));
        panelsw.addMouseListener((MouseListener) this.controller);

        panelsw.setBounds(padding,padding+squareSize+propertiesPerSide*propertyWidth,squareSize,squareSize);
        panelsw.setBorder(new LineBorder(Color.black,1));
        panelsw.setBackground(Color.white);
        this.squares.add(panelsw);
        priceLabel = new JLabel("",SwingConstants.CENTER);
        priceLabel.setBounds(0,(squareSize*2)/3,squareSize,squareSize/3);
        panelsw.add(priceLabel);
        nameLabel = new JLabel("",SwingConstants.CENTER);
        nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,30));
        nameLabel.setBounds(0,0,squareSize,squareSize/2);
        panelsw.add(nameLabel);
        // Counterlabel
        counterLabel = new JLabel("",SwingConstants.CENTER);
        // Create a horizontal boxlayout to put 2 counters next to each other
        counterLabel.setLayout(new BoxLayout(counterLabel,BoxLayout.X_AXIS));
        counterLabel.setBounds(padding,0,squareSize,squareSize/3);
        panelsw.add(counterLabel);

        // This is the left row
        for (int j = propertiesPerSide-1; j >= 0; j--) {
            JPanel newpanel = new JPanel();
            newpanel.setLayout(null);

            // Set index number, the squares array changes dynamically so it increases
            newpanel.setName(Integer.toString(this.squares.size()));
            newpanel.addMouseListener((MouseListener) this.controller);

            // x,y,width,height
            newpanel.setBounds(padding,padding+squareSize+j*propertyWidth,squareSize,propertyWidth);
            newpanel.setBorder(new LineBorder(Color.black,1));
            newpanel.setBackground(Color.white);
            this.squares.add(newpanel);
            priceLabel = new JLabel("",SwingConstants.LEFT);
            priceLabel.setBounds(squareSize/9,propertyWidth/3,squareSize/2,propertyWidth/3);
            newpanel.add(priceLabel);
            nameLabel = new JLabel("",SwingConstants.RIGHT);
            nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,30));
            nameLabel.setBounds(0,propertyWidth/3,squareSize-padding,propertyWidth/3);
            newpanel.add(nameLabel);
            // Counter label
            counterLabel = new JLabel("",SwingConstants.CENTER);
            // Create a horizontal boxlayout to put 2 counters next to each other
            counterLabel.setLayout(new BoxLayout(counterLabel,BoxLayout.X_AXIS));
            counterLabel.setBounds(padding,padding/2,propertyWidth,propertyWidth/3);
            newpanel.add(counterLabel);
            // Star rating
            JLabel starLabel = new JLabel("",this.starIcon,SwingConstants.CENTER);
            starLabel.setFont(new Font(Font.SERIF,Font.BOLD,15));
            starLabel.setText("0");
            starLabel.setBounds(padding,propertyWidth*2/3,propertyWidth,propertyWidth/3);
            newpanel.add(starLabel);
            starLabel.setVisible(false);
        }
        JPanel panelnw = new JPanel();
        panelnw.setLayout(null);

        // Set index number, the squares array changes dynamically so it increases
        panelnw.setName(Integer.toString(this.squares.size()));
        panelnw.addMouseListener((MouseListener) this.controller);

        panelnw.setBounds(padding,padding,squareSize,squareSize);
        panelnw.setBorder(new LineBorder(Color.black,1));
        panelnw.setBackground(Color.white);
        this.squares.add(panelnw);
        priceLabel = new JLabel("",SwingConstants.CENTER);
        priceLabel.setBounds(0,(squareSize*2)/3,squareSize,squareSize/3);
        panelnw.add(priceLabel);
        nameLabel = new JLabel("",SwingConstants.CENTER);
        nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,30));
        nameLabel.setBounds(0,0,squareSize,squareSize/2);
        panelnw.add(nameLabel);
        // Counterlabel
        counterLabel = new JLabel("",SwingConstants.CENTER);
        // Create a horizontal boxlayout to put 2 counters next to each other
        counterLabel.setLayout(new BoxLayout(counterLabel,BoxLayout.X_AXIS));
        counterLabel.setBounds(padding,0,squareSize,squareSize/3);
        panelnw.add(counterLabel);

        // This is the top row
        for (int j = 0; j < propertiesPerSide; j++) {
            JPanel newpanel = new JPanel();
            newpanel.setLayout(null);

            // Set index number, the squares array changes dynamically so it increases
            newpanel.setName(Integer.toString(this.squares.size()));
            newpanel.addMouseListener((MouseListener) this.controller);

            // x,y,width,height
            newpanel.setBounds(padding+squareSize+j*propertyWidth,padding,propertyWidth,squareSize);
            newpanel.setBorder(new LineBorder(Color.black,1));
            newpanel.setBackground(Color.white);
            this.squares.add(newpanel);
            priceLabel = new JLabel("",SwingConstants.CENTER);
            priceLabel.setBounds(0,(squareSize*2)/3,propertyWidth,squareSize/3);
            newpanel.add(priceLabel);
            nameLabel = new JLabel("",SwingConstants.CENTER);
            nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,30));
            nameLabel.setBounds(0,0,propertyWidth,propertyWidth/2);
            newpanel.add(nameLabel);
            // Counter label
            counterLabel = new JLabel("",SwingConstants.CENTER);
            // Create a horizontal boxlayout to put 2 counters next to each other
            counterLabel.setLayout(new BoxLayout(counterLabel,BoxLayout.X_AXIS));
            counterLabel.setBounds(padding/2,propertyWidth/2,propertyWidth,propertyWidth/2);
            newpanel.add(counterLabel);
            // Star rating
            JLabel starLabel = new JLabel("",this.starIcon,SwingConstants.CENTER);
            starLabel.setFont(new Font(Font.SERIF,Font.BOLD,15));
            starLabel.setText("0");
            starLabel.setBounds(0,propertyWidth,propertyWidth,propertyWidth/2);
            newpanel.add(starLabel);
            starLabel.setVisible(false);

        }

        JPanel panelne = new JPanel();
        panelne.setLayout(null);

        // Set index number, the squares array changes dynamically so it increases
        panelne.setName(Integer.toString(this.squares.size()));
        panelne.addMouseListener((MouseListener) this.controller);

        panelne.setBounds(padding+squareSize+propertiesPerSide*propertyWidth,padding,squareSize,squareSize);
        panelne.setBorder(new LineBorder(Color.black,1));
        panelne.setBackground(Color.white);
        this.squares.add(panelne);
        priceLabel = new JLabel("",SwingConstants.CENTER);
        priceLabel.setBounds(0,(squareSize*2)/3,squareSize,squareSize/3);
        panelne.add(priceLabel);
        nameLabel = new JLabel("",SwingConstants.CENTER);
        nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,30));
        nameLabel.setBounds(0,0,squareSize,squareSize/2);
        panelne.add(nameLabel);
        // Counterlabel
        counterLabel = new JLabel("",SwingConstants.CENTER);
        // Create a horizontal boxlayout to put 2 counters next to each other
        counterLabel.setLayout(new BoxLayout(counterLabel,BoxLayout.X_AXIS));
        counterLabel.setBounds(padding,0,squareSize,squareSize/3);
        panelne.add(counterLabel);

        // This is the right row
        for (int j = 0; j < propertiesPerSide; j++) {
            JPanel newpanel = new JPanel();
            newpanel.setLayout(null);

            // Set index number, the squares array changes dynamically so it increases
            newpanel.setName(Integer.toString(this.squares.size()));
            newpanel.addMouseListener((MouseListener) this.controller);

            // x,y,width,height
            newpanel.setBounds(padding+squareSize+propertiesPerSide*propertyWidth,padding+squareSize+j*propertyWidth,squareSize,propertyWidth);
            newpanel.setBorder(new LineBorder(Color.black,1));
            newpanel.setBackground(Color.white);
            this.squares.add(newpanel);
            priceLabel = new JLabel("",SwingConstants.RIGHT);
            // X is 2 thirds
            priceLabel.setBounds(squareSize*2/3,propertyWidth/3,propertyWidth/2,propertyWidth/3);
            newpanel.add(priceLabel);
            nameLabel = new JLabel("",SwingConstants.LEFT);
            nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,30));
            nameLabel.setBounds(padding,propertyWidth/3,squareSize,propertyWidth/3);
            newpanel.add(nameLabel);
            // Counter label
            counterLabel = new JLabel("",SwingConstants.CENTER);
            // Create a horizontal boxlayout to put 2 counters next to each other
            counterLabel.setLayout(new BoxLayout(counterLabel,BoxLayout.X_AXIS));
            counterLabel.setBounds(squareSize/2,padding/2,propertyWidth,propertyWidth/3);
            newpanel.add(counterLabel);
            // Star rating
            JLabel starLabel = new JLabel("",this.starIcon,SwingConstants.CENTER);
            starLabel.setFont(new Font(Font.SERIF,Font.BOLD,15));
            starLabel.setText("0");
            starLabel.setBounds(squareSize/2,propertyWidth*2/3,propertyWidth,propertyWidth/3);
            newpanel.add(starLabel);
            starLabel.setVisible(false);
        }

        // Add squares onto boardPanel
        for (int i = 0; i < this.squares.size(); i++) {
            this.boardPanel.add(this.squares.get(i));
        }

    }


    public void createGUI() {
        // Create frame
        this.frame = new JFrame("Hotels");
        this.frame.setSize(1400,1050);
        this.frame.setVisible(true);
        this.frame.setLayout(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create outer panel
        this.container = new Container();
        this.container = this.frame.getContentPane();

        this.outerPanel = new JPanel();
        this.outerPanel.setSize(new Dimension(1400,1000));
        this.frame.setContentPane(this.outerPanel);
        this.outerPanel.setLayout(null);

        this.boardPanel = new JPanel();
        this.boardPanel.setLayout(null);
        this.boardPanel.setBounds(0,0,1000,1000);
        this.boardPanel.setBackground(Color.lightGray);
        this.outerPanel.add(this.boardPanel);


        this.player1Panel = new JPanel(null);
        this.player1Panel.setBounds(1000,0,400,500);
        this.player1Panel.setBorder(new LineBorder(Color.black,1));
        this.outerPanel.add(this.player1Panel);
        this.player2Panel = new JPanel(null);
        this.player2Panel.setBounds(1000,500,400,500);
        this.player2Panel.setBorder(new LineBorder(Color.black,1));
        this.outerPanel.add(player2Panel);

        this.starIcon = new ImageIcon(createImageIcon("resources/star1.png","Star rating").getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT));

        createButtonsAndLabels();
        createSquares();
        createPlayerInfoPanels();
        updateTurn();
        this.initialised = true;

    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    public ImageIcon createImageIcon(String path, String description) {
        File file = new File("./");
        try {
            System.out.println(file.getCanonicalPath()+"/"+path);
            String pathToIcon = new String(file.getCanonicalPath()+"/"+path);
            return new ImageIcon(pathToIcon, description);

        } catch (IOException e) {
            System.err.println("Couldn't find file: " + path);
        }
        return null;
    }

    private void updateTurn() {
        String playerName = model.getCurrentPlayerName();
        ((JLabel)boardPanel.getComponent(0)).setText(playerName+"'s turn.");
        ImageIcon icon = model.getPlayerImageIcon(playerName);
        ((JLabel)boardPanel.getComponent(0)).setIcon(icon);

    }

    private void updateMessageLabel(String message) {
        ((JLabel)boardPanel.getComponent(1)).setText(message);
    }

    /**
     * Implemented method from Observer interface updates GUI to reflect state of model
     * @param observable : this is the Model
     * @param o : this is a string of what change has happened
     */
    @Override
    public void update(Observable observable, Object o) {
        if (model.isGameOver()) {
            endgameScreen();
        } else {
            if (!initialised){
                this.frame.dispose();
                createGUI();
            }
            // Object o is instruction to player what has happened
            String message = (String) o;
            updateMessageLabel("<html>" + message + "</html>");
            // Update every square getting new information from Model
            for (int i = 0; i < this.squares.size(); i++) {
                updateSquare(i);
            }
            // Update player info panels each time there is a change
            updatePlayerInfoPanel(0);
            updatePlayerInfoPanel(1);
            //
            this.updateTurn();
            this.updateButtons();
        }

    }

    private void endgameScreen() {
        initialised = false;
        outerPanel.removeAll();
        String winnerName = model.getWinnerName();
        Color winnerColor = model.getPlayerColor(winnerName);
        ImageIcon winnerIcon = model.getPlayerImageIcon(winnerName);
        winnerIcon = new ImageIcon(winnerIcon.getImage().getScaledInstance(256,256,Image.SCALE_DEFAULT));
        String winnerMessage = (String) "<html>" + winnerName + " has won the game!!!!! </html>";
        JLabel winLabel = new JLabel(winnerMessage, SwingConstants.CENTER);
        winLabel.setIcon(winnerIcon);
        winLabel.setFont(new Font(Font.SERIF, Font.BOLD, 90));
        winLabel.setBounds(0,0,outerPanel.getWidth(),outerPanel.getHeight());
        // New game button
        JButton newgameButton = new JButton("New game");
        newgameButton.setBounds(this.outerPanel.getWidth()/2,(this.outerPanel.getHeight()/3*2)-newgameButton.getWidth(),this.outerPanel.getWidth()/8,this.outerPanel.getHeight()/8);
        newgameButton.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        newgameButton.setActionCommand("newgame");
        newgameButton.addActionListener(this.controller);
        outerPanel.setBackground(winnerColor);
        outerPanel.add(winLabel);
        outerPanel.add(newgameButton, SwingConstants.CENTER);
    }
}
