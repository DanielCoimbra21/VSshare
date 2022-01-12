package Server;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter
{
    /**
     * Méthode de la classe LogFormatter qui sert à modifier la sortie des log events
     * @param LogRecord record à modifier
     */

    @Override
    public String format(LogRecord record)
    {
        StringBuilder builder = new StringBuilder();

        // Date entre crochets
        builder.append("[");
        builder.append(calcDate(record.getMillis()));
        builder.append("]");

        // Source de l'information entre crochets
        builder.append(" [");
        builder.append(record.getSourceClassName());
        builder.append("]");

        // Importance du log entre crochets
        builder.append(" [");
        builder.append(record.getLevel().getName());
        builder.append("]");

        // Message du log
        builder.append(" - ");
        builder.append(record.getMessage());

        Object[] params = record.getParameters();

        if (params != null)
        {
            builder.append("\t");
            for (int i = 0; i < params.length; i++)
            {
                builder.append(params[i]);
                if (i < params.length - 1)
                    builder.append(", ");
            }
        }

        builder.append("\n");
        return builder.toString();
    }

    /**
     * Méthode de la classe LogFormatter qui sert à modifier le format de la date
     * @param long milisecs de la date actuelle
     */

    private String calcDate(long millisecs) {
        SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
    }
}
