package Logging;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerHandler {
    private static Logger logger = Logger.getLogger("MyLog");

    public static void writelog(LOG_SECTION section, String message, Level level){
        FileHandler fh = null;
        String path = System.getProperty("user.dir") + "\\src\\LOGS";

        try{
            fh = new FileHandler(String.format(path += "\\%s" + ".txt", section.toString()));
            logger.addHandler(fh);
            logger.log(level, message);

        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Logger Problem");
        }
    }
}
