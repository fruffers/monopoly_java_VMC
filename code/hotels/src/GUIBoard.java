//import java.util.ArrayList;
//
//public class GUIBoard {
//    private ArrayList<Square> squares;
//    int xVal;
//    int yVal;
//    JPanel boardPanel;
//    int[] sideSquareBound = {120,60};
//    int[] topSquareBound = {60,120};
//    int[] bigSquareBound = {120,120};
//    int squareSize = 150;
//    int propertiesPerSide = 9;
//    int padding = 10;
//    int[] prices;
//    String[] names;
//    Hotel[] hotels;
//
//
//    public Board(JPanel boardPanel,int xVal,int yVal,int width,int height) {
//        this.boardPanel = boardPanel;
//        this.squares = new ArrayList<Square>();
//        this.boardPanel.setBorder(new LineBorder(Color.black,1));
//        this.boardPanel.setBackground(new Color(255, 255, 199));
//        this.boardPanel.setBounds(xVal,yVal,width,height);
//        this.names = new String[]{"GO","A1", "", "A2", "A3", "", "B1", "", "B2", "B3", "", "C1", "", "C2", "C3", "", "D1", "", "D2", "D3", "", "E1", "", "E2", "E3", "", "F1", "", "F2", "F3", "", "G1", "", "G2", "G3", "", "H1", "", "H2", "H3"};
//        this.prices = new int[]{0, 50, 0, 50, 70, 0, 100, 0, 100, 120, 0, 150, 0, 150, 170, 0, 200, 0, 200, 220, 0, 250, 0, 250, 270, 0, 300, 0, 300, 320, 0, 350, 0, 350, 370, 0, 400, 0, 400, 420};
//        ImageIcon icon1 = createImageIcon("../resources/hotel1.png","hotel1");
//        this.hotels = new Hotel[] {null,new Hotel("A1",true,50,0,5,icon1),null,new Hotel("A2",true,50,0,1,icon1),
//        new Hotel("A3",true,70,0,0,icon1)};
//
//        this.createSquares();
//
//    }
//
//    public void createSquares() {
//        // Property counter
//        int p = 0;
//        int propertyWidth = squareSize / 2;
//        // GO square
//        Square se = new Square(this.boardPanel,names[p],prices[p++], new int[]{squareSize, squareSize},padding+squareSize+propertiesPerSide*propertyWidth,padding+squareSize+propertiesPerSide*propertyWidth,Color.WHITE);
//        this.squares.add(se);
//        // This is the bottom row
//        for (int i = propertiesPerSide-1; i >= 0; i--) {
//            Square prop = new Square(this.boardPanel,names[p],prices[p++],new int[]{propertyWidth,squareSize},padding+squareSize+i*propertyWidth,padding+squareSize+propertiesPerSide*propertyWidth,Color.WHITE);
//            this.squares.add(prop);
//        }
//        Square sw = new Square(this.boardPanel,names[p],prices[p++], new int[]{squareSize, squareSize},padding+0,padding+squareSize+propertiesPerSide*propertyWidth,Color.WHITE);
//        this.squares.add(sw);
//        // This is the left row
//        for (int j = propertiesPerSide-1; j >= 0; j--) {
//            Square prop = new Square(this.boardPanel,names[p],prices[p++],new int[]{squareSize,propertyWidth},padding+0,padding+squareSize+j*propertyWidth,Color.WHITE);
//            this.squares.add(prop);
//        }
//        Square nw = new Square(this.boardPanel,names[p],prices[p++], new int[]{squareSize, squareSize},padding+0,padding+0,Color.WHITE);
//        this.squares.add(nw);
//        // This is the top row
//        for (int j = 0; j < propertiesPerSide; j++) {
//            Square prop = new Square(this.boardPanel,names[p],prices[p++],new int[]{propertyWidth,squareSize},padding+squareSize+j*propertyWidth,padding+0,Color.WHITE);
//            this.squares.add(prop);
//        }
//        Square ne = new Square(this.boardPanel,names[p],prices[p++], new int[]{squareSize, squareSize},padding+squareSize+propertiesPerSide*propertyWidth,padding+0,Color.WHITE);
//        // This is the right row
//        for (int j = 0; j < propertiesPerSide; j++) {
//            Square prop = new Square(this.boardPanel,names[p],prices[p++],new int[]{squareSize,propertyWidth},padding+squareSize+propertiesPerSide*propertyWidth,padding+squareSize+j*propertyWidth,Color.WHITE);
//            this.squares.add(prop);
//        }
//
//
//    }
//
//    // ************* Duplicated in GUI
//    public ImageIcon createImageIcon(String path, String description) {
//        java.net.URL imgURL = getClass().getResource(path);
//        if (imgURL != null) {
//            return new ImageIcon(imgURL, description);
//        } else {
//            System.err.println("Couldn't find file: " + path);
//            return null;
//        }
//    }
//
//    public ArrayList<Square> getSquares() {
//        return this.squares;
//    }
//
//    public int squareIndex(Square name) {
//        int found = this.squares.indexOf(name);
//        return found;
//    }
//    public void addSquare(Square square) {
//        this.squares.add(square);
//    }
//
//
//}
