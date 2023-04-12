
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;
public class Main implements ActionListener {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {

        System.out.println("Hello world!");
        //GUI gui = new GUI();
        Model model = new Model();
        Controller controller = new Controller(model);
        View view = new View(model,controller);

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}