package ua.university.util.Logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4j2Logger implements ILogger {
    private static final Logger logger = LoggerFactory.getLogger("DigiUni");

    @Override
    public void info(String message) {
        logger.info(message);
    }

    // Додаємо метод для логування помилок (знадобиться для Exception handling)
    public void error(String message, Throwable t) {
        logger.error(message, t);
    }
}