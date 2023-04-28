import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {

        Model model = new Model(true);
        Controller controller = new Controller(model);
        View view = new View(model,controller);

    }

}