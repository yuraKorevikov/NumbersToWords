import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yura on 30.10.2016.
 */
public class NumbersToWords {

    private String number;
    private String words;

    public String getNumber() {
        if(number == null) throw new NullPointerException("Данные пусты, вызовите convert()");
        return number;
    }

    public String getWords() {
        if(words == null) throw new NullPointerException("Данные пусты, вызовите convert()");
        return words;
    }

    NumbersToWords() {
        readTriadFromFile();
    }

    private static final String SEPARATOR = " ";

    private static final String[] TOKENS = new String[]{"", "один", "два", "три", "четыре", "пять",
            "шесть", "семь", "восемь", "девять", "десять", "одиннадцать", "двенадцать", "тринадцать",
            "четырнадцать", "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать"};

    private static final String[] PLURMTOKENS = new String[]{"ноль", "одна", "две"};

    private static final String[] DECADE = new String[]{"двадцать", "тридцать", "сорок",
            "пятьдесят", "шестьдесят", "семьдесят", "восемьдесят", "девяносто"};

    private static final String[] HUNDREDS = new String[]{"сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот",
            "семьсот", "восемьсот", "девятьсот"};

    private Map<Integer, String> degreeThousand;

    private void readTriadFromFile() {
        degreeThousand = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("triads.txt"))) {
            String line;
            String triad[];

            while ((line = reader.readLine()) != null) {
                triad = line.split("-");
                degreeThousand.put(Integer.valueOf(triad[0]), triad[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String convert(String numberToConvert) {
        this.dataCorrecntes(numberToConvert);
        number = numberToConvert;
        String stringWithWords = "";

        if (numberToConvert.startsWith("-")) {
            stringWithWords += "минус ";
            numberToConvert = numberToConvert.substring(1);
        }

        if (number.equals("0")) {
            words = PLURMTOKENS[0];
            return words;
        }

        // Определяем максимальный разряд числа
        int degree = 0;
        for (Integer key : degreeThousand.keySet())
            if (numberToConvert.length() > key)
                degree = key;

        if (numberToConvert.length() > (degree + 3))
            throw new IllegalArgumentException("Размер числа превышает предельный допустимый программой.");

        for (int i = 0; i < numberToConvert.length() % 3; i++)
            numberToConvert = "0" + numberToConvert;

        for (int i = 0; i < numberToConvert.length(); i += 3) {
            String threeSumbols = numberToConvert.substring(i, i + 3);
            if (threeSumbols.equals("000"))
                continue;
            stringWithWords += threeSymbols(threeSumbols, degree - i) + SEPARATOR;
        }

        words = stringWithWords.trim();
        return words;
    }

    private void dataCorrecntes(String numberToConvert) {
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
                    return degreeThousand.get(key) + "а";
                else if (decades != 1 && units >= 2 && units <= 4)
                    return degreeThousand.get(key) + "и";
                else
                    return degreeThousand.get(key) + "";
            }

            default: {
                if (decades != 1 && units == 1)
                    return degreeThousand.get(key) + "";
                else if (decades != 1 && units >= 2 && units <= 4)
                    return degreeThousand.get(key) + "а";
                else
                    return degreeThousand.get(key) + "ов";
            }
        }
    }


    private String threeSymbols(String str, Integer key) {
        String tempString = "";

        int firstSymb = Integer.valueOf(str.substring(0, 1));
        int twoLastSymb = Integer.valueOf(str.substring(1, 3));

        for (int i = 1; i < 10; i++)
            if (firstSymb == i)
                tempString += HUNDREDS[i - 1] + SEPARATOR;

        if (twoLastSymb >= 0 && twoLastSymb <= 19) {
            for (int i = 0; i <= 19; i++)
                if (twoLastSymb == i)
                    if (key == 3 && (i == 1 || i == 2)) //проверка является ли число разряд тысячным
                        tempString += (PLURMTOKENS[i]);
                    else
                        tempString += (TOKENS[i]);

        } else {
            int secondSymb = Integer.valueOf(str.substring(1, 2));
            int thirdSymb = Integer.valueOf(str.substring(2, 3));

            for (int i = 0; i < 10; i++)
                if (secondSymb == i)
                    tempString += DECADE[i - 2] + SEPARATOR;

            for (int i = 0; i < 10; i++)
                if (thirdSymb == i)
                    if (key == 3 && (i == 1 || i == 2))
                        tempString += (PLURMTOKENS[i]);
                    else
                        tempString += (TOKENS[i]);
        }

        String degreeThousand = SEPARATOR + returnNamedDegree(twoLastSymb, key);

        return (tempString + degreeThousand).trim();
    }
}