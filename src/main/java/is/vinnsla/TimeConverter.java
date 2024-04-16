package is.vinnsla;

import javafx.util.Duration;

public class TimeConverter {
    public static String convertTime(Duration time, Boolean precise)
    {
        // Assume the largest time unit is the hour
        // This isn't pretty but it works
        int hours = (int)time.toMillis() / 3600000;
        int minutes = (int)(time.toMillis() % 3600000) / 60000;
        int seconds = (int)((time.toMillis() % 3600000) % 60000) / 1000;
        int milliseconds = (int)time.toMillis() % 1000;

        // Clean up some clutter
        if (hours > 0) {
            if (precise == true) {
                String formattedDuration = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
                return formattedDuration;
            }

            else {
                String formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                return formattedDuration;
            }
        }

        else {
            if (precise == true) {
                String formattedDuration = String.format("%02d:%02d.%03d", minutes, seconds, milliseconds);
                return formattedDuration;
            }

            else {
                String formattedDuration = String.format("%02d:%02d", minutes, seconds);
                return formattedDuration;
            }
        }
    }

    // World's worst polymorphism
    public static String convertTime(int time, Boolean precise)
    {
        return convertTime(Duration.millis(time), precise);
    }
}
