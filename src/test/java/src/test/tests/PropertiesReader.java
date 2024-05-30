package src.test.tests;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesReader {

    public static Map<String, String> readProperties(String filePath) {
        Map<String, String> propertiesMap = new HashMap<>();
        try (InputStream input = Files.newInputStream(Paths.get(filePath))) {
            Properties prop = new Properties();
            prop.load(input);
            for (String key : prop.stringPropertyNames()) {
                propertiesMap.put(key, prop.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propertiesMap;
    }
}
