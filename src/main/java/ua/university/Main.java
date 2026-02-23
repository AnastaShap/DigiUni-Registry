package ua.university;

import ua.university.ui.CRUD;
import ua.university.ui.MainMenu;
import ua.university.util.ConsoleLogger;

public class Main {
    public static void main(String[] args) {

        // Menu test
        MainMenu mainMenu = new MainMenu(new ConsoleLogger());
        mainMenu.run();

        /*
        boolean running = true;

        while (running) {
            System.out.println("""
                    --- MENU ---
           1- Add Student
           2- List Students
           3- Update Student
           4- Delete Student
           5- Exit""");

            int choice = CRUD.intInRange("Your choice: ", 0, 5);

            switch (choice) {
                case 1 -> CRUD.create();
                case 2 -> CRUD.showStudents();
                case 3 -> CRUD.update();
                case 4 -> CRUD.delete();
                case 5 -> running = false;
            }
        }
        System.out.println("Program finished.");*/
    }
}