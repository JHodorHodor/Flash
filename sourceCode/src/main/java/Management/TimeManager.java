package Management;

import Types.LangUserType;
import Types.StatsType;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

public class TimeManager {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static void sort(LinkedList<Calendar> list){
        list.sort((calendar1, calendar2) -> {
            if(calendar1.equals(calendar2)) return 0;
            return after(calendar1,calendar2) ? -1 : 1;
        });
    }

    private static LinkedList<Calendar> cut(LinkedList<Calendar> list, int bound, boolean noBound) {
        LinkedList<Calendar> result = new LinkedList<>();
        Calendar calendar = getCurrentDate();
        calendar.add(Calendar.DAY_OF_MONTH, (-1) * bound + (noBound ? 0 : 1));


        if(list.size() == 1){
            if(!before(list.get(0),calendar))
                result.add(list.get(0));
        } else if(list.size() > 1){
            if(after(list.get(0),list.get(1))){
                for(Calendar c : list){
                    if(before(c,calendar)) break;
                    result.add(c);
                }
            } else {
                for(int i = list.size() - 1; i >= 0; i--){
                    if(before(list.get(i),calendar)) break;
                    result.add(list.get(i));
                }
            }
        }
        return result;
    }


    private static Calendar getCurrentDate(){
        LocalDateTime now = LocalDateTime.now();
        return convertToDate(dtf.format(now));
    }

    public static Calendar convertToDate(String dateSQL){
        if(dateSQL == null)
            return null;
        int year = Integer.valueOf(dateSQL.substring(0,4));
        int month = Integer.valueOf(dateSQL.substring(5,7));
        int day = Integer.valueOf(dateSQL.substring(8,10));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        return calendar;
    }

    static String toString(Calendar calendar){
        if(calendar == null) return null;
        return calendar.get(Calendar.YEAR) + "-" +
                (calendar.get(Calendar.MONTH) > 9 ? calendar.get(Calendar.MONTH) : "0" + calendar.get(Calendar.MONTH)) + "-" +
                (calendar.get(Calendar.DAY_OF_MONTH) > 9 ? calendar.get(Calendar.DAY_OF_MONTH) : "0" + calendar.get(Calendar.DAY_OF_MONTH));
    }


    private static boolean equals(Calendar calendar1, Calendar calendar2){
        if(calendar1 == null || calendar2 == null)
            return false;
        return
                calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    private static boolean after(Calendar calendar1, Calendar calendar2){
        if(calendar1 == null || calendar2 == null)
            return false;
        if (calendar1.get(Calendar.YEAR) > calendar2.get(Calendar.YEAR))
            return true;
        else if(calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR))
            if(calendar1.get(Calendar.MONTH) > calendar2.get(Calendar.MONTH))
                return true;
            else if(calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH))
                return calendar1.get(Calendar.DAY_OF_MONTH) > calendar2.get(Calendar.DAY_OF_MONTH);

        return false;
    }

    private static boolean before(Calendar calendar1, Calendar calendar2){
        return !equals(calendar1,calendar2) && !after(calendar1,calendar2);
    }

    public static String convertTimeToReadable(int sec){
        if(sec < 60)
            return sec + "s";
        String s = sec % 60 > 0 ? sec % 60 + "s" : "";

        int min = sec / 60;
        if(min < 60)
            return min + "m " + s;
        int h = min / 60;
        return h + "h " + (min % 60 > 0 ? min % 60 + "m" : "") + s;
    }

    static int compareLanguages(LangUserType l1, LangUserType l2, String loginDB){
        Pair<Integer, Integer> p1 = TimeManager.computeDates(StatsType.getDates(loginDB, RegexManager.convertIntoPreparedConsistent(l1.getLanguage())));
        Pair<Integer, Integer> p2 = TimeManager.computeDates(StatsType.getDates(loginDB, RegexManager.convertIntoPreparedConsistent(l2.getLanguage())));

        if(p1.getKey() > p2.getKey()) return -1;
        if(p2.getKey() > p1.getKey()) return 1;
        if(p1.getValue() > p2.getValue()) return -1;
        if(p2.getValue() > p1.getValue()) return 1;
        return 0;
    }

    public static boolean ifDone(String loginDB, String languageDB){
        Calendar lastDone = StatsType.getLast(loginDB, languageDB);
        return equals(lastDone, getCurrentDate());
    }

    public static int daysAgo(Calendar calendar){
        int result = 1;
        while(!equals(calendar,getCurrentDate())){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            result++;
        }
        return result;
    }


    private static Pair<Integer,Integer> computeDates(LinkedList<Calendar> dates, int bound){

        sort(dates);

        int i = -1;
        boolean noBound = Integer.MAX_VALUE == bound;
        bound = Integer.min(bound, dates.size());
        Calendar calendar = getCurrentDate();
        Calendar calendar1 = getCurrentDate();
        calendar1.add(Calendar.DAY_OF_MONTH,-1);

        if(dates.size() > 0)
            if(equals(calendar, dates.get(0)) || equals(calendar1,dates.get(0))) {
                for (i = 0; i < bound - 1; i++) {
                    Calendar c = Calendar.getInstance();
                    c.set(dates.get(i).get(Calendar.YEAR), dates.get(i).get(Calendar.MONTH), dates.get(i).get(Calendar.DAY_OF_MONTH));
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    if (!equals(c, dates.get(i + 1)))
                        break;
                }
            }

        return new Pair<>(Integer.min(dates.size(),i + 1), cut(dates, bound, noBound).size());
    }

    public static Pair<Integer,Integer> computeDates(LinkedList<Calendar> dates){
        return computeDates(dates, Integer.MAX_VALUE);
    }

    public static Integer getFrequency(String loginDB, String languageDB, int bound){
        Double d = (double) cut(StatsType.getDates(loginDB,languageDB),bound, Integer.MAX_VALUE == bound).size() / bound;
        d *= 100;
        return d.intValue();
    }


    public static Pair<Integer, Integer> computeCorrectness(String loginDB, String languageDB, int bound){
        LinkedList<Calendar> dates = StatsType.getDates(loginDB, languageDB);
        HashMap<String, StatsType> map = StatsType.toHashMap(loginDB, languageDB);

        sort(dates);
        LinkedList<Calendar> newDates = cut(dates, bound, Integer.MAX_VALUE == bound);

        Integer good = 0;
        Integer aall = 0;

        for(Calendar c : newDates){
            StatsType statsType = map.get(toString(c));
            good += statsType.getGood();
            aall += statsType.getAall();
        }

        return new Pair<>(good, aall);
    }

    public static Integer getCorrectness(String loginDB, String languageDB, int bound){
        Pair<Integer, Integer> pair = computeCorrectness(loginDB,languageDB,bound);
        Double d = (double) pair.getKey() / pair.getValue();
        d *= 100;
        return d.intValue();
    }

    public static Integer getCorrectnessDaily(String loginDB, String languageDB, int bound){
        return computeCorrectness(loginDB, languageDB, bound).getValue() / bound;
    }


    public static Integer getTtime(String loginDB, String languageDB, int bound){
        LinkedList<Calendar> dates = StatsType.getDates(loginDB, languageDB);
        HashMap<String, StatsType> map = StatsType.toHashMap(loginDB, languageDB);

        sort(dates);
        LinkedList<Calendar> newDates = cut(dates, bound, Integer.MAX_VALUE == bound);

        Integer ttime = 0;

        for(Calendar c : newDates)
            ttime += map.get(toString(c)).getTtime();

        return ttime;
    }

    public static Integer getTtimeDaily(String loginDB, String languageDB, int bound){
        return getTtime(loginDB, languageDB, bound) / bound;
    }

}
