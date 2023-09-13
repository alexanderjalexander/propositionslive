package com.app;

import java.io.*;
import java.util.Properties;

public class MainProperties {

    private Properties prop;

    private boolean darkMode;
    public boolean getDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
        prop.setProperty("darkMode", darkMode ? "true" : "false");
    }

    public static MainProperties INSTANCE;

    public static void init() {
        MainProperties.INSTANCE = new MainProperties();
    }

    public MainProperties() {
        loadProperties();
    }

    public void exit() {
        saveProperties();
    }

    /* LIST OF LOAD-ABLE and SAVE-ABLE PROPERTIES
     * darkMode - boolean
     *
     */

    /**
     * Loads all of the properties from the config.properties file and updates variables in class definition with them.
     * If a variable is not present in a class definition
     */
    private void loadProperties() {
        File config_properties = new File("config.properties");
        try {
            if (config_properties.createNewFile()) {
                System.out.println("config.properties created.");
            } else {
                System.out.println("config.properties found.");
            }

            System.out.println("Creating new FileInputStream and reading properties.");
            InputStream input = new FileInputStream(config_properties);
            prop = new Properties();

            prop.load(input);
            input.close();

            if (prop.getProperty("darkMode") == null) {
                prop.setProperty("darkMode", "true");
                setDarkMode(true);
                System.out.println("Initializing Property:\tdarkMode\t-->\ttrue");
            } else {
                setDarkMode(prop.getProperty("darkMode").equals("true"));
                System.out.println("Reading Property:\tdarkMode\t-->\t" + prop.getProperty("darkMode"));
            }

            System.out.println("Properties successfully initialized and loaded.");

        } catch (IOException io_ex) {
            throw new RuntimeException(io_ex);
        }
    }

    private void saveProperties() {
        File config_properties = new File("config.properties");
        try {

            System.out.println("Saving User Properties...");

            OutputStream output = new FileOutputStream(config_properties);

            // set the properties value
            prop.setProperty("darkMode", (getDarkMode() ? "true" : "false") );
            System.out.println("Saving Property:\tdarkMode\t-->\t" + prop.getProperty("darkMode"));


            // save all properties to project root folder
            prop.store(output, null);
            output.close();
            System.out.println("Successfully stored all user properties.");

        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
