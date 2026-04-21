package ua.university.io;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import java.io.*;

@Slf4j // Анотація для автоматичного створення поля log
public class DataStorageService {

    public boolean exists(Path path) {
        return Files.exists(path);
    }

    public void save(Path path, UniversityDataSnapshot snapshot) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            try (ObjectOutputStream out = new ObjectOutputStream(
                    new BufferedOutputStream(Files.newOutputStream(path)))) {
                out.writeObject(snapshot);
                log.info("Data successfully saved to: {}", path);
            }
        } catch (IOException e) {
            log.error("Error saving data to {}: {}", path, e.getMessage());
        }
    }

    public UniversityDataSnapshot load(Path path) {
        if (!exists(path)) {
            log.warn("File not found: {}", path);
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(path)))) {
            UniversityDataSnapshot snapshot = (UniversityDataSnapshot) in.readObject();
            log.info("Data successfully loaded from: {}", path);
            return snapshot;
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error loading data from {}: {}", path, e.getMessage());
            return null;
        }
    }
}

/*// додати використовування використовувати SLF4J + Log4j2 замість System.out.println!!!
public class DataStorageService {

    public boolean exists(Path path) {
        return Files.exists(path);
    }

    public void save(Path path, UniversityDataSnapshot snapshot) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            try (ObjectOutputStream out = new ObjectOutputStream(
                    new BufferedOutputStream(Files.newOutputStream(path)))) {
                out.writeObject(snapshot);
            }
        } catch (IOException e) {
            System.out.println("Error loading:" + e.getMessage());
        }
    }

    public UniversityDataSnapshot load(Path path) {
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(path)))) {
            return (UniversityDataSnapshot) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error saving: " + e.getMessage());
            return null;
        }
    }
}*/