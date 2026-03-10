package ua.university;
import ua.university.ui.MainMenu;
import ua.university.util.ConsoleLogger;

public class Main {
    public static void main(String[] args) {

        // Menu test
        MainMenu mainMenu = new MainMenu(new ConsoleLogger());
        mainMenu.run();
    }
}