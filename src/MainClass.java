/**
 * Created by Yura on 31.10.2016.
 */
public class MainClass {
    public static void main(String args[]){
        try {
            NumbersToWords numbersToWords = new NumbersToWords();
            System.out.println(numbersToWords.convert("1000"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}