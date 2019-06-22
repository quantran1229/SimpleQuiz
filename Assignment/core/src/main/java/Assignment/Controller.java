package Assignment;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/*
Controller class recive input from GUI and connect to Model(which is a File object and a quiz Object
*/
public class Controller {
    
    //private class field
    private GUI gui;
    private File file;
    private Quiz quiz;//store Quiz object
    private Thread thread;//store thread for starting Quiz
    
    //contructor
    public Controller()
    {
        quiz = null;
        thread = new Thread();
        thread.setDaemon(true);
    }
    
    //setter
    public void setGUI(GUI ui)
    {
        gui = ui;
    }
    
    //stop current quiz if there is a quiz running
    public void stop()
    {
        //check if thread start Quiz is alive or not
        if (thread.isAlive())
        {
            quiz.stop(); //stop quiz
            gui.resetUI(); //reset ui to empty
        }
    }
    
    //reset quiz, load current quiz and run again
    public void reset()
    {
	stop();        
	try {
            QuizLoader load = new QuizLoader();
            quiz = (Quiz)load.load(file);
            quiz.setController(this);
            Task task = new Task<Void>()
            {
                @Override
                protected Void call() throws Exception {
                	System.out.println("Reset quiz");    
			quiz.runQuiz(new QuestionLoader());
                    return null;
                }
            };
            thread = new Thread(task);
            thread.setName("new-quiz");
            thread.start();
        } catch (Exception ex) {
            popup(ex.getMessage());
	}
    }
    
    //start a new quiz from file. load file and create Quiz object
    public boolean start(File file)
    {
        boolean check = false;
        stop();
        try {
            QuizLoader load = new QuizLoader();
            quiz = (Quiz)load.load(file);
            quiz.setController(this);
            Task task = new Task<Void>()
            {
                @Override
                protected Void call() throws Exception {
                	System.out.println("Start quiz");    
			quiz.runQuiz(new QuestionLoader());
                    return null;
                }
            };
            thread = new Thread(task);
            thread.setName("new-quiz");
            thread.start();
            check = true;
		this.file = file;
        } catch (Exception ex) {
            popup(ex.getMessage());
        } 
        return check;
    }
    
    public void popup(String message)
    {
        gui.popup(message);
    }
    
    //show result from quiz to user with number of answers and number of correct questions
    public void toResult(int number,int count)
    {
        VBox pane = new VBox();
        Label n = new Label("Number of Question asked:"+number);
        Label s = new Label("Number of Correct answers:"+count);
        Label d = new Label("Number of Incorrect answer:"+((int)number-(int)count));
        pane.getChildren().addAll(n,s,d);
        gui.setResultPage(pane);
    }
    
    //getter ui
    public GUI getGUI() {
        return gui;
    }
}
