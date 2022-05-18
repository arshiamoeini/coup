package config;

import java.util.HashMap;
import java.util.Map;

public class Config {
    private Map<String, String> table;

    public Config() {
        this.table = new HashMap<>();
    }

    public String getStr(String key) {
        return table.get(key);
    }
    public int getInt(String key) {
        return Integer.parseInt(table.get(key));
    }
}
