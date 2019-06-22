package shortquestion;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import Assignment.Question;
import Assignment.QuestionPlugin;

/**
 *
 * @author downy
 */
public class ShortQuestion implements QuestionPlugin{
	public ShortQuestion () {}
	@Override    
	public Question makeQuestion(String[] text,String text2)
    	{
        //Create a variable Question
        Question q = new Question();

        //Create a main pane to store question and text field
        Pane pane = new VBox();
        
        //set pane into Question
        q.makeMainView(pane);
        //add Question text to question q
        q.setQuestionText(text[0]);
        //create a text field for answer area and add it to pane
        TextField field = new TextField();
        pane.getChildren().add(field);
        
        //create Next Button
        Button next = new Button("Submit");
	EventHandler<ActionEvent> event = (EventHandler) (Event event1) ->
		{
			if (field.getText().equals(text2)) {q.setCorrect();}
                	q.showResult(text2);
		};
	next.addEventHandler(ActionEvent.ACTION,event);
	q.setNext(next);
        return q;
    }
	@Override	
	public Question makeQuestion(String[] texts,int i)
	{
		throw new IllegalArgumentException("Wrong question format");
	}

    	@Override
    	public Question makeQuestion(String text)
	{
		throw new IllegalArgumentException("Wrong question format");
	}
	@Override
	public Question makeQuestion(String[] texts,int[] i)
	{
		throw new IllegalArgumentException("Wrong question format");
	}
	@Override    
	public Question makeQuestion(Object[] objects)
	{
		throw new IllegalArgumentException("Wrong question format");
	}
}
