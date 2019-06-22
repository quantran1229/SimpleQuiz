package Assignment;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

//Class QuizLoader load dynamic Quiz class
public class QuizLoader extends ClassLoader{
    public Quiz load(File file)
    {
	try        
	{
            byte[] classData = Files.readAllBytes(file.toPath());
            Class<?> cls = defineClass(null, classData, 0, classData.length);
            return (Quiz)cls.newInstance();
        } catch (IOException | InstantiationException | IllegalAccessException ex) {
            throw new IllegalArgumentException("Error with read Quiz:"+ex.getMessage());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Error with read Quiz:"+ex.getMessage());
        }
    }
}
