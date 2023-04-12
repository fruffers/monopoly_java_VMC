import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
public class View implements Observer {
    int emptySquares = 15;
    JFrame frame;
    JPanel outerPanel;
    JPanel boardPanel;
    JPanel player1Panel;
    JPanel player2Panel;
    Container container;
    int squareSize = 150;
    int propertiesPerSide = 9;
    //Board board;
    ArrayList<JPanel> squares;
    // TODO: Remove reference to board and players objects
    ArrayList<Player> players;
    int padding = 10;
    Model model;
    Controller controller;
    public View(Model model, Controller controller) throws InterruptedException, InvocationTargetException {
        this.model = model;
        this.controller = controller;
        this.squares = new ArrayList<JPanel>();
        // Use threads
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                createGUI();
                for (int i = 0; i < 40; i++) {
                    updateSquare(i);
                }
            }

             });

    }

    private void updateSquare(int squareIndex) {
        JPanel square = this.squares.get(squareIndex);
        int price = model.getHotelPrice(squareIndex);
        if (price > 0) {
            ((JLabel)square.getComponent(0)).setText("£"+Integer.toString(model.getHotelPrice(squareIndex)));
        }
        ((JLabel)square.getComponent(1)).setText(model.getSquareName(squareIndex));

    }

    private void createPlayerInfoPanels() {
        // This sets up the player info panels initially, but we will have to update
        // the panels when information updates in the model, so we'll use an Observer/Observable for that
        int rowHeight = 30;
        this.player1Panel.setBackground(Color.yellow);
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

        ImageIcon icon1 = createImageIcon("resources/car4.png","player1");
        JLabel iconLabel = new JLabel(icon1);
        iconLabel.setBounds(300-padding,padding,rowHeight*2,rowHeight*2);
        player1Panel.add(iconLabel);
    }

    private void createSquares() {
        // Property counter
        int p = 0;
        int propertyWidth = squareSize / 2;
        // GO square
//        Square se = new Square(this.boardPanel,names[p],prices[p++], new int[]{squareSize, squareSize},padding+squareSize+propertiesPerSide*propertyWidth,padding+squareSize+propertiesPerSide*propertyWidth,Color.WHITE);
        JPanel panelse = new JPanel();
        panelse.setLayout(null);
        // propertywidth is half of squareSize (it's the smaller squares)
        panelse.setBounds(padding+squareSize+propertiesPerSide*propertyWidth,padding+squareSize+propertiesPerSide*propertyWidth,squareSize,squareSize);
        panelse.setBackground(Color.white);
        panelse.setBorder(new LineBorder(Color.black,1));
        JLabel priceLabel = new JLabel("",SwingConstants.CENTER);
        priceLabel.setBounds(0,(squareSize*2)/3,squareSize,squareSize/3);
        panelse.add(priceLabel);
        JLabel nameLabel = new JLabel("",SwingConstants.CENTER);
        nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,70));
        nameLabel.setBounds(0,0,squareSize,squareSize);
        panelse.add(nameLabel);

        this.squares.add(panelse);
        // This is the bottom row
        for (int i = propertiesPerSide-1; i >= 0; i--) {
//            Square prop = new Square(this.boardPanel,names[p],prices[p++],new int[]{propertyWidth,squareSize},padding+squareSize+i*propertyWidth,padding+squareSize+propertiesPerSide*propertyWidth,Color.WHITE);
            JPanel newpanel = new JPanel();
            newpanel.setLayout(null);
            // x,y,width,height
            newpanel.setBounds(padding+squareSize+i*propertyWidth,padding+squareSize+propertiesPerSide*propertyWidth,propertyWidth,squareSize);
            newpanel.setBorder(new LineBorder(Color.black,1));
            newpanel.setBackground(Color.white);
            this.squares.add(newpanel);
            priceLabel = new JLabel("",SwingConstants.CENTER);
            priceLabel.setBounds(0,(squareSize*2)/3,propertyWidth,squareSize/3);
//            priceLabel.setBounds(0,(propertyWidth*2)/3,propertyWidth,propertyWidth/3);
            newpanel.add(priceLabel);
            nameLabel = new JLabel("",SwingConstants.CENTER);
            nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,30));
            nameLabel.setBounds(0,0,propertyWidth,propertyWidth/2);
            newpanel.add(nameLabel);
        }
        JPanel panelsw = new JPanel();//this.boardPanel,names[p],prices[p++], new int[]{squareSize, squareSize},padding+0,padding+squareSize+propertiesPerSide*propertyWidth,Color.WHITE);
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
        // This is the left row
        for (int j = propertiesPerSide-1; j >= 0; j--) {
            JPanel newpanel = new JPanel();
            newpanel.setLayout(null);
            // x,y,width,height
            newpanel.setBounds(padding,padding+squareSize+j*propertyWidth,squareSize,propertyWidth);
            newpanel.setBorder(new LineBorder(Color.black,1));
            newpanel.setBackground(Color.white);
            this.squares.add(newpanel);
            //this.nameLabel.setBounds(0,squareBound[1]/3,squareBound[0]/2,squareBound[1]/2);
//            this.priceLabel.setBounds(squareBound[0]/2,squareBound[1]/2,squareBound[0]/2,squareBound[1]/3);
            priceLabel = new JLabel("",SwingConstants.LEFT);
            priceLabel.setBounds(squareSize/9,propertyWidth/3,squareSize/2,propertyWidth/3);
            newpanel.add(priceLabel);
            nameLabel = new JLabel("",SwingConstants.RIGHT);
            nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,30));
            nameLabel.setBounds(0,propertyWidth/3,squareSize-padding,propertyWidth/3);
            newpanel.add(nameLabel);
        }
        JPanel panelnw = new JPanel();//new Square(this.boardPanel,names[p],prices[p++], new int[]{squareSize, squareSize},padding+0,padding+0,Color.WHITE);
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
        // This is the top row
        for (int j = 0; j < propertiesPerSide; j++) {
//            Square prop = new Square(this.boardPanel,names[p],prices[p++],new int[]{propertyWidth,squareSize},padding+squareSize+j*propertyWidth,padding+0,Color.WHITE);
//            this.squares.add(prop);
            JPanel newpanel = new JPanel();
            newpanel.setLayout(null);
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
        }
        //Square ne = new Square(this.boardPanel,names[p],prices[p++], new int[]{squareSize, squareSize},padding+squareSize+propertiesPerSide*propertyWidth,padding+0,Color.WHITE);
        JPanel panelne = new JPanel();
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
        // This is the right row
        for (int j = 0; j < propertiesPerSide; j++) {
//            Square prop = new Square(this.boardPanel,names[p],prices[p++],new int[]{squareSize,propertyWidth},padding+squareSize+propertiesPerSide*propertyWidth,padding+squareSize+j*propertyWidth,Color.WHITE);
//            this.squares.add(prop);
            JPanel newpanel = new JPanel();
            newpanel.setLayout(null);
            // x,y,width,height
            newpanel.setBounds(padding+squareSize+propertiesPerSide*propertyWidth,padding+squareSize+j*propertyWidth,squareSize,propertyWidth);
            newpanel.setBorder(new LineBorder(Color.black,1));
            newpanel.setBackground(Color.white);
            this.squares.add(newpanel);
            priceLabel = new JLabel("",SwingConstants.RIGHT);
            priceLabel.setBounds(squareSize*2/3,propertyWidth/3,propertyWidth/2,propertyWidth/3);
            newpanel.add(priceLabel);
            nameLabel = new JLabel("",SwingConstants.LEFT);
            nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,30));
            nameLabel.setBounds(padding,propertyWidth/3,squareSize,propertyWidth/3);
            newpanel.add(nameLabel);
        }

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

//        this.board = new GUIBoard(this.boardPanel,0,0,1000,1000);

//        this.playerInfoPanel = new JPanel(null);
//        this.playerInfoPanel.setBounds(1000,0,400,1000);
//        //this.playerInfoPanel.setBackground(new Color(199, 199, 97));
//        this.playerInfoPanel.setBorder(new LineBorder(Color.black,1));
//        this.outerPanel.add(this.playerInfoPanel);

        //createPlayerInfoPanel(this.playerInfoPanel);

        this.player1Panel = new JPanel(null);
        this.player1Panel.setBounds(1000,0,400,500);
        this.player1Panel.setBorder(new LineBorder(Color.black,1));
        this.outerPanel.add(this.player1Panel);
        this.player2Panel = new JPanel(null);
        this.player2Panel.setBounds(1000,500,400,500);
        this.player2Panel.setBorder(new LineBorder(Color.black,1));
        this.outerPanel.add(player2Panel);

//        this.players = new ArrayList<Player>();
//        ImageIcon icon1 = createImageIcon("../resources/car1.png","player1");
//        Player player1 = new Player("player1",icon1,this.board.getSquares().get(0));
//        this.players.add(player1);
//        ImageIcon icon2 = createImageIcon("../resources/car2.png","player2");
//        Player player2 = new Player("player2",icon2,this.board.getSquares().get(0));
//        this.players.add(player2);

        createSquares();
        createPlayerInfoPanels();


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

    private void updateBoardPanel() {

    }


    @Override
    public void update(Observable observable, Object o) {
        for (int i = 0; i < this.squares.size(); i++) {
            updateSquare(i);
        }
    }
}
