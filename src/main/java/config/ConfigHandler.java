package config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gui.GameFrame;
import gui.Manual;
import models.Court;
import models.Player;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConfigHandler {
    static ConfigHandler instance;
    static {
        instance = new ConfigHandler();
    }

    public static String SOURCE = "src/main/resources/";
    public static String IMAGE_SOURCE = SOURCE+"cart image/";
    public static Gson GSON;
    static {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        GSON = builder.create();
    }
    private static final Map<String, Config> table;
    static {
        Map<String, Config> temp = new HashMap<>();
        temp.put(Court.class.getName(), loadConfig("hand cart"));
        temp.put(Player.class.getName(), loadConfig("player seats"));
        temp.put(GameFrame.class.getName(), loadConfig("game frame properties"));
        temp.put(Manual.class.getName(), loadConfig("manual properties"));
        table = Collections.unmodifiableMap(temp);
    }
    public static Config loadConfig(String fileName) {
        try {
            File file = new File(SOURCE+fileName+".json");
            Reader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);

            return GSON.fromJson(reader, Config.class);
        } catch (Exception e) {
            try {
                FileOutputStream fos = new FileOutputStream(SOURCE + fileName + ".json");
                OutputStreamWriter isr = new OutputStreamWriter(fos,
                        StandardCharsets.UTF_8);

                GSON.toJson(new Config(), isr);

                isr.close();
            } catch (Exception err) {
                throw new RuntimeException(err);
            }
            //try again
            return loadConfig(fileName);
        }
    }

    public ConfigHandler() {
    }

    public static ConfigHandler getInstance() {
        return instance;
    }
    public Config getConfig(String name) {
        return table.get(name);
    }

    public ImageIcon getImage(String dir) {
        return new ImageIcon(IMAGE_SOURCE+dir+".jpg");
    }
}
