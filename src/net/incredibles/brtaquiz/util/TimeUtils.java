package net.incredibles.brtaquiz.util;

/**
 * @author sharafat
 * @Created 2/23/12 5:16 PM
 */
public class TimeUtils {

    public static long getTestDuration(long timePerQuestionInMillis, int noOfQuestions) {
        return timePerQuestionInMillis * noOfQuestions;
    }

    public static String getFormattedTime(long timeInMillis) {
        int hours   = (int) ((timeInMillis / (1000*60*60)) % 24);
        int minutes = (int) ((timeInMillis / (1000*60)) % 60);
        int seconds = (int) (timeInMillis / 1000) % 60 ;

        String paddedHours = (hours < 10 ? "0" : "") + hours;
        String paddedMinutes = (minutes < 10 ? "0" : "") + minutes;
        String paddedSeconds = (seconds < 10 ? "0" : "") + seconds;

        return paddedHours + ":" + paddedMinutes + ":" + paddedSeconds;
    }
}
