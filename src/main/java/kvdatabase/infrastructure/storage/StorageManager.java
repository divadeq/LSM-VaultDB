package kvdatabase.infrastructure.storage;

import kvdatabase.infrastructure.index.IndexManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class StorageManager {
    private RandomAccessFile databaseFile;

    public StorageManager(String filepath) throws IOException {
        File file = new File(filepath);
        this.databaseFile = new RandomAccessFile(file, "rw");
    }

    public synchronized long append(byte[] key, byte[] value) throws IOException {
        databaseFile.seek(databaseFile.length());
        long offset = databaseFile.getFilePointer();
        databaseFile.writeInt(key.length);
        databaseFile.writeInt(value.length);
        databaseFile.write(key);
        databaseFile.write(value);
        return offset;
    }

    public synchronized byte[] read(long offset) throws IOException {
        databaseFile.seek(offset);
        int keySize = databaseFile.readInt();
        int valueSize = databaseFile.readInt();
        databaseFile.skipBytes(keySize);
        byte[] valueBytes = new byte[valueSize];
        databaseFile.readFully(valueBytes);
        return valueBytes;
    }

    public void close() throws IOException {
        if (databaseFile != null) {
            databaseFile.close();
        }
    }

    public synchronized void loadIndex(IndexManager indexManager) throws IOException {
        databaseFile.seek(0);
        while (databaseFile.getFilePointer() < databaseFile.length()) {
            long offset = databaseFile.getFilePointer();
            int keySize = databaseFile.readInt();
            int valueSize = databaseFile.readInt();

            byte[] keyBytes = new byte[keySize];
            databaseFile.readFully(keyBytes);
            String key = new String(keyBytes, StandardCharsets.UTF_8);

            databaseFile.skipBytes(valueSize);

            indexManager.put(key, offset);
        }
    }
}
