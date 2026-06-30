package kvdatabase;

import kvdatabase.infrastructure.index.IndexManager;
import kvdatabase.infrastructure.storage.StorageManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DatabaseEngine {

    private final StorageManager storageManager;
    private final IndexManager indexManager;

    DatabaseEngine(StorageManager storageManager, IndexManager indexManager) {
        this.storageManager = storageManager;
        this.indexManager = indexManager;
    }

    public void put(String key, String value) throws IOException {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
        long offset = storageManager.append(keyBytes, valueBytes);
        indexManager.put(key, offset);
    }

    public String get(String key) throws IOException {
        Long offset = indexManager.get(key);
        if (offset == null) {
            return null;
        }
        byte[] valueBytes = storageManager.read(offset);
        return new String(valueBytes, StandardCharsets.UTF_8);
    }
}
