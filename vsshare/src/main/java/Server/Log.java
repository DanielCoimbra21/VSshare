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
    private int year = calendar.get(Calendar.YEAR);
    private String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    int day = calendar.get(Calendar.DATE);
    private String fileName = "["+day+""+month+""+year+"]";
    private File file = new File(fileName);
    private Formatter formatter = new LogFormatter();


    /**
     * Constructeur de la classe Log qui sert à afficher et classer tous les logs
     */

    public Log() { }


    /**
     * Méthode de la classe Log qui sert à noter tous les events du type info dans le cahier des logs
     * @param String message de l'event info
     */

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

    /**
     * Méthode de la classe Log qui sert à noter tous les events du type warning dans le cahier des logs
     * @param String message de l'event warning
     */

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

    /**
     * Méthode de la classe Log qui sert à noter tous les events du type severe dans le cahier des logs
     * @param String message de l'event severe
     */

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