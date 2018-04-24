package classes;

import java.io.InputStream;
import java.util.Properties;

public class LoadConfig {
    private static LoadConfig config;

    private String riotApiKey;
    private String databaseUser;
    private String databasePass;

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
        this.databaseUser = configProps.getProperty("DB_USER", "");
        this.databasePass = configProps.getProperty("DB_PASS","");
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

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePass() {
        return databasePass;
    }
}