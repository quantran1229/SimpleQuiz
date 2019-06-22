package Assignment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

//Quiz abstract class store some convinion method to deal with ui and controller
public abstract class Quiz {
	//private class field
    private Controller controller;
    public ExecutorService executor; //executor will help load 1 or more questions at same time and show to UI

    //contructor
    public Quiz()
    {
        controller = null;
    }

    //setter controller
    public void setController(Controller con)
    {
        controller = con;
    }
    
	//getter get UI from controller so Plugin don't have to see controller.ui
    public GUI getGUI()
    {
        return controller.getGUI();
    }
    
	//main abstract class runQuiz with QuestionLoader
    public abstract void runQuiz(QuestionLoader loader);

	//popup if something wrong happen
    public void popup(String mess)
    {
        controller.popup(mess);
    }
    
	//get all question from Quiz and calculate all right/wrong/ not showing question and give information to controller
    public void toResult(Question[] q) {
       int number = 0;
       int correctCount = 0;
        for (Question i:q)
       {
           if (i.getIsInvoked()) number++;
           if (i.isCorrect()) correctCount++;
       }
        controller.toResult(number,correctCount);
    }

	//stop quiz by force executor stop all thread
    public void stop()
    {
        executor.shutdownNow();
    }
    
	//finish quiz. wait for all question to finish and active show result
    public void quizFinish(Question[] q)
    {
        executor.shutdown();
        try
        {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        }catch (InterruptedException e) {System.out.println("Stop while waiting to finish");}
        toResult(q);
    }
}
