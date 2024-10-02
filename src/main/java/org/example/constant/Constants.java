package org.example.constant;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Constants {
    public static final Double MIN_SALARY_THRESHOLD_PERCENTAGE;
    public static final Double MAX_SALARY_THRESHOLD_PERCENTAGE;

    static {
        Properties properties = System.getProperties();
        try {
            properties.load(new FileReader("src/main/resources/application.properties"));
            MIN_SALARY_THRESHOLD_PERCENTAGE = Double.parseDouble((String)properties.get("com.example.min-salary-threshold"));
            MAX_SALARY_THRESHOLD_PERCENTAGE = Double.parseDouble((String)properties.get("com.example.max-salary-threshold"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
