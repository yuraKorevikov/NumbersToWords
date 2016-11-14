import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Yura on 01.11.2016.
 */
public class NumbersToWordsTest {

    NumbersToWords testedObj = new NumbersToWords();

    @Test
    public void mainTest() throws Exception {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("0", "ноль");
        testMap.put("9", "девять");
        testMap.put("19", "девятнадцать");
        testMap.put("30", "тридцать");
        testMap.put("79", "семьдесят девять");
        testMap.put("100", "сто");
        testMap.put("431", "четыреста тридцать один");
        testMap.put("2000", "две тысячи");
        testMap.put("1082", "одна тысяча восемьдесят два");
        testMap.put("3981973312856", "три триллиона девятьсот восемьдесят один миллиард девятьсот семьдесят три миллиона" +
                " триста двенадцать тысяч восемьсот пятьдесят шесть");
        testMap.put("-78912812", "минус семьдесят восемь миллионов девятьсот двенадцать тысяч восемьсот двенадцать");

        for (String key: testMap.keySet()) {
            String expected = testMap.get(key);
            String actual = testedObj.convert(key);
            assertEquals("Error in the mainTest, mistake in number: " + key, expected, actual);
        }
    }

    @Test
    public void dataDrivenTest() throws Exception{
        Map<String, String> testMap = new HashMap<>();
        HSSFWorkbook wb = new HSSFWorkbook(
                new FileInputStream("tests/dataForTests/DataExcel.xls"));

        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        while (it.hasNext()) {
            Row row = it.next();
            String key = String.valueOf(
                    (int)row.getCell(0).getNumericCellValue());
            String str = row.getCell(1).getStringCellValue();
            testMap.put(key, str);
        }

        for(String key: testMap.keySet()){
            String expected = testMap.get(key);
            String actual = testedObj.convert(key);
            assertEquals("Error in the dataDrivenTest, mistake in number: " + key, expected, actual);
        }
    }
}