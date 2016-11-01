import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Created by Yura on 01.11.2016.
 */
public class NumbersToWordsTest {

    NumbersToWords processor = new NumbersToWords();

    @Test
    public void testZeroToNineteen() throws Exception {
        System.out.println("Test Programm: test - Numbers 0-19.");

        String[] TOKENS = new String[]{"ноль", "один", "два", "три", "четыре",
                "пять", "шесть", "семь", "восемь", "девять", "десять", "одиннадцать", "двенадцать", "тринадцать",
                "четырнадцать", "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать"};

        for (int i = 0; i < 20; i++)
            assertEquals("Ошибка в числах от 0-ого до 19-и", TOKENS[i], processor.convert(String.valueOf(i)));


    }

    @Test
    public void testTriads() throws Exception {
        System.out.println("Test Programm: test - random number from 19 to 999.");
        String[] TOKENS = new String[]{"девяносто восемь", "семьсот десять", "сто семьдесят", "двадцать один",
                "триста восемьдесят", "восемьдесят девять", "четыреста пятьдесят один", "двести тридцать"};
        int tokens[] = new int[]{98, 710, 170, 21, 380, 89, 451, 230};

        for (int i = 0; i <= 7; i++)
            assertEquals(TOKENS[i], processor.convert(String.valueOf(tokens[i])));

    }

    @Test
    public void testBigNumbers() throws Exception {
        System.out.println("Test Programm: test - big numbers.");

        String[] TOKENS = new String[]{"девяносто восемь тысяч сто двадцать три", "одна тысяча семьсот двенадцать",
                "семь миллионов восемьсот двадцать три тысячи сто девяносто восемь",
                "два квадриллиона триста двадцать три триллиона семьсот сорок один миллиард двести тридцать один миллион" +
                        " двести семьдесят три тысячи четыреста двенадцать"};
        String[] tokens = new String[]{"98123", "1712", "7823198", "444234123", "2323741231273412"};

        for (int i = 0; i <= 2; i++)
            assertEquals(TOKENS[i], processor.convert(tokens[i]));
    }

    @Test
    public void testGetNameAllTable() throws Exception {
        System.out.println("Test Programm: test  - Different numbers.");
        InputStream in = new FileInputStream("dataForTests/DataExel.xls");
        HSSFWorkbook wb = new HSSFWorkbook(in);

        long inNumber = 0;
        String inString = null;

        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        while (it.hasNext()) {
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                int cellType = cell.getCellType();

                switch (cellType) {
                    case Cell.CELL_TYPE_NUMERIC:
                        inNumber = (long) cell.getNumericCellValue();
                        break;

                    case Cell.CELL_TYPE_STRING:
                        inString = cell.getStringCellValue();
                        break;

                    default:
                        break;
                }
            }
            assertEquals("Ошибка в числе: " + inNumber, inString, processor.convert(String.valueOf(inNumber)));
        }
    }
}