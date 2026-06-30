package kvdatabase;

import kvdatabase.infrastructure.index.IndexManager;
import kvdatabase.infrastructure.storage.StorageManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String dbPath = "database.bin";

        StorageManager storage = new StorageManager(dbPath);
        IndexManager index = new IndexManager();

        storage.loadIndex(index);

        DatabaseEngine engine = new DatabaseEngine(storage, index);

        engine.put("test1","welcome");

        System.out.println("Read: " + engine.get("test1"));
    }
}
