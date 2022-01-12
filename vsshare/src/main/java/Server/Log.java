package Server;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.*;

public class Log {

    private static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Zurich"));
    private Logger logger = Logger.getLogger("log");
    private FileHandler fileHandler;
    private int year = calendar.get(Calendar.YEAR);
    private String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    int day = calendar.get(Calendar.DATE);
    private String fileName = "["+day+""+month+""+year+"]";
    private File file = new File(fileName);
    //private SimpleFormatter formatter = new SimpleFormatter();
    private Formatter formatter = new LogFormatter();
    public static final String ANSI_YELLOW = "\u001B[33m";

    public Log()
    {

    }

    public Logger getMyLogger()
    {
        return logger;
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

        logger.setLevel(Level.INFO);
    }

    //warning : network problems
    public void warning(String message){
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
        logger.warning(message);
        fileHandler.close();

        logger.setLevel(Level.WARNING);
    }


    //servere: exceptions
    public void severe(String message){
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
        logger.severe(message);
        fileHandler.close();

        logger.setLevel(Level.SEVERE);
    }


}