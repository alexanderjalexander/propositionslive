package com.propositions;

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
        try (InputStream input = new FileInputStream("resources/config.properties")) {

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

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveProperties() {
        try (OutputStream output = new FileOutputStream("resources/config.properties")) {

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
