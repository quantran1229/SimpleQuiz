package weirdquiz;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import Assignment.Question;
import Assignment.QuestionLoader;
import Assignment.Quiz;
import Assignment.QuestionPlugin;
import java.nio.file.Paths;

/**
 *
 * @author downy
 */
public class WeirdQuiz extends Quiz{

    public WeirdQuiz() {
        super();
        executor = Executors.newFixedThreadPool(2);//using only 2 thread to read first 2 questions.
    }

    @Override
    public void runQuiz(QuestionLoader loader) {
	Question[] q = new Question[8];            
	try {
		QuestionPlugin mc = loader.loader("MultiChoice");
                QuestionPlugin sq = loader.loader("ShortQuestion");
		QuestionPlugin cm = loader.loader("ChooseMulti");
                q[0] = mc.makeQuestion(new String[]{"WHAT?", "Nani?", "Fuck off.", "?", "DOES THE FOX SAY?"}, 3);
                q[1] = sq.makeQuestion(new String[]{"What is 10+10/10"}, "11");
                q[2] = sq.makeQuestion(new String[]{"What is your IQ on this test?"}, "0");
                q[3] = mc.makeQuestion(new String[]{"Are you stupid or not?", "Yes", "True", "1", "$@%$@%$%@%$%"}, 1);
                q[4] = mc.makeQuestion(new String[]{"How many genders there are?", "2 male and female", "3 male,female and helicopter", "Did you just assume my gender?", "There are more than 2"}, 0);
                q[5] = mc.makeQuestion(new String[]{"Who are you?", "I am god", "I am Quiz creator", "I am a walking comedy", "My name is Jeff"}, 1);
                q[6] = sq.makeQuestion(new String[]{"Why do you even do this quiz?"}, "I was bored");
                q[7] = cm.makeQuestion(new String[]{"Choose a lucky number?","777","69","29","1"}, new int[]{2,3});
                //Invoke question
                //Create callable for each question;
                Callable<Boolean> task1 = () ->{
                    return q[0].invoke(super.getGUI(),30);
                };
                Callable<Boolean> task2 = () ->{
                    return q[1].invoke(super.getGUI(),5);
                };
                Callable<Boolean> task3 = () ->{
                    return q[2].invoke(super.getGUI());
                };
                Callable<Boolean> task4 = () ->{
                    return q[3].invoke(super.getGUI());
                };
                Callable<Boolean> task5 = () ->{
                    return q[4].invoke(super.getGUI(),10);
                };
                Callable<Boolean> task6 = () ->{
                    return q[5].invoke(super.getGUI(),10);
                };
                Callable<Boolean> task7 = () ->{
                    return q[6].invoke(super.getGUI());
                };
		Callable<Boolean> task8 = () ->{
                    return q[7].invoke(super.getGUI());
                };
                BlockingQueue queue = new ArrayBlockingQueue(5);
                //put task in queue and ran it in a order you choose
                queue.put(task1);
                queue.put(task2);
                Future<Boolean> q1Result =executor.submit((Callable)queue.poll());
                executor.submit((Callable)queue.poll());
                if (q1Result.get())
                {
                    queue.put(task3);
                } else
                {
                    queue.put(task4);
                }
                queue.put(task5);
                executor.submit(((Callable)queue.poll()));//start question 3 or 4
                Future<Boolean> q5result = executor.submit(((Callable)queue.poll()));//start question 5
                if (q5result.get()) 
                {
                    queue.put(task6);
                    executor.submit(((Callable)queue.poll()));
                }
                queue.put(task7);
		queue.put(task8);
		executor.submit(((Callable)queue.poll()));
                Future<Boolean> lastResult = executor.submit(((Callable)queue.poll()));
                lastResult.get(); 
                quizFinish(q);
            } catch (ClassCastException | InterruptedException | ExecutionException ex) {
                System.out.println("Error with create question: "+ex.getMessage());
            } catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
    }
}
