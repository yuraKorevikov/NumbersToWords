import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yura on 30.10.2016.
 */
public class NumbersToWords {
    private String number;
    private String words;
    private boolean checker = true;

    private Map<Integer, String> degreeThousands;
    private ArrayList<String> tokens;
    private ArrayList<String> plurmtokens;
    private ArrayList<String> decades;
    private ArrayList<String> hundreds;

    private final String SEPARATOR = " ";

    private void initialization() {
        tokens = new ArrayList<>();
        readResource("src/resources/tokens.txt", tokens);
        plurmtokens = new ArrayList<>();
        readResource("src/resources/plurmtokens.txt", plurmtokens);
        decades = new ArrayList<>();
        readResource("src/resources/decades.txt", decades);
        hundreds = new ArrayList<>();
        readResource("src/resources/hundreds.txt", hundreds);

        readDegreeThousands();
        checker = false;
    }

    public String getNumber() {
        if (number == null) throw new NullPointerException("Данные пусты, вызовите convert()");
        return number;
    }

    public String getWords() {
        if (words == null) throw new NullPointerException("Данные пусты, вызовите convert()");
        return words;
    }

    private void readResource(String filepath, ArrayList<String> arrayList) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(filepath), "UTF-8"))) {
            String nextString;
            while ((nextString = br.readLine()) != null)
                arrayList.add(nextString);

        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void readDegreeThousands() {
        degreeThousands = new TreeMap<>();
        Properties prop = new Properties();

        try (FileInputStream fis = new FileInputStream("src/resources/degree")) {
            prop.loadFromXML(fis);

            for (String keys : prop.stringPropertyNames())
                degreeThousands.put(Integer.valueOf(keys), prop.getProperty(keys));

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public String convert(String numberToConvert) {
        if (checker)
            initialization();

        this.dataCorrectness(numberToConvert);
        number = numberToConvert;
        String stringWithWords = "";

        if (numberToConvert.startsWith("-")) {
            stringWithWords += "минус ";
            numberToConvert = numberToConvert.substring(1);
        }

        if (number.equals("0")) {
            words = plurmtokens.get(0);
            return words;
        }

        /**
         * Определяем максимальный разряд числа
         */
        int maxDegree = 0;
        for (Integer key : degreeThousands.keySet())
            if (numberToConvert.length() > key)
                maxDegree = key;

        /**
         * проверка не больше ли длинна числа, чем
         * максимальный разряд именованной константы хранимой в файле
         */
        if (numberToConvert.length() > (maxDegree + 3))
            throw new NumberFormatException("Размер числа превышает предельный допустимый программой.");

        for (int i = 0; i < numberToConvert.length() % 3; i++)
            numberToConvert = "0" + numberToConvert;

        for (int i = 0; i < numberToConvert.length(); i += 3) {
            String threeSumbols = numberToConvert.substring(i, i + 3);
            if (threeSumbols.equals("000"))//Заккоментить этот момент
                continue;
            stringWithWords += threeSymbols(threeSumbols, maxDegree - i) + SEPARATOR;
        }

        words = stringWithWords.trim();
        return words;
    }

    private void dataCorrectness(String numberToConvert) {
        Pattern pattern = Pattern.compile("[-]?[0-9]+");
        Matcher m = pattern.matcher(numberToConvert);
        if (!m.matches())
            throw new IllegalArgumentException("переданный аргумент не является числом.");
    }

    private String returnNamedDegree(int threeSymbols, Integer key) {
        int decades = threeSymbols / 10;
        int units = threeSymbols % 10;

        switch (key) {
            case 0: {
                return "";
            }

            case 3: {
                if (decades != 1 && units == 1)
                    return degreeThousands.get(key) + "а";
                else if (decades != 1 && units >= 2 && units <= 4)
                    return degreeThousands.get(key) + "и";
                else
                    return degreeThousands.get(key) + "";
            }

            default: {
                if (decades != 1 && units == 1)
                    return degreeThousands.get(key) + "";
                else if (decades != 1 && units >= 2 && units <= 4)
                    return degreeThousands.get(key) + "а";
                else
                    return degreeThousands.get(key) + "ов";
            }
        }
    }

    private String threeSymbols(String str, Integer key) {
        String tempString = "";

        int firstSymb = Integer.valueOf(str.substring(0, 1));
        int twoLastSymb = Integer.valueOf(str.substring(1, 3));

        for (int i = 1; i < 10; i++)
            if (firstSymb == i)
                tempString += hundreds.get(i - 1) + SEPARATOR;

        if (twoLastSymb >= 0 && twoLastSymb <= 19) {
            for (int i = 0; i <= 19; i++)
                if (twoLastSymb == i)
                    if (key == 3 && (i == 1 || i == 2)) //проверка является ли число разряд тысячным
                        tempString += (plurmtokens.get(i));
                    else
                        tempString += (tokens.get(i));

        } else {
            int secondSymb = Integer.valueOf(str.substring(1, 2));
            int thirdSymb = Integer.valueOf(str.substring(2, 3));

            for (int i = 0; i < 10; i++)
                if (secondSymb == i)
                    tempString += decades.get(i - 2) + SEPARATOR;

            for (int i = 0; i < 10; i++)
                if (thirdSymb == i)
                    if (key == 3 && (i == 1 || i == 2))
                        tempString += (plurmtokens.get(i));
                    else
                        tempString += (tokens.get(i));
        }

        String degreeThousand = SEPARATOR + returnNamedDegree(twoLastSymb, key);

        return (tempString + degreeThousand).trim();
    }
}