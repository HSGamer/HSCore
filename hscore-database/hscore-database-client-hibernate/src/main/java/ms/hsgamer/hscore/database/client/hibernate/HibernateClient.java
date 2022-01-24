package ms.hsgamer.hscore.database.client.hibernate;

import me.hsgamer.hscore.database.Client;
import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

/**
 * The Hibernate client
 */
public class HibernateClient implements Client<Configuration> {
  private final Setting setting;
  private final Configuration configuration;

  /**
   * Create new Hibernate client
   *
   * @param setting the setting
   * @param driver  the driver
   */
  public HibernateClient(Setting setting, Driver driver) {
    this.setting = setting;
    this.configuration = new Configuration();
    configuration.setProperty(AvailableSettings.DRIVER, driver.getDriverClass().getName());
    configuration.setProperty(AvailableSettings.URL, driver.convertURL(setting));
    configuration.setProperty(AvailableSettings.USER, setting.getUsername());
    configuration.setProperty(AvailableSettings.PASS, setting.getPassword());
    setting.getProperties().forEach((key, value) -> configuration.setProperty(key, String.valueOf(value)));
  }

  @Override
  public Configuration getOriginal() {
    return configuration;
  }

  @Override
  public Setting getSetting() {
    return setting;
  }

  /**
   * Add an entity class to the client
   *
   * @param clazz the entity class
   */
  public void addEntityClass(Class<?> clazz) {
    configuration.addAnnotatedClass(clazz);
  }

  /**
   * Build the session factory from the client
   *
   * @return the session factory
   */
  public SessionFactory buildFactory() {
    return configuration.buildSessionFactory();
  }
}
