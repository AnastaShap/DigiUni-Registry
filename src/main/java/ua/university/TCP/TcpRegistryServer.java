package ua.university.TCP;
import ua.university.service.StudentService;
import ua.university.util.Logging.ILogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpRegistryServer {
    private final int port;
    private final StudentService studentService;
    private final ILogger logger;

    private final ExecutorService pool = Executors.newFixedThreadPool(4);
    private volatile boolean running = true;
    private ServerSocket serverSocket;

    public TcpRegistryServer(int port, StudentService studentService, ILogger logger) {
        this.port = port;
        this.studentService = studentService;
        this.logger = logger;
    }

    public void start() {
        try (ServerSocket server = new ServerSocket(port)) {
            this.serverSocket = server;
            logger.info("TCP server started on port " + port);

            while (running) {
                Socket client = server.accept();
                logger.info("TCP client connected: " + client.getRemoteSocketAddress());
                pool.submit(new TcpClientHandler(client, studentService, logger));
            }
        } catch (IOException e) {
            if (running) {
                logger.info("TCP server error: " + e.getMessage());
            }
        } finally {
            pool.shutdownNow();
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            logger.info("TCP stop error: " + e.getMessage());
        }
        pool.shutdownNow();
    }
}