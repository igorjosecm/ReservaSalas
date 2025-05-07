package classes;

import java.util.HashMap;
import java.util.Map;

public class CompositeKey {
    private final Map<String, Object> keys = new HashMap<>();

    public void addKey(String columnName, Object value) {
        keys.put(columnName, value);
    }

    public Object getKey(String columnName) {
        return keys.get(columnName);
    }

    public Map<String, Object> getKeys() {
        return new HashMap<>(keys);
    }
}
