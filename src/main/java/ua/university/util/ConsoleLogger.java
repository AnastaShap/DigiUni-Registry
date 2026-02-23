package ua.university.util;

import ua.university.domain.Student;

import java.util.List;

public class ConsoleLogger implements ILogger{

    @Override
    public void info(String message) {
        System.out.println(message);
    }
}
