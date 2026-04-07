package ua.university;
import ua.university.ui.MainMenu;
import ua.university.util.Log4j2Logger;

public class Main {
    public static void main(String[] args) {

        // Menu test
        MainMenu mainMenu = new MainMenu(new Log4j2Logger());
        mainMenu.run();
    }
}