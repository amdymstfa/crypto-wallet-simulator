package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {
    
    private static final Map<String, String> env = new HashMap<>();
    
    static {
        loadEnv();
    }
    
    private static void loadEnv() {
        
        String[] paths = {
            ".env",                   
            "../.env",                
            "../../.env",              
            System.getProperty("user.dir") + "/.env"  
        };
        
        for (String path : paths) {
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty() && !line.startsWith("#")) {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            env.put(parts[0].trim(), parts[1].trim());
                        }
                    }
                }
                LoggerUtil.logInfo("Loaded .env from: " + path);
                return;  
            } catch (IOException e) {
                
            }
        }
        
        LoggerUtil.logError("Failed to load .env file from any path");
    }
    
    public static String get(String key) {
        return env.get(key);
    }
    
    public static String get(String key, String defaultValue) {
        return env.getOrDefault(key, defaultValue);
    }
}