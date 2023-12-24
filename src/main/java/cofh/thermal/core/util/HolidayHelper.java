package cofh.thermal.core.util;

import java.util.Calendar;

public class HolidayHelper {

    static Calendar curTime = Calendar.getInstance();
    static Calendar holidayStart = Calendar.getInstance();
    static Calendar holidayEnd = Calendar.getInstance();

    private HolidayHelper() {

    }

    public static boolean isChristmas(int pre, int post) {

        setDate(holidayStart, Calendar.DECEMBER, 24 - pre, false);
        setDate(holidayEnd, Calendar.DECEMBER, 26 + post, true);

        return dateCheck();
    }

    // region HELPERS
    private static void setDate(Calendar cal, int month, int date, boolean endOfDay) {

        cal.clear();

        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, date);

        if (endOfDay) {
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
        } else {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }
    }

    private static boolean dateCheck() {

        curTime = Calendar.getInstance();
        return curTime.after(holidayStart) && curTime.before(holidayEnd);
    }
    // endregion
}