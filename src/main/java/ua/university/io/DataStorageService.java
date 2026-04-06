package ua.university.io;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

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
            System.out.println("Error saving:" + e.getMessage());
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
}