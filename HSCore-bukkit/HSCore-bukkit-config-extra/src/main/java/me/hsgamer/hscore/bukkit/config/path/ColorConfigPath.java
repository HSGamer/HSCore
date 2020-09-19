package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.bukkit.Color;

import java.util.Map;

public class ColorConfigPath extends SerializableMapConfigPath<Color> {

    /**
     * Create a config path
     *
     * @param path the path to the value
     * @param def  the default value if it's not found
     */
    public ColorConfigPath(String path, Color def) {
        super(path, def);
    }

    @Override
    public Color convert(Map<String, Object> rawValue) {
        return Color.deserialize(rawValue);
    }

    @Override
    public Map<String, Object> convertToRaw(Color value) {
        return value.serialize();
    }
}
