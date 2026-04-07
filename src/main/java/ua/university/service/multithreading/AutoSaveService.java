package ua.university.service.multithreading;

import lombok.RequiredArgsConstructor;
import ua.university.service.FacultyService;
import ua.university.service.*;
import ua.university.util.Logging.ILogger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class AutoSaveService {
    private final FacultyService facultyService;
    private final StudentService studentService;
    private final ILogger logger;

    // Пул потоків з одним потоком для фонової задачі
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true); // Робимо потік демоном, щоб він не заважав програмі закритися
        return thread;
    });

    public void startAutoSave(int intervalSeconds) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                saveAllData();
            } catch (Exception e) {
                logger.info("Auto-save failed: " + e.getMessage());
            }
        }, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);
    }

    private void saveAllData() {
        // Тут буде логіка виклику I/O методів (тема 12)
        // Наприклад: repository.saveToFile("backup.json");
        System.out.println("\n[System]: Background auto-save completed.");
    }

    public void stop() {
        scheduler.shutdown();
    }
}
