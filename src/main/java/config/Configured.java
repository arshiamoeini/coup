package config;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Configured {
    static Config getConfig(String name) {
        return ConfigHandler.getInstance().getConfig(name);
    }
}
