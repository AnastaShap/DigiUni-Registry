package ua.university.util;

import ua.university.util.Logging.ILogger;

public class ConsoleLogger implements ILogger {

    @Override
    public void info(String message) {
        System.out.println(message);
    }
}
