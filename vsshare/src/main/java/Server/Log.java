package Server;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {

    private static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Zurich"));
    private Logger logger = Logger.getLogger("log");
    private FileHandler fileHandler;
    private int year = calendar.get(Calendar.YEAR);
    private String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    int day = calendar.get(Calendar.DATE);
    private String fileName = "["+day+"_"+month+"_"+year+"]";
    private File file = new File(fileName);
    private SimpleFormatter formatter = new SimpleFormatter();

    public Log()
    {

    }

    // usefull commands
    public void info(String string) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler(fileName, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fileHandler);

        fileHandler.setFormatter(formatter);
        logger.info(string);
        fileHandler.close();
    }

    //warning : network problems

    //servere: exceptions



}
