package choosemulti;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import Assignment.Question;
import Assignment.QuestionPlugin;

public class ChooseMulti implements QuestionPlugin
{
	@Override
	public Question makeQuestion(String[] texts,int[] numbers)
	{
		Question q = new Question();
		VBox pane = new VBox();
		pane.setSpacing(20);
		q.makeMainView(pane);
		q.setQuestionText(texts[0]);
		CheckBox[] checks = new CheckBox[4];
		checks[0] = new CheckBox(texts[1]);
		checks[1] = new CheckBox(texts[2]);
		checks[2] = new CheckBox(texts[3]);
		checks[3] = new CheckBox(texts[4]);
		GridPane check = new GridPane();
		check.setHgap(50);
		check.setVgap(20);
		check.add(checks[0], 0, 0);
        	check.add(checks[1], 0, 1);
        	check.add(checks[2], 1, 0);
        	check.add(checks[3], 1, 1);
		pane.getChildren().add(check);
		Button next = new Button("Submit");
		EventHandler<ActionEvent> event = (EventHandler) (Event event1) ->{
			boolean correct = true;
			String co = "";
			for (int i:numbers)
			{
				if (!checks[i].isSelected()) correct = false;
				co = co + " " + checks[i].getText();
			}
			if (correct) q.setCorrect();
			q.showResult(co);
		};
		next.addEventHandler(ActionEvent.ACTION,event);
        	q.setNext(next);
        	return q;
	}
	
	@Override
	public Question makeQuestion(String[] texts,int i){return null;}

	@Override
	public Question makeQuestion(String text)
	{
		return null;
	}

	@Override
    	public Question makeQuestion(String[] texts,String text)
	{
		return null;
	}

	@Override
    	public Question makeQuestion(Object[] objects)
	{
		return null;
	}
}
