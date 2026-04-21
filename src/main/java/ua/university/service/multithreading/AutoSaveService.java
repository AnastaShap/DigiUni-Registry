package ua.university.service.multithreading;

import lombok.RequiredArgsConstructor;
import ua.university.io.DataStorageService;
import ua.university.io.UniversityDataSnapshot;
import ua.university.service.FacultyService;
import ua.university.service.*;
import ua.university.util.Logging.ILogger;

import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class AutoSaveService {
    private final DataStorageService dataStorageService;
    private final Path dataFile;
    private final FacultyService facultyService;
    private final DepartmentService departmentService;
    private final StudentService studentService;
    private final ILogger logger;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
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
        UniversityDataSnapshot snapshot = new UniversityDataSnapshot(
                facultyService.findAll(),
                departmentService.findAll(),
                studentService.getAllStudents()
        );
        dataStorageService.save(dataFile, snapshot);
        logger.info("[Auto-save]: Data successfully saved in background.");
    }

    public void stop() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}
