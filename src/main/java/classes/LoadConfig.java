package classes;

import java.io.InputStream;
import java.util.Properties;

public class LoadConfig {
    private static LoadConfig config;

    private String riotApiKey;

    private LoadConfig() {
        String resourceName = "config.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties configProps = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            configProps.load(resourceStream);
        } catch(Exception e) {
            e.printStackTrace();
        }
        this.riotApiKey = configProps.getProperty("RIOT_API_KEY", "");
    }

    public static LoadConfig getInstance() {
        if(config == null) {
            config = new LoadConfig();
        }
        return config;
    }


    public String getRiotApiKey() {
        return riotApiKey;
    }
}