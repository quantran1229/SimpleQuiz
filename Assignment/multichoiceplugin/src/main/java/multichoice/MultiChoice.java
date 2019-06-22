package multichoice;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import Assignment.Question;
import Assignment.QuestionPlugin;

/**
 *
 * @author Quan Tran
 */
public class MultiChoice implements QuestionPlugin{
	public MultiChoice(){}
	@Override
	public Question makeQuestion(String[] text,int correct)
    {        
//set up question	
	final Question q = new Question();
        VBox pane = new VBox();
        pane.setSpacing(20);
        q.makeMainView(pane);
        q.setQuestionText(text[0]);
	ToggleGroup group = new ToggleGroup();
        RadioButton[] choice = new RadioButton[4];
        choice[0] = new RadioButton(text[1]);
        choice[1] = new RadioButton(text[2]);
        choice[2] = new RadioButton(text[3]);
        choice[3] = new RadioButton(text[4]);
        choice[0].setToggleGroup(group);
        choice[1].setToggleGroup(group);
        choice[2].setToggleGroup(group);
        choice[3].setToggleGroup(group);
        GridPane choices = new GridPane();
        choices.setHgap(50);
        choices.setVgap(20);
        choices.add(choice[0], 0, 0);
        choices.add(choice[1], 0, 1);
        choices.add(choice[2], 1, 0);
        choices.add(choice[3], 1, 1);
        pane.getChildren().add(choices);
        Button next = new Button("Submit");
	EventHandler<ActionEvent> event = (EventHandler) (Event event1) ->
		{
			if (choice[correct].isSelected())
			{
				q.setCorrect();
			}
			q.showResult(choice[correct].getText());
		};
	next.addEventHandler(ActionEvent.ACTION,event);
        q.setNext(next);
        return q;
    }
	@Override
    	public Question makeQuestion(String[] texts,String text)
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
