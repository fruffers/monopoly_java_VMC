import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {

        // Defines the state (define initial state immediately) and changes to state are changes to model
        // Maintains list of observers that are prompted to update themselves if a change is made to the model,
        // the View is one of these Observers, each view is an observer
        Model model = new Model(true);
        // Controller handles requests from View by sending commands to Model
        // Controller uses the model
        Controller controller = new Controller(model);
        // View gets data from model and sends requests to the controller
        // View uses model and controller
        View view = new View(model,controller);

    }

}