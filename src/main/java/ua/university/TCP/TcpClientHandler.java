package ua.university.TCP;
import ua.university.domain.Student;
import ua.university.service.StudentService;
import ua.university.util.Logging.ILogger;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class TcpClientHandler implements Runnable {
    private final Socket socket;
    private final StudentService studentService;
    private final ILogger logger;

    public TcpClientHandler(Socket socket, StudentService studentService, ILogger logger) {
        this.socket = socket;
        this.studentService = studentService;
        this.logger = logger;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            out.write("Connected to DigiUni TCP server\n");
            out.write("Commands: PING, LIST_STUDENTS, FIND_STUDENT <id>, QUIT\n");
            out.flush();

            String line;
            while ((line = in.readLine()) != null) {
                String response = handleCommand(line.trim());
                out.write(response);
                out.write("\n");
                out.flush();

                if ("BYE".equals(response)) {
                    break;
                }
            }
        } catch (IOException e) {
            logger.info("TCP client handler error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private String handleCommand(String command) {
        logger.info("TCP command: " + command);

        if (command.equalsIgnoreCase("PING")) {
            return "PONG";
        }

        if (command.equalsIgnoreCase("LIST_STUDENTS")) {
            List<Student> students = studentService.getAllStudents();
            if (students.isEmpty()) return "No students";
            return students.toString();
        }

        if (command.toUpperCase().startsWith("FIND_STUDENT ")) {
            String id = command.substring("FIND_STUDENT ".length()).trim();
            Optional<Student> student = studentService.findById(id);
            return student.map(Student::toString).orElse("Student not found");
        }

        if (command.equalsIgnoreCase("QUIT")) {
            return "BYE";
        }

        return "UNKNOWN COMMAND";
    }
}