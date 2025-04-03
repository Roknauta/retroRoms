import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Test {

    public static void main(String[] args) throws IOException {
        Properties config = new Properties();
        config.load(new FileInputStream("/home/douglas/.config/JetBrains/IntelliJIdea2024.3/scratches/config.properties"));
        System.out.println("");
    }

}
