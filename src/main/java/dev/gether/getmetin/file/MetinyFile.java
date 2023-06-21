package dev.gether.getmetin.file;

import dev.gether.getmetin.GetMetin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


public class MetinyFile {
    private static File file;
    private static FileConfiguration config;

    public static void setup() {
        file = new File(GetMetin.getInstance().getDataFolder(), "metiny.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            GetMetin.getInstance().saveResource("metiny.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            System.out.println("Nie mozna zapisac pliku!");
        }
    }


    public static void loadFile() {
        setup();
        save();
    }
}
