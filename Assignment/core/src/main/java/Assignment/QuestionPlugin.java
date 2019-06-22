package Assignment;

//interface for QuestionPlugin with some default make question.Plugin maker must follow this
public interface QuestionPlugin {
    //take a array of string and an int
    public Question makeQuestion(String[] texts,int i);
	//take array of string and a string for result or whatever
    public Question makeQuestion(String[] texts,String text);
	//take array of a single string
    public Question makeQuestion(String text);
	//take 2 array string and number
    public Question makeQuestion(String[] texts,int[] i);
	//take array of objecs
    public Question makeQuestion(Object[] objects);
}
