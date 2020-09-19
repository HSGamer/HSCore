package me.hsgamer.hscore.config;

/**
 * An advanced config path
 *
 * @param <F> the type of the raw value from the config
 * @param <T> the type of the final value
 */
public abstract class AdvancedConfigPath<F, T> extends BaseConfigPath<T> {

    /**
     * Create a config path
     *
     * @param path the path to the value
     * @param def  the default value if it's not found
     */
    public AdvancedConfigPath(String path, T def) {
        super(path, def);
    }

    /**
     * Get the raw value from the config
     *
     * @param config the config
     * @return the raw value
     */
    public abstract F getFromConfig(Config config);

    /**
     * Convert to the final value
     *
     * @param rawValue the raw value
     * @return the final value
     */
    public abstract T convert(F rawValue);

    /**
     * Convert to the raw value
     *
     * @param value the value
     * @return the raw value
     */
    public abstract F convertToRaw(T value);

    @Override
    public T getValue() {
        if (config == null) {
            return def;
        }

        F rawValue = getFromConfig(config);
        if (rawValue == null) {
            return def;
        }

        T value = convert(rawValue);
        return value != null ? value : def;
    }

    @Override
    public void setValue(T value) {
        if (config == null) {
            return;
        }

        config.getConfig().set(path, convertToRaw(value));
    }

    @Override
    public void setConfig(Config config) {
        super.setConfig(config);
        config.getConfig().addDefault(path, convertToRaw(def));
    }
}
