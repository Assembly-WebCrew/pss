package org.assembly.pss.config;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertyConfig {

    private static final Logger LOG = LogManager.getLogger();
    private static final Map<String, String> config = new HashMap<>();

    static {
        createDefaultConfig();
        readConfig();
    }

    /**
     * Get the value for specified config key
     *
     * @param key
     * @return the value, or null if it's not specified
     */
    public static String get(String key) {
        return config.get(key);
    }

    /**
     * Get the value for specified config key as an int
     *
     * @param key
     * @param defaultValue Value to return if the specified config value is not
     * an int or it is unset
     * @return the value, or defaultValue if the value is not specified
     */
    public static int getAsInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key));
        } catch (NullPointerException | NumberFormatException ex) {
            return defaultValue;
        }
    }

    private static void readConfig() {
        try {
            FileFilter configFileFilter = f -> f.isFile() && f.getName().equals("pss.properties");
            File jarLocation = new File(PropertyConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
            File[] configFiles = jarLocation.listFiles(configFileFilter);
            if (configFiles == null || configFiles.length == 0) {
                // look for config files in the parent directory if none found in the current directory, this is useful during development when
                // pss can be run from maven target directory directly while the config file sits in the project root
                configFiles = jarLocation.getParentFile().listFiles(configFileFilter);
            }
            if (configFiles != null && configFiles.length > 0) {
                LOG.debug("Config: " + configFiles[0]);
                Properties props = new Properties();
                props.load(new FileInputStream(configFiles[0]));
                Enumeration<?> e = props.propertyNames();
                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    String value = props.getProperty(key);
                    config.put(key, value);
                }
            }
            System.getenv()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith("PSS_"))
                .forEach(entry -> {
                    switch (entry.getKey()) {
                        case "PSS_HTTP_PORT":
                            config.put("http.port", entry.getValue());
                            break;
                        case "PSS_DATABASE_URL":
                            config.put("db.url", entry.getValue());
                            break;
                        case "PSS_DATABASE_USER":
                            config.put("db.user", entry.getValue());
                            break;
                        case "PSS_DATABASE_PASSWORD":
                            config.put("db.password", entry.getValue());
                            break;
                        default:
                            break;
                    }
                });
        } catch (URISyntaxException | IOException ex) {
            LOG.warn("Failed to read configuration, using default values...", ex);
        }
    }

    private static void createDefaultConfig() {
        config.put("http.port", "8080");
        config.put("db.url", "jdbc:mysql://127.0.0.1:3306/pss");
        config.put("db.options", "serverTimezone=UTC");
        config.put("db.user", "pss");
        config.put("db.password", "pss");
    }
}
