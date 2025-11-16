import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Core engine for the In-Memory Key-Value Store.
 * Uses ConcurrentHashMap for thread-safe operations.
 */
public class InMemoryDatabase {
    // ConcurrentHashMap provides thread-safety and fast O(1) average time complexity for GET/PUT.
    private final Map<String, String> store;

    public InMemoryDatabase() {
        this.store = new ConcurrentHashMap<>();
    }

    /**
     * Inserts a key-value pair.
     * @param key The unique key.
     * @param value The value associated with the key.
     * @return true if the operation was successful.
     */
    public boolean put(String key, String value) {
        if (key == null || key.trim().isEmpty() || value == null || value.trim().isEmpty()) {
            System.err.println("Error: Key and value cannot be empty.");
            return false;
        }
        store.put(key, value);
        System.out.println("Success: Key '" + key + "' stored.");
        return true;
    }

    /**
     * Retrieves the value associated with a key.
     * @param key The key to look up.
     * @return The value, or null if the key is not found.
     */
    public String get(String key) {
        String value = store.get(key);
        if (value == null) {
            System.out.println("Error: Key '" + key + "' not found.");
        }
        return value;
    }

    /**
     * Removes a key-value pair.
     * @param key The key to remove.
     * @return true if the key was removed, false otherwise.
     */
    public boolean delete(String key) {
        if (store.containsKey(key)) {
            store.remove(key);
            System.out.println("Success: Key '" + key + "' deleted.");
            return true;
        }
        System.err.println("Error: Key '" + key + "' not found for deletion.");
        return false;
    }

    /**
     * Prints all keys currently in the database.
     */
    public void printAllKeys() {
        Set<String> keys = store.keySet();
        if (keys.isEmpty()) {
            System.out.println("The database is currently empty.");
        } else {
            System.out.println("\n--- All Stored Keys (" + keys.size() + ") ---");
            keys.forEach(System.out::println);
            System.out.println("-----------------------------------\n");
        }
    }
}