import java.io.InputStream;
import java.util.Properties;

public class LoadConfig {
    private String riotApiKey;
    private String databaseUser;
    private String databasePass;

    public LoadConfig() {
        setupVars();
    }

    private void setupVars() {
        String resourceName = "config.properties"; // could also be a constant
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties configProps = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            configProps.load(resourceStream);
        } catch(Exception e) {
            e.printStackTrace();
        }
        riotApiKey = configProps.getProperty("RIOT_API_KEY", "");
        databaseUser = configProps.getProperty("DB_USER", "");
        databasePass = configProps.getProperty("DB_PASS","");
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