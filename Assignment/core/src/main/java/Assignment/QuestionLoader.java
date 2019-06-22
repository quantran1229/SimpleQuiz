package Assignment;

import java.nio.file.Paths;
import java.io.*;
import java.nio.file.Files;

//load QuestionPlugin type from file .class in every subproject
public class QuestionLoader extends ClassLoader{

    QuestionLoader() {
        
    }
    public QuestionPlugin loader(String name) 
    {
	File file = new File(Paths.get(".").toAbsolutePath().normalize().toString()+"/"+name.toLowerCase()+"plugin/build/classes/main/"+name.toLowerCase()+"/"+name+".class");
	QuestionPlugin ques = null;
	try{
		byte[] classData = Files.readAllBytes(file.toPath());
            	Class<?> cls = defineClass(null, classData, 0, classData.length);
		ques = (QuestionPlugin)cls.newInstance();
	}
	catch (IOException | InstantiationException | IllegalAccessException ex)
	{
		System.out.println("Error with load question "+name+" "+ex.getMessage());
	}
	catch (Exception ex) {
		System.out.println("Error with load question "+ex.getMessage());
	}
	return ques;
    }
}
