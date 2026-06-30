package kvdatabase.infrastructure.index;

import java.util.concurrent.ConcurrentHashMap;

public class IndexManager {
    ConcurrentHashMap<String, Long> currentHashMap = new ConcurrentHashMap<>();

    public void put(String key, long offset) {
        currentHashMap.put(key, offset);
    }

    public Long get(String key) {
        return currentHashMap.get(key);
    }
}
