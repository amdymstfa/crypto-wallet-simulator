package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvLoader {
    private static final Properties props = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(".env")) {
            props.load(fis);
        } catch (IOException e) {
            LoggerUtil.logError("Failed to load .env file", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
