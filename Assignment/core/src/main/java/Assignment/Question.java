package Assignment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.concurrent.Task;
import javafx.scene.text.Font;


//Question object store imformation about object and dealling with question
public class Question {
    //private class field
    private Pane pane;
    private boolean correct;
    private Thread timeCountDown;
    private Button next;
    private GUI ui;
    private boolean isInvoke;

    //contructor
    public Question()
    {
        correct = false;
        timeCountDown = new Thread(); // create new threead for time count down
        isInvoke = false; //invoked if program show this question
    }
    
    //recive question pane form questionplugin
    public void makeMainView(Pane pane)
    {
        this.pane = pane;
    }
    
    //getter return if question answer correctly
    public boolean isCorrect()
    {
        return correct;
    }
    
    //set correct to true; we don't need to set correct to false because it is automatic false
    public void setCorrect()
    {
        correct = true;
    }
    
    //this will be use by 2 thread. when a question is finish, notify thread that wait for this question finish
    private synchronized void nextQuestion()
    {
        notify();
        ui.setEmpty(); //set question pane in UI freeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
        isInvoke = true;
    }
    
    //getter for invoked, use for show result
    public boolean getIsInvoked()
    {
        return isInvoke;
    }
    
    //set Question text to Question pane. using this is for styling text
    public void setQuestionText(String text)
    {
        Label question = new Label(text);
        question.setTextFill(Color.PURPLE);
        question.setFont(new Font("Arial",20));
        pane.getChildren().add(question);
    }
    
    //set finishing next button
    public void setNext(Button next)
    {
        this.next = next;
        pane.getChildren().add(this.next);
    }
    
    //show question pane to ui without start time. if question pane in ui occupied, wait until it free again
    public synchronized boolean invoke(GUI ui)
    {
        this.ui = ui;
        try{    
            ui.setMainPage(pane);
            ui.updateTimer();//set timer to infinitive and beyond
                wait();//wait until this question is finish when thread call nextQuestion(meaning question is finish)
            }
        catch(InterruptedException e){} //when thread interrupt, do nothing. just finish jobs
        return correct;
    }
    
	//show question pane to ui. if question pane in ui occupied, wait until it free again. And start timer thread
    public synchronized boolean invoke(GUI ui,int time)
    {
        this.ui = ui;
        try{
            ui.setMainPage(pane);
            Task timer = new Task(){
            @Override
            protected Object call() throws Exception {
                int n = time;
                for (int i = n;i>=0;i--)
                {
                    ui.updateTimer(i);
                    Thread.sleep(1000);
			//let timer thread sleep for 1s
                }
                next.fire(); //if timer finish it job, fire next button to enter next question
                return null;
            }
            };
            timeCountDown = new Thread(timer);
            timeCountDown.setDaemon(true);
            timeCountDown.setName("count-down-thread");
            timeCountDown.start();
            wait(); //wait until this question is finish when thread call nextQuestion(meaning question is finish)
        }
        catch(InterruptedException e){System.out.println(Thread.currentThread().getName()+" out at invoke");timeCountDown.interrupt();}
        catch(IllegalArgumentException e) {System.out.println(Thread.currentThread().getName()+" out at invoke+update");}
        return correct;
    }
	
	//show result to user with text is the correct answer
    public void showResult(String text) {
        if (timeCountDown.isAlive()) timeCountDown.interrupt(); //stop timer if timer still running. can be here or nextQuestion
	//create answer pane        
	VBox paneNew = new VBox();
        Label result = new Label();
        Label answer = new Label("The correct answer is:"+text);
	//check if user enter correct question or not        
	if (this.correct)
        {
            result.setText("CORRECT");
            result.setTextFill(Color.BLUE); //show blue color
        }
        else 
        {
            result.setText("INCORRECT");
            result.setTextFill(Color.RED); //show red color
        }
	//create next button
        Button btn = new Button("Next>");
	EventHandler<ActionEvent> event = (EventHandler) (Event event1) ->
		{
			nextQuestion();
		};
        btn.setOnAction(event);
        paneNew.getChildren().addAll(result,answer,btn);
	//show to ui result pane        
	ui.setResultPage(paneNew);
    }
}

