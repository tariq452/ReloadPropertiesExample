package org.example;
import java.io.*;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class ReloadPropertiesExample {
    private static Properties properties;

    public static void main(String[] args) throws Exception {
        // Load properties initially
        loadProperties();

        // Schedule a timer task to check for changes every 5 seconds
        Timer timer = new Timer();
        timer.schedule(new PropertiesReloadTask(), 0, 5000);

        // Do some work that uses the properties
        while (true) {
            String message = properties.getProperty("89");
            System.out.println(message);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = new FileInputStream("/Users/tariqtami/Downloads/messages/messages_ar.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void reloadProperties() {
        Properties newProperties = new Properties();
        try (InputStream inputStream = new FileInputStream("/Users/tariqtami/Downloads/messages/messages_ar.properties")) {
            newProperties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        properties = newProperties;
    }

    private static class PropertiesReloadTask extends TimerTask {
        private long lastModified = 0;

        @Override
        public void run() {
            long currentModified = getFileLastModifiedTime("/Users/tariqtami/Downloads/messages/messages_ar.properties");
            if (currentModified > lastModified) {
                reloadProperties();
                lastModified = currentModified;
            }
        }

        private long getFileLastModifiedTime(String fileName) {
            try {
                return new java.io.File(fileName).lastModified();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }
}