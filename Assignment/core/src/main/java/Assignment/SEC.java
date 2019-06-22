package Assignment;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

//main application, set up application. set up when close stop everything
public class SEC extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        //stop every threads when close appliction
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent e) {
            Platform.exit();
            System.exit(0);
        }
        });
        //create controller and gui and connect them together
        Controller control = new Controller();
        GUI gui = new GUI(control,primaryStage);
        gui.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
