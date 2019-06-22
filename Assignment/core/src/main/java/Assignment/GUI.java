package Assignment;
 
import java.io.File;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
 /*
*GUI is for create main view window. It will interact with Controller and Question(Model)
*/
public class GUI {
    //private class field
    private Controller controller; //main controller for user input and stuff
    private Stage stage; //main window for application
    private BorderPane mainContainer; //store main window where question is show
    private Label timer; //Timer label. mostly just to show time
    private boolean empty; //check if question pane where user can input is empty or not. if yes then put question into this pane. Else put to review side

    //controller take primaryStage from Application and controller
    public GUI(Controller inControl,Stage primaryStage)
    {
        controller = inControl;
        stage = primaryStage;
        controller.setGUI(this);
        empty = true;
    }
     
    //free center pane, and notify thread waiting for center pane
    public synchronized void setEmpty()
    {
        empty = true;
        notify();
    }
    
    //get empty
    public boolean getEmpty()
    {
        return empty;
    }
    
    //Show result page to user
    public void setResultPage(Pane pane)
    {
        Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    mainContainer.setCenter(pane);
                }
                });
    }
    
    //Reset UI to initial start
    public void resetUI()
    {
        Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    mainContainer.setCenter(null);
                    mainContainer.setRight(null);
                    timer.setText("Timer");
                }
                });
    }
    
    /*
    *Name:setMainPane
    *Import:pane(question pane create by Question object)
    *This is where question pane setting. Usually there will be 2 threads(or 1) using this function. 1 will be set into center while other
    *will set in right as preview pane.
    *First, check if main pane is available or not. If yes, thread will go in and add question pane to center pane
    *If not, thread will put question pane to right pane(review pane which is disable input) and wait until center is empty again. Then put preview pane to center.
    *However if thread is interrupted,set Center pane empty(because thread interrupted only when user reset Quiz or stop Quiz. Also throw another
    *exception for Question to catch. This will stop Question doing any thing after thread is interrupted.
    */
    public synchronized void setMainPage(Pane pane)
    {
        //check if center is empty or not
        if (empty)
        {
            //set center is full
            empty = false;
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    mainContainer.setCenter(pane);
                }
                });
        }
        else
        {
            //set up preview pane
            try {
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        mainContainer.setRight(pane);
                        mainContainer.getRight().setDisable(true); //disable preview pane so there will receive no user input
                    }
                });
                //wait until center is avaiable again
                wait();
                empty = false;
                //put preview pane to center pane and clear preview pane
                Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    mainContainer.setRight(null);
                    mainContainer.setCenter(pane);
                    mainContainer.getCenter().setDisable(false);
                }
                });
            } catch (InterruptedException ex) {
                //check when thread is interrupted, clear preview and center, notify any wating thread and throw exception for Question to catch
                setEmpty();
                System.out.println(Thread.currentThread().getName()+" interrupt set main page");
                throw new IllegalArgumentException("out of current thread");
            }
        }
    }
     
     
    /*
    *Name: popup
    *Import:a string message
    *Purpose: create a pop up window when something wrong happen
    */
    public void popup(String message)
    {
        Platform.runLater(new Runnable(){
        @Override
        public void run() {
            //setting up opup window
            Stage popup = new Stage();
            popup.initOwner(stage);
            popup.setTitle("ERROR");
            popup.initModality(Modality.NONE);
            popup.initStyle(StageStyle.DECORATED);
            Text mess = new Text(message);
            VBox container = new VBox();
            container.setAlignment(Pos.CENTER);
            container.setSpacing(30);
         
            //create OK button. click to exit popup
            Button btn = new Button("OK");
            btn.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                   popup.close();
                }
            });
            //finalize popup
            container.getChildren().addAll(mess,btn);
            Scene root = new Scene(container,400,100);
            popup.setScene(root);
            popup.show();
            }
        });
    }
     
    /*
    *Name:start
    *Import: none
    *Purpose: start create main feature for program.
    *mainContainer: Container for whole program. This will be a border pane so it is easier to moving around.
    *For top of container will contain button for start new quiz or refresh quiz
    *Right of container will be preview pane. However when there is nothing to preview, nothing will be in this side.
    *Therefor only center will be show
    *Center of container will contain Question and input
    *Bottom of container will contain Timer count down
    */
    public void start()
    {
        //create container
        mainContainer = new BorderPane();
         
        //addting button to top and timer at bottom
        //add Start button to choose a class file and start new quiz
        Button btn = new Button("Open new Quiz");
        
        //adding stop button to stop current quiz, return a blank space;
        Button btn2 = new Button("Stop");
        btn2.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                controller.stop();
            }
        });
        
        //adding reset button to reset current quiz
        Button btn3 = new Button("Reset");
        btn3.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
            controller.reset();
            }
        });
        
        //create function for btn. When choose a correct file, enable btn2 and btn3
        btn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent eFvent) {
            //create a new task for controller to seperate it from GUI   
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose your quiz plugin");
                final File file = fileChooser.showOpenDialog(new Stage());
                if (file != null)
                {
                    if (controller.start(file))
                    {
                        btn2.setDisable(false);
                        btn3.setDisable(false);
                    }
                }
            }
        });
        //create menu pane for button. set Stop and reset button disable becasue there are nothing in database
        HBox pane2 = new HBox();
        pane2.setSpacing(30);
        pane2.getChildren().addAll(btn,btn2,btn3);
        btn2.setDisable(true);
        btn3.setDisable(true);
        //set up main page
        timer = new Label("TIMER");//Timer start with Timer
        mainContainer.setTop(pane2);
        mainContainer.setBottom(timer);
         
        //seting up stage and show it to user
        Scene root = new Scene(mainContainer,800,300);
        stage.setScene(root);
        stage.show();
    }
     
    /*
    *Name:updateTimer
    *Import: none
    *Purpose: set timer = infinitive
    */
    void updateTimer()
    {
        //this was used by another thread
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                timer.setText("infinitive");
            }
        });
    }
     
    /*
    *Name:updateTimer
    *Import: integer i
    *Purpose: update timer count down to value i and update it on GUI
    */
    void updateTimer(int i) {
        //this was used by another thread
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                timer.setText(i+"");
            }
        });
    }
}
