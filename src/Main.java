import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

/**
 * Main application class and command-line interface.
 * Also demonstrates concurrency using Java's ExecutorService.
 */
public class Main {
    private static final int THREAD_POOL_SIZE = 4;
    private static final InMemoryDatabase db = new InMemoryDatabase();

    public static void main(String[] args) {
        System.out.println("--- In-Memory Key-Value Store Initialized ---");
        System.out.println("Type 'HELP' for commands.");
        System.out.println("---------------------------------------------");

        // Start command-line interface
        runCLI();
    }

    /**
     * Runs the Command Line Interface (CLI) loop.
     */
    private static void runCLI() {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.print("> ");
            input = scanner.nextLine().trim();
            String[] parts = input.toUpperCase().split("\\s+", 3);
            String command = parts[0];

            try {
                switch (command) {
                    case "PUT":
                        if (parts.length < 3) throw new IllegalArgumentException("Usage: PUT <key> <value>");
                        db.put(parts[1], parts[2]);
                        break;
                    case "GET":
                        if (parts.length < 2) throw new IllegalArgumentException("Usage: GET <key>");
                        String value = db.get(parts[1]);
                        if (value != null) {
                            System.out.println("Result: " + value);
                        }
                        break;
                    case "DELETE":
                        if (parts.length < 2) throw new IllegalArgumentException("Usage: DELETE <key>");
                        db.delete(parts[1]);
                        break;
                    case "KEYS":
                        db.printAllKeys();
                        break;
                    case "CONCURRENCY_TEST":
                        runConcurrencyTest();
                        break;
                    case "HELP":
                        printHelp();
                        break;
                    case "EXIT":
                        System.out.println("Shutting down database simulator.");
                        scanner.close();
                        return;
                    default:
                        System.err.println("Unknown command. Type 'HELP'.");
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Demonstrates concurrency by submitting multiple write tasks to a thread pool.
     */
    private static void runConcurrencyTest() {
        System.out.println("\n--- Starting Concurrency Test (Simulating multiple users) ---");
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        for (int i = 0; i < 20; i++) {
            final int taskId = i;
            executor.submit(() -> {
                String key = "thread_key_" + taskId;
                String value = "Task_Value_" + taskId + "_by_Thread_" + Thread.currentThread().getName();
                System.out.println("[Thread " + taskId + "] Attempting to PUT: " + key);
                db.put(key, value);
            });
        }

        executor.shutdown();
        try {
            // Wait for all tasks to complete (touches on OS/Concurrency concepts)
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.out.println("Warning: Tasks did not finish in time.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("--- Concurrency Test Finished. Check KEYS to see results. ---\n");
    }

    private static void printHelp() {
        System.out.println("\n--- Available Commands ---");
        System.out.println("PUT <key> <value>   - Inserts or updates a key-value pair.");
        System.out.println("GET <key>           - Retrieves the value for a key.");
        System.out.println("DELETE <key>        - Removes a key-value pair.");
        System.out.println("KEYS                - Lists all keys currently stored.");
        System.out.println("CONCURRENCY_TEST    - Runs a multi-threaded test (OS concept).");
        System.out.println("EXIT                - Terminates the application.");
        System.out.println("--------------------------\n");
    }
}