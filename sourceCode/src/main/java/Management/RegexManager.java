package Management;

import Types.WordType;
import javafx.scene.image.Image;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexManager {

    public static boolean isNotSafe(String s) {
        char[] array = s.toCharArray();
        for (char c : array) {
            if (!Character.isAlphabetic(c)
                    && !Character.isWhitespace(c)
                    && !Character.isDigit(c)
                    && c != '.'
                    && c != ',')
                return true;
        }

        return false;
    }

    public static void convertArrayIntoPreparedConsistent(ArrayList<Object> arrayList){
        for(int i = 0; i < arrayList.size(); i++){
            if(arrayList.get(i) instanceof String)
                arrayList.set(i, convertIntoPreparedConsistent((String) arrayList.get(i)));
        }
    }

    public static String convertIntoPreparedConsistent(String string){
        if("".equals(string) || string == null)
            return null;
        if(string.charAt(0) == '\'') {
            return string;
        } else
            return '\'' + string + '\'';
    }

    public static Pair<LinkedList<String>, LinkedList<String>> getSymbols(String symbols, int quantity){
        LinkedList<String> small = new LinkedList<>();
        LinkedList<String> great = new LinkedList<>();
        for(int i = 0; i < quantity; i++){
            small.add(String.valueOf(symbols.charAt(4 * i)));
            great.add(String.valueOf(symbols.charAt(4 * i + 2)));
        }
        return new Pair<>(small, great);
    }

    public static String convertToDisplay(WordType wordType){
        return wordType.getOriginal() + " --- " + wordType.getPolish();
    }

    private static String getRidOfWhite(String s){
        Pattern pat = Pattern.compile("^[ \n]+");
        Matcher mat = pat.matcher(s);
        s = mat.replaceAll("");
        Pattern pat2 = Pattern.compile("[ \n]+$");
        Matcher mat2 = pat2.matcher(s);
        return mat2.replaceAll("");
    }

    public static Pair<LinkedList<String>, LinkedList<String>> separateWords(String input){
        LinkedList<String> original = new LinkedList<>();
        LinkedList<String> polish = new LinkedList<>();

        while(input.length() > 0){
            int end = input.indexOf('|');
            String o = input.substring(0,end);
            original.add(getRidOfWhite(o));
            input = input.substring(end + 1);

            end = input.indexOf('\n') < 0 ? input.length() : input.indexOf('\n');
            String p = input.substring(0, end);
            polish.add(getRidOfWhite(p));
            input = input.substring(Integer.min(end + 1,input.length()));
        }

        return new Pair<>(original,polish);
    }

    static Image setImage(String name, Object o){
        return new Image(o.getClass().getResourceAsStream(Launcher.imagesLocation + name + ".png"));
    }

    public static boolean isGood(String history){
        String check = history.substring(history.length() - 8);
        char[] arr = check.toCharArray();
        for(char c : arr)
            if(c != '1' && c != '3')
                return false;
        return true;
    }
}