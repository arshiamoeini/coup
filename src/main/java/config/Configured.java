package config;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Configured {
    static Config getConfig(String name) {
        return ConfigHandler.getInstance().getConfig(name);
    }
    static ImageIcon getImage(String name, int width, int height) {
        Image image = ConfigHandler.getInstance().getImage(name).getImage();
        return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_DEFAULT));
    }
    default ImageIcon getImage(String name) {
        return null;
    }
}
